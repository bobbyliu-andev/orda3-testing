package faith.changliu.orda3testing.agent.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import faith.changliu.orda3testing.agent.R
import faith.changliu.orda3testing.agent.SimplePageListener
import faith.changliu.orda3testing.agent.fragments.OrderFragment
import faith.changliu.orda3testing.agent.fragments.account.AgentAccountFragment
import faith.changliu.orda3testing.base.BaseActivity
import faith.changliu.orda3testing.base.BaseFragment
import faith.changliu.orda3testing.base.fragments.RequestFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : BaseActivity(),
		NavigationView.OnNavigationItemSelectedListener,
		BottomNavigationView.OnNavigationItemSelectedListener {

	private lateinit var frags: List<BaseFragment>
	private val mSectionsPagerAdapter by lazy { SectionsPagerAdapter(supportFragmentManager) }

	private val onPageChangeListener by lazy {
		SimplePageListener { position ->
			when (position) {
				0 -> mNavBottom.selectedItemId = R.id.mNavShipping
				1 -> mNavBottom.selectedItemId = R.id.mNavRequests
				2 -> mNavBottom.selectedItemId = R.id.mNavAccount
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		frags = listOf(OrderFragment(), RequestFragment(), AgentAccountFragment.newInstance())

		// init views
		mPager.apply {
			adapter = mSectionsPagerAdapter
			addOnPageChangeListener(onPageChangeListener)
		}


		val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
		drawer_layout.addDrawerListener(toggle)
		toggle.syncState()

		mNavDrawer.setNavigationItemSelectedListener(this)
		mNavBottom.setOnNavigationItemSelectedListener(this)
	}
	
	override fun onSaveInstanceState(outState: Bundle?) {
		super.onSaveInstanceState(outState)
		outState?.putString("key", "key")
	}

	override fun onBackPressed() {
		if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
			drawer_layout.closeDrawer(GravityCompat.START)
		} else {
			super.onBackPressed()
		}
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.mNavShipping -> {
				mPager.currentItem = 0
			}
			R.id.mNavRequests -> {
				mPager.currentItem = 1
			}

			R.id.mNavAccount -> {
				mPager.currentItem = 2
			}
		}

		drawer_layout.closeDrawer(GravityCompat.START)
		return true
	}

	inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

		override fun getItem(position: Int): Fragment {
			return frags[position]
		}

		override fun getCount(): Int {
			return frags.size
		}
	}
}