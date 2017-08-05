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

        if(sDualFragments)
            mTvCard.setCardBackgroundColor(fetchColor(getActivity(), R.color.white));

        mRecyclerViewSteps.setHasFixedSize(true);
        mRecyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));
        StepListAdapter adapter = new StepListAdapter(this, mRecipe);
        mRecyclerViewSteps.setAdapter(adapter);
    }


    // Called from Detail Activity to get the ingredients from the recipe
    public void displayRecipeDetail(Recipe recipe)
    {
        // get the recipe
        mRecipe = recipe;
        if(recipe != null)
        {
            int num = recipe.getmIngredientsList().size();
            for(int i = 0; i < num; i++)
            {
                String qty = String.valueOf(recipe.getmIngredientsList().get(i).getmQuantity());
                if(qty.endsWith(".0"))
                    qty = qty.replace(".0", "");

                mTvIngredientsList.append(String.format(getString(R.string.detail_ingredients),
                        qty,
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


    @Override
    public void onStepItemClick(Step step)
    {
        // communicate back the Step from the adapter to the caller
        this.mStepSelectedListener.onItemStepSelected(step);
    }


    // RecipeDetailViewActivity must implement this interface since it passes the step to it
    public interface OnItemStepSelectedListener
    {
        void onItemStepSelected(Step step);
    }
    public void setItemStepListener(OnItemStepSelectedListener listener)
    {
        this.mStepSelectedListener = listener;
    }

}

