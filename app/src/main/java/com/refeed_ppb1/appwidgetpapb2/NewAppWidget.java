package com.refeed_ppb1.appwidgetpapb2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String mSharedPreference = BuildConfig.APPLICATION_ID;
    private static final String COUNT_KEY = "count";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.widget_id, String.valueOf(appWidgetId));

        // Mendapatkan nilai count sebelumnya
        SharedPreferences pref = context.getSharedPreferences(mSharedPreference, 0);
        int count = pref.getInt(COUNT_KEY+appWidgetId, 0);
        count++;

        // Menyimpan nilai count ke SharedPreferences lagi
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(COUNT_KEY+appWidgetId, count);
        editor.commit();

        String currentTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
        String output = String.valueOf(count) + " @" + currentTime;

        views.setTextViewText(R.id.appwidget_update, output);

        // Membuat intent untuk dikirimkan ke AppWidgetManager saat button diklik
        Intent intentUpdate = new Intent(context, NewAppWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        int[] appWidgetIds = new int[] {appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        // Extra di bawah tidak bekerja :(
//        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.update_btn, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}