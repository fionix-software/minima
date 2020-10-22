package net.fionix.minima

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.util.OnButtonClickDismissAlertDialog
import net.fionix.minima.util.UtilHtmlGenerator

class ActivityTimetableTable : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_table_timetable, container, false)

        // interface component
        val webView: WebView = view.findViewById(R.id.webView)
        val textView: TextView = view.findViewById(R.id.textView)
        val saveTimetableAsPng: Button = view.findViewById(R.id.button)

        // get timetable list
        GlobalScope.launch(Dispatchers.IO) {

            // parse cursor
            val timetableList = ArrayList(DatabaseMain.getDatabase(view.context).timetableDao().getList())

            // check if overlap timetable exist
            withContext(Dispatchers.Main) {

                // overlap exist
                if (UtilHtmlGenerator.checkOverlapTimetableExist(timetableList)) {

                    // confirmation dialog
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    alertDialogBuilder.setTitle("Overlapped timetable detected")
                    alertDialogBuilder.setMessage("Please check your course list and timetable. Due to overlapping timetable, timetable table could not be generated.")
                    alertDialogBuilder.setNegativeButton("Dismiss", OnButtonClickDismissAlertDialog {
                        // noop
                    })
                    alertDialogBuilder.show()

                    // show / hide interface based on overlap status
                    textView.visibility = View.VISIBLE
                    webView.visibility = View.GONE
                    saveTimetableAsPng.visibility = View.GONE

                    // return
                    return@withContext

                }

                // show / hide interface based on overlap status
                textView.visibility = View.GONE
                webView.visibility = View.VISIBLE
                saveTimetableAsPng.visibility = View.VISIBLE
            }

        }

        // return
        return view
    }

}