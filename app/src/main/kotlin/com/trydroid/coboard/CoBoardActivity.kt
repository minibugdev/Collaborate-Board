package com.trydroid.coboard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.byox.drawview.views.DrawView
import kotlinx.android.synthetic.main.activity_main.*


class CoBoardActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setupDrawView()
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_co_board, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?) =
		when (item?.itemId) {
			R.id.action_clear -> consumeMenuSelected { clearDrawView() }
			R.id.action_undo  -> consumeMenuSelected { undoDrawView() }
			R.id.action_redo  -> consumeMenuSelected { redoDrawView() }
			else              -> super.onOptionsItemSelected(item)
		}

	private fun setupDrawView() {
		drawView.setOnDrawViewListener(mDrawViewListener)
	}

	private fun redoDrawView() {
		if (drawView.canRedo()) drawView.redo()
	}

	private fun undoDrawView() {
		if (drawView.canUndo()) drawView.undo()
	}

	private fun clearDrawView() {
		drawView.restartDrawing()
	}

	private val mDrawViewListener = object : DrawView.OnDrawViewListener {
		override fun onStartDrawing() {
		}

		override fun onRequestText() {
		}

		override fun onEndDrawing() {
		}

		override fun onClearDrawing() {
		}
	}

	inline fun consumeMenuSelected(func: () -> Unit): Boolean {
		func()
		return true
	}
}
