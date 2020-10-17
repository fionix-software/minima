package net.fionix.minima.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.fionix.minima.R
import net.fionix.minima.model.ModelCourse
import net.fionix.minima.util.OnCourseItemLongClickListener

class AdapterCourse(private val dataset: MutableList<ModelCourse>, private val courseItemOnLongClickListener: OnCourseItemLongClickListener) : RecyclerView.Adapter<AdapterCourse.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context: Context = itemView.context
        val facultyCode: TextView = itemView.findViewById(R.id.textView2)
        val courseName: TextView = itemView.findViewById(R.id.textView3)
        val courseCode: TextView = itemView.findViewById(R.id.textView4)
        val courseGroup: TextView = itemView.findViewById(R.id.textView5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCourse.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listview_course, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterCourse.ViewHolder, position: Int) {
        holder.facultyCode.text = dataset[position].facultyCode
        holder.courseName.text = if (dataset[position].courseName.isEmpty()) holder.context.getString(R.string.not_available) else dataset[position].courseName
        holder.courseCode.text = dataset[position].courseCode
        holder.courseGroup.text = dataset[position].courseGroup
        holder.itemView.setOnLongClickListener {
            courseItemOnLongClickListener.onCourseItemLongClickListener(dataset[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}