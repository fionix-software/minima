package net.fionix.minima;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by nazebzurati on 10/31/17.
 */

public class ClassMinima {

    public static void saveTimetable(ArrayList<ClassTimetable> a, Context c) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sanitizeTimetable(a));
        prefsEditor.putString("timetable", json);
        prefsEditor.commit();
    }

    public static ArrayList<ClassTimetable> loadTimetable(Context c) {
        Gson gson = new Gson();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        String response = mPrefs.getString("timetable", "");
        ArrayList<ClassTimetable> arrayListTimetable = gson.fromJson(response, new TypeToken<List<ClassTimetable>>() {
        }.getType());

        if (arrayListTimetable != null) {
            return arrayListTimetable;
        } else {
            return new ArrayList<>();
        }
    }

    public static void saveFaculty(ArrayList<ClassFaculty> a, Context c) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(a);
        prefsEditor.putString("faculty", json);
        prefsEditor.commit();
    }

    public static ArrayList<ClassFaculty> loadFaculty(Context c) {
        Gson gson = new Gson();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        String response = mPrefs.getString("faculty", "");
        ArrayList<ClassFaculty> arrayListFaculty = gson.fromJson(response, new TypeToken<List<ClassFaculty>>() {
        }.getType());

        if (arrayListFaculty != null) {
            return arrayListFaculty;
        } else {
            return new ArrayList<>();
        }
    }

    public static void saveCourse(ArrayList<ClassCourse> a, Context c) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sanitizeCourse(a));
        prefsEditor.putString("course", json);
        prefsEditor.commit();
    }

    public static ArrayList<ClassCourse> loadCourse(Context c) {
        Gson gson = new Gson();
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(c);
        String response = mPrefs.getString("course", "");
        ArrayList<ClassCourse> arrayListCourse = gson.fromJson(response, new TypeToken<List<ClassCourse>>() {
        }.getType());

        if (arrayListCourse != null) {
            return arrayListCourse;
        } else {
            return new ArrayList<>();
        }
    }

    public static ArrayList<ClassTimetable> sanitizeTimetable(ArrayList<ClassTimetable> items) {

        boolean hasChanges = false;

        Collections.sort(items, new Comparator<ClassTimetable>() {
            @Override
            public int compare(ClassTimetable o1, ClassTimetable o2) {
                return o1.getDayCode().compareTo(o2.getDayCode());
            }
        });

        for (int i = 0; i < items.size(); i++) {
            if (i < (items.size() - 1)) {

                // correct original string
                if (items.get(i).start.equals("12:00 am")) {
                    items.get(i).start = "12:00 pm";
                }
                if (items.get(i).end.equals("12:00 am")) {
                    items.get(i).end = "12:00 pm";
                }
                if (items.get(i).start.equals("13:00 pm")) {
                    items.get(i).start = "1:00 pm";
                }
                if (items.get(i).end.equals("13:00 pm")) {
                    items.get(i).end = "1:00 pm";
                }
                if (items.get(i).start.contains(":5 ")) {
                    String temp = items.get(i).start.replace(":5 ", ":15 ");
                    items.get(i).start = temp;
                }
                if (items.get(i).end.contains(":5 ")) {
                    String temp = items.get(i).end.replace(":5 ", ":15 ");
                    items.get(i).end = temp;
                }

                if (items.get(i).day.equals(items.get(i + 1).day) && items.get(i).course.equals(items.get(i + 1).course) && items.get(i).end.equals(items.get(i + 1).start)) {
                    items.get(i).end = items.get(i + 1).end;
                    items.remove(i + 1);
                    hasChanges = true;
                    continue;
                }

                if (items.get(i).day.equals(items.get(i + 1).day) && items.get(i).course.equals(items.get(i + 1).course) && (items.get(i).start.equals(items.get(i + 1).start))) {
                    items.remove(i + 1);
                    hasChanges = true;
                    continue;
                }

                if (items.get(i).day.equals(items.get(i + 1).day) && items.get(i).course.equals(items.get(i + 1).course) && (items.get(i).end.equals(items.get(i + 1).end))) {
                    items.remove(i + 1);
                    hasChanges = true;
                    continue;
                }
            }
        }

        if (hasChanges) {
            sanitizeTimetable(items);
        }

        return items;
    }

    public static ArrayList<ClassCourse> sanitizeCourse(ArrayList<ClassCourse> items) {

        boolean hasChanges = false;

        Collections.sort(items, new Comparator<ClassCourse>() {
            @Override
            public int compare(ClassCourse o1, ClassCourse o2) {
                return o1.getCode().compareTo(o2.getCode());
            }
        });

        for (int i = 0; i < items.size(); i++) {
            if (i < (items.size() - 1)) {
                if (items.get(i).code.equals(items.get(i + 1).code) && items.get(i).faculty.equals(items.get(i + 1).faculty)) {
                    items.remove(i + 1);
                    hasChanges = true;
                }
            }
        }

        if (hasChanges) {
            sanitizeCourse(items);
        }

        return items;
    }

    public static void saveString(Context context, String variable, String value) {
        if (context != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(variable, value);
            editor.commit();
        }
    }

    public static String loadString(Context context, String variable) {
        if (context != null) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            return sharedPref.getString(variable, "");
        } else {
            return "";
        }
    }

    public static String guide() {
        return "[Create]\n" +
                "Create menu is where you ic_add your courses in order to create your timetable. To ic_add course, fill in the course code and group name. Please make sure your course name and group name is as specified format as the example (the Ex: text) mentioned. After you have fill in the course code and group name, press 'Add Course' to ic_add course into your timetable. You can 'Recheck timetable' to recheck the timetable for time or location changes. You can also 'Clear Timetable' to clear all your saved courses and clear all your timetable.\n" +
                "\n" +
                "[Timetable]\n" +
                "Timetable menu is where your timetable is display in list. Current day class will be highlighted on the left side of the class.\n" +
                "\n" +
                "[View]\n" +
                "View is where you can view your added courses and delete your courses from your timetable. Long press on an item to delete a course. You can enable or disable long press feature in the Setting menu.\n" +
                "\n" +
                "[Link]\n" +
                "It is just a normal web browser but it automatically load UiTM's student portal. The bg_button at the top of the browser is Refresh, Home, Logout, Print.\n" +
                "- Refresh: To refresh current webpage\n" +
                "- Home: To go back to the student portal homepage\n" +
                "- Logout: Logout your session from student portal\n" +
                "- Print: Print current webpage\n" +
                "\n" +
                "[Setting]\n" +
                "Setting menu is where you can disable/enable the long press to delete course feature on View menu. You can also directly email to me by clicking the 'Email feedback' or open this guide on the Minima version setting. Email me if there is any problem or question regarding Minima. If Minima crash, please send the report on the crash prompt for further improvement." +
                "\n" +
                "[Homescreen Widget]\n" +
                "You can ic_add widget on your homescreen. Click the refresh icon on the upper right side of the widget to refresh. Click the day and the class count on the left of the refresh icon to open Minima directly.";
    }

    public static ArrayList<ClassTimetable> filterForToday(ArrayList<ClassTimetable> data) {

        if (data != null) {
            Calendar calendar = Calendar.getInstance();
            int day_int = calendar.get(Calendar.DAY_OF_WEEK);
            boolean dataChanged = false;

            for (int i = 0; i < data.size(); i++) {
                switch (day_int) {
                    case Calendar.MONDAY:
                        if (!data.get(i).day.equals("Monday")) {
                            data.remove(i);
                            dataChanged = true;
                        }
                        break;

                    case Calendar.TUESDAY:
                        if (!data.get(i).day.equals("Tuesday")) {
                            data.remove(i);
                            dataChanged = true;
                        }
                        break;

                    case Calendar.WEDNESDAY:
                        if (!data.get(i).day.equals("Wednesday")) {
                            data.remove(i);
                            dataChanged = true;
                        }
                        break;

                    case Calendar.THURSDAY:
                        if (!data.get(i).day.equals("Thursday")) {
                            data.remove(i);
                            dataChanged = true;
                        }
                        break;

                    case Calendar.FRIDAY:
                        if (!data.get(i).day.equals("Friday")) {
                            data.remove(i);
                            dataChanged = true;
                        }
                        break;

                    case Calendar.SATURDAY:
                        if (!data.get(i).day.equals("Saturday")) {
                            data.remove(i);
                            dataChanged = true;
                        }
                        break;

                    case Calendar.SUNDAY:
                        if (!data.get(i).day.equals("Sunday")) {
                            data.remove(i);
                            dataChanged = true;
                        }
                        break;
                }
            }

            if (dataChanged) return filterForToday(data);
            else return data;
        }
        return new ArrayList<>();
    }

    public static String generateTable(String day, ArrayList<ClassTimetable> arrayListTimetable) {

        char dayCode;

        if (day.equals("Monday")) {
            dayCode = 'A';
        } else if (day.equals("Tuesday")) {
            dayCode = 'B';
        } else if (day.equals("Wednesday")) {
            dayCode = 'C';
        } else if (day.equals("Thursday")) {
            dayCode = 'D';
        } else if (day.equals("Friday")) {
            dayCode = 'E';
        } else if (day.equals("Saturday")) {
            dayCode = 'F';
        } else if (day.equals("Sunday")) {
            dayCode = 'G';
        } else {
            dayCode = 'Z';
        }

        String html = "<tr><td style=\"background: rgb(211,211,211);\">" + day + "</td>";

        int lastEndCol = 0, colspan = 0;
        for (int i = 0; i < arrayListTimetable.size(); i++) {

            if (Character.toString(arrayListTimetable.get(i).dayCode.charAt(0)).equals(Character.toString(dayCode))) {

                // get start hour
                int charIndexHourStart = arrayListTimetable.get(i).start.indexOf(":");
                String extractedHour = arrayListTimetable.get(i).start.substring(0, charIndexHourStart);
                int intHourStart = Integer.valueOf(extractedHour);
                if (arrayListTimetable.get(i).start.contains("pm")) {
                    if (intHourStart != 12) {
                        intHourStart += 12;
                    }
                }

                // get end hour
                int charIndexHourEnd = arrayListTimetable.get(i).end.indexOf(":");
                extractedHour = arrayListTimetable.get(i).end.substring(0, charIndexHourEnd);
                int intHourEnd = Integer.valueOf(extractedHour);
                if (arrayListTimetable.get(i).end.contains("pm")) {
                    if (intHourEnd != 12) {
                        intHourEnd += 12;
                    }
                }

                // timetable
                int colStart = ((intHourStart - 8) * 2) + 2;
                if (!arrayListTimetable.get(i).start.contains(":00 ")) {
                    colStart += 1;
                }
                int colEnd = ((intHourEnd - 8) * 2) + 1;
                if (!arrayListTimetable.get(i).end.contains(":00 ")) {
                    colEnd += 1;
                }

                // filler
                if (lastEndCol > 0) {
                    colspan = colStart - lastEndCol - 1;
                } else {
                    colspan = colStart - 2;
                }
                if (colspan > 1) {
                    html += "<td style=\"background: rgb(169,169,169);\" colspan=\"" + colspan + "\"></td>";
                }
                if (colspan == 1) {
                    html += "<td style=\"background: rgb(169,169,169);\"></td>";
                }

                // timetable
                colspan = (colEnd - colStart) + 1;
                html += "<td colspan=\"" + colspan + "\">" + arrayListTimetable.get(i).course + "<br>";

                // add location and time
                if (arrayListTimetable.get(i).location.isEmpty()) {
                    html += "-";
                } else {
                    html += arrayListTimetable.get(i).location;
                }
                html += "<br>" + arrayListTimetable.get(i).start + " - " + arrayListTimetable.get(i).end + "</td>";

                lastEndCol = colEnd;
            }
        }

        // fill timetable
        if (lastEndCol > 0) {
            colspan = 31 - lastEndCol;
        }
        else {
            colspan = 31 - 1;
        }
        html += "<td style=\"background: rgb(169,169,169);\" colspan=\"" + colspan + "\"></td>";
        html += "</tr>";

        return html;
    }

    public static String generateCourseList(ArrayList<ClassCourse> arrayListCourse) {

        // create course list as unordered list
        String html = "<p><u>Course list</u></p><ul>";

        // course listing
        for (int i = 0; i < arrayListCourse.size(); i++) {
            html += "<li><strong>" + arrayListCourse.get(i).group + ", " + arrayListCourse.get(i).code + "</strong>: ";
            if (arrayListCourse.get(i).title.isEmpty()) {
                html += "-";
            } else {
                html += arrayListCourse.get(i).title + "</li>";
            }
        }

        // close listing
        html += "</ul>";

        return html;
    }

    public static boolean checkOverlapTimetableTime(ArrayList<ClassTimetable> arrayList) {

        // check for overlap timetable
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < arrayList.size(); j++) {

                // check for same time for the same day
                if (i != j) {
                    if (arrayList.get(i).day.equals(arrayList.get(j).day) && arrayList.get(i).start.equals(arrayList.get(j).start)) {
                        return true;
                    }
                }

                // check for timetable timeline
                if (j > i) {
                    try {
                        if (arrayList.get(i).day.equals(arrayList.get(j).day)) {
                            DateFormat dateFormat1 = new SimpleDateFormat("hh:mm a");
                            Date date1 = dateFormat1.parse(arrayList.get(i).end);

                            DateFormat dateFormat2 = new SimpleDateFormat("hh:mm a");
                            Date date2 = dateFormat2.parse(arrayList.get(j).start);

                            if (date2.before(date1)) {
                                return true;
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return false;
    }

    public static boolean checkDuplicateCourse(ArrayList<ClassCourse> arrayList) {

        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < arrayList.size(); j++) {
                // check for same course code
                if (i != j) {
                    if (arrayList.get(i).code.equals(arrayList.get(j).code)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static class JsoupAsyncTaskFetchFaculty extends AsyncTask<Void, Void, Void> {

        public ArrayList<ClassFaculty> arrayListFaculty;
        Document htmlDoc;
        boolean failToFetch;

        @Override
        protected void onPreExecute() {
            failToFetch = true;
            arrayListFaculty = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // get faculty list
                htmlDoc = Jsoup.connect("http://icress.uitm.edu.my/jadual/jadual/jadual.asp").get();
                Elements lists = htmlDoc.select("option");
                arrayListFaculty = new ArrayList<>();
                String temp = "";
                for (Element e : lists) {
                    temp = e.html().toString().replaceAll(" \\[", ", ").replaceAll(" &amp; ", " ").replaceAll("\\(", "").replaceAll("\\)", "").toUpperCase();
                    arrayListFaculty.add(new ClassFaculty(temp.substring(0, Math.min(temp.length(), 2)), temp.substring(3, temp.length())));
                    failToFetch = false;
                }
            } catch (IOException e) {
                // exception for error occur
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (failToFetch) {
                // load existing faculty list
                arrayListFaculty.clear();
                arrayListFaculty = loadFaculty(ActivityMain.contextMain);
            } else {
                // save latest faculty list
                saveFaculty(arrayListFaculty, ActivityMain.contextMain);
            }
        }
    }

    public static class JsoupAsyncTaskFetchTimetable extends AsyncTask<String, Void, Void> {

        public ArrayList<ClassTimetable> arrayListTimetable;
        public ArrayList<ClassFaculty> arrayListFaculty;
        public ArrayList<ClassCourse> arrayListCourse;
        Document htmlDoc;
        boolean failToFetch;

        protected void onPreExecute() {
            failToFetch = true;
            arrayListFaculty = loadFaculty(ActivityMain.contextMain);
            arrayListTimetable = loadTimetable(ActivityMain.contextMain);
            arrayListCourse = loadCourse(ActivityMain.contextMain);
            ActivityCourse.progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        protected Void doInBackground(String... params) {

            String group = params[0];
            String course = params[1];
            String timeIndex;

            for (int i = 0; i < arrayListFaculty.size(); i++) {
                // Log.d("Code: ", arrayListFaculty.get(i).code);
                try {
                    htmlDoc = Jsoup.connect("http://icress.uitm.edu.my/jadual/" + arrayListFaculty.get(i).code + "/" + course + ".html").get();
                    for (Element table : htmlDoc.select("table")) {
                        for (Element row : table.select("tr")) {
                            Elements tds = row.select("td");
                            if (tds.get(0).text().contains(group)) {
                                // day based code
                                if (tds.get(3).text().equals("Monday")) {
                                    timeIndex = "A";
                                } else if (tds.get(3).text().equals("Tuesday")) {
                                    timeIndex = "B";
                                } else if (tds.get(3).text().equals("Wednesday")) {
                                    timeIndex = "C";
                                } else if (tds.get(3).text().equals("Thursday")) {
                                    timeIndex = "D";
                                } else if (tds.get(3).text().equals("Friday")) {
                                    timeIndex = "E";
                                } else if (tds.get(3).text().equals("Saturday")) {
                                    timeIndex = "F";
                                } else if (tds.get(3).text().equals("Sunday")) {
                                    timeIndex = "G";
                                } else {
                                    timeIndex = "Z";
                                }

                                // meridian based code
                                if (tds.get(1).text().contains("am")) {
                                    timeIndex += "A";
                                } else if (tds.get(1).text().contains("pm")) {
                                    timeIndex += "B";
                                } else {
                                    timeIndex += "Z";
                                }

                                // time based code
                                if (tds.get(1).text().contains("12:")) {
                                    timeIndex += "A";
                                } else if (tds.get(1).text().substring(0, Math.min(tds.get(1).text().length(), 2)).contains("1:")) {
                                    timeIndex += "B";
                                } else if (tds.get(1).text().contains("2:")) {
                                    timeIndex += "C";
                                } else if (tds.get(1).text().contains("3:")) {
                                    timeIndex += "D";
                                } else if (tds.get(1).text().contains("4:")) {
                                    timeIndex += "E";
                                } else if (tds.get(1).text().contains("5:")) {
                                    timeIndex += "F";
                                } else if (tds.get(1).text().contains("6:")) {
                                    timeIndex += "G";
                                } else if (tds.get(1).text().contains("7:")) {
                                    timeIndex += "H";
                                } else if (tds.get(1).text().contains("8:")) {
                                    timeIndex += "I";
                                } else if (tds.get(1).text().contains("9:")) {
                                    timeIndex += "J";
                                } else if (tds.get(1).text().contains("10:")) {
                                    timeIndex += "K";
                                } else if (tds.get(1).text().contains("11:")) {
                                    timeIndex += "L";
                                } else {
                                    timeIndex += "Z";
                                }

                                // add and save data
                                arrayListTimetable.add(new ClassTimetable(course, tds.get(0).text(), tds.get(1).text(), tds.get(2).text(), tds.get(3).text(), timeIndex, tds.get(6).text()));
                                arrayListCourse.add(new ClassCourse(arrayListFaculty.get(i).code, course, group, ""));
                                failToFetch = false;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (failToFetch) {
                // notify by toast
                Toast.makeText(ActivityMain.contextMain, "Course not found", Toast.LENGTH_SHORT).show();
            } else {
                // sanitize and save data
                saveCourse(arrayListCourse, ActivityMain.contextMain);
                saveTimetable(arrayListTimetable, ActivityMain.contextMain);
                Toast.makeText(ActivityMain.contextMain, "Course added", Toast.LENGTH_SHORT).show();
            }

            ActivityCourse.progressBar.setVisibility(View.GONE);
            AdapterCourse adapter = new AdapterCourse(arrayListCourse, ActivityMain.contextMain);
            ActivityCourse.listview.setAdapter(adapter);
        }
    }

    public static class JsoupAsyncTaskRefetchTimetable extends AsyncTask<Void, Void, Void> {

        public ArrayList<ClassTimetable> arrayListTimetable;
        public ArrayList<ClassFaculty> arrayListFaculty;
        public ArrayList<ClassCourse> arrayListCourse;
        Document htmlDoc;
        boolean failToFetch;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ActivityTimetable.progressBar.setVisibility(View.VISIBLE);

            // initialize variables
            arrayListTimetable = new ArrayList<>();
            arrayListCourse = loadCourse(ActivityMain.contextMain);
            failToFetch = true;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String timeIndex;

            for (int i = 0; i < arrayListCourse.size(); i++) {
                try {
                    htmlDoc = Jsoup.connect("http://icress.uitm.edu.my/jadual/" + arrayListCourse.get(i).faculty + "/" + arrayListCourse.get(i).code + ".html").get();
                    for (Element table : htmlDoc.select("table")) {
                        for (Element row : table.select("tr")) {
                            Elements tds = row.select("td");
                            if (tds.get(0).text().contains(arrayListCourse.get(i).group)) {

                                // day based code
                                if (tds.get(3).text().equals("Monday")) {
                                    timeIndex = "A";
                                } else if (tds.get(3).text().equals("Tuesday")) {
                                    timeIndex = "B";
                                } else if (tds.get(3).text().equals("Wednesday")) {
                                    timeIndex = "C";
                                } else if (tds.get(3).text().equals("Thursday")) {
                                    timeIndex = "D";
                                } else if (tds.get(3).text().equals("Friday")) {
                                    timeIndex = "E";
                                } else if (tds.get(3).text().equals("Saturday")) {
                                    timeIndex = "F";
                                } else if (tds.get(3).text().equals("Sunday")) {
                                    timeIndex = "G";
                                } else {
                                    timeIndex = "Z";
                                }

                                // meridian based code
                                if (tds.get(1).text().contains("am")) {
                                    timeIndex += "A";
                                } else if (tds.get(1).text().contains("pm")) {
                                    timeIndex += "B";
                                } else {
                                    timeIndex += "Z";
                                }

                                // time based code
                                if (tds.get(1).text().contains("12:")) {
                                    timeIndex += "A";
                                } else if (tds.get(1).text().substring(0, Math.min(tds.get(1).text().length(), 2)).contains("1:")) {
                                    timeIndex += "B";
                                } else if (tds.get(1).text().contains("2:")) {
                                    timeIndex += "C";
                                } else if (tds.get(1).text().contains("3:")) {
                                    timeIndex += "D";
                                } else if (tds.get(1).text().contains("4:")) {
                                    timeIndex += "E";
                                } else if (tds.get(1).text().contains("5:")) {
                                    timeIndex += "F";
                                } else if (tds.get(1).text().contains("6:")) {
                                    timeIndex += "G";
                                } else if (tds.get(1).text().contains("7:")) {
                                    timeIndex += "H";
                                } else if (tds.get(1).text().contains("8:")) {
                                    timeIndex += "I";
                                } else if (tds.get(1).text().contains("9:")) {
                                    timeIndex += "J";
                                } else if (tds.get(1).text().contains("10:")) {
                                    timeIndex += "K";
                                } else if (tds.get(1).text().contains("11:")) {
                                    timeIndex += "L";
                                } else {
                                    timeIndex += "Z";
                                }

                                // save data
                                arrayListTimetable.add(new ClassTimetable(arrayListCourse.get(i).code, tds.get(0).text(), tds.get(1).text(), tds.get(2).text(), tds.get(3).text(), timeIndex, tds.get(6).text()));
                                failToFetch = false;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // save and notify user
            if (arrayListTimetable.size() > 0) {
                saveTimetable(arrayListTimetable, ActivityMain.contextMain);
                Toast.makeText(ActivityMain.contextMain, "Course rechecked", Toast.LENGTH_SHORT).show();
            } else {
                arrayListTimetable = loadTimetable(ActivityMain.contextMain);
                Toast.makeText(ActivityMain.contextMain, "Course recheck failed", Toast.LENGTH_SHORT).show();
            }

            AdapterTimetable adapter = new AdapterTimetable(arrayListTimetable, ActivityMain.contextMain);
            ActivityTimetable.listview.setAdapter(adapter);
            ActivityTimetable.progressBar.setVisibility(View.GONE);
        }
    }
}
