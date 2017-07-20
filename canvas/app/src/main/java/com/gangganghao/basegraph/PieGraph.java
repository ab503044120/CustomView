package com.gangganghao.basegraph;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 饼状图
 * 1. 可以点击
 * 2. 可以旋转
 * 3. 文字尽量不重叠
 * 4. value是0的时候不显示
 */

public class PieGraph extends View {

    /**
     * 默认在计算高度的时候上面和下面都预留2dp
     */
    private static final int DEFAULT_PADDING = 2;
    /**
     * 默认文字的大小10sp
     */
    private static final int DEFAULT_TEXT_SIZE_SP = 10;
    /**
     * 默认饼状图的半径 80dp
     */
    private static final int DEFAULT_RADIUS_DP = 80;
    /**
     * 默认选中拉出来的距离 8do
     */
    private static final int DEFAULT_SELECT_OFFSET_DP = 4;
    /**
     * 默认文字连接线在半径方向多出来的距离 14dp
     */
    private static final int DEFAULT_MARKER_LINE1_DP = 14;
    /**
     * 默认文字连接线着之后水平方向多出来的距离 6dp
     */
    private static final int DEFAULT_MARKER_LINE2_DP = 6;
    /**
     * 线dot半径
     */
    private static final int DEFAULT_LINE_DOT_DP = 3;
    /**
     * 线半径
     */
    private static final int DEFAULT_LINE_STROKE_DP = 1;
    /**
     * 线字距离
     */
    private static final int DEFAULT_LINE_TEXT_MARGIN_DP = 3;
    /**
     * 线距离圆
     */
    private static final int DEFAULT_LINE_MARGIN_DP = 5;

    /**
     * 饼状图的半径
     */
    private float mPieRadius;
    /**
     * 选中的时候偏移出来的位置（注意哦，是选中的那块一半延着半径拉的距离哦）
     */
    private float mSelectOffset;
    /**
     * 文字大小
     */
    private int mTextSize;
    /**
     * 文字颜色
     */
    private int mTextColor;
    /**
     * 文字的高度
     */
    private float mTextHeight;
    /**
     * 画百分比还是文字 true的时候之后百分比，false的时候画文字加百分比
     */
    private boolean mIsDrawRatio;
    /**
     * 画文字的时候先在原有的圆上面延伸出来的长度
     */
    private float mMarkerLine1;
    /**
     * 线宽
     */
    private float mLineStroke;
    /**
     * dot半径
     */
    private float mLineDot;
    /**
     * 线和大圆距离
     */
    private float mLineMargin;
    /**
     * 绘制字体的画笔
     */
    private TextPaint mTextPaint;
    /**
     * 画文字连接线的画笔
     */
    private Paint mLinePaint;
    /**
     * 划线的点
     */
    private Paint mLineDotPaint;
    /**
     * 饼状图信息列表
     */
    private List<PieDataHolder> pieDataHolders;
    /**
     * 饼状图正常时候那个矩形区域
     */
    private RectF mPieNormalRectF;
    /**
     * 饼状图选中时候那个矩形区域，和这个mSelectOffset有关系
     */
    private RectF mPieTmpRectF;
    /**
     * 饼状图的画笔
     */
    private Paint mPiePaint;
    /**
     * 上一个画的文字的区域（用来判断文字是否有重叠的情况，为了提升体验重叠的时候我们是不画的）
     */
    private Rect mPreTextRect;
    /**
     * 当前要画的文字的区域（用来判断文字是否有重叠的情况，为了提升体验重叠的时候我们是不画的）
     */
    private Rect mCurrentTextRect;

    /**
     * 第一个文字的区域，在判断最后一个文字的区域是否有重叠的时候，即判断了前一个也判断了第一个
     */
    private Rect mFirstTextRect;
    /**
     * 监听器，监听哪一款是否有选中
     */
    private OnPieGraphListener mListener;
    /**
     * 滑动产生的距离
     */
    private int mTouchSlop;
    /**
     * 旋转的角度,随手指旋转
     */
    private float mRotate;
    /**
     * 是否可以旋转
     */
    private boolean mCanRotate;
    /**
     * 确认比例保存几位小数
     */
    private DecimalFormat mDecimalFormat;

    /**
     * 点距离圆的距离
     */
    private float dotMargin = 20;


    private PointF sliceVector = new PointF();

