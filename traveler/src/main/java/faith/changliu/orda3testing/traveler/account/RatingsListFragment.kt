package faith.changliu.orda3testing.traveler.account

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.data.firebase.firestore.FireDB
import faith.changliu.orda3testing.base.data.models.Rating
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.utils.log
import faith.changliu.orda3testing.base.utils.tryBlock
import faith.changliu.orda3testing.traveler.R
import kotlinx.android.synthetic.main.fragment_ratings_list_traveler.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import java.util.*

class RatingsListFragment : BaseFragment() {
	
	companion object {
		fun newInstance(): RatingsListFragment {
			val instance = RatingsListFragment()
			
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		log(container.toString())
		return inflater.inflate(R.layout.fragment_ratings_list_traveler, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mRcvRatings.apply {
			layoutManager = LinearLayoutManager(context)
			
			tryBlock {
				val ratings = async(CommonPool) {
					FireDB.readAllRatingsForTravelerId(UserPref.getId())
				}.await()
				adapter = TravelerRatingAdapter(ratings)
				
				var total = 0.0
				ratings.map { total += it.rate }
				val avgRating = total / ratings.size
				
				mTvAvgRating.text = "Average Rating: $avgRating"
			}
		}
	}
}