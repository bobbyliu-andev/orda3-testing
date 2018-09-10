package faith.changliu.orda3testing.base.data.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import faith.changliu.orda3testing.base.data.models.User
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

private const val USERS = "users"

suspend fun FirebaseFirestore.saveUser(user: User) = suspendCancellableCoroutine<Unit> { cont ->
	collection(USERS).document(user.id)
			.set(user)
			.addOnSuccessListener {
				cont.resume(Unit)
			}.addOnFailureListener { exception ->
				cont.resumeWithException(exception)
			}.addOnCanceledListener {
				cont.cancel(Throwable("Cancelled"))
			}
}

suspend fun FirebaseFirestore.readUserWithId(id: String) = suspendCancellableCoroutine<User> { cont ->
	collection(USERS).document(id).get().addOnSuccessListener { documentSnapshot ->
		// todo: catch RuntimeException, etc
		documentSnapshot.toObject(User::class.java)?.let { user ->
			cont.resume(user)
		}
	}.addOnFailureListener { exception ->
		cont.resumeWithException(exception)
	}.addOnCanceledListener { cont.cancel() }
}

suspend fun FirebaseFirestore.getUserTypeWithEmail(email: String) = suspendCancellableCoroutine<Int> { cont ->
	collection(USERS).whereEqualTo("email", email).get().addOnSuccessListener { snapshots ->
		snapshots.first().toObject(User::class.java).let { user ->
			cont.resume(user.type)
		}
	}.addOnFailureListener { ex ->
		cont.resumeWithException(ex)
	}.addOnCanceledListener { cont.cancel() }
}

// read all users

// delete user
