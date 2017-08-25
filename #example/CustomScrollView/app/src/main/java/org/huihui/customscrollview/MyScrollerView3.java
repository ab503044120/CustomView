package org.huihui.customscrollview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;

public class MyScrollerView3 extends FrameLayout {

	private static final int INVALID_POINTER = -1;
	private static final float OVER_MOVE_SCALE = 0.25f;
	
	private int mOverScrollDistance = 100;
	private int mOverflingDistance = mOverScrollDistance/2;
	
	private float mLastMotionX;
	private int mActivePointerId;

	private boolean mIsBeingDragged;

	private VelocityTracker mVelocityTracker;
	private OverScroller mScroller;
	private int mMinimumVelocity;
	private int mMaximumVelocity;
	private int mTouchSlop;

	public MyScrollerView3(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initScrollView();
	}

	public MyScrollerView3(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public MyScrollerView3(Context context) {
		this(context, null);
	}

	@SuppressLint({ "ClickableViewAccessibility"})
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		initVelocityTrackerIfNotExists();
		mVelocityTracker.addMovement(ev);

		final int action = ev.getAction();

		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			mIsBeingDragged = getChildCount() != 0;
			if (!mIsBeingDragged) {
				return false;
			}

			// Remember where the motion event started
			mLastMotionX = (int) ev.getX();
			mActivePointerId = ev.getPointerId(0);
			break;
		}
		case MotionEvent.ACTION_MOVE:
			final int activePointerIndex = ev
					.findPointerIndex(mActivePointerId);
			final float x = ev.getX(activePointerIndex);
			int deltaX = (int) (mLastMotionX - x);

			if (!mIsBeingDragged) {
				// 判断是否为一个可用move的依据
				if (Math.abs(deltaX) > mTouchSlop) {
					mIsBeingDragged = true;
				}
				
			}

			if (mIsBeingDragged) {
				// Scroll to follow the motion event
				mLastMotionX = x;

				// over scroll时 模拟一个难以拖动的效果
				if(getScrollX() <= 0 || getScrollX() >= getScrollRangeX()) {
					deltaX *= OVER_MOVE_SCALE;
				}
				
				float oldScrollX = getScrollX();
				float scrollX = oldScrollX + deltaX;
				
				overScrollBy(deltaX, 0, (int)scrollX, getScrollY(), getScrollRangeX(), 0, mOverScrollDistance, 0, true);
//				scrollTo((int) (scrollX), getScrollY());
			}
			break;
		case MotionEvent.ACTION_UP:
			if (mIsBeingDragged) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
				int initialVelocity = (int) velocityTracker
						.getXVelocity(mActivePointerId);

				if (getChildCount() > 0) {
					// 速度超过某个阀值时才视为fling
					if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
						fling(-initialVelocity);
					} else {
						// 没有fling时,自定义回弹
						if (mScroller.springBack(getScrollX(), getScrollY(), 
								0, getScrollRangeX(), 0, 0)) {
                            postInvalidate();
                        }
					}
				}

				mActivePointerId = INVALID_POINTER;
				endDrag();
			}
			break;
		}
		return true;
	}
	
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		// 模拟"扯断效果",即拉到over scroll边界时,弹回去
		if(clampedX) {
			mScroller.springBack(getScrollX(), getScrollY(), 0, getScrollRangeX(), 0, 0);
			postInvalidate();
		} else {
			scrollTo(scrollX, scrollY);
		}
	}
	
    private int getScrollRangeX() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0, child.getWidth() - (getWidth() - getPaddingLeft() - getPaddingRight()));
        }
        return scrollRange;
    }

	private void endDrag() {
		mIsBeingDragged = false;

		recycleVelocityTracker();
	}

	private void recycleVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	/**
	 * Fling the scroll view
	 * 
	 * @param velocityX
	 *            The initial velocitX in the X direction. Positive numbers mean
	 *            that the finger/cursor is moving down the screen, which means
	 *            we want to scroll towards the top.
	 */
	public void fling(int velocityX) {
		if (getChildCount() > 0) {

			mScroller.fling(getScrollX(), getScrollY(), 
					velocityX, 0, 
					0, getScrollRangeX(),
					0, 0,
					mOverflingDistance, 0);

			invalidate();
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldX = getScrollX();
			int oldY = getScrollY();
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();

			if (oldX != x || oldY != y) {
				scrollTo(x, y);
			}

			// Keep on drawing until the animation has finished.
			invalidate();
			return;
		}
	}

	private void initScrollView() {
		mScroller = new OverScroller(getContext());
		final ViewConfiguration configuration = ViewConfiguration
				.get(getContext());
		mTouchSlop = ViewConfigurationCompat
				.getScaledPagingTouchSlop(configuration);
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
		setOverScrollMode(OVER_SCROLL_ALWAYS);
	}

	private void initVelocityTrackerIfNotExists() {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
	}
}
