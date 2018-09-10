package faith.changliu.orda3testing.traveler

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import faith.changliu.orda3testing.base.BaseActivity
import faith.changliu.orda3testing.base.data.models.Request
import faith.changliu.orda3testing.base.utils.FRAG_TAG_REQUEST_DETAIL
import faith.changliu.orda3testing.traveler.account.RatingsListFragment
import faith.changliu.orda3testing.traveler.account.TravelerAccountFragment
import faith.changliu.orda3testing.traveler.request.TravelerRequestFragment
import kotlinx.android.synthetic.main.activity_traveler_main.*
import kotlinx.android.synthetic.main.content_traveler_main.*
import org.jetbrains.anko.email
import org.jetbrains.anko.toast

class TravelerMainActivity : BaseActivity() {
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_traveler_main)
		setSupportActionBar(toolbar)

		supportFragmentManager.beginTransaction()
				.replace(R.id.container, TravelerRequestFragment.newInstance())
				.commit()
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_account, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.mni_account) {
			supportFragmentManager.beginTransaction()
					.addToBackStack(null)
					.add(R.id.container, TravelerAccountFragment.newInstance())
					.commit()

			return true
		}
		return false
	}

}
