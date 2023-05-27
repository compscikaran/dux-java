package org.flux.store.main;

import org.flux.store.api.AsyncNotificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public class AsyncProcessor {

    private static Logger log = LoggerFactory.getLogger(AsyncProcessor.class);

    public static <T> void submitNotify(Consumer<T> fn, T state) {
        CompletableFuture
                .runAsync(() -> fn.accept(state))
                .thenRun(() -> log.info("Notified subscriber successfully " + fn))
                .exceptionally(throwable -> {
                    log.error("Could not notify subscriber", new AsyncNotificationException(throwable));
                    return null;
                });
    }

}
