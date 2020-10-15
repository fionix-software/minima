package net.fionix.minima.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import net.fionix.minima.R
import net.fionix.minima.model.ClassMinima
import net.fionix.minima.model.ClassTimetable
import java.util.*

class AdapterTimetable(private val dataSet: ArrayList<ClassTimetable?>?, var mContext: Context?) : ArrayAdapter<ClassTimetable?>(mContext, R.layout.listview_timetable, dataSet) {
    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.listview_timetable, parent, false)
        val indicator = view.findViewById<ImageView>(R.id.highlight)
        val dayTxt = view.findViewById<TextView>(R.id.textView1)
        val courseNameTxt = view.findViewById<TextView>(R.id.textViewCourseName)
        val courseCodeTxt = view.findViewById<TextView>(R.id.textViewCourseCode)
        val groupTxt = view.findViewById<TextView>(R.id.textViewGroup)
        val timeTxt = view.findViewById<TextView>(R.id.textViewTime)
        val locationTxt = view.findViewById<TextView>(R.id.textViewLocation)
        indicator.visibility = View.VISIBLE
        var courseName = ""
        val arrayListCourseCode = ClassMinima.loadCourse(context)
        for (i in arrayListCourseCode!!.indices) {
            if (arrayListCourseCode[i]!!.code!!.contains(dataSet!![position]!!.course!!)) {
                courseName = if (arrayListCourseCode[i]!!.title.isEmpty()) "You can edit course name at the Course tab" else arrayListCourseCode[i]!!.title
                break
            }
        }
        courseNameTxt.text = courseName
        courseCodeTxt.text = dataSet!![position]!!.course
        groupTxt.text = dataSet[position]!!.group
        timeTxt.text = dataSet[position]!!.start + " - " + dataSet[position]!!.end
        if (!dataSet[position]!!.location.isEmpty()) locationTxt.text = dataSet[position]!!.location.toUpperCase() else locationTxt.text = "-"
        dayTxt.text = dataSet[position]!!.day.substring(0, Math.min(dataSet[position]!!.day.toString().length, 3)).toUpperCase()
        val calendar = Calendar.getInstance()
        val day = calendar[Calendar.DAY_OF_WEEK]
        when (day) {
            Calendar.MONDAY -> if (dataSet[position]!!.day != "Monday") {
                indicator.visibility = View.GONE
            }
            Calendar.TUESDAY -> if (dataSet[position]!!.day != "Tuesday") {
                indicator.visibility = View.GONE
            }
            Calendar.WEDNESDAY -> if (dataSet[position]!!.day != "Wednesday") {
                indicator.visibility = View.GONE
            }
            Calendar.THURSDAY -> if (dataSet[position]!!.day != "Thursday") {
                indicator.visibility = View.GONE
            }
            Calendar.FRIDAY -> if (dataSet[position]!!.day != "Friday") {
                indicator.visibility = View.GONE
            }
            Calendar.SATURDAY -> if (dataSet[position]!!.day != "Saturday") {
                indicator.visibility = View.GONE
            }
            Calendar.SUNDAY -> if (dataSet[position]!!.day != "Sunday") {
                indicator.visibility = View.GONE
            }
        }
        return view
    }
}