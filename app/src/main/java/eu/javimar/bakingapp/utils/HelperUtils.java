package eu.javimar.bakingapp.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.javimar.bakingapp.R;

/**
 * Utilities classs with helper methods
 */
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


    /** Loads the Recipes json file from assets into a string memory
     *  (Currently not necessary since it gets it from the Internet)
     */
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

    /** Stores the ingredients in the preferences for the widget */
    public static void storeIngredientsInPreferences(Context context, String recipeName,
                                                     List<String> ingredientList)
    {
        Set<String> ingredients = new HashSet<>(ingredientList);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(recipeName, ingredients);
        editor.apply();
    }

    /** Gets list of ingredients from the SharedPreferences */
    public static String[] getIngredientsFromPreferences(Context context, String recipeName)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set = prefs.getStringSet(recipeName, new HashSet<String>());
        return set.toArray(new String[set.size()]);
    }

    /** Returns the recipe names = the keys of the preferences */
    public static Set<String> getRecipeKeysFromPreferences(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> allEntries = prefs.getAll();
        return allEntries.keySet();
    }

    /** Deletes step numbering to make step more consistent */
    public static String deletesStepNumber(String string)
    {
        // Remove all numbers from stepsn
        return string.replaceAll("(^\\d\\.|\\d\\d\\.)", "");
    }

    /** Displays colorful snackbar messages */
    public static void showSnackbar (Context context, View view, String message)
    {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbview = snack.getView();
        sbview.setBackgroundColor(fetchColor(context, R.color.primary_light));
        TextView textView = sbview.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context, R.color.primary_dark));
        snack.show();
    }


    /** Returns true if url string ends with an image extension */
    public static boolean isPicture(String url) {
        return !TextUtils.isEmpty(url) &&
                (url.endsWith(".jpg") || url.endsWith(".png"));
    }

}
