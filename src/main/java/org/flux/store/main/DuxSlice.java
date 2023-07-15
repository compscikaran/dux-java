package org.flux.store.main;

import org.apache.commons.lang3.StringUtils;
import org.flux.store.api.*;
import org.flux.store.utils.Utilities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DuxSlice<T extends State> implements Slice<T> {

    private DuxStore<T> store;
    private List<String> actions;

    private DuxSlice(DuxStore<T> store, List<String> actions) {
        this.store = store;
        this.actions = actions;
    }

    protected static <T extends State> DuxSlice<T> createSlice(T initialState, Map<String, Reducer<T>> reducers, List<Consumer<T>> subscribers, Middleware<T> middleware, Boolean asyncFlag, Boolean autoBackup, String backupPath) {
        Reducer<T> reducer = (action, state) -> {
            for (String key: reducers.keySet()) {
                if(action.getType().equalsIgnoreCase(key)) {
                    Reducer<T> current = reducers.get(key);
                    state = current.reduce(action,state);
                }
            }
            return state;
        };
        DuxStore<T> myStore;
        if(middleware != null) {
            myStore = new DuxStore<>(initialState, reducer, middleware);
        } else {
            myStore = new DuxStore<>(initialState, reducer);
        }
        for (Consumer<T> subscriber: subscribers) {
            myStore.subscribe(subscriber);
        }
        if(asyncFlag) {
            myStore.enableAsyncNotifications();
        }
        if(StringUtils.isNoneEmpty(backupPath)) {
            myStore.setBackupPath(backupPath);
        }
        if(autoBackup) {
            myStore.enableAutoBackup();
        }
        DuxSlice<T> slice = new DuxSlice<>(myStore, new ArrayList<>(reducers.keySet()));
        return slice;
    }

    protected static <T extends State> DuxSlice<T> createSlice(DuxSliceBuilder<T> builder) {
        return DuxSlice.createSlice(
                builder.getInitialState(),
                builder.getReducers(),
                builder.getSubscribers(),
                builder.getMiddleware(),
                builder.getAsyncFlag(),
                builder.getAutoBackup(),
                builder.getBackupPath());
    }

    public Consumer getAction(String type) throws InvalidActionException {
        if(!actions.contains(type))
            throw new InvalidActionException("Action type does not exist on slice");
        return payload -> store.dispatch(Utilities.actionCreator(type, payload));
    }

    @Override
    public void restore(StoreBackup<T> backup) {
        this.store.restore(backup);
    }

    @Override
    public StoreBackup<T> backup() {
        return this.store.backup();
    }

    @Override
    public void goBack() {
        this.store.goBack();
    }

    @Override
    public void goForward() {
        this.store.goForward();
    }

    public T getState() {
        return store.getState();
    }
}
