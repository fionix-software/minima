package net.fionix.minima.util

import android.content.Context
import android.content.DialogInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.database.DatabaseMain

class OnButtonClickConfirmClearDataAlertDialog(val ctx: Context) : DialogInterface.OnClickListener {

    override fun onClick(p0: DialogInterface?, p1: Int) {

        // get course list
        GlobalScope.launch(Dispatchers.IO) {

            // parse cursor
            DatabaseMain.getDatabase(ctx).timetableDao().clearData()

            // update
            withContext(Dispatchers.Main) {
                p0?.dismiss()
            }
        }
    }
}