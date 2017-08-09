package eu.javimar.bakingapp.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.javimar.bakingapp.MainActivity;
import eu.javimar.bakingapp.R;

import static eu.javimar.bakingapp.utils.HelperUtils.getIngredientsFromPreferences;
import static eu.javimar.bakingapp.utils.HelperUtils.getRecipeKeysFromPreferences;


/**
 * The configuration screen for the {@link RecipeWidgetProvider RecipeWidgetProvider} AppWidget.
 */
public class RecipeWidgetProviderConfigureActivity extends AppCompatActivity
{
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @BindView(R.id.widget_add_button)Button bAddWidget;
    @BindView(R.id.ll_radio_buttons) LinearLayout llRadioButtons;

    String[] mIngredients;
    String mRecipe;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_provider_configure);
        ButterKnife.bind(this);

        // Add radios buttons with Recipe name to choose
        createRadioButton();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null)
        {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
        {
            finish();
        }
    }



    @OnClick(R.id.widget_add_button)
    public void addWidget()
    {
        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        // Instantiating the class RemoteViews with widget_layout
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.recipe_widget_provider);

        // set widge title with recipe name
        views.setTextViewText(R.id.widget_header_title, mRecipe);

        // Create an Intent to launch MainActivity when tapping on the widget title
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_header_frame, pendingIntent);

        // Set up the collection to display the ingredients
        setRemoteAdapter(views);

        // Create an Intent to launch MainActivity when tapping on the list
        Intent clickIntentTemplate = new Intent(this, MainActivity.class);
        PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(clickIntentTemplate)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_listview_ingredients,
                clickPendingIntentTemplate);

        // set empty view text, if ListView is empty
        views.setEmptyView(R.id.widget_listview_ingredients, R.id.widget_empty);

        // tell the AppWidgetManager to perform an update on the app widget
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        // pass the result of this activity to the widget to create it
        Intent resultValue = new Intent();
        // Make sure we pass back the original appWidgetId
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void setRemoteAdapter(RemoteViews views)
    {
        Intent intent = new Intent(new Intent(this, RecipeWidgetRemoteViewsService.class));
        intent.putExtra("widget_ing", mIngredients);
        views.setRemoteAdapter(R.id.widget_listview_ingredients, intent);
    }


    private void createRadioButton()
    {
        // get recipes from preferences
        Set<String> recipes = getRecipeKeysFromPreferences(this);
        RadioButton rb;

        RadioGroup rg = new RadioGroup(this);
        rg.setOrientation(RadioGroup.VERTICAL);

        for(final String recipe : recipes)
        {
            rb = new RadioButton(this);
            rb.setText(recipe);
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parseRecipe(recipe);
                }
            });
            rg.addView(rb); // RadioButtons are added to the radioGroup instead of the layout
        }
        llRadioButtons.addView(rg);//you add the whole RadioGroup to the layout
    }


    private void parseRecipe(String recipe)
    {
        mIngredients = getIngredientsFromPreferences(this, recipe);
        mRecipe = recipe;
    }

}

