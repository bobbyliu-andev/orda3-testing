package faith.changliu.orda3testing.base.fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.R
import faith.changliu.orda3testing.base.adapters.RequestsAdapter
import faith.changliu.orda3testing.base.data.AppRepository
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.data.viewmodels.RequestViewModel
import faith.changliu.orda3testing.base.utils.snackConfirm
import faith.changliu.orda3testing.base.utils.tryBlock
import kotlinx.android.synthetic.main.fragment_requests_list.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast

class RequestListFragment : BaseFragment() {
	
	private val mViewModel by lazy { RequestViewModel() }
	private lateinit var mRequestAdapter: RequestsAdapter
	private lateinit var mRequestListListener: RequestListListener
	
	companion object {
		/**
		 * Factory
		 */
		fun newInstance(requestListListener: RequestListListener): RequestListFragment {
			val instance = RequestListFragment()
			instance.mRequestListListener = requestListListener
			
			val bundle = Bundle()
			bundle.putString("test", "String Value")
			instance.arguments = bundle
			
			return instance
		}
	}
	
	override fun onAttach(context: Context?) {
		super.onAttach(context)
		
		val listener = context as? RequestListListener
		listener?.let {
			mRequestListListener = it
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_requests_list, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		// setup for both phone and tablet
		mRcvRequests.layoutManager = LinearLayoutManager(context)
		
		// todo: implement onUpdate, onDelete
		mRequestAdapter = RequestsAdapter(arrayListOf(), { toast(it.title) }, {mRequestListListener.onUpdate(it)}, {
			mRcvRequests.snackConfirm("Confirm Delete Request") { _ ->
				tryBlock {
					async(CommonPool) {
						AppRepository.deleteRequest(it.id)
					}.await()
					toast("Deleted")
				}
			}
		})
	}
	
	override fun onResume() {
		super.onResume()
		mViewModel.requests.observe(this, Observer { requests ->
			requests?.let {
				mRequestAdapter.requests.apply {
					clear()
					addAll(it)
				}
				mRcvRequests.adapter = mRequestAdapter
			}
		})
	}
	
}

interface RequestListListener {
	fun onUpdate(request: Request)
}