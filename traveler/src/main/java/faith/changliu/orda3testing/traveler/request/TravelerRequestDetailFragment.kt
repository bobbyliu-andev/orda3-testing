package faith.changliu.orda3testing.traveler.request

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.data.firebase.firestore.FireDB
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.data.models.RequestApplication
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.utils.KEY_REQUEST
import faith.changliu.orda3testing.base.utils.snackConfirm
import faith.changliu.orda3testing.base.utils.tryBlock
import faith.changliu.orda3testing.traveler.R
import kotlinx.android.synthetic.main.fragment_request_detail_traveler.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.email
import org.jetbrains.anko.support.v4.toast
import java.util.*
import kotlin.properties.Delegates

class TravelerRequestDetailFragment : BaseFragment() {
	
	private lateinit var mListener: Listener
	
	private var mRequest by Delegates.observable(Request()) { _, _, newValue ->
		bind(newValue)
	}
	
	companion object {
		fun newInstance(request: Request, listener: Listener): TravelerRequestDetailFragment {
			val instance = TravelerRequestDetailFragment()
			instance.mListener = listener
			
			val bundle = Bundle()
			bundle.putSerializable(KEY_REQUEST, request)
			instance.arguments = bundle
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_request_detail_traveler, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		(arguments?.getSerializable(KEY_REQUEST) as? Request)?.let {
			mRequest = it
		}
		
		mBtnEmailAgent.setOnClickListener {
			context?.email(mRequest.agentEmail, "Regarding Request ${mRequest.title} from ORDA", "Hi, may I ask...")
		}
		
		mBtnCancel.setOnClickListener {
			mListener.onFinished()
		}
		
		mBtnApply.setOnClickListener {
			onApply()
		}
	}
	
	private fun bind(request: Request) {
		with(request) {
			mCusTvTitle.setText(title)
			mCusTvDeadline.setText(deadline.toString())
			mCusTVCityCountry.setText("$city, $country")
			mCusTvWeight.setText("$weight")
			mCusTvCompensation.setText("$$compensation")
			mCusTvDescription.setText(description)
		}
	}
	
	private fun onApply() {
		mBtnApply.snackConfirm("Confirm to Apply") {
			// todo: more efficient way to get id
			val id = mRequest.id + UserPref.getId().subSequence(0, 3)
			val newApplication = RequestApplication(id, mRequest.id, UserPref.getId(), Date())
			
			tryBlock {
				// check if id exists
				val hasApplied = async(CommonPool) {
					FireDB.hasApplied(id)
				}.await()
				
				if (hasApplied) {
					toast("You Already Applied")
				} else {
					async(CommonPool) {
						FireDB.saveApplication(newApplication)
					}.await()
					toast("Apply Submitted")
				}
				
				mListener.onFinished()
			}
		}
	}
	
	interface Listener {
		fun onFinished()
	}
}
