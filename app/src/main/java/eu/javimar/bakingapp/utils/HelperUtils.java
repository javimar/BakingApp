package eu.javimar.bakingapp.utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;


public final class HelperUtils
{
    private static final String LOG_TAG = HelperUtils.class.getName();

    // final class
    private HelperUtils() {}

    /** Returns true if the network is connected or about_layout to become available */
    public static boolean isNetworkAvailable(Context context)
    {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public static int fetchColor(Context context, @ColorRes int color)
    {
        return ContextCompat.getColor(context, color);
    }


    /** Loads the Recipes json file from assets into a string memory */
    public static String loadJSONFromAsset(Context context)
    {
        String json = null;
        try
        {
            InputStream is = context.getAssets().open("baking.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex) {
            Log.e(LOG_TAG, "Problem parsing the assets JSON file ", ex);
        }
        return json;
    }



}
