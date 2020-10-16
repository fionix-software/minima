package net.fionix.minima

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.adapter.AdapterCourse
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.ModelCourse

class ActivityCourse : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_list_course, container, false)
        // prefs = PreferenceManager.getDefaultSharedPreferences(context)

        // add button
        val addButton: Button = view.findViewById(R.id.button)
        addButton.setOnClickListener {
            // show add course dialog
            val dialog = DialogAdd(view.context)
            dialog.show()
        }

        // course list
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val courseList: ArrayList<ModelCourse> = arrayListOf()
        val adapterCourse = AdapterCourse(courseList)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapterCourse

        // get course list
        GlobalScope.launch(Dispatchers.IO) {

            // parse cursor
            val tempCourseList: ArrayList<ModelCourse> = arrayListOf()
            val cursor: Cursor = DatabaseMain.getDatabase(view.context).timetableDao().getCourseList()
            cursor.use { c ->
                while (c.moveToNext()) {
                    tempCourseList.add(ModelCourse(c.getString(0), c.getString(1), c.getString(2), c.getString(3)))
                }
            }
            courseList.clear()
            courseList.addAll(tempCourseList)

            // update
            withContext(Dispatchers.Main) {
                adapterCourse.notifyDataSetChanged()
            }
        }

        // return view
        return view
    }
}