package com.codepath.volunteerhero.storage;

import android.content.Context;

import com.codepath.volunteerhero.models.Event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan_spidlen on 10/15/17.
 */

public class LocalStorage {
    String FILENAME = "data.dat";
    String string = "hello world!";

    private final Context context;

    public LocalStorage(Context context) {
        this.context = context;
    }

    public List<Event> readAllStoredEvents() {

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            List<Event> events = (ArrayList<Event>) is.readObject();
            return events;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void saveEvent(Event e) {
        List<Event>  events = readAllStoredEvents();
        events.add(0, e);
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(events);
            os.close();
            fos.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
