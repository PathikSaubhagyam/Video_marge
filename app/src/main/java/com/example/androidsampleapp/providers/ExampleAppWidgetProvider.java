package com.example.androidsampleapp.providers;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;

public class ExampleAppWidgetProvider  extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i=0; i < appWidgetIds.length; i++) {
            int appWidgetId = appWidgetIds[i];
            System.out.println("mnb:: appWidgetId: " + appWidgetId);
            // Create an Intent to launch ExampleActivity
//            Intent intent = new Intent(context, ExampleActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(
//                    /* context = */ context,
//                    /* requestCode = */ 0,
//                    /* intent = */ intent,
//                    /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
//            );

            // Get the layout for the widget and attach an on-click listener
            // to the button.
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_appwidget_layout);
//            views.setOnClickPendingIntent(R.id.button, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget.
//            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }



}
