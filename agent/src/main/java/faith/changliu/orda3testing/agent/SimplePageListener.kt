package faith.changliu.orda3testing.agent

import android.support.v4.view.ViewPager

/**
 * Page listener with default empty implementation for ViewPager.OnPageChangeListener
 */
class SimplePageListener(private val onSelectPage: (Int) -> Unit) : ViewPager.OnPageChangeListener {
	override fun onPageScrollStateChanged(state: Int) {}

	override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

	override fun onPageSelected(position: Int) {
		onSelectPage(position)
	}
}