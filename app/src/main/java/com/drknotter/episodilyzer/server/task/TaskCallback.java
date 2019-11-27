package com.drknotter.episodilyzer.server.task;

public interface TaskCallback<T> {
    void onSuccess(T result);
    void onError(String errorMessage);
}
