package eu.javimar.bakingapp.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.javimar.bakingapp.R;
import eu.javimar.bakingapp.model.Recipe;
import eu.javimar.bakingapp.model.Step;

import static eu.javimar.bakingapp.RecipeDetailViewActivity.sDualFragments;
import static eu.javimar.bakingapp.utils.HelperUtils.fetchColor;


public class RecipeDetailViewFragment extends Fragment implements
        StepListAdapter.ListStepClickListener
{
    private static String LOG_TAG = RecipeDetailViewFragment.class.getName();

    @BindView(R.id.rv_recipe_steps)RecyclerView mRecyclerViewSteps;
    @BindView(R.id.tv_ingredients_list)TextView mTvIngredientsList;
    @BindView(R.id.ingredients_card)CardView mTvCard;
    @BindView(R.id.tv_servings)TextView mTvServings;

    public RecipeDetailViewFragment() {
    }

    private Recipe mRecipe;
    OnItemStepSelectedListener mStepSelectedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View viewGroup = inflater.inflate(R.layout.recipe_detail_view, container, false);
        ButterKnife.bind(this, viewGroup);

        return viewGroup;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // make the card white for tablets (cosmetically better)
        if(sDualFragments)
            mTvCard.setCardBackgroundColor(fetchColor(getActivity(), R.color.white));

        // set up adapter to display RecyclerView for step list
        mRecyclerViewSteps.setHasFixedSize(true);
        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));
        StepListAdapter adapter = new StepListAdapter(this, mRecipe);
        mRecyclerViewSteps.setAdapter(adapter);
    }


    // Called from Detail Activity to fill the ingredients for the recipe
    public void displayRecipeDetail(Recipe recipe)
    {
        // get the recipe
        mRecipe = recipe;
        if(recipe != null)
        {
            int num = recipe.getmIngredientsList().size();
            for(int i = 0; i < num; i++)
            {
                mTvIngredientsList.append(String.format(getString(R.string.detail_ingredients),
                        recipe.getmIngredientsList().get(i).getmQuantity(),
                        recipe.getmIngredientsList().get(i).getmMeasureUnit(),
                        recipe.getmIngredientsList().get(i).getmIngredientName()));
                // skip last newline
                if(i != num - 1)
                    mTvIngredientsList.append("\n");
            }
            mTvServings.setText(String.format(getString(R.string.detail_step_servings),
                    recipe.getmServings()));
        }
    }

    // from StepListAdapter, it brings the step being clicked
    @Override
    public void onStepItemClick(Step step)
    {
        // communicate back the Step from the adapter to RecipeDetailViewActivity
        this.mStepSelectedListener.onItemStepSelected(step);
    }


    // RecipeDetailViewActivity must implement this interface so it can deliver the step to it
    public interface OnItemStepSelectedListener
    {
        void onItemStepSelected(Step step);
    }
    public void setItemStepListener(OnItemStepSelectedListener listener)
    {
        this.mStepSelectedListener = listener;
    }

}

