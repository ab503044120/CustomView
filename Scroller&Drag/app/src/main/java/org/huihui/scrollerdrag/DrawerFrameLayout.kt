package org.huihui.scrollerdrag

import android.content.Context
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

class DrawerFrameLayout(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : FrameLayout(context, attrs, defStyleAttr) {
    lateinit var targetView: View

    private lateinit var viewDragHelper: ViewDragHelper

    init {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null, 0)

    private fun init() {
        viewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return false
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return left
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return top
            }

            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
                viewDragHelper.captureChildView(targetView, pointerId)
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return 100
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return 100
            }

        })
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }
}