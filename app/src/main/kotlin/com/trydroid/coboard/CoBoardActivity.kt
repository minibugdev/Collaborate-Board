package com.trydroid.coboard

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class CoBoardActivity : AppCompatActivity() {
	private val mLinesReference: DatabaseReference by lazy {
		FirebaseDatabase.getInstance().reference.child(CHILD_LINES)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_co_board, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem?) =
		when (item?.itemId) {
			R.id.action_clear -> consumeMenuSelected { removeFirebaseChild() }
			else              -> super.onOptionsItemSelected(item)
		}


	override fun onStart() {
		super.onStart()
		mLinesReference.addChildEventListener(mLinesReferenceListener)
		drawView.drawListener = { lineList ->
			lineList?.let { sendToFirebase(lineList) }
		}
	}

	override fun onStop() {
		super.onStop()
		mLinesReference.removeEventListener(mLinesReferenceListener)
		drawView.drawListener = null
	}

	private fun sendToFirebase(lineList: List<Point>) {
		mLinesReference.push().setValue(lineList)
	}

	private fun removeFirebaseChild() {
		mLinesReference.removeValue()
	}

	private fun clearDrawView() {
		drawView.clear()
	}

	private fun drawLine(lineList: List<Point>) {
		drawView.drawLine(lineList)
	}

	private val mLinesReferenceListener = object : ChildEventListener {
		override fun onChildAdded(dataSnapshot: DataSnapshot?, p1: String?) {
			Log.e(TAG, "onChildAdded")
			dataSnapshot?.children
				?.map { children -> children.getValue<Point>(Point::class.java) }
				?.let { lineList -> drawLine(lineList) }
		}

		override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
			Log.e(TAG, "onChildRemoved")
			clearDrawView()
		}

		override fun onChildMoved(dataSnapshot: DataSnapshot?, p1: String?) {
		}

		override fun onChildChanged(dataSnapshot: DataSnapshot?, p1: String?) {
		}

		override fun onCancelled(databaseError: DatabaseError) {
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
