package net.fionix.minima

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.EntityTimetable
import net.fionix.minima.util.OnButtonClickDismissAlertDialog
import net.fionix.minima.util.UtilData


class ActivityTimetableTable : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_table_timetable, container, false)

        // interface component
        val timetableView = ViewTimetable(view.context)
        timetableView.generateTimetableView(view.findViewById(R.id.tableLayout))

        // get timetable list
        GlobalScope.launch(Dispatchers.IO) {

            val timetableList = ArrayList(DatabaseMain.getDatabase(view.context).timetableDao().getList())

            // check if overlap timetable exist
            withContext(Dispatchers.Main) {

                // generate timetable
                timetableView.setTimetableData(timetableList, view.findViewById(R.id.relativeLayout))

                // overlap exist
                if (UtilData.checkOverlapTimetableExist(timetableList)) {

                    // confirmation dialog
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    alertDialogBuilder.setTitle(getString(R.string.dialog_overlap_timetable_title))
                    alertDialogBuilder.setMessage(R.string.dialog_overlap_timetable_message)
                    alertDialogBuilder.setNegativeButton(getString(R.string.dialog_button_dismiss), OnButtonClickDismissAlertDialog {
                        // noop
                    })
                    alertDialogBuilder.show()

                    // return
                    return@withContext

                }
            }
        }

        // return
        return view
    }

}