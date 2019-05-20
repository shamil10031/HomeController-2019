package shomazzapp.com.homecontorl.ui.views

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
    private var currentValue: Float = 65f
    private val oval = RectF()
    private val strokeWidth = 10f

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
        backgroundPaint.color = typedAttrs.getColor(
                R.styleable.CircularProgressBar_backgroundColor,
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
        )
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = strokeWidth

        foregroundPaint.color = typedAttrs.getColor(
                R.styleable.CircularProgressBar_backgroundColor,
                ContextCompat.getColor(context, R.color.colorPrimaryLight)
        )
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = strokeWidth

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = Math.min(w, h) / 2f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)

        val size = Math.min(w, h)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = x + width / 2
        val centerY = y + height / 2

        oval.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(oval, 90f, 360f, false, backgroundPaint)
        canvas.drawArc(oval, 150f, currentValue / maxValue * 360, false, foregroundPaint)
    }
}

