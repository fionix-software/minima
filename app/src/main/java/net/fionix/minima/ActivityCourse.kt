package net.fionix.minima

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.fragment.app.Fragment
import net.fionix.minima.adapter.AdapterCourse
import net.fionix.minima.model.ClassCourse
import net.fionix.minima.model.ClassMinima.JsoupAsyncTaskFetchFaculty
import net.fionix.minima.model.ClassMinima.JsoupAsyncTaskFetchTimetable
import net.fionix.minima.model.ClassFaculty
import net.fionix.minima.model.ClassMinima
import net.fionix.minima.model.ClassTimetable
import java.util.*

class ActivityCourse : Fragment() {
    var adapter: AdapterCourse? = null
    var prefs: SharedPreferences? = null
    var arrayListTimetable: ArrayList<ClassTimetable?>? = null
    var arrayListFaculty: ArrayList<ClassFaculty?>? = null
    var arrayListCourse: ArrayList<ClassCourse?>? = null
    var button: Button? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_list_course, container, false)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)

        // Load timetable
        arrayListTimetable = ClassMinima.loadTimetable(context)
        arrayListFaculty = ClassMinima.loadFaculty(context)
        arrayListCourse = ClassMinima.loadCourse(context)
        val jsoupAsyncTaskFetchFaculty = JsoupAsyncTaskFetchFaculty()
        jsoupAsyncTaskFetchFaculty.execute()

        // populate data
        adapter = AdapterCourse(arrayListCourse, context)

        // initialize interface components
        button = view.findViewById(R.id.button)
        listview = view.findViewById(R.id.listView)
        listview.setAdapter(adapter)

        // button onClick
        button.setOnClickListener(View.OnClickListener { // initialize new dialog box
            val dialogAdd = Dialog(context)
            dialogAdd.setCanceledOnTouchOutside(false)
            dialogAdd.setContentView(R.layout.dialog_add)

            // adding interface component
            val buttonAdd = dialogAdd.findViewById<View>(R.id.button1) as Button
            val editTextCourse = dialogAdd.findViewById<View>(R.id.editText1) as EditText
            val editTextGroup = dialogAdd.findViewById<View>(R.id.editText2) as EditText
            progressBar = dialogAdd.findViewById<View>(R.id.progressBar) as ProgressBar
            progressBar!!.visibility = View.GONE

            // button onClick
            buttonAdd.setOnClickListener {
                var exist = false
                val group = editTextGroup.text.toString().trim { it <= ' ' }.toUpperCase()
                val course = editTextCourse.text.toString().trim { it <= ' ' }.toUpperCase()

                // check for the latest data
                arrayListCourse = ClassMinima.loadCourse(context)

                // check existing course
                for (i in arrayListCourse!!.indices) {
                    if (course == arrayListCourse!![i]!!.code) {
                        exist = true
                    }
                }

                // check for empty field
                if (!group.isEmpty() && !course.isEmpty()) {
                    // check for data not existed
                    if (!exist) {
                        // fetching timetable
                        val jsoupAsyncTaskFetchTimetable = JsoupAsyncTaskFetchTimetable()
                        jsoupAsyncTaskFetchTimetable.execute(group, course)
                    } else {
                        // notify by toast
                        Toast.makeText(context, "Course already exist in the timetable", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // notify by toast
                    Toast.makeText(context, "Input empty", Toast.LENGTH_SHORT).show()
                }
            }

            // show dialog box
            dialogAdd.show()
        })
        listview.setOnItemLongClickListener(OnItemLongClickListener { parent, view, position, id -> // initialize new dialog box
            val dialogEdit = Dialog(context)
            dialogEdit.setCanceledOnTouchOutside(false)
            dialogEdit.setContentView(R.layout.dialog_edit)

            // check latest data
            arrayListCourse = ClassMinima.loadCourse(context)

            // adding interface component
            val buttonSave = dialogEdit.findViewById<View>(R.id.button1) as Button
            val buttonDelete = dialogEdit.findViewById<View>(R.id.button2) as Button
            val editTextCourse = dialogEdit.findViewById<View>(R.id.editText1) as EditText

            // put the current course name
            editTextCourse.setText(arrayListCourse!![position]!!.title)

            // button delete onClick
            buttonDelete.setOnClickListener { // delete timetable
                for (i in arrayListTimetable!!.indices) {
                    if (arrayListTimetable!![i]!!.course == arrayListCourse!![position]!!.code) {
                        arrayListTimetable!!.removeAt(i)
                    }
                }

                // delete course and save changes
                arrayListCourse!!.removeAt(position)
                ClassMinima.saveCourse(arrayListCourse, context)
                ClassMinima.saveTimetable(arrayListTimetable, context)

                // refresh listview
                adapter = AdapterCourse(arrayListCourse, context)
                listview.setAdapter(adapter)
                Toast.makeText(context, "Course deleted", Toast.LENGTH_SHORT).show()
                dialogEdit.dismiss()
            }

            // button add onClick
            buttonSave.setOnClickListener {
                if (!editTextCourse.text.toString().isEmpty()) {

                    // edit course name and save changes
                    arrayListCourse!!.add(ClassCourse(arrayListCourse!![position]!!.faculty, arrayListCourse!![position]!!.code, arrayListCourse!![position]!!.group, editTextCourse.text.toString()))
                    arrayListCourse!!.removeAt(position)
                    ClassMinima.saveCourse(arrayListCourse, context)

                    // refresh listview
                    adapter = AdapterCourse(arrayListCourse, context)
                    listview.setAdapter(adapter)
                    Toast.makeText(context, "Save successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Field empty", Toast.LENGTH_SHORT).show()
                }
                dialogEdit.dismiss()
            }
            dialogEdit.show()
            true
        })
        return view
    }

    companion object {
        var listview: ListView? = null
        var progressBar: ProgressBar? = null
    }
}