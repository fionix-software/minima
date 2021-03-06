package net.fionix.minima

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.util.OnButtonClickDismissAlertDialog
import net.fionix.minima.util.UtilData


class ActivityTimetableTable : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_table_timetable, container, false)

        // timetable view
        val timetableView = ViewTimetable(view.context)
        timetableView.initTimetableView(view.findViewById(R.id.tableLayout))

        // banner text
        val bannerText: TextView = view.findViewById(R.id.bannerText)
        bannerText.text = getString(R.string.timetable_banner_text).replace("\$version", BuildConfig.VERSION_NAME)

        // get timetable list
        GlobalScope.launch(Dispatchers.IO) {

            // get timetable list
            val timetableList = ArrayList(DatabaseMain.getDatabase(view.context).timetableDao().getList())

            // check if overlap timetable exist
            withContext(Dispatchers.Main) {

                if (context == null) {
                    return@withContext
                }

                // overlap exist
                if (UtilData.checkOverlapTimetableExist(timetableList)) {

                    // confirmation dialog
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
                    alertDialogBuilder.setTitle(getString(R.string.dialog_overlap_timetable_title))
                    alertDialogBuilder.setMessage(R.string.dialog_overlap_timetable_message)
                    alertDialogBuilder.setNegativeButton(getString(R.string.dialog_button_dismiss), OnButtonClickDismissAlertDialog {
                        // noop
                    })
                    alertDialogBuilder.show()

                    // return
                    return@withContext

                } else {

                    // generate timetable
                    timetableView.generateTimetableSticker(view.findViewById(R.id.relativeLayout), timetableList)
                }
            }
        }

        // return
        return view
    }

}