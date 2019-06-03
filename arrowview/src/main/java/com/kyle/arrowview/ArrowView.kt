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

class ArrowView : View {
    private lateinit var mPaint: Paint
    //返回箭头的两根线条长度,单位dp
    private var mLineLength: Float = 0.0f
    //两根线条夹角
    private var mLineRadius: Double = 0.0
    //箭头方向
    private var mDirection = Direction.LEFT


    enum class Direction {
        TOP, LEFT, RIGHT, BOTTOM
    }

    constructor(context: Context) : super(context) {
        init()
    }

    /***
     * 初始化
     */
    private fun init() {
        mLineLength = DensityUtil.dip2px(context, 14F).toFloat()
        mLineRadius = Math.toRadians(90.0)

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = Color.parseColor("#606060")
        mPaint.strokeWidth = DensityUtil.dip2px(context, 2f).toFloat()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    class Builder(view: ArrowView) {
        var arrowView = view
        /***
         * 设置线的宽度
         */
        fun setLineWidth(width: Float): Builder {
            arrowView.mPaint.strokeWidth = DensityUtil.dip2px(arrowView.context, width).toFloat()
            return this
        }

        /***
         * 设置线的长度,单位dp
         */
        fun setLineLength(length: Float): Builder {
            arrowView.mLineLength = DensityUtil.dip2px(arrowView.context, length).toFloat()
            return this
        }

        /***
         * 设置方向，有TOP、LEFT、RIGHT、BOTTOM可选
         */
        fun setDirection(direction: Direction): Builder {
            arrowView.mDirection = direction
            return this
        }

        /***
         * 颜色，非资源文件路径，格式如:Color.WHITE、Color.parseColor("#FFF")
         */
        fun setColor(color: Int): Builder {
            arrowView.mPaint.color = color
            return this
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.e("onDraw", "onDraw")
        val points: ArrayList<Float> = ArrayList()
        var headPointX: Float
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
        var heightSpec=heightMeasureSpec
        var widthSpec=widthMeasureSpec
        val overX = (Math.sin(mLineRadius/2) * mPaint.strokeWidth / 2).toFloat()
        val overY = (Math.cos(mLineRadius/2) * mPaint.strokeWidth / 2).toFloat()
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            var height = 0f
            height = when (mDirection) {
                Direction.TOP, Direction.BOTTOM -> {
                    (Math.cos(mLineRadius / 2) * mLineLength).toFloat()+2*overY
                }
                Direction.LEFT, Direction.RIGHT -> {
                    2 * (Math.sin(mLineRadius / 2) * mLineLength).toFloat()
                }
            }
            heightSpec=MeasureSpec.makeMeasureSpec(height.toInt(),MeasureSpec.EXACTLY)
        }
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            var width = 0f
            width = when (mDirection) {
                Direction.TOP, Direction.BOTTOM -> {
                    2 * (Math.sin(mLineRadius / 2) * mLineLength).toFloat()
                }
                Direction.LEFT, Direction.RIGHT -> {
                    (Math.cos(mLineRadius / 2) * mLineLength).toFloat()+2*overX
                }
            }
            widthSpec=MeasureSpec.makeMeasureSpec(width.toInt(),MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthSpec, heightSpec)
    }
}
