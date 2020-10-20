package net.fionix.minima.util

import android.content.DialogInterface

class OnButtonClickDismissAlertDialog(val functionCallback: () -> Unit) : DialogInterface.OnClickListener {

    override fun onClick(p0: DialogInterface?, p1: Int) {
        functionCallback()
        p0?.dismiss()
    }
}