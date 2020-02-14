package com.marsapps.triptip.country

import android.view.View

interface OnExpandTextClickListener {
    fun onExpandClick(viewId: Int): View.OnClickListener?
}