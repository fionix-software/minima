package net.fionix.minima

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment

class ActivityTimetable : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_list_timetable, container, false)

        // recheck timetable from iCress button
        val recheckButton: Button = view.findViewById(R.id.button)
        recheckButton.setOnClickListener {
            // todo: add button implementation to add item
        }

        // timetable list
        val listview: ListView = view.findViewById(R.id.listView)
        // todo: populate timetable list

        // return view
        return view
    }
}