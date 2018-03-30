package org.huihui.scrollerdrag

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup

class DrawerFrameLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : ViewGroup(context, attrs, defStyleAttr) {


    private val TAG: String = "DrawerFrameLayout"

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {
        Log.e(TAG, "two parameter")
    }

    constructor(context: Context?) : this(context, null) {
        Log.e(TAG, "one parameter")
    }

    init {
        Log.e(TAG, "three parameter")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}