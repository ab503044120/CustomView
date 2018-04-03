package org.huihui.scrollerdrag

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.os.Build
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.support.v4.widget.ViewDragHelper.create
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams

class DrawerFrameLayout : ViewGroup {
    private lateinit var topViewDragHelper: ViewDragHelper
    private lateinit var drawerCallback: DrawerCallback
    private lateinit var paint: Paint
    private var drawerListeners = ArrayList<DrawerListener>()
    private val TAG: String = "DrawerFrameLayout"

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?) : this(context, null)

    init {
        drawerCallback = DrawerCallback()
        topViewDragHelper = create(this, drawerCallback)
        topViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var highMode = MeasureSpec.getMode(heightMeasureSpec)
        var high = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode != MeasureSpec.EXACTLY || highMode != MeasureSpec.EXACTLY) {
            throw IllegalArgumentException("DrawerFrameLayout should be measured with EXACTLY")
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var content = getChildAt(0)
        var lp1 = content.layoutParams as LayoutParams
        content.measure(MeasureSpec.makeMeasureSpec(width - lp1.leftMargin - lp1.rightMargin, MeasureSpec.EXACTLY)
                , MeasureSpec.makeMeasureSpec(high - lp1.topMargin - lp1.bottomMargin, MeasureSpec.EXACTLY))
        var drawer = getChildAt(1)
        var lp2 = drawer.layoutParams as LayoutParams
        var childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, lp2.leftMargin + lp2.rightMargin, lp2.width)
        var childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, lp2.bottomMargin, lp2.height)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawer.elevation = 30f
        }
        drawer.measure(childWidthMeasureSpec, childHeightMeasureSpec)

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var content = getChildAt(0)
        var lp1 = content.layoutParams as LayoutParams
        content.layout(lp1.leftMargin, lp1.topMargin, lp1.leftMargin + content.measuredWidth,
                lp1.topMargin + content.measuredHeight)
        var drawer = getChildAt(1)
        var lp2 = drawer.layoutParams as LayoutParams
        drawer.layout(lp2.leftMargin, -drawer.measuredHeight + lp2.offset
                , lp2.leftMargin + drawer.measuredWidth, lp2.offset)
    }

    class LayoutParams : ViewGroup.MarginLayoutParams {
        var offset = 0

        constructor(width: Int, height: Int) : super(width, height)

        constructor(source: LayoutParams) : super(source)

        constructor(source: ViewGroup.LayoutParams) : super(source)

        constructor(source: ViewGroup.MarginLayoutParams) : super(source)

        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams?): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    inner class DrawerCallback : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child == getChildAt(1)
        }

        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            if (isEnabled) {
                topViewDragHelper.captureChildView(getChildAt(1), pointerId)
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            var lp = releasedChild.layoutParams as LayoutParams
            if (yvel > 0 || (yvel == 0f && releasedChild.top > -releasedChild.measuredHeight / 2)) {
                topViewDragHelper.smoothSlideViewTo(releasedChild, lp.leftMargin, 0)
                invalidate()
            } else if (yvel <= 0 || (yvel == 0f && releasedChild.top <= -releasedChild.measuredHeight / 2)) {
                topViewDragHelper.smoothSlideViewTo(releasedChild, lp.leftMargin, -releasedChild.measuredHeight)
                invalidate()
            }

        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            var lp = child.layoutParams as LayoutParams
            return lp.leftMargin
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            var finalTop = if (top > 0) 0 else top
            var lp = child.layoutParams as LayoutParams
            lp.offset = child.measuredHeight + finalTop
            Log.e(TAG, "clampViewPositionVertical: " + finalTop)
            return finalTop
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            var lp = changedView.layoutParams as LayoutParams
            var childOffset = top + changedView.measuredHeight
            if (childOffset == 0) {
                if (changedView.visibility == View.VISIBLE) {
                    changedView.visibility = View.INVISIBLE
                    for (drawerListener in drawerListeners) {
                        drawerListener.onClose()
                    }
                }
            } else {
                if (changedView.visibility == View.INVISIBLE) {
                    changedView.visibility = View.VISIBLE
                    for (drawerListener in drawerListeners) {
                        drawerListener.onOpen()
                    }
                }
            }
            if (lp.offset != childOffset) {
                lp.offset = childOffset
            }
            invalidate()
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return child.width
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return child.height
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        topViewDragHelper.processTouchEvent(event)
        return true
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        var isContent = child == getChildAt(0)
        var drawer = getChildAt(1)

        var save = canvas!!.save()
        var clipTop = 0
        if (isContent) {
            //如果没有
            if (!hasOpaqueBackground(drawer) && drawer.width >= width) {
                clipTop = drawer.bottom
            }
            canvas.clipRect(0, clipTop, width, height)
        }
        var result = super.drawChild(canvas, child, drawingTime)
        canvas.restoreToCount(save)
        if (isContent) {
            var color = 0x00
            if (drawer.height != 0) {
                var layoutParams = drawer.layoutParams as LayoutParams
                var percent = layoutParams.offset / (drawer.height * 1f)
                color = ((percent * 0x99).toInt()).shl(24)
            }
            paint.color = color
            canvas.drawRect(0f, clipTop.toFloat(), width.toFloat(), height.toFloat(), paint)
        }


        return result
    }

    private fun hasOpaqueBackground(v: View): Boolean {
        val bg = v.background
        return if (bg != null) {
            bg.opacity == PixelFormat.OPAQUE
        } else false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return topViewDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun computeScroll() {
        if (topViewDragHelper.continueSettling(false)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    public fun addDrawerListenr(drawerListener: DrawerListener) {
        drawerListeners.add(drawerListener)
    }

    public fun removeDrawerListenr(drawerListener: DrawerListener) {
        drawerListeners.remove(drawerListener)
    }

    interface DrawerListener {
        fun onOpen()

        fun onClose()
    }
}