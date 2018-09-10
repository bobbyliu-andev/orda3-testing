package faith.changliu.orda3testing.base.data.preferences

import android.content.Context
import android.content.SharedPreferences
import faith.changliu.orda3testing.base.AppContext
import faith.changliu.orda3testing.base.data.models.User
import faith.changliu.orda3testing.base.data.models.UserType
import kotlin.properties.Delegates

object UserPref {
	
	private val mSp: SharedPreferences by lazy { AppContext.getSharedPreferences("user_pref", Context.MODE_PRIVATE) }
	var mUser: User by Delegates.observable(User()) { property, oldValue, newValue ->
		with(newValue) {
			setEmail(email)
			setId(id)
			setPhone(phone)
			setZipcode(zipcode)
			setName(name)
			setType(type)
		}
	}
	
	const val PKG_NAME = "faith.changliu.base"
	const val USER_ID = "$PKG_NAME.id"
	const val USER_EMAIL = "$PKG_NAME.email"
	const val USER_PHONE = "$PKG_NAME.phone"
	const val USER_ZIPCODE = "$PKG_NAME.zipcode"
	const val USER_NAME = "$PKG_NAME.name"
	const val USER_TYPE = "$PKG_NAME.type"

	fun getId() = mSp.getString(USER_ID, "")
	fun setId(userId: String) {
		mSp.edit { putString(USER_ID, userId) }
	}
	
	fun getEmail() = mSp.getString(USER_EMAIL, "")
	fun setEmail(email: String) {
		mSp.edit { putString(USER_EMAIL, email) }
	}
	
	fun getPhone() = mSp.getString(USER_PHONE, "")
	fun setPhone(phone: String) {
		mSp.edit { putString(USER_PHONE, phone) }
	}
	
	fun getZipcode() = mSp.getInt(USER_ZIPCODE, 0)
	fun setZipcode(zipcode: Int) {
		mSp.edit { putInt(USER_ZIPCODE, zipcode) }
	}
	
	fun getName() = mSp.getString(USER_NAME, "")
	fun setName(name: String) {
		mSp.edit { putString(USER_NAME, name) }
	}
	
	fun getType() = mSp.getInt(USER_TYPE, UserType.BOTH)
	fun setType(type: Int) {
		mSp.edit { putInt(USER_TYPE, type) }
	}
}

inline fun SharedPreferences.edit(f: SharedPreferences.Editor.() -> Unit) {
//	Date()
	val editor = edit()
	editor.f()
	editor.apply()
}