package bakingapp.example.com;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bakingapp.example.com.model.Step;

/**
 * A fragment representing a single Instruction detail screen.
 * This fragment is either contained in a {@link InstructionListActivity}
 * in two-pane mode (on tablets) or a {@link InstructionDetailActivity}
 * on handsets.
 */
public class InstructionDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String INSTRUCTION_INTENT_KEY = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Step mInstruction;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InstructionDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(INSTRUCTION_INTENT_KEY)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mInstruction = getArguments().getParcelable(INSTRUCTION_INTENT_KEY);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mInstruction.getShortDescription());
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.instruction_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mInstruction != null) {
            ((TextView) rootView.findViewById(R.id.instruction_detail)).setText(mInstruction.getDescription());
        }

        return rootView;
    }
}
