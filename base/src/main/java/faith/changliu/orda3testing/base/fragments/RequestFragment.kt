package faith.changliu.orda3testing.base.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.R
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.utils.FRAG_TAG_REQUEST_DETAIL
import faith.changliu.orda3testing.base.utils.log
import kotlinx.android.synthetic.main.fragment_requests.*

class RequestFragment : BaseFragment() {
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_requests, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mFabAddRequest.setOnClickListener {
			if (request_detail_container == null) {
				activity?.supportFragmentManager?.beginTransaction()
						?.addToBackStack(null)?.add(R.id.requests_list_container, RequestAddFragment.newInstance(mEditListener), FRAG_TAG_REQUEST_DETAIL)
						?.commit()
			} else {
				activity?.supportFragmentManager?.beginTransaction()
						?.replace(R.id.request_detail_container, RequestAddFragment.newInstance(mEditListener), FRAG_TAG_REQUEST_DETAIL)
						?.commit()
			}
		}
		
		activity?.supportFragmentManager?.beginTransaction()
				?.replace(R.id.requests_list_container, RequestListFragment.newInstance(mRequestListListener))
				?.commit()
		
	}
	
	// region { Implement List Listener }
	private val mRequestListListener by lazy {
		if (request_detail_container == null) {
			// todo: phone
			object : RequestListListener {
				override fun onUpdate(request: Request) {
					val requestCopy = request.copy()
					activity?.supportFragmentManager?.beginTransaction()
							?.addToBackStack(null)
							?.add(R.id.requests_list_container, RequestEditFragment.newInstance(requestCopy, mEditListener), FRAG_TAG_REQUEST_DETAIL)
							?.commit()
				}
			}
		} else {
			object : RequestListListener {
				override fun onUpdate(request: Request) {
					val requestCopy = request.copy()
					activity?.supportFragmentManager?.beginTransaction()
							?.replace(R.id.request_detail_container, RequestEditFragment.newInstance(requestCopy, mEditListener), FRAG_TAG_REQUEST_DETAIL)
							?.commit()
				}
			}
		}
	}
	// endregion
	
	// region { Implement Detail Listener }
	
	private val mEditListener by lazy {
		object : RequestEditListener {
			override fun onFinished() {
				with(activity?.supportFragmentManager) {
					this?.beginTransaction()?.remove(findFragmentByTag(FRAG_TAG_REQUEST_DETAIL))?.commit()
				}
			}
		}
	}
	
	// endregion
	
}