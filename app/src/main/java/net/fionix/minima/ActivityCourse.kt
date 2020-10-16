package net.fionix.minima

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment

class ActivityCourse : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_list_course, container, false)
        // prefs = PreferenceManager.getDefaultSharedPreferences(context)

        // add button
        val addButton: Button = view.findViewById(R.id.button)
        addButton.setOnClickListener {
            // show add course dialog
            val dialog = DialogAdd(view.context)
            dialog.show()
        }

        // course list
        val listView: ListView = view.findViewById(R.id.listView)
        // todo: populate course list
        // todo: add listview on item long click to edit

        // return view
        return view
    }
}