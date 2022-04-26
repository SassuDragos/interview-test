package com.englishscore.interview.ui.view

import android.graphics.*
import android.graphics.drawable.Drawable

const val radius = 30f

internal class ProgressDrawable(
    private val numberOfSegments: Int,
    private val mForeground: Int,
    private val mBackground: Int
) :
    Drawable() {
    private val mPaint: Paint = Paint()
    private val mSegment = RectF()
    override fun onLevelChange(level: Int): Boolean {
        invalidateSelf()
        return true
    }

    private val rHorizontalCorner = floatArrayOf(
        0f, 0f,   // Top left radius in px
        radius, radius,   // Top right radius in px
        radius, radius,     // Bottom right radius in px
        0f, 0f      // Bottom left radius in px
    )

    private val lHorizontalCorner = floatArrayOf(
        radius, radius,   // Top left radius in px
        0f, 0f,   // Top right radius in px
        0f, 0f,     // Bottom right radius in px
        radius, radius      // Bottom left radius in px
    )

    override fun draw(canvas: Canvas) {
        val firstSegmentIndex = 0
        val lastSegmentIndex = numberOfSegments - 1
        val level = level / 10000f
        val b: Rect = bounds
        val gapWidth: Float = b.height() / 10f
        val segmentWidth: Float =
            (b.width() - (numberOfSegments - 1) * gapWidth) / numberOfSegments
        mSegment[0f, 0f, segmentWidth] = b.height().toFloat()
        mPaint.color = mForeground
        for (i in 0 until numberOfSegments) {
            val loLevel = i / numberOfSegments.toFloat()
            val hiLevel = (i + 1) / numberOfSegments.toFloat()
            if (level in loLevel..hiLevel) {
                val middle: Float =
                    mSegment.left + numberOfSegments * segmentWidth * (level - loLevel)
                val rect = RectF(mSegment.left, mSegment.top, middle, mSegment.bottom)

                if (i == firstSegmentIndex) {
                    val path = Path()
                    path.addRoundRect(rect, lHorizontalCorner, Path.Direction.CW)
                    canvas.drawPath(path, mPaint)
                } else {
                    canvas.drawRect(mSegment.left, mSegment.top, middle, mSegment.bottom, mPaint)
                }

                mPaint.color = mBackground
                if (i == lastSegmentIndex) {
                    val path = Path()
                    val lastSegmentRectangle =
                        RectF(middle, mSegment.top, mSegment.right, mSegment.bottom)
                    path.addRoundRect(lastSegmentRectangle, rHorizontalCorner, Path.Direction.CW)
                    canvas.drawPath(path, mPaint)
                } else {
                    canvas.drawRect(middle, mSegment.top, mSegment.right, mSegment.bottom, mPaint)
                }
            } else {
                if (i == lastSegmentIndex) {
                    val path = Path()
                    path.addRoundRect(mSegment, rHorizontalCorner, Path.Direction.CW)
                    canvas.drawPath(path, mPaint)
                } else if (i == firstSegmentIndex) {
                    val path = Path()
                    path.addRoundRect(mSegment, lHorizontalCorner, Path.Direction.CW)
                    canvas.drawPath(path, mPaint)
                } else {
                    canvas.drawRect(mSegment, mPaint)
                }
            }
            mSegment.offset(mSegment.width() + gapWidth, 0f)
        }
    }


    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}

private fun Canvas.drawTopRoundRect(rect: RectF, paint: Paint, radius: Float) {
    drawRoundRect(rect, radius, radius, paint)

    // Step 2. Draw simple rect with reduced height,
    // so it wont cover top rounded corners.
    drawRect(
        rect.left,
        rect.top + radius,
        rect.right,
        rect.bottom,
        paint
    )
}