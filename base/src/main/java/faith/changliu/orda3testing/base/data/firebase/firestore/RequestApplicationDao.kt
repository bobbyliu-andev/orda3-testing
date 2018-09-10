package faith.changliu.orda3testing.base.data.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import faith.changliu.orda3testing.base.data.models.RequestApplication
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

private const val REQUEST_APPLICATIONS = "applications"

suspend fun FirebaseFirestore.saveApplication(application: RequestApplication) = suspendCancellableCoroutine<Unit> { cont ->
	collection(REQUEST_APPLICATIONS).document(application.id).set(application)
			.addOnSuccessListener {
				cont.resume(Unit)
			}.addOnFailureListener { exception ->
				cont.resumeWithException(exception)
			}.addOnCanceledListener {
				cont.cancel(Throwable("Cancelled"))
			}
}

suspend fun FirebaseFirestore.readAllApplicationsWithRequestId(requestId: String) = suspendCancellableCoroutine<ArrayList<RequestApplication>> { cont ->
	collection(REQUEST_APPLICATIONS).whereEqualTo("requestId", requestId).get(). addOnSuccessListener { snapshot ->
		cont.resume(snapshot.toApplications())
	}.addOnFailureListener { exception ->
		exception.printStackTrace()
		cont.resumeWithException(exception)
	}.addOnCanceledListener {
		cont.cancel(Throwable("Cancelled"))
	}
}

suspend fun FirebaseFirestore.hasApplied(id: String) = suspendCancellableCoroutine<Boolean> { cont ->
	collection(REQUEST_APPLICATIONS).whereEqualTo("id", id).get().addOnSuccessListener { querySnapshot ->
		cont.resume(querySnapshot.isEmpty.not())
	}.addOnFailureListener { exception ->
		exception.printStackTrace()
		cont.resumeWithException(exception)
	}.addOnCanceledListener {
		cont.cancel(Throwable("Cancelled"))
	}
}

fun QuerySnapshot.toApplications(): ArrayList<RequestApplication> {
	val applications = arrayListOf<RequestApplication>()
	forEach { doc ->
		doc?.toObject(RequestApplication::class.java)?.let { application ->
			applications.add(application)
		}
	}
	return applications
}