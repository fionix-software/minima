package net.fionix.minima

import android.content.Context
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
import net.fionix.minima.util.OnCourseItemLongClickListener

class ActivityCourse : Fragment(), OnCourseItemLongClickListener {

    private lateinit var ctx: Context
    private lateinit var adapterCourse: AdapterCourse
    private var courseList: ArrayList<ModelCourse> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_list_course, container, false)
        this.ctx = view.context
        // prefs = PreferenceManager.getDefaultSharedPreferences(context)

        // add button
        val addButton: Button = view.findViewById(R.id.button)
        addButton.setOnClickListener {
            // show add course dialog
            val dialog = DialogAdd(view.context)
            dialog.setOnCancelListener {
                updateList()
            }
            dialog.show()
        }

        // course list
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        adapterCourse = AdapterCourse(courseList, this)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapterCourse

        // update list
        updateList()

        // return view
        return view
    }

    override fun onCourseItemLongClickListener(data: ModelCourse) {

        // check if context is not yet initialize
        if (!this::ctx.isInitialized) {
            return
        }

        // show add course dialog
        val dialog = DialogEdit(ctx, data)
        dialog.setOnDismissListener {
            updateList()
        }
        dialog.show()
    }

    private fun updateList() {

        // get course list
        GlobalScope.launch(Dispatchers.IO) {

            // parse cursor
            val tempCourseList: ArrayList<ModelCourse> = arrayListOf()
            val cursor: Cursor = DatabaseMain.getDatabase(ctx).timetableDao().getCourseList()
            cursor.use { c ->
                while (c.moveToNext()) {
                    // column 0: course code
                    // column 1: course name
                    // column 2: course group
                    // column 3: faculty code
                    // column 4: faculty name
                    tempCourseList.add(ModelCourse(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4)))
                }
            }
            courseList.clear()
            courseList.addAll(tempCourseList)

            // update
            withContext(Dispatchers.Main) {
                adapterCourse.notifyDataSetChanged()
            }
        }
    }
}