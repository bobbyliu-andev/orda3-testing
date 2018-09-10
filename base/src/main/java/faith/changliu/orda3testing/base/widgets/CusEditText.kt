package faith.changliu.orda3testing.base.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import faith.changliu.orda3testing.base.R
import kotlinx.android.synthetic.main.cus_edit_text.view.*

class CusEditText @JvmOverloads constructor(
		context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
	
	var hint: String = ""
	
	/**
	 * TYPE_CLASS_NUMBER = 0x00000002
	 */
	var inputType: Int
	private var maxLength: Int
	var text: String
		get() = mEtInput.text.toString()
		set(value) {
			mEtInput.setText(value)
		}
	
	init {
		val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CusEditText)
		hint = typedArray.getString(R.styleable.CusEditText_cus_et_hint)
		inputType = typedArray.getInt(R.styleable.CusEditText_cus_et_inputType, 0x00000001)
		maxLength = typedArray.getInt(R.styleable.CusEditText_cus_et_maxLength, 80)
		
		View.inflate(context, R.layout.cus_edit_text, this)
		mEtLayout.hint = hint
		mEtInput.inputType = inputType
		mEtLayout.counterMaxLength = maxLength
		
		typedArray.recycle()
	}
	
	fun setOnTextChangeListener(onTextChanged: (s: CharSequence) -> Unit) {
		mEtInput.addTextChangedListener(object : TextWatcher {
			override fun afterTextChanged(s: Editable?) {}
			
			override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
			
			override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
				s?.let {
					onTextChanged(it)
				}
			}
		})
	}
}