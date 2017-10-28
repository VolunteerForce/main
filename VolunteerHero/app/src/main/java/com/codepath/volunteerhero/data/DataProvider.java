package com.codepath.volunteerhero.data;

import android.util.Log;

import com.codepath.volunteerhero.models.BaseModelWithId;
import com.codepath.volunteerhero.settings.Filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by jan_spidlen on 10/17/17.
 */

public abstract class DataProvider<T extends BaseModelWithId> {

    public interface DataChangedListener<T> {
//        void dataChanged(T data);
        void dataChanged(List<T> data);
//        void dataAdded(T data);
        void dataAdded(List<T> data);
        void dataRemoved(List<T> data);
    }

    protected class State {
        int page = 1;
        boolean loading = false;
        boolean wasError = false;
    }

    private Set<DataChangedListener<T>> listeners = new HashSet<>();
    private Map<String, T> dataStorage = new HashMap<String, T>();
    protected State state = new State();

    public synchronized void addDataChangedListener(DataChangedListener<T> l) {
        listeners.add(l);
    }

    public synchronized void addOrUpdateData(T data) {
        List<T> list = new ArrayList<T>();
        list.add(data);
        addOrUpdateData(list);
    }

    public synchronized void addOrUpdateData(List<T> data) {
        List<T> updatedData = new ArrayList<>();
        List<T> addedData = new ArrayList<>();
        for (T t: data) {
            if (dataStorage.containsKey(t.id)) {
                updatedData.add(t);
            } else {
                addedData.add(t);
            }
            dataStorage.put(t.id, t);
        }
        if (!updatedData.isEmpty()) {
            notifyAllListenersOfChange(updatedData);
        }
        if (!addedData.isEmpty()) {
            notifyAllListenersOfAddition(addedData);
        }
    }

    public synchronized void startDataLoad(Filter filter) {
        Log.d("jenda", "startDataLoad requested");
        if (state.loading) {
            return;
        }
        wipeData();
        state.page = 1;
        loadData(filter);
    }

    protected void onFinish() {
        state.page++;
        state.loading = false;
    }

    public synchronized void loadMoreData(Filter filter) {
        Log.d("jenda", "loadMoreData requested");
        loadData(filter);
    }

    protected synchronized void loadData(Filter filter) {
        if (state.loading) {
            return;
        }
        state.loading = true;
        loadDataInternal(filter);
    }

    protected abstract void loadDataInternal(Filter filter);

    public synchronized void wipeData() {
        List<T> data= new ArrayList<T>(dataStorage.values());
        dataStorage.clear();
        notifyAllListenersOfRemoval(data);
    }

//    private void notifyAllListenersOfChange(T data) {
//        for(DataChangedListener<T> l: listeners) {
//            l.dataChanged(data);
//        }
//    }
//
//    private void notifyAllListenersOfAddition(T data) {
//        for(DataChangedListener<T> l: listeners) {
//            l.dataAdded(data);
//        }
//    }

    private void notifyAllListenersOfChange(List<T> data) {
        for(DataChangedListener<T> l: listeners) {
            l.dataChanged(data);
        }
    }

    private void notifyAllListenersOfAddition(List<T> data) {
        for(DataChangedListener<T> l: listeners) {
            l.dataAdded(data);
        }
    }

    private void notifyAllListenersOfRemoval(List<T> data) {
        for(DataChangedListener<T> l: listeners) {
            l.dataRemoved(data);
        }
    }
}
