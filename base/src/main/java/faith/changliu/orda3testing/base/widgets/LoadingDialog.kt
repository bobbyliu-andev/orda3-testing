package faith.changliu.orda3testing.base.widgets

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import faith.changliu.orda3testing.base.R

class LoadingDialog(ctx: Context) : Dialog(ctx, R.style.ProgressBar) {

	private var isLoading = false

	init {
		setContentView(R.layout.progress_dialog)
		setCancelable(true)
		setCanceledOnTouchOutside(false)
		window.attributes.gravity = Gravity.CENTER
		// dim
		// layout params
		val lp = window.attributes
		lp.dimAmount = 0f
		window.attributes = lp
	}

	fun startLoading() {
		if (!isLoading) {
			isLoading = true
			show()
		}
	}

	fun stopLoading() {
		if (isLoading) {
			isLoading = false
			dismiss()
		}
	}

	override fun onBackPressed() {
		stopLoading()
		super.onBackPressed()
	}
}