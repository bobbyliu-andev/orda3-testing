package faith.changliu.orda3testing.base.widgets

import android.content.Context
import android.view.View
import faith.changliu.orda3testing.base.BaseDialog
import faith.changliu.orda3testing.base.R
import faith.changliu.orda3testing.base.data.firebase.FireAuth
import faith.changliu.orda3testing.base.data.firebase.firestore.FireDB
import faith.changliu.orda3testing.base.data.models.User
import faith.changliu.orda3testing.base.data.models.UserType
import faith.changliu.orda3testing.base.utils.getEmail
import faith.changliu.orda3testing.base.utils.getString
import faith.changliu.orda3testing.base.utils.ifConnected
import faith.changliu.orda3testing.base.utils.log
import kotlinx.android.synthetic.main.register_dialog.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast

class RegisterAgentDialog(ctx: Context) : BaseDialog(ctx), View.OnClickListener {

	override val layoutResId: Int = R.layout.register_dialog

	override fun setupOnConfirm() {
		mBtnSubmit.setOnClickListener(this)
	}

	override fun onClick(v: View) {
		if (v.id == mBtnSubmit.id) {
			val email = mEtEmail.getEmail() ?: return
			val name = mEtName.getString() ?: return

			ifConnected {
				launch(UI) {
					try {
						mLoading.startLoading()
						val isRegistered = async(CommonPool) {
							FireAuth.isEmailRegistered(email)
						}.await()

						if (isRegistered) {
							checkUserType(email)
						}
						else {
							// todo: get real user
							val newUser = User(email, name)
							async(CommonPool) {
								FireDB.saveAgentRegisterRequest(newUser)
							}.await()
							dismissWithToast("Register Request submitted.")
						}

					} catch (ex: Exception) {
						ex.printStackTrace()
					} finally {
						// todo: finally sometimes is omitted
						mLoading.stopLoading()
					}
				}
			}
		}
	}

	private suspend inline fun checkUserType(email: String) {
		// todo: get user type
		val userType = async(CommonPool) {
			FireDB.getUserTypeWithEmail(email)
		}.await()

		// check user type
		if (userType == UserType.TRAVELER) {
			// todo: notify manager to handle it, change UserType from traveler to both after signing contract, etc.
			dismissWithToast("From Traveler to Agent function to be added")
		} else {
			dismissWithToast("User already registered")
		}
	}
}