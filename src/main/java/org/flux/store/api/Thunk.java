package org.flux.store.api;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface Thunk<T extends State> {

    void process(Consumer<Action> dispatch, Supplier<T> getState);
}
