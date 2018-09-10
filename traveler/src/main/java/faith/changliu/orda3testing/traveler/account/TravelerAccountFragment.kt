package faith.changliu.orda3testing.traveler.account

import faith.changliu.orda3testing.base.fragments.account.BaseAccountFragment
import faith.changliu.orda3testing.traveler.R

class TravelerAccountFragment : BaseAccountFragment() {
	
	
	companion object {
		fun newInstance(): TravelerAccountFragment {
			val instance = TravelerAccountFragment()
			
			return instance
		}
	}
	
	override fun onResume() {
		super.onResume()
		activity?.supportFragmentManager?.beginTransaction()
				?.replace(R.id.account_frag_container, RatingsListFragment.newInstance())
				?.commit()
	}
}