package com.codepath.volunteerhero.database;

import android.graphics.Bitmap;
import android.net.Uri;

import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.models.Subscription;
import com.codepath.volunteerhero.models.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejalpar
 */
public class FirebaseDBHelper {
    public static String TAG = FirebaseDBHelper.class.getSimpleName();

    private static final String NODE_ID = "id";
    private static final String USERS_NODE = "users";
    private static final String EVENTS_NODE = "events";
    private static final String CARRIER_NODE = "carrier";
    private static final String SUBSCRIPTION_NODE = "subscription";
    private static final String PROFILE_IMAGE_LOC = "/profile.jpg";

    private static FirebaseDBHelper instance;
    private static FirebaseClient firebaseClient;
    private static StorageReference storageReference;
    private static List<DataChangeEventListener> dataChangeListeners;

//    public Collection<? extends Event> geFireBaseEvents() {
//        // TODO: Currently contains dummy code; Fill up with real code.
//        Event event = new Event();
//
//        event.title = "This is dummy event from Firebase";
//        event.description = "blabla";
//        event.id = "id:" + System.currentTimeMillis();
//        event.eventHeaderImageUrl = "https://images.pexels.com/photos/207962/pexels-photo-207962.jpeg";
//
//        event.topics = new ArrayList<>();
//        event.topics.add("Dummy topic");
//        event.carrier = new Carrier();
//        event.createdAt = event.updatedAt = new Date();
//        event.creator = VolunteerHeroApplication.getLoggedInUser();
//
//        event.contact = new Contact();
//        event.contact.email = VolunteerHeroApplication.getLoggedInUser().email;
//        event.contact.name = VolunteerHeroApplication.getLoggedInUser().name;
//        List<Event> events = new ArrayList<>();
//        events.add(event);
//        return events;
//    }

    public interface DataChangeEventListener {
        void onUserDataUpdated(User user);
        void onEventDataUpdated(Event event);
        void onUserInfoAvailable(User loggedInUser);
        void onUserInfoNotFound(FirebaseUser firebaseUser);
    }

    public interface ImageDownloadListener {
        void onSuccess(byte[] result);
        void onFailure(Exception exception);
    }

