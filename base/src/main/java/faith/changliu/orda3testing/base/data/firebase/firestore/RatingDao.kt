package faith.changliu.orda3testing.base.data.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import faith.changliu.orda3testing.base.data.models.Rating
import kotlinx.coroutines.experimental.suspendCancellableCoroutine

private const val RATINGS = "ratings"

suspend fun FirebaseFirestore.saveRating(rating: Rating) = suspendCancellableCoroutine<Unit> { cont ->
	collection(RATINGS).document(rating.id)
			.set(rating)
			.addOnSuccessListener {
				cont.resume(Unit)
			}.addOnFailureListener { exception ->
				cont.resumeWithException(exception)
			}.addOnCanceledListener {
				cont.cancel(Throwable("Cancelled"))
			}
}

suspend fun FirebaseFirestore.readAllRatingsForTravelerId(travelerId: String) = suspendCancellableCoroutine<ArrayList<Rating>> { cont ->
	val ratings = arrayListOf<Rating>()
	collection(RATINGS).whereEqualTo("travelerId", travelerId).get().addOnSuccessListener { snapshot ->
		snapshot.forEach { doc ->
			doc?.toObject(Rating::class.java)?.let { rating ->
				ratings.add(rating)
			}
		}
		cont.resume(ratings)
	}.addOnFailureListener { ex ->
		ex.printStackTrace()
		cont.resumeWithException(ex)
	}.addOnCanceledListener { cont.cancel() }
}

suspend fun FirebaseFirestore.readAllRatingsForAgentId(agentId: String) = suspendCancellableCoroutine<ArrayList<Rating>> { cont ->
	val ratings = arrayListOf<Rating>()
	collection(RATINGS).whereEqualTo("agentId", agentId).get().addOnSuccessListener { snapshot ->
		snapshot.forEach { doc ->
			doc?.toObject(Rating::class.java)?.let { rating ->
				ratings.add(rating)
			}
		}
		cont.resume(ratings)
	}.addOnFailureListener { ex ->
		ex.printStackTrace()
		cont.resumeWithException(ex)
	}.addOnCanceledListener { cont.cancel() }
}
