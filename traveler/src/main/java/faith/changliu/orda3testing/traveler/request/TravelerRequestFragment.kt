package faith.changliu.orda3testing.traveler.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.utils.FRAG_TAG_REQUEST_DETAIL
import faith.changliu.orda3testing.traveler.R
import kotlinx.android.synthetic.main.content_traveler_main.*

class TravelerRequestFragment : BaseFragment() {
	
	companion object {
		fun newInstance(): TravelerRequestFragment {
			val instance = TravelerRequestFragment()
			
			return instance
		}
	}
	
	private val requestDetailListener = object : TravelerRequestDetailFragment.Listener {
		override fun onFinished() {
			with(activity?.supportFragmentManager) {
				this?.beginTransaction()?.remove(
						this?.findFragmentByTag(FRAG_TAG_REQUEST_DETAIL)
				)?.commit()
			}
		}
	}

	private val requestsListListener = object : TravelerRequestsListFragment.Listener {
		override fun onShowDetail(request: Request) {
			if (request_detail_container == null) {
				activity?.supportFragmentManager?.beginTransaction()
						?.addToBackStack(null)
						?.add(R.id.requests_list_container, TravelerRequestDetailFragment.newInstance(request, requestDetailListener), FRAG_TAG_REQUEST_DETAIL)
						?.commit()
			} else {
				activity?.supportFragmentManager?.beginTransaction()
						?.replace(R.id.request_detail_container, TravelerRequestDetailFragment.newInstance(request, requestDetailListener), FRAG_TAG_REQUEST_DETAIL)
						?.commit()
			}
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.content_traveler_main, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		activity?.supportFragmentManager?.beginTransaction()
				?.replace(R.id.requests_list_container, TravelerRequestsListFragment.newInstance(requestsListListener))
				?.commit()
	}
}