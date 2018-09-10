package faith.changliu.orda3testing.agent.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.agent.R
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.data.AppRepository
import faith.changliu.orda3testing.base.data.models.Order
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.utils.*
import kotlinx.android.synthetic.main.content_add_order.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast
import kotlin.properties.Delegates

class OrderDetailFragment : BaseFragment() {
	
	private var mOrder by Delegates.observable(Order()) { _, _, newValue ->
		bind(newValue)
	}
	
	private lateinit var mListener: OrderDetailListener
	
	companion object {
		/**
		 * Factory
		 */
		fun newInstance(order: Order, listener: OrderDetailListener): OrderDetailFragment {
			val instance = OrderDetailFragment()
			instance.mListener = listener
			
			val bundle = Bundle()
			bundle.putSerializable(KEY_ORDER, order)
			instance.arguments = bundle
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.content_add_order, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mBtnScan.setVisible(false)
		mEtBarcode.inputType = InputType.TYPE_NULL
		mEtBarcode.keyListener = null
//		mEtBarcode.isClickable = false
		
		(arguments?.getSerializable(KEY_ORDER) as? Order)?.let {
			mOrder = it
		}
		
		mBtnSubmitNewOrder.setOnClickListener {
			updateOrder()
		}
		
		mBtnCancel.setOnClickListener {
			mListener.onFinished()
		}
	}
	
	// region { bind request }
	
	private fun bind(order: Order) {
		mEtBarcode.setText(order.id)
		mEtTitle.setText(order.title)
		mEtPrice.setText(order.price.toString())
		mEtWeight.setText(order.weight.toString())
		mEtDescription.setText(order.description)
	}
	
	// endregion
	
	/**
	 * update request
	 */
	private fun updateOrder() {
		
		val title = mEtTitle.getString() ?: return
		// todo: date picker for deadline, set to mRequest
		val weight = mEtWeight.getDouble() ?: return
		val price = mEtPrice.getDouble() ?: return
		val description = mEtDescription.getString() ?: return
		
		val newOrder = Order(mOrder.id, title, weight, price, mOrder.createdAt, mOrder.pickedAt, UserPref.getId(), description)
		
		tryBlock {
			async(CommonPool) {
				AppRepository.insertOrder(newOrder)
			}.await()
			toast("Order Updated")
			mListener.onFinished()
		}
	}
	
}

interface OrderDetailListener {
	fun onFinished()
}