package faith.changliu.orda3testing.traveler

import android.content.Context
import android.view.View
import faith.changliu.orda3testing.base.BaseDialog
import faith.changliu.orda3testing.base.data.firebase.FireAuth
import faith.changliu.orda3testing.base.data.firebase.firestore.FireDB
import faith.changliu.orda3testing.base.data.models.User
import faith.changliu.orda3testing.base.data.models.UserStatus
import faith.changliu.orda3testing.base.data.models.UserType
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.utils.getEmail
import faith.changliu.orda3testing.base.utils.getString
import faith.changliu.orda3testing.base.utils.ifConnected
import kotlinx.android.synthetic.main.register_traveler_dialog.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast
import java.util.*

class TravelerRegisterDialog(ctx: Context, private val toMain: () -> Unit) : BaseDialog(ctx), View.OnClickListener {

	override val layoutResId: Int = R.layout.register_traveler_dialog

	override fun setupOnConfirm() {
		mBtnSubmit.setOnClickListener(this)
	}

	override fun onClick(v: View) {
		if (v.id == mBtnSubmit.id) {
			val email = mEtEmail.getEmail() ?: return
			val name = mEtName.getString() ?: return
			val pwd = mEtPwd.getString() ?: return
			val pwdConfirm = mEtPwdConfirm.getString() ?: return
			if (pwd != pwdConfirm) {
				mEtPwd.error = "Password not match"
				return
			}

			ifConnected {
				launch(UI) {
					try {
						mLoading.startLoading()
						val isRegistered = async(CommonPool) {
							FireAuth.isEmailRegistered(email)
						}.await()

						if (isRegistered) {
							dismissWithToast("$email is registered. Please login")
						} else {

							val id = async(CommonPool) {
								FireAuth.register(email, pwd)
							}.await()

							val newUser = User(id, email, "", name, 0, UserType.TRAVELER, 0, Date(), UserStatus.ACTIVE)

							async(CommonPool) {
								FireDB.saveUser(newUser)
							}.await()

							UserPref.mUser = newUser

							dismissWithToast("Registered")
							toMain()
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
}