package faith.changliu.orda3testing.agent.activities

import faith.changliu.orda3testing.agent.R
import faith.changliu.orda3testing.base.activities.BaseLoginActivity
import faith.changliu.orda3testing.base.data.AppRepository
import faith.changliu.orda3testing.base.utils.tryBlock
import faith.changliu.orda3testing.base.widgets.RegisterAgentDialog

class AgentLoginActivity : BaseLoginActivity() {
	override fun toMain() {
		tryBlock {
			AppRepository.syncAll()
			startActivitySingleTop(MainActivity::class.java)
		}
	}

	override val mRegisterTextResId = R.string.register_agent

	override fun register() {
		RegisterAgentDialog(this).show()
	}
}