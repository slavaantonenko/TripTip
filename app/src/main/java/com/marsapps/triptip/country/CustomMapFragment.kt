package com.marsapps.triptip.country

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.SupportMapFragment

class CustomMapFragment : SupportMapFragment() {

    companion object {
        var onTouchListener: OnTouchListener? = null
    }

    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, savedInstance: Bundle?): View? {
        val layout = super.onCreateView(layoutInflater, viewGroup, savedInstance)

        val frameLayout = TouchableWrapper(context!!)

        frameLayout.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent))

        (layout as ViewGroup?)!!.addView(frameLayout,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        return layout
    }

    fun setListener(listener: OnTouchListener) {
        onTouchListener = listener
    }

    interface OnTouchListener {
        fun onTouch()
    }

    class TouchableWrapper(context: Context) : FrameLayout(context) {

        override fun dispatchTouchEvent(event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> onTouchListener?.onTouch()
                MotionEvent.ACTION_UP -> onTouchListener?.onTouch()
            }
            return super.dispatchTouchEvent(event)
        }
    }
}