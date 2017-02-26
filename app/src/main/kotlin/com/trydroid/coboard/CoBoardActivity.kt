package com.trydroid.coboard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.byox.drawview.dictionaries.DrawMove
import com.byox.drawview.enums.DrawingMode
import com.google.firebase.database.*
import com.trydroid.coboard.views.ExtenedDrawView
import kotlinx.android.synthetic.main.activity_main.*

class CoBoardActivity : AppCompatActivity() {
	private val mLinesReference: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference.child(CHILD_LINES) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setupDrawView()
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_co_board, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onStart() {
		super.onStart()
		mLinesReference.addChildEventListener(mLinesReferenceListener)
		drawView.setOnDrawViewListener(mDrawViewListener)

	}

	override fun onStop() {
		super.onStop()
		mLinesReference.removeEventListener(mLinesReferenceListener)
		drawView.setOnDrawViewListener(null)
	}

	override fun onOptionsItemSelected(item: MenuItem?) =
		when (item?.itemId) {
			R.id.action_clear -> consumeMenuSelected { clearDrawView() }
			R.id.action_undo  -> consumeMenuSelected { undoDrawView() }
			R.id.action_redo  -> consumeMenuSelected { redoDrawView() }
			else              -> super.onOptionsItemSelected(item)
		}

	private fun setupDrawView() {
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

	private fun sendToFirebase(drawMove: DrawMove) {
		mLinesReference.push().setValue(drawMove)
	}

	private val mLinesReferenceListener = object : ChildEventListener {
		override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
		}

		override fun onChildChanged(dataSnapshot: DataSnapshot?, p1: String?) {
		}

		override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
			Log.e(TAG, "onDataChange")
		}

		override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
		}

		override fun onCancelled(databaseError: DatabaseError) {
			Log.w(TAG, "Lines Reference onCancelled", databaseError.toException())
		}
	}

	private val mDrawViewListener = object : ExtenedDrawView.OnDrawViewListener {
		override fun onStartDrawing() {
		}

		override fun onEndDrawing() {
			val lastestDrawMove = drawView.drawMoveHistory.lastOrNull { drawMove -> DrawingMode.DRAW == drawMove.drawingMode }
			lastestDrawMove?.let { sendToFirebase(it) }
		}

		override fun onRequestText() {
		}

		override fun onClearDrawing() {
		}
	}

	inline fun consumeMenuSelected(func: () -> Unit): Boolean {
		func()
		return true
	}

	companion object {
		private val TAG = CoBoardActivity::class.java.simpleName
		private val CHILD_LINES = "lines"
	}
}
