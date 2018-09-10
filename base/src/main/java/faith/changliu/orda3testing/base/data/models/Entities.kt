package faith.changliu.orda3testing.base.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.*

data class User(val id: String,
				val email: String,
				val phone: String,
				val name: String,
				val zipcode: Int,
				val type: Int,
				val tokenId: Long,
				val createdAt: Date,
				val status: Int) {
	constructor() : this("", "", "", "", 0, 0, 0, Date(), 0)
	constructor(email: String, name: String)
			: this(email, email, "", name, 0, 0, 0, Date(), 0)
}

@Entity(tableName = "orders")
data class Order(@PrimaryKey val id: String, // barcode
				 var title: String,
				 var weight: Double,
				 var price: Double,
				 val createdAt: Date,
				 val pickedAt: Date,
				 val createdBy: String,
				 var description: String) : Serializable {
	@Ignore
	constructor() : this("", "", 0.0, 0.0, Date(), Date(), "", "")
}

@Entity(tableName = "requests")
data class Request(@PrimaryKey val id: String,
				   val title: String,
				   var status: Int,
				   var assignedTo: String,
				   val deadline: Date,
				   val country: String,
				   val city: String,
				   val address: String,
				   val weight: Double,
				   val volume: Double,
				   val compensation: Double,
				   val description: String,
				   val createdAt: Date,
				   val createdBy: String,
				   val agentEmail: String) : Serializable {
	@Ignore
	constructor() : this("", "", 0, "", Date(), "", "", "", 0.0, 0.0, 0.0, "", Date(), "", "")
}

class RequestApplication(val id: String,
						 val requestId: String,
						 val appliedBy: String,
						 val createdAt: Date) {
	constructor() : this("", "", "", Date())
}

data class Rating(val id: String,
			 var agentId: String,
			 var travelerId: String,
			 var rate: Double,
			 val createdAt: Date,
			 var comment: String) {
	constructor() : this("", "", "", 0.0, Date(), "")
}

object UserStatus {
	const val PENDING = 0
	const val ACTIVE = 1
	const val INACTIVE = 2
	const val BANNED = 3
}

object UserType {
	const val AGENT = 0
	const val TRAVELER = 1
	const val BOTH = 2
}

object RequestStatus {
	const val PENDING = 0
	const val ASSIGNED = 1
	const val CLOSED = 2
}
