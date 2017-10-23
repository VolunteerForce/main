package com.codepath.volunteerhero.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.volunteerhero.R;
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
            dismissDialog();
        });
        cancelButton.setOnClickListener(v -> dismissDialog());
        currentFilter = loadSettingsFromSharedPreference(context);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
