package faith.changliu.orda3testing.agent.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.agent.OrdersAdapter
import faith.changliu.orda3testing.agent.R
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.data.AppRepository
import faith.changliu.orda3testing.base.data.models.Order
import faith.changliu.orda3testing.base.data.viewmodels.MainViewModel
import faith.changliu.orda3testing.base.utils.tryBlock
import kotlinx.android.synthetic.main.fragment_orders_list.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast

class OrdersListFragment : BaseFragment() {
	
	private val mViewModel by lazy { MainViewModel() }
	private lateinit var mOrderAdapter: OrdersAdapter
	private lateinit var mOrdersListListener: OrdersListListener
	
	companion object {
		fun newInstance(listener: OrdersListListener): OrdersListFragment {
			val instance = OrdersListFragment()
			instance.mOrdersListListener = listener
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_orders_list, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mRcvOrders.layoutManager = LinearLayoutManager(context)
		mOrderAdapter = OrdersAdapter(arrayListOf(), { mOrdersListListener.onUpdate(it)}, { order ->
			tryBlock {
				async(CommonPool) {
					AppRepository.deleteOrder(order.id)
				}.await()
				toast("Order Deleted")
			}
		})
		mRcvOrders.adapter = mOrderAdapter
	}
	
	override fun onResume() {
		super.onResume()
		mViewModel.orders.observe(this, Observer<List<Order>> { orders ->
			orders?.let { orders ->
				mOrderAdapter.orders.apply {
					// todo: opt delete/insert
					clear()
					addAll(orders)
				}
				mOrderAdapter.notifyDataSetChanged()
			}
		})
	}
}

interface OrdersListListener {
	fun onUpdate(order: Order)
}