package faith.changliu.orda3testing.agent

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.base.data.models.Order
import faith.changliu.orda3testing.base.utils.snackConfirm
import kotlinx.android.synthetic.main.cell_order.view.*
import kotlin.properties.Delegates

class OrdersAdapter(
		var orders: ArrayList<Order>,
		private val onUpdate: (Order) -> Unit,
		private val onDelete: (Order) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_order, parent, false)
		return ViewHolder(view)
	}
	
	override fun getItemCount(): Int {
		return orders.size
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(orders[position])
	}
	
	inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

		private var isOpen by Delegates.observable(false) { _, _, newValue ->
			itemView.mLayoutReveal.visibility = if (newValue) View.VISIBLE else View.GONE
		}
		
		fun bind(order: Order) {
			itemView.apply {
				mTvTitle.text = order.title
				mTvMemo.text = order.description
				mBtnDelete.setOnClickListener {
					snackConfirm("Confirm to delete") {
						onDelete(order)
					}
					isOpen = false
				}
				mBtnEdit.setOnClickListener {
					onUpdate(order)
					isOpen = false
				}
				setOnClickListener {
					isOpen = !isOpen
				}
			}
		}
	}
}