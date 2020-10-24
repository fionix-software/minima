package net.fionix.minima.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.fionix.minima.R
import net.fionix.minima.model.EntityTimetable
import net.fionix.minima.util.OnTimetableItemLongClickListener

class AdapterTimetable(private val dataset: MutableList<EntityTimetable>, private val onTimetableItemLongClickListener: OnTimetableItemLongClickListener) : RecyclerView.Adapter<AdapterTimetable.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context: Context = itemView.context
        val titleText: TextView = itemView.findViewById(R.id.title_text)
        val secondaryText: TextView = itemView.findViewById(R.id.secondary_text)
        val tertiaryText: TextView = itemView.findViewById(R.id.tertiary_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterTimetable.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_three_line, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterTimetable.ViewHolder, position: Int) {

        // title
        var courseCode: String = dataset[position].courseCode
        if (dataset[position].courseName.isNotEmpty()) {
            courseCode = courseCode + " - " + dataset[position].courseName
        }
        holder.titleText.text = courseCode

        // secondary
        val secondaryString: String = dataset[position].timetableDay + ", " + dataset[position].timetableTimeStart + " - " + dataset[position].timetableTimeEnd
        holder.secondaryText.text = secondaryString

        // tertiary
        var tertiaryString: String = dataset[position].courseGroup + " (" + dataset[position].facultyCode + ")"
        if (dataset[position].timetableVenue.isNotEmpty()) {
            tertiaryString = tertiaryString + " - " + dataset[position].timetableVenue
        }
        holder.tertiaryText.text = tertiaryString

        // set on long click listener
        holder.itemView.setOnLongClickListener {
            onTimetableItemLongClickListener.onTimetableItemLongClickListener(dataset[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}