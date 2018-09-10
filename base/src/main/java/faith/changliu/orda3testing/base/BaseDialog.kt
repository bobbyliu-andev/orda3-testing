package faith.changliu.orda3testing.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import faith.changliu.orda3testing.base.widgets.LoadingDialog
import org.jetbrains.anko.toast

abstract class BaseDialog(ctx: Context) : Dialog(ctx) {
	val mLoading: LoadingDialog by lazy { LoadingDialog(this.context) }

	abstract val layoutResId: Int

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(layoutResId)
		setupOnConfirm()
	}

	protected open fun setupOnConfirm() {}
	
	protected fun dismissWithToast(msg: String) {
		context.toast(msg)
		dismiss()
	}
}