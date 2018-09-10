package faith.changliu.orda3testing.base

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import faith.changliu.orda3testing.base.widgets.LoadingDialog
import org.jetbrains.anko.clearTop

open class BaseActivity : AppCompatActivity() {

	val mLoading by lazy { LoadingDialog(this) }

	inline fun <reified T: BaseActivity> startActivitySingleTop(activityClass: Class<T>) {
		val intent = Intent(this, activityClass).clearTop()
		startActivity(intent)
		finish()
	}
}