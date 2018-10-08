package net.fionix.minima;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;

public class WidgetProvider extends AppWidgetProvider {

    public static int randomNumber;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; ++i) {
            // layout
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // randomizer
            Random rand = new Random();
            randomNumber = rand.nextInt(50);

            // bg_button
            remoteViews.setOnClickPendingIntent(R.id.imageView1, getPendingSelfIntent(context, "refresh"));

            // open apps
            Intent openMainAppIntent = new Intent(context, ActivitySplash.class);
            openMainAppIntent.putExtra("Source", "Widget");
            PendingIntent openMainAppPendingIntent = PendingIntent.getActivity(context, 0, openMainAppIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.textView1, openMainAppPendingIntent);

            // day
            Calendar calendar = Calendar.getInstance();
            int day_int = calendar.get(Calendar.DAY_OF_WEEK);
            String day_str = "";

            switch (day_int) {
                case Calendar.MONDAY:
                    day_str = "Monday";
                    break;

                case Calendar.TUESDAY:
                    day_str = "Tuesday";
                    break;

                case Calendar.WEDNESDAY:
                    day_str = "Wednesday";
                    break;

                case Calendar.THURSDAY:
                    day_str = "Thursday";
                    break;

                case Calendar.FRIDAY:
                    day_str = "Friday";
                    break;

                case Calendar.SATURDAY:
                    day_str = "Saturday";
                    break;

                case Calendar.SUNDAY:
                    day_str = "Sunday";
                    break;
            }

            // textview
            remoteViews.setTextViewText(R.id.textView1, day_str);

            // populate data
            Intent remoteIntent = new Intent(context, WidgetService.class);
            remoteIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            remoteIntent.setData(Uri.fromParts("contents", String.valueOf(appWidgetIds[i] + randomNumber), null));
            remoteViews.setRemoteAdapter(R.id.listView1, remoteIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();

        // refresh widget
        if (action.equals("refresh")) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int widgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, WidgetProvider.class));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            onUpdate(context, appWidgetManager, widgetIds);
            appWidgetManager.updateAppWidget(new ComponentName(context, WidgetProvider.class), remoteViews);
            Toast.makeText(context, "Refreshed!", Toast.LENGTH_SHORT).show();
        }
    }
}
