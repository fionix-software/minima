package net.fionix.minima

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import net.fionix.minima.adapter.AdapterTimetable
import net.fionix.minima.model.ClassMinima
import net.fionix.minima.model.ClassMinima.JsoupAsyncTaskRefetchTimetable
import net.fionix.minima.model.ClassTimetable
import java.util.*

class ActivityTimetable : Fragment() {
    var adapter: AdapterTimetable? = null
    var arrayListTimetable: ArrayList<ClassTimetable?>? = null
    var button: Button? = null
    var prefs: SharedPreferences? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // inflate view
        val view = inflater.inflate(R.layout.activity_list_timetable, container, false)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)

        // populate Data
        arrayListTimetable = ClassMinima.loadTimetable(context)
        adapter = AdapterTimetable(arrayListTimetable, context)

        // initialize interface component
        listview = view.findViewById(R.id.listView)
        listview.setAdapter(adapter)
        button = view.findViewById(R.id.button)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.setVisibility(View.GONE)

        // button onClick
        button.setOnClickListener(View.OnClickListener { // recheck timetable
            val jsoupAsyncTaskRefetchTimetable = JsoupAsyncTaskRefetchTimetable()
            jsoupAsyncTaskRefetchTimetable.execute()
        })
        return view
    }

    companion object {
        var listview: ListView? = null
        var progressBar: ProgressBar? = null
    }
}