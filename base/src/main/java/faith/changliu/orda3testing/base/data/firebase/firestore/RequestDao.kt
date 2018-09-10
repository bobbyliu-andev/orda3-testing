package faith.changliu.orda3testing.base.data.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.data.preferences.UserPref
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

private const val REQUESTS = "requests"

suspend fun FirebaseFirestore.saveRequest(request: Request) = suspendCancellableCoroutine<Unit> { cont ->
	collection(REQUESTS).document(request.id)
			.set(request)
			.addOnSuccessListener {
				cont.resume(Unit)
			}.addOnFailureListener { exception ->
				cont.resumeWithException(exception)
			}.addOnCanceledListener {
				cont.cancel(Throwable("Cancelled"))
			}
}

suspend fun FirebaseFirestore.readAllRequests() = suspendCancellableCoroutine<ArrayList<Request>> { cont ->
	val requests = arrayListOf<Request>()
	collection(REQUESTS).get().addOnSuccessListener { snapshot ->
		snapshot.forEach { doc ->
			doc?.toObject(Request::class.java)?.let { request ->
				requests.add(request)
			}
		}
		cont.resume(requests)
	}.addOnFailureListener { ex ->
		ex.printStackTrace()
		cont.resumeWithException(ex)
	}.addOnCanceledListener { cont.cancel() }
}

suspend fun FirebaseFirestore.readAllRequestsCreatedByCurrentUser() = suspendCancellableCoroutine<ArrayList<Request>> { cont ->
	val userId = UserPref.getId()
	val requests = arrayListOf<Request>()
	collection(REQUESTS).whereEqualTo("createdBy", userId).get().addOnSuccessListener { snapshot ->
		snapshot.forEach { doc ->
			doc?.toObject(Request::class.java)?.let { request ->
				requests.add(request)
			}
		}
		cont.resume(requests)
	}.addOnFailureListener { ex ->
		ex.printStackTrace()
		cont.resumeWithException(ex)
	}.addOnCanceledListener { cont.cancel() }
}

suspend fun FirebaseFirestore.deleteRequest(requestId: String) = suspendCancellableCoroutine<Unit> { cont ->
	collection(REQUESTS).document(requestId).delete().addOnSuccessListener {
		cont.resume(Unit)
	}.addOnFailureListener { ex ->
		cont.resumeWithException(ex)
	}.addOnCanceledListener {
		cont.cancel()
	}
}