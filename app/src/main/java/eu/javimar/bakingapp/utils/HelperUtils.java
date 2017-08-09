package eu.javimar.bakingapp.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
        String jsonString = null;
        try
        {
            InputStream is = context.getAssets().open("baking.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        }
        catch (IOException ex) {
            Log.e(LOG_TAG, "Problem parsing the assets JSON file ", ex);
        }
        return jsonString;
    }

    // Stores the ingredients in the preferences for the widget
    public static void storeIngredientsInPreferences(Context context, String recipeName,
                                                     List<String> ingredientList)
    {
        Set<String> ingredients = new HashSet<>(ingredientList);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(recipeName, ingredients);
        editor.apply();
    }


    public static String[] getIngredientsFromPreferences(Context context, String recipeName)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = prefs.getStringSet(recipeName, new HashSet<String>());
        return set.toArray(new String[set.size()]);
    }

    // returns the recipe names = the keys of the prefenrences
    public static Set<String> getRecipeKeysFromPreferences(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> allEntries = prefs.getAll();
        return allEntries.keySet();
    }

    public static String deletesStepNumber(String string)
    {
        // Remove all numbers from steps
        return string.replaceAll("(^\\d\\.|\\d\\d\\.)", "");
    }


}
