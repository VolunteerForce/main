package com.codepath.volunteerhero.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.VolunteerHeroApplication;
import com.codepath.volunteerhero.data.EventDataProvider;
import com.codepath.volunteerhero.database.FirebaseDBHelper;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.models.TranslateResult;
import com.codepath.volunteerhero.models.User;
import com.codepath.volunteerhero.utils.VolunteerHeroConstants;
import com.google.gson.Gson;

import org.parceler.Parcels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dharinic on 10/17/17.
 */

public class EventDetailFragment extends Fragment {

    public static final String TAG = EventDetailFragment.class.getSimpleName();

    private Event mEvent;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvOrg)
    TextView tvOrg;

    @BindView(R.id.ivImage)
    ImageView ivImage;

    @BindView(R.id.tvLocation)
    TextView tvLocation;

    @BindView(R.id.tvNumVacancies)
    TextView tvNumVacancies;

    @BindView(R.id.tvActivities)
    TextView tvActivities;

    @BindView(R.id.tvContactEmail)
    TextView tvContactEmail;

    @BindView(R.id.tvContactName)
    TextView tvContactName;

    @BindView(R.id.btnSubscribe)
    Button btnSubscribe;

    @BindView(R.id.delete_button)
    Button deleteButton;

    boolean userHasSubscribed;

    public static EventDetailFragment newInstance() {
        EventDetailFragment fragment = new EventDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_event_detail,
                container, false);

        mEvent = (Event)Parcels.unwrap(getActivity().getIntent().getParcelableExtra(VolunteerHeroConstants.EXTRA_EVENT));
        ButterKnife.bind(this, view);
        String textToBeTranslated = "Hello world, yeah I know it is stereotye.";
        String languagePair = "de-en"; //English to French ("<source_language>-<target_language>")
        //Executing the translation function
        translate(mEvent.title, languagePair);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateFragmentWithData();
    }


    @OnClick(R.id.btnSubscribe)
    void subscribe() {
        //TODO: use confirm dialog?
        if (!userHasSubscribed) {
            FirebaseDBHelper helper = FirebaseDBHelper.getInstance();
            helper.addUsersSubscribedEvent(VolunteerHeroApplication.getLoggedInUser(), mEvent);
        } else {
            FirebaseDBHelper helper = FirebaseDBHelper.getInstance();
            helper.userUnSubscribeFromEvent(VolunteerHeroApplication.getLoggedInUser(), mEvent);
        }
        getActivity().finish();
    }

    @OnClick(R.id.delete_button)
    void deleteEvent() {
        // TODO: Confirmation dialog.
        mEvent.isDeleted = true;

        EventDataProvider.getInstance().addOrUpdateData(mEvent);
        FirebaseDBHelper.getInstance().updateEvent(mEvent);
        getActivity().finish();

        // TODO: this needs to run on list activity.
//        Utils.showSnackBar(
//                this.getView(), this.getString(R.string.event_deleted),
//                this.getString(R.string.undelete), v-> {
//
//                });
    }

    @OnClick (R.id.ibShare)
    void shareEvent() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, mEvent.title);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    void populateFragmentWithData() {

        userHasSubscribed = hasUserSubscribedToEvent();
        Log.e(TAG, ".. event = " + mEvent.title );

        btnSubscribe.setText(userHasSubscribed? getString(R.string.unsubscribe) : getString(R.string.subscribe));

        tvTitle.setText(mEvent.title);
        tvLocation.setText(mEvent.getLocation());
        Glide.with(getActivity()).load(mEvent.getImageUrl()).apply(new RequestOptions())
                .into(ivImage);
        tvNumVacancies.setText(String.valueOf(mEvent.vacancies == 0 ? 10: mEvent.vacancies));

        tvOrg.setText(mEvent.getCarrierName());
        tvActivities.setText(mEvent.getActivities());

        if (mEvent.contact != null) {
            tvContactEmail.setText(mEvent.contact.getEmail());
            tvContactName.setText(mEvent.contact.getName());
        }

        boolean isUserCreator = mEvent.creator != null
                && mEvent.creator.id.equals(VolunteerHeroApplication.getLoggedInUser().id);
        deleteButton.setVisibility(isUserCreator ? View.VISIBLE : View.GONE);
     }

    private boolean hasUserSubscribedToEvent() {

        User currentUser = VolunteerHeroApplication.getLoggedInUser();
        List<Event> userEvents = currentUser.events;
        if (userEvents != null && !userEvents.isEmpty()) {
            for (Event e : userEvents) {
                if (e.id.equals(mEvent.id)) {
                    return true;
                }
            }
        }
        return false;
    }

    void translate(String textToBeTranslated,String languagePair){
        TranslatorBackgroundTask translatorBackgroundTask= new TranslatorBackgroundTask();
        translatorBackgroundTask.execute(textToBeTranslated,languagePair);
    }


    public class TranslatorBackgroundTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String textToBeTranslated = params[0];
            String languagePair = params[1];

            String jsonString;

            try {
                //Set up the translation call URL
                String yandexKey = "trnsl.1.1.20171030T181605Z.0bd0d40edf2f20b9.bbf9b45e9cc270ed0c7e8dfde8fd4b0e12e39b93";
                String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + yandexKey
                        + "&text=" + textToBeTranslated + "&lang=" + languagePair;
                URL yandexTranslateURL = new URL(yandexUrl);

                //Set Http Conncection, Input Stream, and Buffered Reader
                HttpURLConnection httpJsonConnection = (HttpURLConnection) yandexTranslateURL.openConnection();
                InputStream inputStream = httpJsonConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                //Set string builder and insert retrieved JSON result into it
                StringBuilder jsonStringBuilder = new StringBuilder();
                while ((jsonString = bufferedReader.readLine()) != null) {
                    jsonStringBuilder.append(jsonString + "\n");
                }

                //Close and disconnect
                bufferedReader.close();
                inputStream.close();
                httpJsonConnection.disconnect();

                //Making result human readable
                String resultString = jsonStringBuilder.toString().trim();
                //Getting the characters between [ and ]
                resultString = resultString.substring(resultString.indexOf('[')+1);
                resultString = resultString.substring(0,resultString.indexOf("]"));
                //Getting the characters between " and "
                resultString = resultString.substring(resultString.indexOf("\"")+1);
                resultString = resultString.substring(0,resultString.indexOf("\""));

                Log.d("Translation Result:", resultString);
                return jsonStringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            int SUCCESS = 200;
            if (result != null) {
                // if result is not null, somehow pass back to app to show
                Gson g = new Gson();
                TranslateResult res = g.fromJson(result, TranslateResult.class);
                if (res.code == SUCCESS) {
                    if (res != null && res.text != null && !res.text.get(0).isEmpty()) {
                        tvTitle.setText(res.text.get(0));
                    }
                }
            }
            Log.d("DC", "translated text - " + result);
      }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }


}
