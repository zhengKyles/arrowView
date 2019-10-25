package com.kyle.arrowview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.*

open class ArrowView : View {
    private lateinit var mPaint: Paint
    //返回箭头的两根线条长度,单位dp
    private var mLineLength: Float = 0.0f
    //返回箭头的两根线条宽度,单位dp
    private var mLineWidth: Float = 0.0f
    //两根线条夹角
    private var mLineRadius: Double = 0.0
    //箭头方向
    private var mDirection = Direction.LEFT

    private var DEFAULT_LINE_COLOR = Color.parseColor("#606060")
    private var DEFAULT_LINE_LENGTH = 14F
    private var DEFAULT_LINE_WIDTH = 2F

    private var mLineColor = DEFAULT_LINE_COLOR


    enum class Direction {
        TOP, LEFT, RIGHT, BOTTOM
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArrowView)
        mLineColor = typedArray.getColor(R.styleable.ArrowView_av_lineColor, DEFAULT_LINE_COLOR)
        mDirection= Direction.values()[typedArray.getInt(R.styleable.ArrowView_av_direction,Direction.LEFT.ordinal)]
        mLineLength=typedArray.getDimension(R.styleable.ArrowView_av_line_length,
            DensityUtil.dip2px(context,DEFAULT_LINE_LENGTH).toFloat()
        )
        mLineWidth=typedArray.getDimension(R.styleable.ArrowView_av_line_width,
            DensityUtil.dip2px(context,DEFAULT_LINE_WIDTH).toFloat()
        )
        init()
    }

    /***
     * 初始化
     */
    private fun init() {
        mLineRadius = Math.toRadians(90.0)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = mLineColor
        mPaint.strokeWidth = mLineWidth
        invalidate()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.e("onDraw", "onDraw")
        val points: ArrayList<Float> = ArrayList()
        val headPointX: Float
        var headPointY = 0.0f
        val halfRadius = mLineRadius / 2
        val sinHalfRadius = Math.sin(halfRadius)
        val cosHalfRadius = Math.cos(halfRadius)
        val overX = (sinHalfRadius * mPaint.strokeWidth / 2).toFloat()
        val overY = (cosHalfRadius * mPaint.strokeWidth / 2).toFloat()
        val lineLengthHalfCos = (cosHalfRadius * mLineLength).toFloat()
        val lineLengthHalfSin = (sinHalfRadius * mLineLength).toFloat()

        when (mDirection) {
            Direction.TOP -> {
                headPointX = lineLengthHalfCos
                points.add(headPointX + overX)
                points.add(headPointY + overY)
                points.add(0f + overX)
                points.add(lineLengthHalfSin + overY)
                points.add(headPointX - overX)
                points.add(headPointY + overY)
                points.add(2 * headPointX - overX)
                points.add(lineLengthHalfSin + overY)
            }
            Direction.LEFT -> {
                headPointY = lineLengthHalfSin
                headPointX = overX
                points.add(headPointX)
                points.add(headPointY + overY)
                points.add(lineLengthHalfCos + overX)
                points.add(0f + overY)
                points.add(headPointX)
                points.add(headPointY - overY)
                points.add(lineLengthHalfCos + overX)
                points.add(2 * headPointY - overY)
            }
            Direction.RIGHT -> {
                headPointY = lineLengthHalfSin
                headPointX = lineLengthHalfCos
                points.add(headPointX + overX)
                points.add(headPointY + overY)
                points.add(0f + overX)
                points.add(0f + overY)
                points.add(headPointX)
                points.add(headPointY)
                points.add(0f + overX)
                points.add(2 * headPointY - overY)
            }
            Direction.BOTTOM -> {
                headPointX = lineLengthHalfSin
                headPointY = lineLengthHalfCos
                points.add(headPointX + overX)
                points.add(headPointY + overY)
                points.add(0f + overX)
                points.add(0f + overY)
                points.add(headPointX)
                points.add(headPointY)
                points.add(2 * headPointX - overX)
                points.add(0f + overY)
            }
        }
        canvas?.drawLines(points.toFloatArray(), mPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightSpec = heightMeasureSpec
        var widthSpec = widthMeasureSpec
        val overX = (Math.sin(mLineRadius / 2) * mPaint.strokeWidth / 2).toFloat()
        val overY = (Math.cos(mLineRadius / 2) * mPaint.strokeWidth / 2).toFloat()
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            var height = 0f
            height = when (mDirection) {
                Direction.TOP, Direction.BOTTOM -> {
                    (Math.cos(mLineRadius / 2) * mLineLength).toFloat() + 2 * overY
                }
                Direction.LEFT, Direction.RIGHT -> {
                    2 * (Math.sin(mLineRadius / 2) * mLineLength).toFloat()
                }
            }
            heightSpec = MeasureSpec.makeMeasureSpec(height.toInt(), MeasureSpec.EXACTLY)
        }
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            var width = 0f
            width = when (mDirection) {
                Direction.TOP, Direction.BOTTOM -> {
                    2 * (Math.sin(mLineRadius / 2) * mLineLength).toFloat()
                }
                Direction.LEFT, Direction.RIGHT -> {
                    (Math.cos(mLineRadius / 2) * mLineLength).toFloat() + 2 * overX
                }
            }
            widthSpec = MeasureSpec.makeMeasureSpec(width.toInt(), MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthSpec, heightSpec)
    }
}
