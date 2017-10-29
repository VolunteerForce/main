package com.codepath.volunteerhero.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.activities.OpportunitiesListActivity;
import com.codepath.volunteerhero.settings.Filter;
import com.codepath.volunteerhero.settings.FilterSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * To set search filter settings
 *
 * @author tejalpar
 */
public class SettingsDialogFragment extends DialogFragment {

    public SettingsDialogFragment() {}

    public interface SettingsUpdateListener {
        void onSettingsUpdated();
    }

    @BindView(R.id.query_input_text)
    EditText searchQueryText;

    @BindView(R.id.loc_query_text)
    EditText locationQueryText;

    @BindView(R.id.save_button)
    Button saveButton;

    @BindView(R.id.cancel_button)
    Button cancelButton;

    private Unbinder unbinder;
    private Filter currentFilter;

    public static SettingsDialogFragment newInstance(String dialogTitle) {
        SettingsDialogFragment frag = new SettingsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", dialogTitle);
        frag.setArguments(args);
        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_dialog_layout, container);
        final Context context = view.getContext();

        unbinder = ButterKnife.bind(this, view);

        saveButton.setOnClickListener(v -> {
            // save to preference
            FilterSettings.getInstance(context).saveFilter(getSelectedFilterSettings());
            OpportunitiesListActivity activity = (OpportunitiesListActivity) getActivity();
            activity.onSettingsUpdated();
            dismissDialog();
        });
        cancelButton.setOnClickListener(v -> dismissDialog());
        currentFilter = loadSettingsFromSharedPreference(context);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    private Filter getSelectedFilterSettings() {
        Filter filter = new Filter();
        filter.setSearchQuery(searchQueryText.getText().toString());
        filter.setLocationQuery(locationQueryText.getText().toString());
        return filter;
    }

    private Filter loadSettingsFromSharedPreference(Context context) {
        Filter filter = FilterSettings.getInstance(context).retrieveFilter();

        if (filter.isFilterSet()) {
            searchQueryText.setText(filter.getSearchQuery());
            locationQueryText.setText(filter.getLocationQuery());
            return filter;
        }
        return null;
    }

    private void dismissDialog() {
        dismiss();
    }
}
