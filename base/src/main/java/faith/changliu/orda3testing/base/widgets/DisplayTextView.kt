package faith.changliu.orda3testing.base.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import faith.changliu.orda3testing.base.R
import kotlinx.android.synthetic.main.text_display.view.*

class DisplayTextView @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
	
	var hint: String
	
	init {
		val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DisplayTextView)
		hint = typedArray.getString(R.styleable.DisplayTextView_hint)
		
		View.inflate(context, R.layout.text_display, this)
		mTvHint.text = hint
		mTvContent.hint = hint
		
		typedArray.recycle()
	}
	
	fun setText(text: String) {
		mTvContent.text = text
	}
	
}