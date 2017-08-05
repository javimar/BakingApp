package eu.javimar.bakingapp.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.javimar.bakingapp.R;
import eu.javimar.bakingapp.model.Recipe;

import static eu.javimar.bakingapp.MainActivity.master_list;
import static eu.javimar.bakingapp.MainActivity.sCardColor;

@SuppressWarnings("all")
public class RecipeListAdapter extends
        RecyclerView.Adapter<RecipeListAdapter.RecipeListHolder>
{
    // The interface that receives onClick messages
    public interface ListRecipeClickListener
    {
        void onRecipeItemClick(Recipe recipe);
    }
    private final ListRecipeClickListener mOnClickListener;

    /**
     * Adapter constructor will receive an object that implements this interface
     */
    public RecipeListAdapter(ListRecipeClickListener listener)
    {
        mOnClickListener = listener;
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     */
    @Override
    public RecipeListHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListAdapter.RecipeListHolder holder, int position)
    {
        holder.bind(master_list.get(position), mOnClickListener);
    }


    /**
     * Cache of the children views for a movie list item.
     */
    public class RecipeListHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_recipe_name)TextView tvRecipeName;
        @BindView(R.id.recipe_card)CardView cvCard;

        public RecipeListHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cvCard.setCardBackgroundColor(sCardColor);
        }

        public void bind(final Recipe recipe, final ListRecipeClickListener listener)
        {
            // set the name for recipe
            tvRecipeName.setText(recipe.getmRecipeName());

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    listener.onRecipeItemClick(recipe);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return master_list.size();
    }


    public void swap(List<Recipe> data)
    {
        master_list.clear();
        master_list.addAll(data);
        notifyDataSetChanged();
    }

}