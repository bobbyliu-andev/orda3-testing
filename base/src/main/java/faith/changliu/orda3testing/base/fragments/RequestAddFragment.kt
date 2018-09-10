package faith.changliu.orda3testing.base.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.R
import faith.changliu.orda3testing.base.data.AppRepository
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.data.models.RequestStatus
import faith.changliu.orda3testing.base.data.models.UserStatus
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.utils.getDouble
import faith.changliu.orda3testing.base.utils.getString
import faith.changliu.orda3testing.base.utils.setVisible
import faith.changliu.orda3testing.base.utils.tryBlock
import kotlinx.android.synthetic.main.fragment_request_detail.*
import kotlinx.android.synthetic.main.fragment_request_detail_text_data.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast
import java.util.*

class RequestAddFragment : BaseFragment() {
	
	private var deadline: Date? = null
	private lateinit var mListener: RequestEditListener
	
	companion object {
		fun newInstance(listener: RequestEditListener): RequestAddFragment {
			val instance = RequestAddFragment()
			instance.mListener = listener
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_request_detail, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mEtStatusLayout.setVisible(false)
		mDisplayApplicationLayout.setVisible(false)
		mEtAssignedToLayout.setVisible(false)
		
		
		// todo: pick date
		mEtDeadline.setOnClickListener {
			deadline = Date()
			toast("Date picked: $deadline")
		}
		
		mBtnSubmitNewRequest.setOnClickListener {
			addRequest()
		}
		
		mBtnCancelNewRequest.setOnClickListener {
			mListener.onFinished()
		}
	}
	
	private fun addRequest() {
		if (deadline == null) {
			toast("Deadline not picked")
			return
		}
		val title = mEtTitle.getString() ?: return
		val address = mEtAddress.getString() ?: return
		val city = mEtCity.getString() ?: return
		val country = mEtCountry.getString() ?: return
		val weight = mEtWeight.getDouble() ?: return
		val volume = mEtVolume.getDouble() ?: return
		val compensation = mEtCompensation.getDouble() ?: return
		val description = mEtDescription.getString() ?: return
		val id = UUID.randomUUID().toString()
		
		val newRequest = Request(id, title, RequestStatus.PENDING, "", deadline!!, country, city, address, weight, volume, compensation, description, Date(), UserPref.getId(), UserPref.getEmail())
		
		tryBlock {
			async(CommonPool) {
				AppRepository.insertRequest(newRequest)
			}.await()
			
			toast("New request added")
			mListener.onFinished()
		}
	}
}