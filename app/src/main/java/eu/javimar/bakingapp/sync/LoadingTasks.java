/**
 *
 * Baking app
 *
 * @author Javier Martín
 * @email: javimardeveloper@gmail.com
 * @link http://www.javimar.eu
 * @package eu.javimar.bakingapp
 * @version 1.0
 *
BSD 3-Clause License

Copyright (c) 2017 JaviMar
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.

 * Neither the name of the copyright holder nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

BAKING APP: Part of the Android Developer Nanodegree
Allow a user to select a recipe and see video-guided steps for how to complete it.
 */
package eu.javimar.bakingapp.sync;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eu.javimar.bakingapp.model.Ingredient;
import eu.javimar.bakingapp.model.Recipe;
import eu.javimar.bakingapp.model.Step;

import static eu.javimar.bakingapp.MainActivity.master_list;
import static eu.javimar.bakingapp.utils.HelperUtils.loadJSONFromAsset;

@SuppressWarnings("all")
public class LoadingTasks
{
    private static final String LOG_TAG = LoadingTasks.class.getSimpleName();

    /** ACTIONS TO PERFORM */
    public static final String ACTION_LOAD_RECIPES= "load_recipes";


    public static void executeTask(Context context, String action)
    {
        switch (action)
        {
            case ACTION_LOAD_RECIPES:
                loadRecipes(context);
                break;
        }
    }


    private static void loadRecipes(final Context context)
    {
        // Clear and instantiate a new master list of Recipes
        master_list = null;
        master_list = new ArrayList<>();

        List<Step> stepList = null;
        List<Ingredient> ingredientList = null;
        double quantity;
        int recipeId, servings, stepId;
        String recipeImage, recipeName, measure, ingredient,
                stepShortDescription, stepDescription, stepVideoUrl, stepThumbnailUrl;

        // load json file into an array
        String recipesJson = loadJSONFromAsset(context);
        try
        {
            // baking.json is an array of JSON objects of type Recipe
            JSONArray recipesArray = new JSONArray(recipesJson);

            for(int i = 0; i < recipesArray.length(); i++)
            {
                ingredientList = new ArrayList<>();
                stepList = new ArrayList<>();

                JSONObject currentRecipe = recipesArray.getJSONObject(i);
                recipeId = currentRecipe.getInt("id");
                recipeName = currentRecipe.getString("name");

                JSONArray ingredientsArray = currentRecipe.getJSONArray("ingredients");
                for(int j = 0; j < ingredientsArray.length(); j++)
                {
                    JSONObject currentIngredient = ingredientsArray.getJSONObject(j);
                    quantity = currentIngredient.getDouble("quantity");
                    measure = currentIngredient.getString("measure");
                    ingredient = currentIngredient.getString("ingredient");
                    ingredientList.add(new Ingredient(quantity, measure, ingredient));
                }

                JSONArray stepArray = currentRecipe.getJSONArray("steps");
                for(int k = 0; k < stepArray.length(); k++)
                {
                    JSONObject currentStep = stepArray.getJSONObject(k);
                    stepId = currentStep.getInt("id");
                    stepShortDescription = currentStep.getString("shortDescription");
                    stepDescription = currentStep.getString("description");
                    stepVideoUrl = currentStep.getString("videoURL");
                    stepThumbnailUrl = currentStep.getString("thumbnailURL");
                    stepList.add(new Step(stepId, stepShortDescription,
                            stepDescription, stepVideoUrl, stepThumbnailUrl));
                }
                servings = currentRecipe.getInt("servings");
                recipeImage = currentRecipe.getString("image");

                // Add recipe to master list
                master_list.add(new Recipe(recipeId, recipeName,
                        ingredientList, stepList, servings, recipeImage));
            }
        }
        catch (JSONException e)
        {
            Log.e(LOG_TAG, "Problem parsing the recipes JSON file", e);
        }
    }

}

