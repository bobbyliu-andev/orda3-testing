package faith.changliu.orda3testing.base.data.firebase

import com.google.firebase.auth.FirebaseAuth
import faith.changliu.orda3testing.base.AppContext
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import org.jetbrains.anko.toast

object FireAuth {
	private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
	
	fun isLoggedIn() = mAuth.currentUser != null
	
	suspend fun login(email: String, pwd: String) = suspendCancellableCoroutine<String> { cont ->
		mAuth.signInWithEmailAndPassword(email, pwd).addOnSuccessListener { authResult ->
			cont.resume(authResult.user.uid)
		}.addOnFailureListener { ex ->
			cont.resumeWithException(ex)
		}.addOnCanceledListener { cont.cancel() }
	}
	
	fun resetPassword(email: String) {
		mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
			AppContext.toast("Reset password email sent")
		}.addOnFailureListener {
			AppContext.toast("Error: ${it.localizedMessage}")
		}
	}
	
	fun logout() {
		mAuth.signOut()
	}

	suspend fun isEmailRegistered(email: String) = suspendCancellableCoroutine<Boolean> { cont ->
		mAuth.fetchSignInMethodsForEmail(email).addOnSuccessListener {
			val methods = it.signInMethods
			if (methods == null || methods.isEmpty()) cont.resume(false)
			else cont.resume(true)
		}.addOnFailureListener { ex ->
			cont.resumeWithException(ex)
		}.addOnCanceledListener { cont.cancel() }
	}

	suspend fun register(email: String, pwd: String) = suspendCancellableCoroutine<String> { cont ->
		mAuth.createUserWithEmailAndPassword(email, pwd).addOnSuccessListener { authResult ->
			// todo: email verification
//			authResult.user.sendEmailVerification()
			cont.resume(authResult.user.uid)
		}.addOnFailureListener { ex ->
			cont.resumeWithException(ex)
		}.addOnCanceledListener { cont.cancel() }
	}
	
}