package net.fionix.minima

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.adapter.AdapterTimetable
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.EntityTimetable
import net.fionix.minima.model.ModelCourse
import net.fionix.minima.model.ModelFaculty
import net.fionix.minima.util.OnTimetableItemLongClickListener
import net.fionix.minima.util.UtilData
import net.fionix.minima.util.UtilScraper

class ActivityTimetableList : Fragment(), OnTimetableItemLongClickListener {

    private lateinit var ctx: Context
    private lateinit var adapterTimetable: AdapterTimetable
    private var timetableList: ArrayList<EntityTimetable> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_list_timetable, container, false)
        this.ctx = view.context

        // progress bar
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

        // recheck timetable from iCress button
        val recheckButton: Button = view.findViewById(R.id.button)
        recheckButton.setOnClickListener {

            // enable progress bar
            progressBar.visibility = View.VISIBLE

            // get course list
            GlobalScope.launch(Dispatchers.IO) {

                // parse cursor
                val cursor: Cursor = DatabaseMain.getDatabase(ctx).timetableDao().getCourseList()
                val courseListFromDatabase: ArrayList<ModelCourse> = ArrayList(UtilData.cursorToCourseList(cursor).sortedWith(compareBy { it.courseCode }))
                cursor.close()

                // add faculty if not exist
                val tempFacultyList: ArrayList<ModelFaculty> = arrayListOf()
                val tempCourseList: ArrayList<ModelCourse> = arrayListOf()
                for (courseItem in courseListFromDatabase) {
                    // parse faculty
                    val faculty = ModelFaculty(courseItem.facultyCode, courseItem.facultyName)
                    if (faculty !in tempFacultyList) {
                        tempFacultyList.add(faculty)
                    }

                    // parse course
                    val course = ModelCourse(courseItem.courseCode, courseItem.courseName, courseItem.courseGroup, courseItem.facultyCode, courseItem.facultyName)
                    if (course !in tempCourseList) {
                        tempCourseList.add(course)
                    }
                }

                // retrieve timetable from icress
                val newTimetableList: ArrayList<EntityTimetable> = arrayListOf()
                val newCourseList: ArrayList<ModelCourse> = arrayListOf()
                for (courseItem in tempCourseList) {

                    // retrieve timetable
                    val retrievedTimetableList = UtilScraper.retrieveTimetable(tempFacultyList, courseItem.courseCode, courseItem.courseGroup)
                    val fixedRetrievedTimetableList = UtilData.mergeClass(UtilData.sortTimetable(UtilData.fixTimetable(retrievedTimetableList)))
                    newTimetableList.addAll(fixedRetrievedTimetableList)

                    // parse course
                    for (retrievedItem in retrievedTimetableList) {
                        val course = ModelCourse(retrievedItem.courseCode, retrievedItem.courseName, retrievedItem.courseGroup, retrievedItem.facultyCode, retrievedItem.facultyName)
                        if (course !in newCourseList) {
                            newCourseList.add(course)
                        }
                    }
                }

                // if failed don't update existing data in database
                if (newTimetableList.isEmpty()) {

                    // notify
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, getString(R.string.failed_recheck_timetable), Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE;

                    }

                    // return
                    return@launch
                }

                // save into database
                DatabaseMain.getDatabase(ctx).timetableDao().clearData()
                for (item in newTimetableList) {
                    DatabaseMain.getDatabase(ctx).timetableDao().insert(item)
                }

                // update course name (use the previously set)
                for (courseItem in newCourseList) {
                    val previouslySetCourseName = tempCourseList.filter { c -> c.courseCode == courseItem.courseCode }[0].courseName
                    DatabaseMain.getDatabase(ctx).timetableDao().updateCourseName(previouslySetCourseName, courseItem.courseCode, courseItem.courseName, courseItem.facultyCode)
                }

                // notify
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, getString(R.string.success), Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE;

                    // update list
                    updateList()
                }
            }
        }

        // timetable list
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        adapterTimetable = AdapterTimetable(timetableList, this)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapterTimetable

        // update list
        updateList()

        // return view
        return view
    }

    override fun onTimetableItemLongClickListener(data: EntityTimetable) {

        // check if context is not yet initialize
        if (!this::ctx.isInitialized) {
            return
        }

        // show add course dialog
        val dialog = DialogEditVenue(ctx, data)
        dialog.setOnDismissListener {
            updateList()
        }
        dialog.show()
    }

    private fun updateList() {

        // get course list
        GlobalScope.launch(Dispatchers.IO) {

            // get timetable
            timetableList.clear()
            timetableList.addAll(UtilData.sortTimetable(ArrayList(DatabaseMain.getDatabase(ctx).timetableDao().getList())))

            // update
            withContext(Dispatchers.Main) {
                adapterTimetable.notifyDataSetChanged()
            }
        }
    }
}