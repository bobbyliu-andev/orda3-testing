package faith.changliu.orda3testing.agent.activities

import faith.changliu.orda3testing.agent.R
import faith.changliu.orda3testing.base.activities.BaseSplashActivity
import faith.changliu.orda3testing.base.data.AppRepository
import faith.changliu.orda3testing.base.utils.tryBlock

class AgentSplashActivity : BaseSplashActivity() {

	override val logoResId: Int = R.drawable.box
	
	override fun toMain() {
		tryBlock {
			AppRepository.syncAll()
			startActivitySingleTop(MainActivity::class.java)
		}
	}

	override fun toLogin() {

		startActivitySingleTop(AgentLoginActivity::class.java)

	}
}