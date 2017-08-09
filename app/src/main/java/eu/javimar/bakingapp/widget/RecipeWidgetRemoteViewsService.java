package eu.javimar.bakingapp.widget;

import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import eu.javimar.bakingapp.R;


public class RecipeWidgetRemoteViewsService extends RemoteViewsService
{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        final String[] mList = intent.getStringArrayExtra("widget_ing");

        return new RemoteViewsFactory()
        {
            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                // This method is called by the app hosting the widget (e.g., the launcher)
            }

            @Override
            public void onDestroy() {
            }

            @Override
            public int getCount() {
                return mList.length;
            }

            @Override
            public RemoteViews getViewAt(int position)
            {
                if (position == AdapterView.INVALID_POSITION)
                {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.recipe_widget_list_item);

                // set the values
                views.setTextViewText(R.id.widget_ingredient_item, mList[position]);

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra("fill_in", mList[position]);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.recipe_widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position)
            {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
