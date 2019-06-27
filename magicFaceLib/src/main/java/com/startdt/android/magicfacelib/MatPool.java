package com.startdt.android.magicfacelib;

import android.annotation.SuppressLint;
import android.util.Log;

import org.opencv.core.Mat;

import java.util.concurrent.atomic.AtomicInteger;

import timber.log.Timber;

/**
 * @author guohelong on 2018/1/15.
 */

public final class MatPool {
    private static final Object LOCK = new Object();
    private static final int MAX_POOL_SIZE = 4;
    private static final AtomicInteger COUNT = new AtomicInteger();

    private static MatPool pool;
    private static int poolSize;

    private final Mat mat;
    private MatPool next;

    @SuppressLint("LogNotTimber")
    private MatPool() {
        mat = new Mat();
        Timber.d("Mat count: " + COUNT.incrementAndGet());
    }

    public static MatPool obtain() {
        synchronized (LOCK) {
            if (pool != null) {
                MatPool mp = pool;
                pool = mp.next;
                mp.next = null;
                poolSize--;
                return mp;
            }
        }
        return new MatPool();
    }

    public static MatPool obtain(Mat mat) {
        MatPool mp = obtain();
        mat.copyTo(mp.mat);
        return mp;
    }

    public static void release() {
        synchronized (LOCK) {
            while (pool != null) {
                MatPool mp = pool;
                mp.mat.release();
                pool = mp.next;
                poolSize--;
            }
        }
    }

    public Mat getMat() {
        return mat;
    }

    public void recycle() {
        synchronized (LOCK) {
            if (poolSize < MAX_POOL_SIZE) {
                next = pool;
                pool = this;
                poolSize++;
            } else {
                mat.release();
            }
        }
    }
}