    public static FirebaseDBHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDBHelper();
        }
        return instance;
    }

    private FirebaseDBHelper() {
        firebaseClient = FirebaseClient.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public StorageReference getStorageReference() {
        return FirebaseStorage.getInstance().getReference();
    }

    public User saveUser(FirebaseUser user) {
        // add or update user
        User dbUser = new User(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl().getPath());
        dbUser.events = new ArrayList<>();
        firebaseClient.getFirebaseDatabase().child(USERS_NODE).child(user.getUid()).setValue(dbUser);

        return dbUser;
    }

    public void getUser(FirebaseUser user, DataChangeEventListener listener) {
        if (user == null) {
            return;
        }
        registerListener(listener);
        // add or update user
        firebaseClient.getFirebaseDatabase().child(USERS_NODE).orderByChild(NODE_ID).equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    count++;
                    User user = userSnapshot.getValue(User.class);
                    notifyUserInfoAvailableEvent(user);
                    return;
                }
                if (count == 0) {
                    notifyUserInfoNotFound(user);
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Event addEvent(Event event) {
        DatabaseReference dfReference = firebaseClient.getFirebaseDatabase();

        event.id = dfReference.child(EVENTS_NODE).push().getKey();
        event.carrier.id = dfReference.child(EVENTS_NODE).child(CARRIER_NODE).push().getKey();
        dfReference.child(EVENTS_NODE).child(event.id).setValue(event);
        return event;
    }

    public void updateEvent(Event event) {
        firebaseClient.getFirebaseDatabase().child(EVENTS_NODE).child(event.id).setValue(event);
    }

    public void addUsersSubscribedEvent(User user, Event subscribedEvent) {
        DatabaseReference dfReference = firebaseClient.getFirebaseDatabase();

        if (user.events == null) {
            List<Event> events = new ArrayList<>();
            events.add(subscribedEvent);
            user.events = events;
        } else {
            user.events.add(subscribedEvent);
        }
        dfReference.child(USERS_NODE).child(user.id).setValue(user);
    }

    public void userUnSubscribeFromEvent(User user, Event subscribedEvent) {
        DatabaseReference dfReference = firebaseClient.getFirebaseDatabase();

        if (user.events != null) {
            List<Event> updatedList = new ArrayList<>();
            for (Event event: user.events) {
                if (!event.id.equals(subscribedEvent.id)) {
                    updatedList.add(event);
                }
            }
            user.events = updatedList;
        }
        dfReference.child(USERS_NODE).child(user.id).setValue(user);
    }

    public void getUsersSubscribedEvents(User lookupUser, DataChangeEventListener listener) {
        registerListener(listener);
        firebaseClient.getFirebaseDatabase().child(USERS_NODE).orderByChild(NODE_ID).equalTo(lookupUser.id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    notifyUserDataChangeEvent(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getEventList(DataChangeEventListener listener) {
        registerListener(listener);
        firebaseClient.getFirebaseDatabase().child(EVENTS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Event event = userSnapshot.getValue(Event.class);
                    notifyEventDataChangeEvent(event);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUsersList(DataChangeEventListener listener) {
        registerListener(listener);
        firebaseClient.getFirebaseDatabase().child(USERS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    notifyUserDataChangeEvent(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addUsersSubscribedEvents(User user, List<Event> subscribedEvents) {
        DatabaseReference dfReference = firebaseClient.getFirebaseDatabase();

        Subscription subscription = new Subscription();
        subscription.id = dfReference.child(SUBSCRIPTION_NODE).push().getKey();
        subscription.date = System.currentTimeMillis();
        subscription.user = user;
        subscription.events = subscribedEvents;
        dfReference.child(SUBSCRIPTION_NODE).child(subscription.id).setValue(subscription);
    }

    public void updateUsersSubscribedEvents(Subscription subscription) {
        firebaseClient.getFirebaseDatabase().child(SUBSCRIPTION_NODE).child(subscription.id).setValue(subscription);
    }

    public void uploadFile(User user, Bitmap bitmap) {
        if (user != null) {
            StorageReference locationRef = storageReference.child(user.id + PROFILE_IMAGE_LOC);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] data = outputStream.toByteArray();

            UploadTask uploadTask = locationRef.putBytes(data);
            uploadTask.addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
            }).addOnSuccessListener(taskSnapshot -> {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            });
        }
    }

    public void getProfileImage(User user, final ImageDownloadListener listener) {
        FirebaseDBHelper.getInstance().getStorageReference()
                .child(user.id + PROFILE_IMAGE_LOC)
                .getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(listener::onSuccess)
                .addOnFailureListener(listener::onFailure);
    }

    private void registerListener(DataChangeEventListener listener) {
        if (dataChangeListeners == null) {
            dataChangeListeners = new ArrayList<>();
        }
        if(!dataChangeListeners.contains(listener)) {
            dataChangeListeners.add(listener);
        }
    }

    private void notifyUserDataChangeEvent(User user) {
        for (DataChangeEventListener listener: dataChangeListeners) {
            listener.onUserDataUpdated(user);
        }
    }

    private void notifyEventDataChangeEvent(Event event) {
        for (DataChangeEventListener listener: dataChangeListeners) {
            listener.onEventDataUpdated(event);
        }
    }

    private void notifyUserInfoAvailableEvent(User user) {
        for (DataChangeEventListener listener: dataChangeListeners) {
            listener.onUserInfoAvailable(user);
        }
    }

    private void notifyUserInfoNotFound(FirebaseUser firebaseUser) {
        for (DataChangeEventListener listener: dataChangeListeners) {
            listener.onUserInfoNotFound(firebaseUser);
        }
    }
}
