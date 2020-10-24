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

class AdapterCourse(private val dataset: MutableList<ModelCourse>, private val onCourseItemLongClickListener: OnCourseItemLongClickListener) : RecyclerView.Adapter<AdapterCourse.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context: Context = itemView.context
        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val secondaryText: TextView = itemView.findViewById(R.id.secondary_text)
        val tertiaryText: TextView = itemView.findViewById(R.id.tertiary_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCourse.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_three_line, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterCourse.ViewHolder, position: Int) {

        // title
        var courseCode: String = dataset[position].courseCode
        if (dataset[position].courseName.isNotEmpty()) {
            courseCode = courseCode + " - " + dataset[position].courseName
        }
        holder.titleText.text = courseCode

        // secondary
        val secondaryString: String = dataset[position].facultyName + " (" + dataset[position].facultyCode + ")"
        holder.secondaryText.text = secondaryString

        // tertiary
        holder.tertiaryText.text = dataset[position].courseGroup

        // set on long click listener
        holder.itemView.setOnLongClickListener {
            onCourseItemLongClickListener.onCourseItemLongClickListener(dataset[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}