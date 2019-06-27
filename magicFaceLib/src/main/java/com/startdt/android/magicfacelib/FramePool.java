package com.startdt.android.magicfacelib;

import org.opencv.core.Mat;

import java.util.concurrent.atomic.AtomicInteger;

import timber.log.Timber;

/**
 * @author guohelong on 2018/1/15.
 */

public final class FramePool {
    private static final Object LOCK = new Object();
    private static final int MAX_POOL_SIZE = 2;
    private static final AtomicInteger COUNT = new AtomicInteger();

    private static FramePool pool;
    private static int poolSize;

    private final Mat gray;
    private final Mat rgba;
    private FramePool next;

    private FramePool() {
        gray = new Mat();
        rgba = new Mat();
        Timber.d("Frame count: %s", COUNT.incrementAndGet());
    }

    public static FramePool obtain() {
        synchronized (LOCK) {
            if (pool != null) {
                FramePool mh = pool;
                pool = mh.next;
                mh.next = null;
                poolSize--;
                return mh;
            }
        }
        return new FramePool();
    }

    public static FramePool obtain(Mat gray, Mat rgba) {
        FramePool mh = obtain();
        gray.copyTo(mh.gray);
        rgba.copyTo(mh.rgba);
        return mh;
    }

    public static void release() {
        synchronized (LOCK) {
            while (pool != null) {
                FramePool fp = pool;
                fp.gray.release();
                fp.rgba.release();
                pool = fp.next;
                poolSize--;
            }
        }
    }

    public Mat getGray() {
        return gray;
    }

    public Mat getRgba() {
        return rgba;
    }

    public void recycle() {
        synchronized (LOCK) {
            if (poolSize < MAX_POOL_SIZE) {
                next = pool;
                pool = this;
                poolSize++;
            } else {
                gray.release();
                rgba.release();
            }
        }
    }
}
