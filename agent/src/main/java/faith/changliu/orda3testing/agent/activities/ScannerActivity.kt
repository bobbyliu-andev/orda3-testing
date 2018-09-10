package faith.changliu.orda3testing.agent.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.Result
import faith.changliu.orda3testing.base.utils.EXTRA_SCAN_RESULT
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
	
	private lateinit var mScannerView: ZXingScannerView
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mScannerView = ZXingScannerView(this)
		setContentView(mScannerView)
	}
	
	override fun onResume() {
		super.onResume()
		with(mScannerView) {
			setResultHandler(this@ScannerActivity)
			startCamera()
		}
	}
	
	override fun onPause() {
		super.onPause()
		mScannerView.stopCamera()
	}
	
	override fun handleResult(result: Result?) {
		val resultIntent = Intent().putExtra(EXTRA_SCAN_RESULT, result?.text)
		setResult(Activity.RESULT_OK, resultIntent)
		finish()
	}
	
}