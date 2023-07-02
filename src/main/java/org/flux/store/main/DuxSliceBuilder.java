package org.flux.store.main;

import lombok.Getter;
import org.flux.store.api.Middleware;
import org.flux.store.api.Reducer;
import org.flux.store.api.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public class DuxSliceBuilder<T extends State> {

    private Map<String, Reducer<T>> reducers = new HashMap<>();

    private List<Consumer<T>> subscribers = new ArrayList<>();

    private Middleware<T> middleware;

    private T initialState;

    private Boolean asyncFlag = false;

    public DuxSliceBuilder<T> setInitialState(T initialState) {
        this.initialState = initialState;
        return this;
    }

    public DuxSliceBuilder<T> setMiddleware(Middleware<T> middleware) {
        this.middleware = middleware;
        return this;
    }

    public DuxSliceBuilder<T> addReducer(String type, Reducer<T> reducer) {
        this.reducers.put(type, reducer);
        return this;
    }

    public DuxSliceBuilder<T> addSubscriber(Consumer<T> subscriber) {
        this.subscribers.add(subscriber);
        return this;
    }

    public DuxSliceBuilder<T> enableAsyncNotifications() {
        this.asyncFlag = true;
        return this;
    }

    public DuxSlice<T> build() {
        return DuxSlice.createSlice(this);
    }
}
