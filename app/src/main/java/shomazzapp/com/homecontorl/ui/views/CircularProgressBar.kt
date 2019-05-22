package shomazzapp.com.homecontorl.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import shomazzapp.com.homecontorl.R

class CircularProgressBar : View {

    private val foregroundPaint: Paint = Paint()
    private val backgroundPaint: Paint = Paint()

    private var radius = 0.toFloat()
    private var maxValue: Float = 0f
    private var currentValue: Float = 0f

    private val oval = RectF()
    private var strokeWidth = 10f
    private var startAngle = 90f
    private var sweepDistance = 360f
    private val sweepAnimationDurataion = 500L

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initPaints(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        initPaints(context, attrs)
    }

    private fun initPaints(context: Context, attrs: AttributeSet) {
        val typedAttrs = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar)

        maxValue = typedAttrs.getFloat(R.styleable.CircularProgressBar_maxValue, 0f)
        startAngle = typedAttrs.getFloat(R.styleable.CircularProgressBar_startAngle, 90f)
        sweepDistance = typedAttrs.getFloat(R.styleable.CircularProgressBar_sweepDistance, 360f)

        backgroundPaint.color = typedAttrs.getColor(
                R.styleable.CircularProgressBar_backgroundColor,
                ContextCompat.getColor(context, android.R.color.holo_red_light)
        )
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = strokeWidth

        foregroundPaint.color = typedAttrs.getColor(
                R.styleable.CircularProgressBar_backgroundColor,
                ContextCompat.getColor(context, android.R.color.holo_green_light)
        )
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = strokeWidth

        typedAttrs.recycle()

    }

    fun setValueWithAnimation(newValue: Float) {
        ValueAnimator.ofFloat(currentValue, newValue).apply {
            duration = sweepAnimationDurataion
            addUpdateListener {
                currentValue = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = Math.min(w, h) / 2f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)

        val size = Math.min(w, h)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        oval.set(0f + strokeWidth, 0f + strokeWidth, width.toFloat() - strokeWidth, height.toFloat() - strokeWidth)
        canvas.drawArc(oval, startAngle, sweepDistance, false, backgroundPaint)
        canvas.drawArc(oval, startAngle, currentValue / maxValue * sweepDistance, false, foregroundPaint)
    }
}

