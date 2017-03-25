package com.trydroid.coboard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.*
import com.trydroid.coboard.views.SimpleDrawView
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
			R.id.action_clear -> consumeMenuSelected { clearDrawView() }
			else              -> super.onOptionsItemSelected(item)
		}


	override fun onStart() {
		super.onStart()
		mLinesReference.addChildEventListener(mLinesReferenceListener)
		drawView.drawListener = { pointList ->
			pointList?.let { sendToFirebase(pointList) }
		}
	}

	override fun onStop() {
		super.onStop()
		mLinesReference.removeEventListener(mLinesReferenceListener)
		drawView.drawListener = null
	}

	private fun sendToFirebase(pointList: List<SimpleDrawView.Point>) {
		mLinesReference.push().setValue(pointList)
	}

	private fun clearFirebase() {
		mLinesReference.push().removeValue()
	}

	private fun clearDrawView() {
		drawView.clear()
		clearFirebase()
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

	inline fun consumeMenuSelected(func: () -> Unit): Boolean {
		func()
		return true
	}

	companion object {
		private val TAG = CoBoardActivity::class.java.simpleName

		private val CHILD_LINES = "lines"
	}
}
