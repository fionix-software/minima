package net.fionix.minima.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import net.fionix.minima.R
import net.fionix.minima.model.ClassCourse
import java.util.*

class AdapterCourse(private val items: ArrayList<ClassCourse?>?, context: Context?) : ArrayAdapter<ClassCourse?>(context, R.layout.listview_timetable, items) {
    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.listview_course, parent, false)
        val facTxt = view.findViewById<TextView>(R.id.textView2)
        val courseNameTxt = view.findViewById<TextView>(R.id.textView3)
        val courseTxt = view.findViewById<TextView>(R.id.textView4)
        val groupTxt = view.findViewById<TextView>(R.id.textView5)
        facTxt.text = items!![position]!!.faculty
        courseTxt.text = items[position]!!.code
        groupTxt.text = items[position]!!.group
        if (items[position]!!.title.isEmpty()) courseNameTxt.text = "Long press here to change course name." else courseNameTxt.text = items[position]!!.title
        return view
    }
}