package faith.changliu.orda3testing.base.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import faith.changliu.orda3testing.base.AppContext
import faith.changliu.orda3testing.base.data.firebase.firestore.FireDB
import faith.changliu.orda3testing.base.data.models.Order
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.data.room.RoomDB
import faith.changliu.orda3testing.base.utils.ifConnected
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.toast
import java.util.concurrent.Executors

object AppRepository {

	private val roomDB = Room.databaseBuilder(AppContext, RoomDB::class.java, RoomDB.name).build()
	private val executor = Executors.newSingleThreadExecutor()

	suspend fun syncAll() {
		syncOrders()
		syncRequestsCreatedByCurrentUser()
	}

	// region { Orders }

	suspend fun syncOrders() {
		ifConnected {
			// orders
			val orders = async(CommonPool) {
				FireDB.readAllOrders()
			}.await()

			async(CommonPool) {
				with(roomDB.orderDao) {
					deleteAll()
					insertAll(orders)
				}
			}.await()
		}
	}

	suspend fun insertOrder(order: Order) {
		ifConnected {
			// todo: upload
			FireDB.saveOrder(order)
			// todo: add to room
			executor.execute { roomDB.orderDao.insertOrder(order) }
		}
	}

	fun getAllOrders(): LiveData<List<Order>> {
		return roomDB.orderDao.getAll()
	}
	
	suspend fun deleteOrder(orderId: String) {
		ifConnected {
			FireDB.deleteOrder(orderId)
			roomDB.orderDao.deleteOrderById(orderId)
		}
	}

	// endregion

	// region { Requests }

	suspend fun syncRequestsCreatedByCurrentUser() {
		ifConnected {
			val requests = async(CommonPool) {
				FireDB.readAllRequestsCreatedByCurrentUser()
			}.await()

			async(CommonPool) {
				with(roomDB.requestDao) {
					deleteAll()
					insertAll(requests)
				}
			}.await()
		}
	}

	suspend fun syncAllRequests() {
		ifConnected {
			val requests = async(CommonPool) {
				FireDB.readAllRequests()
			}.await()

			async(CommonPool) {
				with(roomDB.requestDao) {
					deleteAll()
					insertAll(requests)
				}
			}.await()
		}
	}

	suspend fun insertRequest(request: Request) {
		ifConnected {
			// todo: upload
			FireDB.saveRequest(request)
			// todo: add to room
			executor.execute { roomDB.requestDao.insertRequest(request) }
		}
	}

	fun getAllRequests(): LiveData<List<Request>> {
		return roomDB.requestDao.getAll()
	}

	suspend fun deleteRequest(requestId: String) {
		ifConnected {
			FireDB.deleteRequest(requestId)
			roomDB.requestDao.deleteRequestById(requestId)
		}
	}
	
	/**
	 * Used for testing data passed in-app
	 */
	fun getRequestByIdFromRoom(requestId: String, onRequestGet: (request: Request) -> Unit) {
		executor.execute {
			val request = roomDB.requestDao.getRequestById(requestId)
			onRequestGet(request)
		}
	}

	// endregion
}