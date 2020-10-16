package net.fionix.minima.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.fionix.minima.R
import net.fionix.minima.model.ModelCourse

class AdapterCourse(private val dataset: MutableList<ModelCourse>) : RecyclerView.Adapter<AdapterCourse.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val facultyCode: TextView = itemView.findViewById(R.id.textView1)
        val courseCode: TextView = itemView.findViewById(R.id.textView2)
        val courseGroup: TextView = itemView.findViewById(R.id.textView3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCourse.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listview_course, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterCourse.ViewHolder, position: Int) {
        holder.facultyCode.text = dataset[position].facultyCode
        holder.courseCode.text = dataset[position].courseCode
        holder.courseGroup.text = dataset[position].courseGroup
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}