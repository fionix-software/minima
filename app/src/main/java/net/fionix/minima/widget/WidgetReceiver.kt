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
import java.text.SimpleDateFormat
import java.util.*


open class WidgetReceiver : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {

        for (appWidgetId in appWidgetIds) {

            // layout
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)

            // refresh list
            remoteViews.setOnClickPendingIntent(R.id.imageView, pendingSelfIntentRefresh(context))

            // open application on widget top bar click
            remoteViews.setTextViewText(R.id.textView, SimpleDateFormat("EEEE", Locale.ENGLISH).format(Calendar.getInstance().time))
            remoteViews.setOnClickPendingIntent(R.id.textView, PendingIntent.getActivity(context, 0, Intent(context, ActivitySplash::class.java), 0))

            // randomize (to trick widget framework for update view on-demand)
            randomNumber = Random().nextInt(50)

            // populate data
            val remoteIntent = Intent(context, WidgetService::class.java)
            remoteIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            remoteIntent.data = Uri.fromParts("contents", (appWidgetId + randomNumber).toString(), null)
            remoteViews.setRemoteAdapter(R.id.listView, remoteIntent)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
        }

        // super
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {

        // super
        super.onReceive(context, intent)
        val action = intent.action

        // this is needed due to intent received might come from text view setOnClickPendingIntent
        if (action == "refresh") {

            // get current widgets information
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = appWidgetManager.getAppWidgetIds(ComponentName(context, WidgetReceiver::class.java))

            // update widget
            onUpdate(context, appWidgetManager, widgetIds)
            appWidgetManager.updateAppWidget(ComponentName(context, WidgetReceiver::class.java), remoteViews)

            // toast
            Toast.makeText(context, "Refresh", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pendingSelfIntentRefresh(context: Context): PendingIntent {
        val intent = Intent(context, javaClass)
        intent.action = "refresh"
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    companion object {
        var randomNumber = 0
    }
}