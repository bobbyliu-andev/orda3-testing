package faith.changliu.orda3testing.base.data.room

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {
	@TypeConverter
	fun toDate(timestamp: Long?): Date? {
		timestamp?.let {
			return Date(it)
		}
		return null
	}

	@TypeConverter
	fun toTimestamp(date: Date?): Long? {
		date?.let {
			return it.time
		}
		return null
	}
}