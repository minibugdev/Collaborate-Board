package com.trydroid.coboard

import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.*

class CoBoardActivity : AppCompatActivity() {
	private val mLinesReference: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference.child(CHILD_LINES) }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setupDrawView()
	}

	override fun onStart() {
		super.onStart()
		mLinesReference.addChildEventListener(mLinesReferenceListener)

	}

	override fun onStop() {
		super.onStop()
		mLinesReference.removeEventListener(mLinesReferenceListener)
	}

	private fun setupDrawView() {
	}

	private fun sendToFirebase(drawMove: Point) {
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

	companion object {
		private val TAG = CoBoardActivity::class.java.simpleName
		private val CHILD_LINES = "lines"
	}
}
