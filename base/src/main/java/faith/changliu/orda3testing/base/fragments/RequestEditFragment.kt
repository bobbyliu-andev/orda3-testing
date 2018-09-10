package faith.changliu.orda3testing.base.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.R
import faith.changliu.orda3testing.base.adapters.ApplicationsAdapter
import faith.changliu.orda3testing.base.data.AppRepository
import faith.changliu.orda3testing.base.data.firebase.firestore.FireDB
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.data.models.RequestStatus
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.utils.*
import faith.changliu.orda3testing.base.widgets.RatingDialog
import kotlinx.android.synthetic.main.fragment_applications_list.*
import kotlinx.android.synthetic.main.fragment_request_detail.*
import kotlinx.android.synthetic.main.fragment_request_detail_text_data.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast
import kotlin.properties.Delegates

class RequestEditFragment : BaseFragment() {
	
	private var mRequest by Delegates.observable(Request()) { _, _, newValue ->
		bind(newValue)
	}
	
	private lateinit var mListener: RequestEditListener
	
	companion object {
		/**
		 * Factory
		 */
		fun newInstance(request: Request, listener: RequestEditListener): RequestEditFragment {
			val instance = RequestEditFragment()
			instance.mListener = listener
			
			val bundle = Bundle()
			bundle.putSerializable(KEY_REQUEST, request)
			instance.arguments = bundle
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_request_detail, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(arguments?.getSerializable(KEY_REQUEST) as? Request)?.let {
			mRequest = it
		}
		
		if (mRequest.status == RequestStatus.CLOSED) {
			// region { todo: if request is closed }
			
			mBtnCloseRequest.setVisible(false)
			mBtnSubmitNewRequest.setVisible(false)
			
			// endregion
		}
		
		mBtnSubmitNewRequest.setOnClickListener {
			updateRequest()
		}
		
		mBtnCancelNewRequest.setOnClickListener {
			toast("Update cancelled")
			mListener.onFinished()
		}
		
		mBtnCloseRequest.setOnClickListener {
			closeRequest()
		}
	}
	
	// region { bind request }
	
	private fun bind(request: Request) {
		bindTextData(request)
		mRcvApplications.layoutManager = LinearLayoutManager(context)
		
		// applications
		tryBlock {
			val applications = async(CommonPool) {
				FireDB.readAllApplicationsWithRequestId(request.id)
			}.await()
			
			val mApplicationsAdapter = ApplicationsAdapter(applications) { application ->
				mRequest.assignedTo = application.appliedBy
				mEtAssignedTo.setText(application.appliedBy)
				mEtStatus.setText(getStatusString(RequestStatus.ASSIGNED))
				toast("Assigned. Please submit to confirm.")
			}
			
			mRcvApplications.adapter = mApplicationsAdapter
		}
	}
	
	private fun bindTextData(request: Request) {
		mEtTitle.setText(request.title)
		mEtDeadline.setText(request.deadline.toString())
		mEtStatus.setText(getStatusString(request.status))
		mEtAssignedTo.setText(request.assignedTo)
		mEtAddress.setText(request.address)
		mEtCity.setText(request.city)
		mEtCountry.setText(request.country)
		mEtWeight.setText("${request.weight}")
		mEtVolume.setText("${request.volume}")
		mEtCompensation.setText("${request.compensation}")
		mEtDescription.setText(request.description)
	}
	
	private fun getStatusString(status: Int): String {
		return when (status) {
			0 -> "PENDING"
			1 -> "ASSIGNED"
			else -> "CLOSED"
		}
	}
	
	private fun getStatus(statusString: String): Int {
		return when (statusString) {
			"PENDING" -> RequestStatus.PENDING
			"ASSIGNED" -> RequestStatus.ASSIGNED
			else -> RequestStatus.CLOSED
		}
	}
	
	// endregion
	
	/**
	 * update request
	 */
	private fun updateRequest() {

		val title = mEtTitle.getString() ?: return
		// todo: date picker for deadline, set to mRequest
		val address = mEtAddress.getString() ?: return
		val city = mEtCity.getString() ?: return
		val country = mEtCountry.getString() ?: return
		val weight = mEtWeight.getDouble() ?: return
		val volume = mEtVolume.getDouble() ?: return
		val compensation = mEtCompensation.getDouble() ?: return
		val description = mEtDescription.getString() ?: return

		val newRequest = Request(mRequest.id, title, getStatus(mEtStatus.text.toString()), mEtAssignedTo.text.toString(), mRequest.deadline, country, city, address, weight, volume, compensation, description, mRequest.createdAt, UserPref.getId(), UserPref.getEmail())

		tryBlock {
			async(CommonPool) {
				AppRepository.insertRequest(newRequest)
			}.await()
			toast("Request Updated")
			mListener.onFinished()
		}
	}
	
	/**
	 * Close request
	 */
	private fun closeRequest() {
		if (mRequest.assignedTo.isEmpty()) {
			mBtnCloseRequest.snackConfirm("Request not assigned yet. Sure to close?") {
				mRequest.status = RequestStatus.CLOSED
				updateRequest()
			}
		} else {
			mBtnCloseRequest.snackConfirm("Confirm Closing Request") {
				RatingDialog(context!!, mRequest) {
					mListener.onFinished()
				}.show()
			}
		}
	}
	
}

interface RequestEditListener {
	fun onFinished()
}