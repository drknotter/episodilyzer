package com.drknotter.episodilyzer.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.drknotter.episodilyzer.model.Series;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by plunkett on 1/24/16.
 */
public class SeriesUtils {
    private static HandlerThread handlerThread;
    private static Handler handler;

    public interface OnSeriesChangeListener {
        void onSeriesInserted(List<Series> insertedSeries);
        void onSeriesDeleted(List<Series> deletedSeries);
        void onSeriesUpdated(List<Series> updatedSeries);
    }

    private static List<WeakReference<OnSeriesChangeListener>> listenerRefs = new ArrayList<>();

    private SeriesUtils() {
    }

    public static synchronized void registerOnSeriesChangeListener(OnSeriesChangeListener listener) {
        listenerRefs.add(new WeakReference<>(listener));
    }

    public static synchronized void unregisterOnSeriesChangeListener(OnSeriesChangeListener listenerToRemove) {
        Iterator<WeakReference<OnSeriesChangeListener>> i = listenerRefs.iterator();
        while (i.hasNext()) {
            WeakReference<OnSeriesChangeListener> listenerRef = i.next();
            OnSeriesChangeListener listener = listenerRef.get();
            if (listener == null || listener == listenerToRemove) {
                i.remove();
            }
        }
    }

    public static synchronized void saveSeries(int seriesId) {
        startThreadIfNeeded();
        Message msg = handler.obtainMessage(SeriesUtilsHandler.WHAT_SAVE, seriesId);
        handler.sendMessage(msg);
    }

    public static synchronized void deleteSeries(int seriesId) {
        startThreadIfNeeded();
        Message msg = handler.obtainMessage(SeriesUtilsHandler.WHAT_DELETE, seriesId);
        handler.sendMessage(msg);
    }

    static synchronized void notifyDeleted(List<Series> deletedSeries) {
        Iterator<WeakReference<OnSeriesChangeListener>> i = listenerRefs.iterator();
        while (i.hasNext()) {
            OnSeriesChangeListener listener = i.next().get();
            if (listener != null) {
                listener.onSeriesDeleted(deletedSeries);
            } else {
                i.remove();
            }
        }
    }

    static synchronized void notifyUpdated(List<Series> updatedSeries) {
        Iterator<WeakReference<OnSeriesChangeListener>> i = listenerRefs.iterator();
        while (i.hasNext()) {
            OnSeriesChangeListener listener = i.next().get();
            if (listener != null) {
                listener.onSeriesUpdated(updatedSeries);
            } else {
                i.remove();
            }
        }
    }

    static synchronized void notifyInserted(List<Series> insertedSeries) {
        Iterator<WeakReference<OnSeriesChangeListener>> i = listenerRefs.iterator();
        while (i.hasNext()) {
            OnSeriesChangeListener listener = i.next().get();
            if (listener != null) {
                listener.onSeriesInserted(insertedSeries);
            } else {
                i.remove();
            }
        }
    }

    private static void startThreadIfNeeded() {
        if (handlerThread == null) {
            handlerThread = new HandlerThread("SeriesUtils");
            handlerThread.start();
            handler = new SeriesUtilsHandler(handlerThread.getLooper());
        }
    }
}
