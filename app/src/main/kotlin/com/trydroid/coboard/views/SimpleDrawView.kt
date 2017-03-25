package com.trydroid.coboard.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

typealias OnDrawListener = (List<Point>?) -> Unit

/**
 * Customize form original source
 * - https://github.com/johncarl81/androiddraw/blob/master/src/main/java/org/example/androiddraw/SimpleDrawView.java
 * - https://github.com/ByoxCode/DrawView/blob/master/drawview/src/main/java/com/byox/drawview/views/DrawView.java
 * */
class SimpleDrawView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet), View.OnTouchListener {
	private val mLineHistoryList: MutableList<MutableList<Point>> = mutableListOf()
	private val mPaint: Paint by lazy {
		Paint(Paint.ANTI_ALIAS_FLAG).apply {
			strokeWidth = STROKE_WIDTH
			style = Paint.Style.STROKE
			color = Color.RED
		}
	}

	var drawListener: OnDrawListener? = null

	init {
		isFocusable = true
		isFocusableInTouchMode = true
		this.setOnTouchListener(this)
	}

	override fun onDraw(canvas: Canvas) {
		val path = Path()
		mLineHistoryList.forEach { line ->
			line.forEachIndexed { index, point ->
				val x = point.x.toFloat()
				val y = point.y.toFloat()
				if (index == 0) {
					path.moveTo(x, y)
				}
				else {
					path.lineTo(x, y)
				}
			}
			canvas.drawPath(path, mPaint)
		}
	}

	override fun onTouch(view: View, event: MotionEvent): Boolean {
		when (event.action) {
			MotionEvent.ACTION_DOWN -> {
				onTouchStart()
			}
			MotionEvent.ACTION_MOVE -> {
				onTouchMove(event)
			}
			MotionEvent.ACTION_UP   -> {
				onTouchEnd()
			}
		}

		return true
	}

	fun clear() {
		mLineHistoryList.clear()
		invalidate()
	}

	fun drawLine(lineList: List<Point>) {
		mLineHistoryList.add(lineList.toMutableList())
		invalidate()
	}

	private fun onTouchStart() {
		mLineHistoryList.add(mutableListOf())
	}

	private fun onTouchMove(event: MotionEvent) {
		val point = Point().apply {
			x = event.x.toInt()
			y = event.y.toInt()
		}
		mLineHistoryList.last().add(point)
		invalidate()
	}

	private fun onTouchEnd() {
		drawListener?.invoke(mLineHistoryList.lastOrNull())
	}

	companion object {
		private val STROKE_WIDTH = 4f
	}
}
