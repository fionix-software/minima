package net.fionix.minima.util

import android.content.DialogInterface

class OnButtonClickDismissAlertDialog() : DialogInterface.OnClickListener {

    override fun onClick(p0: DialogInterface?, p1: Int) {
        p0?.dismiss()
    }
}