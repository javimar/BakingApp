package eu.javimar.bakingapp.view;

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

@SuppressWarnings("all")
public class StepListAdapter extends
        RecyclerView.Adapter<StepListAdapter.StepListHolder>
{
    private static String LOG_TAG = StepListAdapter.class.getName();

    // The interface that receives onClick messages
    public interface ListStepClickListener
    {
        void onStepItemClick(Step step);
    }

    private final ListStepClickListener mOnClickListener;

    private Recipe mRecipe;


    /**
     * Adapter constructor will receive an object that implements this interface
     */
    public StepListAdapter(ListStepClickListener listener, Recipe recipe)
    {
        mOnClickListener = listener;
        mRecipe = recipe;
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     */
    @Override
    public StepListAdapter.StepListHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_step_item, parent, false);
        return new StepListHolder(view);
    }


    @Override
    public void onBindViewHolder(StepListHolder holder, int position)
    {
        holder.bind(mRecipe.getmStepsList().get(position), mOnClickListener);
    }

    public class StepListHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_step_number)TextView tvStepNumber;
        @BindView(R.id.tv_step_description)TextView tvStepDescription;

        public StepListHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Step step, final ListStepClickListener listener)
        {
            tvStepNumber.setText(String.valueOf(step.getmStepId()));
            tvStepDescription.setText(step.getmShortDescription());
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    listener.onStepItemClick(step);
                }
            });
        }
    }


    @Override
    public int getItemCount()
    {
        return mRecipe.getmStepsList().size();
    }

}
