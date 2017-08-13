package eu.javimar.bakingapp.sync;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import eu.javimar.bakingapp.MainActivity;

/**
 * An IntentService subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * Notice all request may take as long as necessary (and will not block the application's UI),
 * but only one request will be processed at a time.
 */
public class LoaderIntentService extends IntentService
{
    public LoaderIntentService()
    {
        super(LoaderIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        if (intent != null)
        {
            LoadingTasks.executeTask(this, intent.getAction());

            // once finished, notify MainActivity via a BroadcastReceiver
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MainActivity.RecipesLoadedReceiver.RECIPES_LOADED_RESPONSE);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            sendBroadcast(broadcastIntent);
        }
    }
}
