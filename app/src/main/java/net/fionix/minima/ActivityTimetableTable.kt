package net.fionix.minima

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.github.tlaabs.timetableview.TimetableView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.util.OnButtonClickDismissAlertDialog
import net.fionix.minima.util.UtilData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ActivityTimetableTable : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_table_timetable, container, false)

        // interface component
        val timetableView: TimetableView = view.findViewById(R.id.timetable)

        // get timetable list
        GlobalScope.launch(Dispatchers.IO) {

            // formatter
            val formatter: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val scheduleHourFormatter: DateFormat = SimpleDateFormat("HH", Locale.getDefault())
            val scheduleMinuteFormatter: DateFormat = SimpleDateFormat("mm", Locale.getDefault())

            // parse schedule list
            val timetableList = ArrayList(DatabaseMain.getDatabase(view.context).timetableDao().getList())
            val scheduleList: ArrayList<Schedule> = ArrayList()
            for (timetable in timetableList) {

                // populate schedule data
                val schedule = Schedule()
                schedule.classTitle = timetable.courseCode
                schedule.classPlace = timetable.timetableVenue
                schedule.professorName = timetable.courseGroup
                schedule.day = UtilData.getDayOfTheWeek(timetable.timetableDay)

                // get time start
                try {
                    val date = formatter.parse(timetable.timetableTimeStart)
                    if (date != null) schedule.startTime = Time(scheduleHourFormatter.format(date).toInt(), scheduleMinuteFormatter.format(date).toInt())
                } catch (e: Exception) {
                    continue
                }

                // get time end
                try {
                    val date = formatter.parse(timetable.timetableTimeEnd)
                    if (date != null) schedule.endTime = Time(scheduleHourFormatter.format(date).toInt(), scheduleMinuteFormatter.format(date).toInt())
                } catch (e: Exception) {
                    continue
                }

                // add into schedule list
                scheduleList.add(schedule)
            }

            // check if overlap timetable exist
            withContext(Dispatchers.Main) {

                // overlap exist
                if (UtilData.checkOverlapTimetableExist(timetableList)) {

                    // confirmation dialog
                    val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    alertDialogBuilder.setTitle("Overlapped timetable detected")
                    alertDialogBuilder.setMessage("Please check your course list and timetable. Due to overlapping timetable, timetable table could not be generated.")
                    alertDialogBuilder.setNegativeButton("Dismiss", OnButtonClickDismissAlertDialog {
                        // noop
                    })
                    alertDialogBuilder.show()

                    // return
                    return@withContext

                }

                // display content
                timetableView.add(scheduleList)
                timetableView.setHeaderHighlight(UtilData.getDayOfTheWeek(SimpleDateFormat("EEEE", Locale.ENGLISH).format(Calendar.getInstance().time)))
            }

        }

        // return
        return view
    }

}