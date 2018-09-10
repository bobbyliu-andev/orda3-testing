package faith.changliu.orda3testing.traveler.request

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.data.viewmodels.RequestViewModel
import faith.changliu.orda3testing.traveler.R
import kotlinx.android.synthetic.main.fragment_requests_list_traveler.*
import kotlin.properties.Delegates

class TravelerRequestsListFragment : BaseFragment() {
	
	private val mViewModel by lazy { RequestViewModel() }
	private lateinit var mRequestAdapter: TravelerRequestsAdapter
	private lateinit var mListener: Listener
	private var requestCategory by Delegates.observable(0) { _, _, newValue ->
		updateRequestList()
	}
	
	companion object {
		fun newInstance(listener: Listener): TravelerRequestsListFragment {
			val instance = TravelerRequestsListFragment()
			instance.mListener = listener
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_requests_list_traveler, container, false)
	}
	
	private val onShowDetail = { request: Request -> mListener.onShowDetail(request) }
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		mRequestAdapter = TravelerRequestsAdapter(arrayListOf(), onShowDetail)
		mRcvRequests.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = mRequestAdapter
		}
		
		mSpinnerRequestCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onNothingSelected(parent: AdapterView<*>?) {}
			
			override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
				if (requestCategory != position) requestCategory = position
			}
			
		}
	}
	
	override fun onResume() {
		super.onResume()
		updateRequestList()
	}
	
	private fun updateRequestList() {
		mViewModel.requests.observe(this, Observer { requests ->
			requests?.let { requestsNotEmpty ->
				// 0 for all, 1 for assigned to this user only
				val userId = UserPref.getId()
				val requestsFiltered =
						if (requestCategory == 0) requestsNotEmpty
						else requestsNotEmpty.filter { it.assignedTo == userId}
				
				mRequestAdapter.updateRequests(requestsFiltered)
			}
		})
	}
	
	interface Listener {
		fun onShowDetail(request: Request)
	}
}
