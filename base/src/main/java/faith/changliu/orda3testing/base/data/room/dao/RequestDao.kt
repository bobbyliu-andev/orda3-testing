package faith.changliu.orda3testing.base.data.room.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import faith.changliu.orda3testing.base.data.models.Request

@Dao
interface RequestDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertRequest(request: Request)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertAll(requests: List<Request>)

	@Delete
	fun deleteRequest(request: Request)

	@Query("select * from requests where id = :id")
	fun getRequestById(id: String): Request

	@Query("select * from requests order by createdAt desc")
	fun getAll(): LiveData<List<Request>>

	@Query("delete from requests")
	fun deleteAll()
	
	@Query("delete from requests where id = :id")
	fun deleteRequestById(id: String)

	@Query("select count(*) from requests")
	fun getCount(): Int
}