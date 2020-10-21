package net.fionix.minima

import android.app.AlertDialog
import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.fionix.minima.adapter.AdapterCourse
import net.fionix.minima.database.DatabaseMain
import net.fionix.minima.model.ModelCourse
import net.fionix.minima.util.OnButtonClickConfirmDeleteCourseAlertDialog
import net.fionix.minima.util.OnButtonClickDismissAlertDialog
import net.fionix.minima.util.OnCourseItemLongClickListener
import net.fionix.minima.util.UtilData


class ActivityCourse : Fragment(), OnCourseItemLongClickListener {

    private lateinit var ctx: Context
    private lateinit var adapterCourse: AdapterCourse
    private var courseList: ArrayList<ModelCourse> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_list_course, container, false)
        this.ctx = view.context

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

        // course list item swap to delete item
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                // noop
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {

                // get item index
                val itemIndex = viewHolder.adapterPosition

                // confirmation dialog
                val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                alertDialogBuilder.setTitle("Are you sure to delete ${courseList[itemIndex].courseCode}?")
                alertDialogBuilder.setMessage("This operation is irreversible.")
                alertDialogBuilder.setPositiveButton("Yes", OnButtonClickConfirmDeleteCourseAlertDialog(view.context, courseList[itemIndex]) {
                    updateList()
                })
                alertDialogBuilder.setNegativeButton("No", OnButtonClickDismissAlertDialog() {
                    // without update list on dismiss, the item get missing
                    updateList()
                })
                alertDialogBuilder.show()
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

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
        val dialog = DialogEditCourseName(ctx, data)
        dialog.setOnDismissListener {
            updateList()
        }
        dialog.show()
    }

    private fun updateList() {

        // get course list
        GlobalScope.launch(Dispatchers.IO) {

            // parse cursor
            val cursor: Cursor = DatabaseMain.getDatabase(ctx).timetableDao().getCourseList()
            val tempCourseList: ArrayList<ModelCourse> = ArrayList(UtilData.cursorToCourseList(cursor).sortedWith(compareBy { it.courseCode }))
            cursor.close()

            // update course list
            courseList.clear()
            courseList.addAll(tempCourseList)

            // update
            withContext(Dispatchers.Main) {
                adapterCourse.notifyDataSetChanged()
            }
        }
    }
}