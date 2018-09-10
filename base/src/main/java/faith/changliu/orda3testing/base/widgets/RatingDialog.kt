package faith.changliu.orda3testing.base.widgets

import android.content.Context
import faith.changliu.orda3testing.base.BaseDialog
import faith.changliu.orda3testing.base.R
import faith.changliu.orda3testing.base.data.AppRepository
import faith.changliu.orda3testing.base.data.firebase.firestore.FireDB
import faith.changliu.orda3testing.base.data.models.Rating
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.data.models.RequestStatus
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.utils.getString
import kotlinx.android.synthetic.main.rating_layout.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*

class RatingDialog(ctx: Context, private val request: Request, private val onFinished: () -> Unit) : BaseDialog(ctx) {
	override val layoutResId = R.layout.rating_layout
	
	override fun setupOnConfirm() {
		mBtnSubmit.setOnClickListener {
			val comment = mEtComment.getString() ?: return@setOnClickListener
			val newRating = Rating(UUID.randomUUID().toString(), UserPref.getId(), request.assignedTo, mRatingBar.mRating, Date(), comment)
			request.status = RequestStatus.CLOSED
			
			launch(UI) {
				try {
					mLoading.startLoading()
					async(CommonPool) {
						FireDB.saveRating(newRating)
					}.await()
					async(CommonPool) {
						AppRepository.insertRequest(request)
					}.await()
					dismissWithToast("Request Closed")
				} catch (ex: NullPointerException) {
					ex.printStackTrace()
					dismissWithToast(ex.localizedMessage)
				} catch (ex: Exception) {
					ex.printStackTrace()
					dismissWithToast(ex.localizedMessage)
				} finally {
					mLoading.stopLoading()
					onFinished()
				}
			}
		}
	}
}