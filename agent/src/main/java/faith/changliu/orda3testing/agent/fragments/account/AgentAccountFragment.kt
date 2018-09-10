package faith.changliu.orda3testing.agent.fragments.account

import android.os.Bundle
import android.view.View
import faith.changliu.orda3testing.agent.R
import faith.changliu.orda3testing.base.fragments.account.BaseAccountFragment

class AgentAccountFragment : BaseAccountFragment() {
	
	
	companion object {
		fun newInstance(): AgentAccountFragment {
			val instance = AgentAccountFragment()
			
			return instance
		}
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		activity?.supportFragmentManager?.beginTransaction()
				?.replace(R.id.account_frag_container, RatingsListFragment.newInstance())
				?.commit()
	}
}