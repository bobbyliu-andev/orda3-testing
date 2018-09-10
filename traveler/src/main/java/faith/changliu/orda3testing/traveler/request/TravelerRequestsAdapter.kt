package faith.changliu.orda3testing.traveler.request

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.traveler.R
import kotlinx.android.synthetic.main.cell_request_traveler.view.*

class TravelerRequestsAdapter(
		private var requests: ArrayList<Request>,
		private val onShowDetail: (Request) -> Unit
) : RecyclerView.Adapter<TravelerRequestsAdapter.ViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_request_traveler, parent, false)
		return ViewHolder(view)
	}

	override fun getItemCount(): Int {
		return requests.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(requests[position])
	}

	inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

		fun bind(request: Request) {
			itemView.apply {
				mTvTitle.text = request.title
				mTvMemo.text = request.description
				
				mCellView.setOnClickListener {
					// copy instead of reference
					onShowDetail(request.copy())
				}
			}
		}
	}
	
	fun updateRequests(newRequests: List<Request>) {
		requests.apply {
			clear()
			addAll(newRequests)
		}
		notifyDataSetChanged()
	}
}