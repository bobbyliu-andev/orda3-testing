package faith.changliu.orda3testing.agent.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.agent.R
import faith.changliu.orda3testing.agent.activities.ScannerActivity
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.data.AppRepository
import faith.changliu.orda3testing.base.data.models.Order
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.utils.*
import kotlinx.android.synthetic.main.content_add_order.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import java.util.*

class OrderAddFragment : BaseFragment() {
	
	private lateinit var mListener: OrderDetailListener
	
	companion object {
		/**
		 * Factory
		 */
		fun newInstance(listener: OrderDetailListener): OrderAddFragment {
			val instance = OrderAddFragment()
			instance.mListener = listener
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.content_add_order, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mBtnSubmitNewOrder.setOnClickListener {
			addOrder()
		}
		
		mBtnScan.setOnClickListener { _ ->
			checkPermissionFor(REQUEST_PMS_CAM, Manifest.permission.CAMERA) {
				toScan()
			}
		}
		
		mBtnCancel.setOnClickListener {
			mListener.onFinished()
		}
	}
	
	private fun toScan() {
		startActivityForResult(intentFor<ScannerActivity>(), REQUEST_ACTIVITY_SCAN)
	}
	
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode != Activity.RESULT_OK) {
			toast("Barcode scan cancelled")
			return
		}
		
		if (requestCode == REQUEST_ACTIVITY_SCAN) {
			val barcodeString = data?.getStringExtra(EXTRA_SCAN_RESULT)
			mEtBarcode.setText(barcodeString)
		}
	}
	
	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
			toast("Permission Denied")
			return
		}
		
		if (requestCode == REQUEST_PMS_CAM) {
			toScan()
		}
	}
	
	/**
	 * add order
	 */
	private fun addOrder() {
		
		val title = mEtTitle.getString() ?: return
		// todo: check if barcode exists
		val id = mEtBarcode.getString() ?: return
		val weight = mEtWeight.getDouble() ?: return
		val price = mEtPrice.getDouble() ?: return
		val description = mEtDescription.getString() ?: return
		
		val newOrder = Order(id, title, weight, price, Date(), Date(), UserPref.getId(), description)
		
		tryBlock {
			async(CommonPool) {
				AppRepository.insertOrder(newOrder)
			}.await()
			toast("Order Added")
			mListener.onFinished()
		}
	}
}