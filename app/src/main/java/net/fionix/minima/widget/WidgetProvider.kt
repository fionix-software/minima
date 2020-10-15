package net.fionix.minima.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import net.fionix.minima.ActivitySplash
import net.fionix.minima.R
import java.util.*

class WidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (i in appWidgetIds.indices) {
            // layout
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)

            // randomizer
            val rand = Random()
            randomNumber = rand.nextInt(50)

            // bg_button
            remoteViews.setOnClickPendingIntent(R.id.imageView1, getPendingSelfIntent(context, "refresh"))

            // open apps
            val openMainAppIntent = Intent(context, ActivitySplash::class.java)
            openMainAppIntent.putExtra("Source", "Widget")
            val openMainAppPendingIntent = PendingIntent.getActivity(context, 0, openMainAppIntent, 0)
            remoteViews.setOnClickPendingIntent(R.id.textView1, openMainAppPendingIntent)

            // day
            val calendar = Calendar.getInstance()
            val day_int = calendar[Calendar.DAY_OF_WEEK]
            var day_str = ""
            when (day_int) {
                Calendar.MONDAY -> day_str = "Monday"
                Calendar.TUESDAY -> day_str = "Tuesday"
                Calendar.WEDNESDAY -> day_str = "Wednesday"
                Calendar.THURSDAY -> day_str = "Thursday"
                Calendar.FRIDAY -> day_str = "Friday"
                Calendar.SATURDAY -> day_str = "Saturday"
                Calendar.SUNDAY -> day_str = "Sunday"
            }

            // textview
            remoteViews.setTextViewText(R.id.textView1, day_str)

            // populate data
            val remoteIntent = Intent(context, WidgetService::class.java)
            remoteIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            remoteIntent.data = Uri.fromParts("contents", (appWidgetIds[i] + randomNumber).toString(), null)
            remoteViews.setRemoteAdapter(R.id.listView1, remoteIntent)
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    protected fun getPendingSelfIntent(context: Context?, action: String?): PendingIntent {
        val intent = Intent(context, javaClass)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val action = intent.action

        // refresh widget
        if (action == "refresh") {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, WidgetProvider::class.java))
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
            onUpdate(context, appWidgetManager, widgetIds)
            appWidgetManager.updateAppWidget(ComponentName(context, WidgetProvider::class.java), remoteViews)
            Toast.makeText(context, "Refreshed!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        var randomNumber = 0
    }
}