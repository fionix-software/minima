package net.fionix.minima;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    public Context context;
    public int resourceId;
    ArrayList<ClassTimetable> arrayListTimetable;

    public WidgetFactory(Context context, Intent intent) {
        this.context = context;
        resourceId = Integer.valueOf(intent.getData().getSchemeSpecificPart()) - WidgetProvider.randomNumber;
        arrayListTimetable = ClassMinima.filterForToday(ClassMinima.loadTimetable(context));
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (arrayListTimetable != null) {
            return arrayListTimetable.size();
        } else return 0;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.listview_widget);

        String courseName = "";

        ArrayList<ClassCourse> arrayListCourseCode = ClassMinima.loadCourse(context);
        for (int j = 0; j < arrayListCourseCode.size(); j++) {
            if (arrayListCourseCode.get(j).code.contains(arrayListTimetable.get(i).course)) {
                if (arrayListCourseCode.get(j).title.isEmpty())
                    courseName = "You can edit course name at the Course tab";
                else courseName = arrayListCourseCode.get(j).title;
                break;
            }
        }

        row.setTextViewText(R.id.textViewCourseName, courseName);
        row.setTextViewText(R.id.textViewCourseCode, arrayListTimetable.get(i).course);
        row.setTextViewText(R.id.textViewGroup, arrayListTimetable.get(i).group);
        row.setTextViewText(R.id.textViewTime, arrayListTimetable.get(i).start + " - " + arrayListTimetable.get(i).end);
        row.setTextViewText(R.id.textViewLocation, arrayListTimetable.get(i).location.toUpperCase());
        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
