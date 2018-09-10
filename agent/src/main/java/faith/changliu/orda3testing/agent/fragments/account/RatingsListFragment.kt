package faith.changliu.orda3testing.agent.fragments.account

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import faith.changliu.orda3testing.agent.R
import android.view.ViewGroup
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.data.firebase.firestore.FireDB
import faith.changliu.orda3testing.base.data.preferences.UserPref
import faith.changliu.orda3testing.base.utils.tryBlock
import kotlinx.android.synthetic.main.fragment_ratings_list_agent.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class RatingsListFragment : BaseFragment() {
	
	companion object {
		fun newInstance(): RatingsListFragment {
			val instance = RatingsListFragment()
			
			
			return instance
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return inflater.inflate(R.layout.fragment_ratings_list_agent, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		mRcvRatings.apply {
			layoutManager = LinearLayoutManager(context)
			
			tryBlock {
				val ratings = async(CommonPool) {
					FireDB.readAllRatingsForAgentId(UserPref.getId())
				}.await()
				adapter = AgentRatingAdapter(ratings)
			}
		}
	}
}