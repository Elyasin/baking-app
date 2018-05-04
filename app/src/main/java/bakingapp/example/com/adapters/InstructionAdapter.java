package bakingapp.example.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bakingapp.example.com.InstructionDetailActivity;
import bakingapp.example.com.InstructionDetailFragment;
import bakingapp.example.com.InstructionListActivity;
import bakingapp.example.com.R;
import bakingapp.example.com.model.Step;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {

    private static final String TAG = InstructionAdapter.class.getSimpleName();

    private final InstructionListActivity mParentActivity;

    private final List<Step> mInstructions;

    private final boolean mTwoPane;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Step instruction = (Step) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(InstructionDetailFragment.INSTRUCTION_INTENT_KEY, String.valueOf(instruction.getId()));
                InstructionDetailFragment fragment = new InstructionDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.instruction_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, InstructionDetailActivity.class);
                intent.putExtra(InstructionDetailFragment.INSTRUCTION_INTENT_KEY, instruction);
                context.startActivity(intent);
            }
        }
    };

    public InstructionAdapter(InstructionListActivity parent,
                              @NonNull List<Step> instructions,
                              boolean twoPane) {
        mInstructions = instructions;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.instruction_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mIdView.setText(String.valueOf(mInstructions.get(position).getId()));
        holder.mContentView.setText(mInstructions.get(position).getDescription());

        holder.itemView.setTag(mInstructions.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mInstructions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.id_text);
            mContentView = view.findViewById(R.id.content);
        }

    }
}
