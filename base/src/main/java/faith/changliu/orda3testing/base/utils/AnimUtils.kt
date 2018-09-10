package faith.changliu.orda3testing.base.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun View.slideIn(distance: Float) {
	if (visibility == View.GONE) {
		x -= distance
		alpha = 0f
		
		animate().translationXBy(distance)
				.alpha(1f)
				.setDuration(400)
				.setListener(object : AnimatorListenerAdapter() {
					override fun onAnimationStart(animation: Animator?) {
						super.onAnimationStart(animation)
						setVisible(true)
					}
				})
	}
}

fun View.slideOut(distance: Float) {
	if (visibility == View.VISIBLE) {
		animate().translationXBy(distance)
				.alpha(0f)
				.setDuration(400)
				.setListener(object : AnimatorListenerAdapter() {
					override fun onAnimationEnd(animation: Animator?) {
						super.onAnimationEnd(animation)
						setVisible(false)
					}
				})
	}
}
