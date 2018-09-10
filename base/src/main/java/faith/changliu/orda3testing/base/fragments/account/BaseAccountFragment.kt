package faith.changliu.orda3testing.base.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.R
import faith.changliu.orda3testing.base.data.firebase.firestore.FireDB
import faith.changliu.orda3testing.base.data.models.User
import faith.changliu.orda3testing.base.data.models.UserStatus
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.utils.*
import kotlinx.android.synthetic.main.fragment_account_base.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.support.v4.toast
import java.util.*

open class BaseAccountFragment : BaseFragment() {
	
	private var shouldAnimate = true
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_account_base, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		shouldAnimate = savedInstanceState == null
	
		mBtnUpdateUser.setOnClickListener {
			getNewUser()?.let { user ->
				UserPref.mUser = user
				tryBlock {
					async(CommonPool) {
						FireDB.saveUser(user)
					}.await()
					toast("User updated")
					mBtnUpdateUser.slideOut(100f)
				}
			}
		}
	}
	
	override fun onResume() {
		super.onResume()
		with(UserPref) {
			mCusTvEmail.setText(getEmail())
			mCusEtPhone.text = getPhone()
			mCusEtName.text = getName()
			mCusEtZipcode.text = getZipcode().toString()
		}
		
		mCusEtZipcode.setOnTextChangeListener { s ->
			if (s.length > 6) {
				// todo: mover cursor to the end
				mCusEtZipcode.text = s.substring(0, 6)
				toast("zipcode shall not be more than 6 digits")
			}
			
			slideIn()
		}
		
		mCusEtName.setOnTextChangeListener {
			slideIn()
		}
		
		mCusEtPhone.setOnTextChangeListener {
			slideIn()
		}
		
		shouldAnimate = true
	}
	
	private fun slideIn() {
		if (shouldAnimate) mBtnUpdateUser.slideIn(100f)
	}
	
	private fun getNewUser(): User? {
		
		val zipcode = mCusEtZipcode.text.toIntOrNull()
		if (zipcode == null) {
			toast("Invalid zipcode")
			return null
		}
		
		return User(
				UserPref.getId(),
				UserPref.getEmail(),
				mCusEtPhone.text,
				mCusEtName.text,
				zipcode,
				UserPref.getType(),
				0,
				Date(), // todo: change date to create date
				UserStatus.ACTIVE
		)
	}
	
}