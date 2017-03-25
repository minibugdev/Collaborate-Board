package com.trydroid.coboard.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Customize form original source
 * - https://github.com/johncarl81/androiddraw/blob/master/src/main/java/org/example/androiddraw/SimpleDrawView.java
 * - https://github.com/ByoxCode/DrawView/blob/master/drawview/src/main/java/com/byox/drawview/views/DrawView.java
 * */
class SimpleDrawView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet), View.OnTouchListener {
	private val mPointList: MutableList<MutableList<Point>> = mutableListOf()
	private val mPaint: Paint by lazy {
		Paint(Paint.ANTI_ALIAS_FLAG).apply {
			strokeWidth = STROKE_WIDTH
			style = Paint.Style.STROKE
			color = Color.RED
		}
	}

	init {
		isFocusable = true
		isFocusableInTouchMode = true
		this.setOnTouchListener(this)
	}

	override fun onDraw(canvas: Canvas) {
		val path = Path()
		mPointList.forEach { pointHistory ->
			pointHistory.forEachIndexed { index, point ->
				if (index == 0) {
					path.moveTo(point.x, point.y)
				}
				else {
					path.lineTo(point.x, point.y)
				}
			}
			canvas.drawPath(path, mPaint)
		}
	}

	fun clear() {
		mPointList.clear()
		invalidate()
	}

	override fun onTouch(view: View, event: MotionEvent): Boolean {
		when (event.action) {
			MotionEvent.ACTION_DOWN -> {
				onTouchStart()
			}
			MotionEvent.ACTION_MOVE -> {
				onTouchMove(event)
			}
		}

		return true
	}

	private fun onTouchStart() {
		mPointList.add(mutableListOf())
	}

	private fun onTouchMove(event: MotionEvent) {
		mPointList.last()
			.add(Point(x = event.x,
					   y = event.y))
		invalidate()
	}

	private inner class Point(val x: Float = 0f,
							  val y: Float = 0f) {

		override fun toString(): String {
			return "$x,$y"
		}
	}

	companion object {
		private val STROKE_WIDTH = 4f
	}
}
