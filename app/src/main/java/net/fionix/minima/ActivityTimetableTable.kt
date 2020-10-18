package net.fionix.minima

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ActivityTimetableTable : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // super
        val view = inflater.inflate(R.layout.activity_table_timetable, container, false)

        // todo: add generate html table

        // return
        return view
    }

}