    private ValueAnimator mValueAnimator;
    private Path mPathBuffer;
    private float mTextCenterOffset;
    private TextPaint mHoleTextPaint;
    private int mHoleTextColor;
    private int mHoleTextSize;
    private String mHoleText = "";
    private GestureDetector mGestureDetector;

    /**
     * 选中监听
     */
    public interface OnPieGraphListener {

        void onPieSelect(PieDataHolder pieDataHolder);

        void onNoPieSelect();
    }

    private float mAnimatedValue;

    public PieGraph(Context context) {
        this(context, null);
    }

    public PieGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, defStyleAttr);
        initData();
        initTextMetrics();
    }

    private void initData() {
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        pieDataHolders = new ArrayList<>();
        mPieNormalRectF = new RectF();
        mPieTmpRectF = new RectF();

        mPiePaint = new Paint();
        mPiePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);

        mHoleTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mHoleTextPaint.setTextSize(mHoleTextSize);
        mHoleTextPaint.setColor(mHoleTextColor);

        mPreTextRect = new Rect();
        mCurrentTextRect = new Rect();
        mFirstTextRect = new Rect();

        mLinePaint = new Paint();
        mLinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mLineStroke);
        mLineDotPaint = new Paint();
        mLineDotPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mLineDotPaint.setStyle(Paint.Style.FILL);

        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();

        mRotate = 0;
        // 默认保留两位小数
        mDecimalFormat = new DecimalFormat("0.0000");

        mPathBuffer = new Path();

        mValueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedValue = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnimator.setInterpolator(new OvershootInterpolator());

        setLayerType(LAYER_TYPE_HARDWARE, null);

        mGestureDetector = new GestureDetector(getContext(), mSimpleOnGestureListener);
    }


    /**
     * 获取xml里面定义的属性
     */
    private void initAttrs(AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PieGraph, defStyleAttr, 0);

        mPieRadius = a.getDimensionPixelSize(R.styleable.PieGraph_pie_circle_radius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS_DP,
                        getResources().getDisplayMetrics()));
        mSelectOffset = a.getDimensionPixelSize(R.styleable.PieGraph_pie_select_offset,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SELECT_OFFSET_DP,
                        getResources().getDisplayMetrics()));
        mTextSize = a.getDimensionPixelSize(R.styleable.PieGraph_pie_text_size,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE_SP,
                        getResources().getDisplayMetrics()));
        mHoleTextSize = a.getDimensionPixelSize(R.styleable.PieGraph_pie_hole_TextSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE_SP,
                        getResources().getDisplayMetrics()));
        mTextColor = a.getColor(R.styleable.PieGraph_pie_text_color, 0xff000000);
        mHoleTextColor = a.getColor(R.styleable.PieGraph_pie_hole_TextColor, 0x7f000000);
        mIsDrawRatio = a.getBoolean(R.styleable.PieGraph_pie_show_radio, false);
        mMarkerLine1 = a.getDimensionPixelSize(R.styleable.PieGraph_pie_marker_line1,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_MARKER_LINE1_DP,
                        getResources().getDisplayMetrics()));
        mLineDot = a.getDimensionPixelSize(R.styleable.PieGraph_pie_line_dot_radius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_DOT_DP,
                        getResources().getDisplayMetrics()));
        mLineStroke = a.getDimensionPixelSize(R.styleable.PieGraph_pie_line_stroke,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_STROKE_DP,
                        getResources().getDisplayMetrics()));
        mLineMargin = a.getDimensionPixelSize(R.styleable.PieGraph_pie_line_circle_distance,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_LINE_MARGIN_DP,
                        getResources().getDisplayMetrics()));
        mCanRotate = a.getBoolean(R.styleable.PieGraph_pie_can_rotate, true);
        mCanRotate = a.getBoolean(R.styleable.PieGraph_pie_can_rotate, true);
        a.recycle();
    }

    /**
     * 得到位置的高度，基准线啥啥的
     */
    private void initTextMetrics() {
        mTextPaint.setTextSize(mTextSize);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;
        mTextCenterOffset = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            mPreX = e.getX();
            mPreY = e.getY();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // 这里我们去判断是否是点击事件
            if (inCircle(e.getX(), e.getY())) {
                int position = getHolderPositionByAngle(action2Angle(e.getX(), e.getY()));
                clearHolderSelect(position);
                PieDataHolder holder = getHolderByPosition(position);
                if (holder != null) {
                    holder.mIsSelect = !holder.mIsSelect;
                }
            } else {
                // 不在圆内，清空掉以前的选择
                clearHolderSelect(-1);
            }
            if (mListener != null) {
                // 找出选中的那个
                PieDataHolder holder = findSelectHolder();
                if (holder == null) {
                    mListener.onNoPieSelect();
                } else {
                    mListener.onPieSelect(holder);
                }
            }
            invalidate();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!inCircle(e2.getX(), e2.getY())) {
                // 没有在园内 这个事件结束了，我们不要了
                return false;
            }
            float offsetX = e2.getX() - mPreX;
            float offsetY = e2.getY() - mPreY;
            if (mCanRotate) {
                if (!mDealMove) {
                    mDealMove = true;
                }
                mRotate = mRotate + action2Angle(e2.getX(), e2.getY()) - action2Angle(mPreX, mPreY);
                mPreX = e2.getX();
                mPreY = e2.getY();
                invalidate();
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

    };

    /**
     * 测量控件大小,这里宽度我们不测了，只是去测量高度，宽度直接用父控件传过来的大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PADDING,
                getResources().getDisplayMetrics());
        // 半径 + 选中的时候多出来的部分 + 半径延长线 + 文字的高度 + 预留的padding
        int height = (int) ((mPieRadius + mSelectOffset + mMarkerLine1 + mTextHeight + mLineMargin + padding) * 2);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);

    }

    /**
     * 具体的绘制
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPieRectF();
        drawPie(canvas);
        if (mAnimatedValue >= 1) {
            drawRateText(canvas);
        }
        drawHole(canvas);
    }

    /**
     * 画洞和洞里的文字
     *
     * @param canvas
     */
    private void drawHole(Canvas canvas) {
        mPiePaint.setColor(Color.argb((int) (255 * 0.6f), 255, 255, 255));
        canvas.drawCircle(mPieNormalRectF.centerX(), mPieNormalRectF.centerY(), mPieRadius * 0.55f, mPiePaint);
        mPiePaint.setColor(Color.WHITE);
        canvas.drawCircle(mPieNormalRectF.centerX(), mPieNormalRectF.centerY(), mPieRadius / 2, mPiePaint);
        mPieTmpRectF.left = mPieNormalRectF.centerX() - mPieRadius / 2;
        mPieTmpRectF.top = mPieNormalRectF.centerY() - mPieRadius / 2;
        mPieTmpRectF.right = mPieNormalRectF.centerX() + mPieRadius / 2;
        mPieTmpRectF.bottom = mPieNormalRectF.centerY() + mPieRadius / 2;

        StaticLayout mpAndroidChart = new StaticLayout(mHoleText, mHoleTextPaint
                , (int) (mPieTmpRectF.width() * 0.8f), Layout.Alignment.ALIGN_CENTER, 1, 1, false);
        int height = mpAndroidChart.getHeight();
        canvas.save();
        canvas.translate(mPieNormalRectF.centerX() - mPieRadius / 2 * 0.8f, mPieTmpRectF.centerY() - height / 2);
        mpAndroidChart.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 这里呢，去得到画饼状图的时候的那个矩形
     */
    private void initPieRectF() {
        mPieNormalRectF.left = getWidth() / 2 - mPieRadius;
        mPieNormalRectF.top = getHeight() / 2 - mPieRadius;
        mPieNormalRectF.right = mPieNormalRectF.left + mPieRadius * 2;
        mPieNormalRectF.bottom = mPieNormalRectF.top + mPieRadius * 2;
    }

    private void normalizeVector(PointF point) {
        final float abs = point.length();
        point.set(point.x / abs, point.y / abs);
    }

    /**
     * 画饼状图
     */
    private void drawPie(Canvas canvas) {
        if (pieDataHolders == null || pieDataHolders.size() <= 0) {
            return;
        }
        float angle = 0;
        for (PieDataHolder pieDataHolder : pieDataHolders) {
            mPathBuffer.reset();
            mPiePaint.setColor(pieDataHolder.mColor);
            float sliceSpaceAngleOuter = 1.5f;
            float sliceAngle = pieDataHolder.mSweepAngel;

            if (pieDataHolder.mSweepAngel <= 1) {
                // 0度的不画
                continue;
            }
            mPieTmpRectF.set(mPieNormalRectF);
            if (pieDataHolder.mIsSelect) {
                // 选中的时候往外面拉出来一点
                mPieTmpRectF.top -= mSelectOffset;
                mPieTmpRectF.left -= mSelectOffset;
                mPieTmpRectF.bottom += mSelectOffset;
                mPieTmpRectF.right += mSelectOffset;
            }
            mPathBuffer.reset();
            final float startAngleOuter = mRotate + (angle + sliceSpaceAngleOuter / 2.f) * mAnimatedValue;
            float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * mAnimatedValue;
            float startX = (float) (mPieTmpRectF.centerX() + mPieRadius * Math.cos(Math.toRadians(startAngleOuter)));
            float startY = (float) (mPieTmpRectF.centerY() + mPieRadius * Math.sin(Math.toRadians(startAngleOuter)));
            mPathBuffer.moveTo(startX, startY);

            mPathBuffer.arcTo(
                    mPieTmpRectF,
                    startAngleOuter,
                    sweepAngleOuter
            );
            float angleMiddle = startAngleOuter + sweepAngleOuter / 2.f;

            float sliceSpaceOffset =
                    calculateMinimumRadiusForSpacedSlice(
                            mPieTmpRectF,
                            mPieRadius,
                            sliceAngle * mAnimatedValue,
                            startX,
                            startY,
                            startAngleOuter,
                            sweepAngleOuter);

            float arcEndPointX = mPieTmpRectF.centerX() +
                    sliceSpaceOffset * (float) Math.cos(Math.toRadians(angleMiddle));
            float arcEndPointY = mPieTmpRectF.centerY() +
                    sliceSpaceOffset * (float) Math.sin(Math.toRadians(angleMiddle));

            mPathBuffer.lineTo(
                    arcEndPointX,
                    arcEndPointY);
            mPathBuffer.close();
            canvas.drawPath(mPathBuffer, mPiePaint);
            angle += sliceAngle * mAnimatedValue;
        }
    }


    protected float calculateMinimumRadiusForSpacedSlice(
            RectF center,
            float radius,
            float angle,
            float arcStartPointX,
            float arcStartPointY,
            float startAngle,
            float sweepAngle) {
        final float angleMiddle = startAngle + sweepAngle / 2.f;

        // Other point of the arc
        float arcEndPointX = center.centerX() + radius * (float) Math.cos(Math.toRadians(startAngle + sweepAngle));
        float arcEndPointY = center.centerY() + radius * (float) Math.sin(Math.toRadians(startAngle + sweepAngle));

        // Middle point on the arc
        float arcMidPointX = center.centerX() + radius * (float) Math.cos(Math.toRadians(angleMiddle));
        float arcMidPointY = center.centerY() + radius * (float) Math.sin(Math.toRadians(angleMiddle));

        // This is the base of the contained triangle
        double basePointsDistance = Math.sqrt(
                Math.pow(arcEndPointX - arcStartPointX, 2) +
                        Math.pow(arcEndPointY - arcStartPointY, 2));

        // After reducing space from both sides of the "slice",
        //   the angle of the contained triangle should stay the same.
        // So let's find out the height of that triangle.
        float containedTriangleHeight = (float) (basePointsDistance / 2.0 *
                Math.tan(Math.toRadians((180.0 - angle) / 2.0)));

        // Now we subtract that from the radius
        float spacedRadius = radius - containedTriangleHeight;

        // And now subtract the height of the arc that's between the triangle and the outer circle
        spacedRadius -= Math.sqrt(
                Math.pow(arcMidPointX - (arcEndPointX + arcStartPointX) / 2.f, 2) +
                        Math.pow(arcMidPointY - (arcEndPointY + arcStartPointY) / 2.f, 2));

        return spacedRadius;
    }

    /**
     * 画文字
     */
    private void drawRateText(Canvas canvas) {
        mCurrentTextRect.setEmpty();
        mPreTextRect.setEmpty();
        mFirstTextRect.setEmpty();
        for (int index = 0; index < pieDataHolders.size(); index++) {
            PieDataHolder pieDataHolder = pieDataHolders.get(index);
            if (pieDataHolder.mSweepAngel <= 7) {
                continue;
            }
            if (tableMode == 1 && !pieDataHolder.mIsSelect) {
                continue;
            }
            mLinePaint.setColor(pieDataHolder.mColor);
            mLineDotPaint.setColor(pieDataHolder.mColor);
            if (pieDataHolder.mSweepAngel == 0) {
                // 没有比例的不画
                continue;
            }
            String accuracy = "%.0";
            if (pieDataHolder.mAccuracy != 0) {
                accuracy = accuracy + pieDataHolder.mAccuracy + "f";
            } else {
                accuracy = accuracy + "f";
            }
            String textMarker = String.format(accuracy, pieDataHolder.mValue);
            String TextDown = "";
            if (!mIsDrawRatio) {
                TextDown = String.format(Locale.getDefault(), "%.01f%s", pieDataHolder.mRatio * 100, "%");
            }
            if (textMarker == null) {
                continue;
            }
            // 找到圆弧一半的位置，要往这个方向拉出去
            float middle = (pieDataHolder.mStartAngel + pieDataHolder.mSweepAngel / 2 + mRotate) % 360;
            if (middle < 0) {
                middle += 360;
            }
            Path linePath = new Path();
            linePath.close();
            // 找到圆边缘上的点(分选中和没选中两种情况)
            final float startX;
            if (pieDataHolder.mIsSelect) {
                startX = (float) (getWidth() / 2 + (mPieRadius + mSelectOffset + dotMargin) * Math.cos(Math.toRadians(middle)));
            } else {
                startX = (float) (getWidth() / 2 + (mPieRadius + dotMargin) * Math.cos(Math.toRadians(middle)));
            }
            final float startY;
            if (pieDataHolder.mIsSelect) {
                startY = (float) (getHeight() / 2 + (mPieRadius + mSelectOffset + dotMargin) * Math.sin(Math.toRadians(middle)));
            } else {
                startY = (float) (getHeight() / 2 + (mPieRadius + dotMargin) * Math.sin(Math.toRadians(middle)));
            }
            linePath.moveTo(startX, startY);
            final float x;
            if (pieDataHolder.mIsSelect) {
                x = (float) (getWidth() / 2 + (mMarkerLine1 + mPieRadius + mSelectOffset + dotMargin) * Math.cos(Math.toRadians(middle)));
            } else {
                x = (float) (getWidth() / 2 + (mMarkerLine1 + mPieRadius + dotMargin) * Math.cos(Math.toRadians(middle)));
            }
            final float y;
            if (pieDataHolder.mIsSelect) {
                y = (float) (getHeight() / 2 + (mMarkerLine1 + mPieRadius + mSelectOffset + dotMargin) * Math.sin(Math.toRadians(middle)));
            } else {
                y = (float) (getHeight() / 2 + (mMarkerLine1 + mPieRadius + dotMargin) * Math.sin(Math.toRadians(middle)));
            }
            linePath.lineTo(x, y);
            float landLineX;
            float landLineXFix;
            // 左边 右边的判断
            if (270f > middle && middle >= 90f) {
                landLineX = x;
                landLineXFix = landLineX - mMarkerLine1;
            } else {
                landLineX = x;
                landLineXFix = landLineX + mMarkerLine1;
            }
            linePath.lineTo(landLineXFix, y); // 画文字线先确认了
            canvas.drawCircle(startX, startY, mLineDot, mLineDotPaint);
            canvas.drawPath(linePath, mLinePaint);
            if (270f > middle && middle >= 90f) {
                mTextPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(TextDown, landLineXFix - mMarkerLine1 * 0.1f, y + mTextCenterOffset, mTextPaint);
            } else {
                mTextPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(TextDown, landLineXFix + mMarkerLine1 * 0.1f, y + mTextCenterOffset, mTextPaint);
            }
        }
    }

    public void startAnimation() {
        mValueAnimator.setDuration(2000);
        mValueAnimator.start();
    }

    /**
     * 判断两个矩形是否有重叠的部分
     */
    private boolean isCollisionWithRect(Rect rect1, Rect rect2) {
        return isCollisionWithRect(rect1.left, rect1.top, rect1.width(), rect1.height(), rect2.left, rect2.top, rect2.width(),
                rect2.height());
    }

    /**
     * 判断两个矩形是否有重叠的部分
     */
    private boolean isCollisionWithRect(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        if (x1 >= x2 && x1 >= x2 + w2) {
            return false;
        } else if (x1 <= x2 && x1 + w1 <= x2) {
            return false;
        } else if (y1 >= y2 && y1 >= y2 + h2) {
            return false;
        } else if (y1 <= y2 && y1 + h1 <= y2) {
            return false;
        }
        return true;
    }

    /**
     * 设置饼状图数据(给外部调用的)
     */
    public void setPieData(List<PieDataHolder> pieDataList) {
        if (pieDataList == null || pieDataList.size() == 0) {
            return;
        }
        mRotate = 0;
        pieDataHolders.clear();
        pieDataHolders.addAll(pieDataList);
        // 计算每个饼状图的比例，开始角度，扫过的角度
        float sum = 0;
        for (PieDataHolder pieDataHolder : pieDataHolders) {
            sum += pieDataHolder.mValue;
        }
        float preSum = 0; // 当前位置之前的总的值，算开始角度用的，总共360
        for (int index = 0; index < pieDataList.size(); index++) {
            PieDataHolder pieDataHolder = pieDataHolders.get(index);
            pieDataHolder.mIsSelect = false;
            pieDataHolder.mPosition = index;
            pieDataHolder.mRatio = Float.parseFloat(mDecimalFormat.format(pieDataHolder.mValue / sum));
            pieDataHolder.mStartAngel = preSum;//preSum / sum * 360f;
            if (index == pieDataList.size() - 1) {
                // 如果是最后一个 目的是避免精度的问题
                pieDataHolder.mSweepAngel = 360 - pieDataHolder.mStartAngel;
            } else {
                pieDataHolder.mSweepAngel = pieDataHolder.mRatio * 360;
            }
            preSum += pieDataHolder.mSweepAngel;

        }
        invalidate();
    }

    /**
     * 设置PieGraph的监听（外部调用）
     */
    public void setOnPieGraphListener(OnPieGraphListener listener) {
        mListener = listener;
    }

    /**
     * 0: 一直显示
     * 1: 点击触发
     */
    private int tableMode = 0;

    public void setTabelMode(int mode) {
        tableMode = mode;
    }

    /**
     * 放出一些事件来
     */
    private float mPreX = -1;
    private float mPreY = -1;
    private boolean mDealMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (pieDataHolders == null || pieDataHolders.size() <= 0) {
            return false;
        }
        float eventX = event.getX();
        float eventY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN && !inCircle(eventX, eventY)) {
            // down事件的时候不在园内，这个事件我们不要了
            clearHolderSelect(-1);
            invalidate();
            if (mListener != null) {
                mListener.onNoPieSelect();
            }
            return false;
        }
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mPreX = event.getX();
//                mPreY = event.getY();
//                mDealMove = false;
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                // 想让饼状图旋转起来
//                if (!inCircle(eventX, eventY)) {
//                    // 没有在园内 这个事件结束了，我们不要了
//                    mDealMove = false;
//                    return false;
//                }
//                float offsetX = eventX - mPreX;
//                float offsetY = eventY - mPreY;
////                && Math.sqrt(offsetX * offsetX + offsetY * offsetY) >= mTouchSlop
//                if (mCanRotate) {
//                    if (!mDealMove) {
//                        mDealMove = true;
//                    }
//                    mRotate = mRotate + action2Angle(eventX, eventY) - action2Angle(mPreX, mPreY);
//                    mPreX = eventX;
//                    mPreY = eventY;
//                    invalidate();
//                }
//                return true;
//            case MotionEvent.ACTION_UP:
//                if (!mDealMove) {
//                    // 这里我们去判断是否是点击事件
//                    if (inCircle(eventX, eventY)) {
//                        int position = getHolderPositionByAngle(action2Angle(eventX, eventY));
//                        clearHolderSelect(position);
//                        PieDataHolder holder = getHolderByPosition(position);
//                        if (holder != null) {
//                            holder.mIsSelect = !holder.mIsSelect;
//                        }
//                    } else {
//                        // 不在圆内，清空掉以前的选择
//                        clearHolderSelect(-1);
//                    }
//                    if (mListener != null) {
//                        // 找出选中的那个
//                        PieDataHolder holder = findSelectHolder();
//                        if (holder == null) {
//                            mListener.onNoPieSelect();
//                        } else {
//                            mListener.onPieSelect(holder);
//                        }
//                    }
//                    invalidate();
//
//                }
//                break;
//        }
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 点击的点映射到圆上的角度
     */
    private int action2Angle(float x, float y) {
        int angle = 0;
        // 第一象限
        if (x >= getMeasuredWidth() / 2 && y >= getMeasuredHeight() / 2) {
            angle = (int) (Math.atan((y - getMeasuredHeight() / 2) * 1.0f / (x - getMeasuredWidth() / 2)) * 180 / Math.PI);
        }
        // 第二象限
        if (x <= getMeasuredWidth() / 2 && y >= getMeasuredHeight() / 2) {
            angle = (int) (Math.atan((getMeasuredWidth() / 2 - x) / (y - getMeasuredHeight() / 2)) * 180 / Math.PI + 90);
        }
        // 第三象限
        if (x <= getMeasuredWidth() / 2 && y <= getMeasuredHeight() / 2) {
            angle = (int) (Math.atan((getMeasuredHeight() / 2 - y) / (getMeasuredWidth() / 2 - x)) * 180 / Math.PI + 180);
        }
        // 第四象限
        if (x >= getMeasuredWidth() / 2 && y <= getMeasuredHeight() / 2) {
            angle = (int) (Math.atan((x - getMeasuredWidth() / 2) / (getMeasuredHeight() / 2 - y)) * 180 / Math.PI + 270);
        }
        return angle;
    }

    /**
     * 通过角度去找我们holder
     */
    private int getHolderPositionByAngle(int angle) {
        if (pieDataHolders == null || pieDataHolders.size() <= 0) {
            return -1;
        }
        for (PieDataHolder pieDataHolder : pieDataHolders) {
            // 这里我们拿到真正的开始角度
            float realStartAngel = (pieDataHolder.mStartAngel + mRotate) % 360;
            if (realStartAngel < 0) {
                realStartAngel += 360;
            }
            if (realStartAngel + pieDataHolder.mSweepAngel > 360) {
                if (angle >= realStartAngel) {
                    return pieDataHolder.mPosition;
                } else {
                    if (angle < realStartAngel + pieDataHolder.mSweepAngel - 360) {
                        return pieDataHolder.mPosition;
                    }
                }
            } else {
                if (angle >= realStartAngel && angle < realStartAngel + pieDataHolder.mSweepAngel) {
                    return pieDataHolder.mPosition;
                }
            }
        }
        return -1;
    }

    /**
     * 通过position去找到holder
     */
    private PieDataHolder getHolderByPosition(int position) {
        if (pieDataHolders == null || pieDataHolders.size() <= 0) {
            return null;
        }
        for (PieDataHolder pieDataHolder : pieDataHolders) {
            if (pieDataHolder.mPosition == position) {
                return pieDataHolder;
            }
        }
        return null;
    }

    /**
     * 找到选中的那个holder
     */
    private PieDataHolder findSelectHolder() {
        if (pieDataHolders == null || pieDataHolders.size() <= 0) {
            return null;
        }
        for (PieDataHolder pieDataHolder : pieDataHolders) {
            if (pieDataHolder.mIsSelect) {
                return pieDataHolder;
            }
        }
        return null;
    }

    /**
     * 设置洞里的文字
     *
     * @param holeText
     */
    private void setHoleText(String holeText) {
        mHoleText = holeText;
    }

    /**
     * 清除掉之前的选择，position位置的状态保留
     *
     * @param position: 这个位置的状态不清除
     */
    private void clearHolderSelect(int position) {
        for (PieDataHolder pieDataHolder : pieDataHolders) {
            if (position != pieDataHolder.mPosition) {
                pieDataHolder.mIsSelect = false;
            }
        }
    }

    /**
     * 是否在饼图园范围内
     */
    private boolean inCircle(float x, float y) {
        return Math.sqrt(Math.pow(x - getWidth() / 2, 2) + Math.pow(y - getHeight() / 2, 2)) < mPieRadius;
    }

    /**
     * 饼状图里面每个饼的信息
     */
    public static final class PieDataHolder {

        /**
         * 具体的值
         */
        public float mValue;

        /**
         * 比例
         */
        public float mRatio;

        /**
         * 颜色
         */
        public int mColor;

        /**
         * 文字标记
         */
        public String mMarker;

        /**
         * 起始弧度
         */
        public float mStartAngel;

        /**
         * 扫过的弧度
         */
        public float mSweepAngel;

        /**
         * 是否选中
         */
        public boolean mIsSelect;

        /**
         * 位置下标
         */
        public int mPosition;

        public int mAccuracy;

        public PieDataHolder(float value, int color, String label) {
            mValue = value;
            mColor = color;
            mMarker = label;
        }

        public PieDataHolder(float value, int color, String label, int accuracy) {
            mValue = value;
            mColor = color;
            mMarker = label;
            mAccuracy = accuracy;
        }

    }
}
