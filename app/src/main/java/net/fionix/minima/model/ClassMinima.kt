package net.fionix.minima.model

import android.content.Context
import android.os.AsyncTask
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.Constraints.TAG
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.fionix.minima.*
import net.fionix.minima.adapter.AdapterCourse
import net.fionix.minima.adapter.AdapterTimetable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object ClassMinima {
    fun saveTimetable(a: ArrayList<ClassTimetable?>?, c: Context?) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(c)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(sanitizeTimetable(a))
        prefsEditor.putString("timetable", json)
        prefsEditor.commit()
    }

    fun loadTimetable(c: Context?): ArrayList<ClassTimetable?> {
        val gson = Gson()
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(c)
        val response = mPrefs.getString("timetable", "")
        val arrayListTimetable = gson.fromJson<ArrayList<ClassTimetable?>>(response, object : TypeToken<List<ClassTimetable?>?>() {}.type)
        return arrayListTimetable ?: ArrayList()
    }

    fun saveFaculty(a: ArrayList<ClassFaculty>?, c: Context?) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(c)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(a)
        prefsEditor.putString("faculty", json)
        prefsEditor.commit()
    }

    fun loadFaculty(c: Context?): ArrayList<ClassFaculty> {
        val gson = Gson()
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(c)
        val response = mPrefs.getString("faculty", "")
        val arrayListFaculty = gson.fromJson<ArrayList<ClassFaculty>>(response, object : TypeToken<List<ClassFaculty?>?>() {}.type)
        return arrayListFaculty ?: ArrayList()
    }

    fun saveCourse(a: ArrayList<ClassCourse?>?, c: Context?) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(c)
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(sanitizeCourse(a))
        prefsEditor.putString("course", json)
        prefsEditor.commit()
    }

    fun loadCourse(c: Context?): ArrayList<ClassCourse?> {
        val gson = Gson()
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(c)
        val response = mPrefs.getString("course", "")
        val arrayListCourse = gson.fromJson<ArrayList<ClassCourse?>>(response, object : TypeToken<List<ClassCourse?>?>() {}.type)
        return arrayListCourse ?: ArrayList()
    }

    fun sanitizeTimetable(items: ArrayList<ClassTimetable?>?): ArrayList<ClassTimetable?>? {
        var hasChanges = false
        Collections.sort(items) { o1, o2 -> o1.getDayCode().compareTo(o2.getDayCode()) }
        for (i in items!!.indices) {
            if (i < items.size - 1) {

                // correct original string
                if (items[i]!!.start == "12:00 am") {
                    items[i]!!.start = "12:00 pm"
                }
                if (items[i]!!.end == "12:00 am") {
                    items[i]!!.end = "12:00 pm"
                }
                if (items[i]!!.start == "13:00 pm") {
                    items[i]!!.start = "1:00 pm"
                }
                if (items[i]!!.end == "13:00 pm") {
                    items[i]!!.end = "1:00 pm"
                }
                if (items[i]!!.start.contains(":5 ")) {
                    val temp = items[i]!!.start.replace(":5 ", ":15 ")
                    items[i]!!.start = temp
                }
                if (items[i]!!.end.contains(":5 ")) {
                    val temp = items[i]!!.end.replace(":5 ", ":15 ")
                    items[i]!!.end = temp
                }
                if (items[i]!!.day == items[i + 1]!!.day && items[i]!!.course == items[i + 1]!!.course && items[i]!!.end == items[i + 1]!!.start) {
                    items[i]!!.end = items[i + 1]!!.end
                    items.removeAt(i + 1)
                    hasChanges = true
                    continue
                }
                if (items[i]!!.day == items[i + 1]!!.day && items[i]!!.course == items[i + 1]!!.course && items[i]!!.start == items[i + 1]!!.start) {
                    items.removeAt(i + 1)
                    hasChanges = true
                    continue
                }
                if (items[i]!!.day == items[i + 1]!!.day && items[i]!!.course == items[i + 1]!!.course && items[i]!!.end == items[i + 1]!!.end) {
                    items.removeAt(i + 1)
                    hasChanges = true
                    continue
                }
            }
        }
        if (hasChanges) {
            sanitizeTimetable(items)
        }
        return items
    }

    fun sanitizeCourse(items: ArrayList<ClassCourse?>?): ArrayList<ClassCourse?>? {
        var hasChanges = false
        Collections.sort(items) { o1, o2 -> o1.getCode().compareTo(o2.getCode()) }
        for (i in items!!.indices) {
            if (i < items.size - 1) {
                if (items[i]!!.code == items[i + 1]!!.code && items[i]!!.faculty == items[i + 1]!!.faculty) {
                    items.removeAt(i + 1)
                    hasChanges = true
                }
            }
        }
        if (hasChanges) {
            sanitizeCourse(items)
        }
        return items
    }

    fun saveString(context: Context?, variable: String?, value: String?) {
        if (context != null) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPref.edit()
            editor.putString(variable, value)
            editor.commit()
        }
    }

    fun loadString(context: Context?, variable: String?): String {
        return if (context != null) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            sharedPref.getString(variable, "")
        } else {
            ""
        }
    }

    fun guide(): String {
        return """
               [Create]
               Create menu is where you ic_add your courses in order to create your timetable. To ic_add course, fill in the course code and group name. Please make sure your course name and group name is as specified format as the example (the Ex: text) mentioned. After you have fill in the course code and group name, press 'Add Course' to ic_add course into your timetable. You can 'Recheck timetable' to recheck the timetable for time or location changes. You can also 'Clear Timetable' to clear all your saved courses and clear all your timetable.
               
               [Timetable]
               Timetable menu is where your timetable is display in list. Current day class will be highlighted on the left side of the class.
               
               [View]
               View is where you can view your added courses and delete your courses from your timetable. Long press on an item to delete a course. You can enable or disable long press feature in the Setting menu.
               
               [Link]
               It is just a normal web browser but it automatically load UiTM's student portal. The bg_button at the top of the browser is Refresh, Home, Logout, Print.
               - Refresh: To refresh current webpage
               - Home: To go back to the student portal homepage
               - Logout: Logout your session from student portal
               - Print: Print current webpage
               
               [Setting]
               Setting menu is where you can disable/enable the long press to delete course feature on View menu. You can also directly email to me by clicking the 'Email feedback' or open this guide on the Minima version setting. Email me if there is any problem or question regarding Minima. If Minima crash, please send the report on the crash prompt for further improvement.
               [Homescreen Widget]
               You can ic_add widget on your homescreen. Click the refresh icon on the upper right side of the widget to refresh. Click the day and the class count on the left of the refresh icon to open Minima directly.
               """.trimIndent()
    }

    fun filterForToday(data: ArrayList<ClassTimetable?>?): ArrayList<ClassTimetable?> {
        if (data != null) {
            val calendar = Calendar.getInstance()
            val day_int = calendar[Calendar.DAY_OF_WEEK]
            var dataChanged = false
            for (i in data.indices) {
                when (day_int) {
                    Calendar.MONDAY -> if (data[i]!!.day != "Monday") {
                        data.removeAt(i)
                        dataChanged = true
                    }
                    Calendar.TUESDAY -> if (data[i]!!.day != "Tuesday") {
                        data.removeAt(i)
                        dataChanged = true
                    }
                    Calendar.WEDNESDAY -> if (data[i]!!.day != "Wednesday") {
                        data.removeAt(i)
                        dataChanged = true
                    }
                    Calendar.THURSDAY -> if (data[i]!!.day != "Thursday") {
                        data.removeAt(i)
                        dataChanged = true
                    }
                    Calendar.FRIDAY -> if (data[i]!!.day != "Friday") {
                        data.removeAt(i)
                        dataChanged = true
                    }
                    Calendar.SATURDAY -> if (data[i]!!.day != "Saturday") {
                        data.removeAt(i)
                        dataChanged = true
                    }
                    Calendar.SUNDAY -> if (data[i]!!.day != "Sunday") {
                        data.removeAt(i)
                        dataChanged = true
                    }
                }
            }
            return if (dataChanged) filterForToday(data) else data
        }
        return ArrayList()
    }

    fun generateTable(day: String, arrayListTimetable: ArrayList<ClassTimetable?>?): String? {
        val dayCode: Char
        dayCode = if (day == "Monday") {
            'A'
        } else if (day == "Tuesday") {
            'B'
        } else if (day == "Wednesday") {
            'C'
        } else if (day == "Thursday") {
            'D'
        } else if (day == "Friday") {
            'E'
        } else if (day == "Saturday") {
            'F'
        } else if (day == "Sunday") {
            'G'
        } else {
            'Z'
        }
        var html: String? = "<tr><td style=\"background: rgb(211,211,211);\">$day</td>"
        var lastEndCol = 0
        var colspan = 0
        for (i in arrayListTimetable!!.indices) {
            if (Character.toString(arrayListTimetable[i]!!.dayCode[0]) == Character.toString(dayCode)) {

                // get start hour
                val charIndexHourStart = arrayListTimetable[i]!!.start.indexOf(":")
                var extractedHour = arrayListTimetable[i]!!.start.substring(0, charIndexHourStart)
                var intHourStart = Integer.valueOf(extractedHour)
                if (arrayListTimetable[i]!!.start.contains("pm")) {
                    if (intHourStart != 12) {
                        intHourStart += 12
                    }
                }

                // get end hour
                val charIndexHourEnd = arrayListTimetable[i]!!.end.indexOf(":")
                extractedHour = arrayListTimetable[i]!!.end.substring(0, charIndexHourEnd)
                var intHourEnd = Integer.valueOf(extractedHour)
                if (arrayListTimetable[i]!!.end.contains("pm")) {
                    if (intHourEnd != 12) {
                        intHourEnd += 12
                    }
                }

                // timetable
                var colStart = (intHourStart - 8) * 2 + 2
                if (!arrayListTimetable[i]!!.start.contains(":00 ")) {
                    colStart += 1
                }
                var colEnd = (intHourEnd - 8) * 2 + 1
                if (!arrayListTimetable[i]!!.end.contains(":00 ")) {
                    colEnd += 1
                }

                // filler
                colspan = if (lastEndCol > 0) {
                    colStart - lastEndCol - 1
                } else {
                    colStart - 2
                }
                if (colspan > 1) {
                    html += "<td style=\"background: rgb(169,169,169);\" colspan=\"$colspan\"></td>"
                }
                if (colspan == 1) {
                    html += "<td style=\"background: rgb(169,169,169);\"></td>"
                }

                // timetable
                colspan = colEnd - colStart + 1
                html += "<td colspan=\"" + colspan + "\">" + arrayListTimetable[i]!!.course + "<br>"

                // add location and time
                html += if (arrayListTimetable[i]!!.location.isEmpty()) {
                    "-"
                } else {
                    arrayListTimetable[i]!!.location
                }
                html += "<br>" + arrayListTimetable[i]!!.start + " - " + arrayListTimetable[i]!!.end + "</td>"
                lastEndCol = colEnd
            }
        }

        // fill timetable
        colspan = if (lastEndCol > 0) {
            31 - lastEndCol
        } else {
            31 - 1
        }
        html += "<td style=\"background: rgb(169,169,169);\" colspan=\"$colspan\"></td>"
        html += "</tr>"
        return html
    }

    fun generateCourseList(arrayListCourse: ArrayList<ClassCourse?>?): String {

        // create course list as unordered list
        var html = "<p><u>Course list</u></p><ul>"

        // course listing
        for (i in arrayListCourse!!.indices) {
            html += "<li><strong>" + arrayListCourse[i]!!.group + ", " + arrayListCourse[i]!!.code + "</strong>: "
            html += if (arrayListCourse[i]!!.title.isEmpty()) {
                "-"
            } else {
                arrayListCourse[i]!!.title + "</li>"
            }
        }

        // close listing
        html += "</ul>"
        return html
    }

    fun checkOverlapTimetableTime(arrayList: ArrayList<ClassTimetable?>?): Boolean {

        // check for overlap timetable
        for (i in arrayList!!.indices) {
            for (j in arrayList.indices) {

                // check for same time for the same day
                if (i != j) {
                    if (arrayList[i]!!.day == arrayList[j]!!.day && arrayList[i]!!.start == arrayList[j]!!.start) {
                        return true
                    }
                }

                // check for timetable timeline
                if (j > i) {
                    try {
                        if (arrayList[i]!!.day == arrayList[j]!!.day) {
                            val dateFormat1: DateFormat = SimpleDateFormat("hh:mm a")
                            val date1 = dateFormat1.parse(arrayList[i]!!.end)
                            val dateFormat2: DateFormat = SimpleDateFormat("hh:mm a")
                            val date2 = dateFormat2.parse(arrayList[j]!!.start)
                            if (date2.before(date1)) {
                                return true
                            }
                        }
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return false
    }

    fun checkDuplicateCourse(arrayList: ArrayList<ClassCourse?>?): Boolean {
        for (i in arrayList!!.indices) {
            for (j in arrayList.indices) {
                // check for same course code
                if (i != j) {
                    if (arrayList[i]!!.code == arrayList[j]!!.code) {
                        return true
                    }
                }
            }
        }
        return false
    }

    class JsoupAsyncTaskFetchFaculty : AsyncTask<Void?, Void?, Void?>() {
        var arrayListFaculty: ArrayList<ClassFaculty>? = null
        var htmlDoc: Document? = null
        var failToFetch = false
        override fun onPreExecute() {
            failToFetch = true
            arrayListFaculty = ArrayList()
            super.onPreExecute()
        }

        protected override fun doInBackground(vararg params: Void): Void? {
            try {
                // get faculty list
                htmlDoc = Jsoup.connect("http://icress.uitm.edu.my/jadual/jadual/jadual.asp").get()
                val lists = htmlDoc.select("option")
                arrayListFaculty = ArrayList()
                var temp = ""
                for (e in lists) {
                    temp = e.html().toString().replace(" \\[".toRegex(), ", ").replace(" &amp; ".toRegex(), " ").replace("\\(".toRegex(), "").replace("\\)".toRegex(), "").toUpperCase()
                    arrayListFaculty!!.add(ClassFaculty(temp.substring(0, Math.min(temp.length, 2)), temp.substring(3, temp.length)))
                    failToFetch = false
                }
            } catch (e: IOException) {
                // exception for error occur
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            if (failToFetch) {
                // load existing faculty list
                arrayListFaculty!!.clear()
                arrayListFaculty = loadFaculty(ActivityMain.contextMain)
            } else {
                // save latest faculty list
                saveFaculty(arrayListFaculty, ActivityMain.contextMain)
            }
        }
    }

    class JsoupAsyncTaskFetchTimetable : AsyncTask<String?, Void?, Void?>() {
        var arrayListTimetable: ArrayList<ClassTimetable?>? = null
        var arrayListFaculty: ArrayList<ClassFaculty>? = null
        var arrayListCourse: ArrayList<ClassCourse?>? = null
        var htmlDoc: Document? = null
        var failToFetch = false
        override fun onPreExecute() {
            failToFetch = true
            arrayListFaculty = loadFaculty(ActivityMain.contextMain)
            arrayListTimetable = loadTimetable(ActivityMain.contextMain)
            arrayListCourse = loadCourse(ActivityMain.contextMain)
            ActivityCourse.progressBar!!.setVisibility(View.VISIBLE)
            super.onPreExecute()
        }

        protected override fun doInBackground(vararg params: String): Void? {
            val group = params[0]
            val course = params[1]
            var timeIndex: String
            for (i in arrayListFaculty!!.indices) {
                try {
                    htmlDoc = Jsoup.connect("http://icress.uitm.edu.my/jadual/" + arrayListFaculty!![i].code + "/" + course + ".html").get()
                    for (table in htmlDoc.select("table")) {
                        for (row in table.select("tr")) {
                            val tds = row.select("td")
                            if (tds[0].text().contains(group)) {
                                // day based code
                                timeIndex = if (tds[3].text() == "Monday") {
                                    "A"
                                } else if (tds[3].text() == "Tuesday") {
                                    "B"
                                } else if (tds[3].text() == "Wednesday") {
                                    "C"
                                } else if (tds[3].text() == "Thursday") {
                                    "D"
                                } else if (tds[3].text() == "Friday") {
                                    "E"
                                } else if (tds[3].text() == "Saturday") {
                                    "F"
                                } else if (tds[3].text() == "Sunday") {
                                    "G"
                                } else {
                                    "Z"
                                }

                                // meridian based code
                                timeIndex += if (tds[1].text().contains("am")) {
                                    "A"
                                } else if (tds[1].text().contains("pm")) {
                                    "B"
                                } else {
                                    "Z"
                                }

                                // time based code
                                timeIndex += if (tds[1].text().contains("12:")) {
                                    "A"
                                } else if (tds[1].text().substring(0, Math.min(tds[1].text().length, 2)).contains("1:")) {
                                    "B"
                                } else if (tds[1].text().contains("2:")) {
                                    "C"
                                } else if (tds[1].text().contains("3:")) {
                                    "D"
                                } else if (tds[1].text().contains("4:")) {
                                    "E"
                                } else if (tds[1].text().contains("5:")) {
                                    "F"
                                } else if (tds[1].text().contains("6:")) {
                                    "G"
                                } else if (tds[1].text().contains("7:")) {
                                    "H"
                                } else if (tds[1].text().contains("8:")) {
                                    "I"
                                } else if (tds[1].text().contains("9:")) {
                                    "J"
                                } else if (tds[1].text().contains("10:")) {
                                    "K"
                                } else if (tds[1].text().contains("11:")) {
                                    "L"
                                } else {
                                    "Z"
                                }

                                // add and save data
                                arrayListTimetable!!.add(ClassTimetable(course, tds[0].text(), tds[1].text(), tds[2].text(), tds[3].text(), timeIndex, tds[6].text()))
                                arrayListCourse!!.add(ClassCourse(arrayListFaculty!![i].code, course, group, ""))
                                failToFetch = false
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            if (failToFetch) {
                // notify by toast
                Toast.makeText(ActivityMain.contextMain, "Course not found", Toast.LENGTH_SHORT).show()
            } else {
                // sanitize and save data
                saveCourse(arrayListCourse, ActivityMain.contextMain)
                saveTimetable(arrayListTimetable, ActivityMain.contextMain)
                Toast.makeText(ActivityMain.contextMain, "Course added", Toast.LENGTH_SHORT).show()
            }
            ActivityCourse.progressBar!!.setVisibility(View.GONE)
            val adapter = AdapterCourse(arrayListCourse, ActivityMain.contextMain)
            ActivityCourse.listview!!.setAdapter(adapter)
        }
    }

    class JsoupAsyncTaskRefetchTimetable : AsyncTask<Void?, Void?, Void?>() {
        var arrayListTimetable: ArrayList<ClassTimetable?>? = null
        var arrayListFaculty: ArrayList<ClassFaculty>? = null
        var arrayListCourse: ArrayList<ClassCourse?>? = null
        var htmlDoc: Document? = null
        var failToFetch = false
        override fun onPreExecute() {
            super.onPreExecute()
            ActivityTimetable.progressBar!!.setVisibility(View.VISIBLE)

            // initialize variables
            arrayListTimetable = ArrayList()
            arrayListCourse = loadCourse(ActivityMain.contextMain)
            failToFetch = true
        }

        protected override fun doInBackground(vararg voids: Void): Void? {
            var timeIndex: String
            for (i in arrayListCourse!!.indices) {
                try {
                    htmlDoc = Jsoup.connect("http://icress.uitm.edu.my/jadual/" + arrayListCourse!![i]!!.faculty + "/" + arrayListCourse!![i]!!.code + ".html").get()
                    for (table in htmlDoc.select("table")) {
                        for (row in table.select("tr")) {
                            val tds = row.select("td")
                            if (tds[0].text().contains(arrayListCourse!![i]!!.group!!)) {

                                // day based code
                                timeIndex = if (tds[3].text() == "Monday") {
                                    "A"
                                } else if (tds[3].text() == "Tuesday") {
                                    "B"
                                } else if (tds[3].text() == "Wednesday") {
                                    "C"
                                } else if (tds[3].text() == "Thursday") {
                                    "D"
                                } else if (tds[3].text() == "Friday") {
                                    "E"
                                } else if (tds[3].text() == "Saturday") {
                                    "F"
                                } else if (tds[3].text() == "Sunday") {
                                    "G"
                                } else {
                                    "Z"
                                }

                                // meridian based code
                                timeIndex += if (tds[1].text().contains("am")) {
                                    "A"
                                } else if (tds[1].text().contains("pm")) {
                                    "B"
                                } else {
                                    "Z"
                                }

                                // time based code
                                timeIndex += if (tds[1].text().contains("12:")) {
                                    "A"
                                } else if (tds[1].text().substring(0, Math.min(tds[1].text().length, 2)).contains("1:")) {
                                    "B"
                                } else if (tds[1].text().contains("2:")) {
                                    "C"
                                } else if (tds[1].text().contains("3:")) {
                                    "D"
                                } else if (tds[1].text().contains("4:")) {
                                    "E"
                                } else if (tds[1].text().contains("5:")) {
                                    "F"
                                } else if (tds[1].text().contains("6:")) {
                                    "G"
                                } else if (tds[1].text().contains("7:")) {
                                    "H"
                                } else if (tds[1].text().contains("8:")) {
                                    "I"
                                } else if (tds[1].text().contains("9:")) {
                                    "J"
                                } else if (tds[1].text().contains("10:")) {
                                    "K"
                                } else if (tds[1].text().contains("11:")) {
                                    "L"
                                } else {
                                    "Z"
                                }

                                // save data
                                arrayListTimetable!!.add(ClassTimetable(arrayListCourse!![i]!!.code, tds[0].text(), tds[1].text(), tds[2].text(), tds[3].text(), timeIndex, tds[6].text()))
                                failToFetch = false
                            }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {

            // save and notify user
            if (arrayListTimetable!!.size > 0) {
                saveTimetable(arrayListTimetable, ActivityMain.contextMain)
                Toast.makeText(ActivityMain.contextMain, "Course rechecked", Toast.LENGTH_SHORT).show()
            } else {
                arrayListTimetable = loadTimetable(ActivityMain.contextMain)
                Toast.makeText(ActivityMain.contextMain, "Course recheck failed", Toast.LENGTH_SHORT).show()
            }
            val adapter = AdapterTimetable(arrayListTimetable, ActivityMain.contextMain)
            ActivityTimetable.listview!!.setAdapter(adapter)
            ActivityTimetable.progressBar!!.setVisibility(View.GONE)
        }
    }
}