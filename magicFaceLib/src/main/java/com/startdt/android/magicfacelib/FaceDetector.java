package com.startdt.android.magicfacelib;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;


import com.startdt.android.common.util.HandlerUtils;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Frank Kwok on 2018/3/13.
 */

public final class FaceDetector {
    // 核心线程数0
    private static final int CORE_POOL_SIZE = 2;
    // 最大线程数保持系统满载
    private static final int MAX_POOL_SIZE = 10;
    // 线程空闲存活时常
    private static final int KEEP_ALIVE_SECONDS = 30;
    // 线程工厂
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(r, "FaceDetectorPool #" + count.getAndIncrement());
            t.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
            t.setDaemon(true);
            return t;
        }
    };
    // 检测人脸区域
    private int DETECT_WIDTH = 1000;
    private int DETECT_HEIGHT = 1000;
    // 人脸区域RESIZE大小
    public static Size RESIZE = new Size(160, 160);

    private ThreadLocal<Detector> faceDetectorLocal;
    private ThreadPoolExecutor executor;
    private boolean isCanIdentify = true;
    private FaceDetectorCallback faceDetectorCallback;
    private int cropLeft =0, cropTop = 0;
    private boolean isDealAge = true;

    /**
     * 预览分辨率
     */
    public static final int PREVIEW_WIDTH = 1920;
    public static final int PREVIEW_HEIGHT = 1080;

    FaceDetector() {
        isDealAge = true;
        init();
    }

    FaceDetector(boolean isDealAge) {
        this.isDealAge  = isDealAge;
        init();
    }


    FaceDetector(int leftMargin, int topMargin, int detectWidth, int detctHeight, boolean isDealAge) {
        DETECT_WIDTH = detectWidth;
        DETECT_HEIGHT = detctHeight;
        cropLeft = leftMargin;
        cropTop = topMargin;
        this.isDealAge = isDealAge;
        RESIZE = new Size(160 * DETECT_WIDTH/DETECT_HEIGHT,160);
        init();
    }

    private void init(){
        FaceUtils.copyBigDataToSD();
        //获取跟目录
        final String sdPath = Environment.getExternalStorageDirectory() + File.separator +  "mtcnn";
        faceDetectorLocal = new ThreadLocal<Detector>() {
            @Override
            protected Detector initialValue() {
                return new MtcnnDetector((int) RESIZE.width, (int) RESIZE.height, 30, sdPath);
            }
        };
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, new SynchronousQueue<>(), THREAD_FACTORY,
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    void setCanIdentify(boolean canIdentify) {
        isCanIdentify = canIdentify;
    }


    boolean isDealAge(){
        return isDealAge;
    }


    @UiThread
    void setFaceDetectorCallback(FaceDetectorCallback faceDetectorCallback) {
        this.faceDetectorCallback = faceDetectorCallback;
    }

    void detectFace(Mat gray, Mat rgba) {
        if (executor != null && isCanIdentify) {
            executor.execute(new DetectorTask(gray, rgba));
        }
    }

    public void release() {
        faceDetectorLocal.remove();
    }

    private float[] picInput;
    private class DetectorTask implements Runnable {
        private final FramePool framePool;

        private DetectorTask(Mat gray, Mat rgba) {
            framePool = FramePool.obtain(gray, rgba);
        }

        @Override
        public void run() {
            Mat rgba = framePool.getRgba();
            int width = rgba.width();
            int height = rgba.height();
            if(width - DETECT_WIDTH < 0 || height - DETECT_HEIGHT < 0){
                throw new RuntimeException("width < 0 or height < 0");
            }
            Detector detector =  faceDetectorLocal.get();
            double resize = DETECT_WIDTH / RESIZE.width;
            // 裁剪
            Mat crop = rgba.colRange(cropLeft, cropLeft + DETECT_WIDTH).rowRange(cropTop, cropTop + DETECT_HEIGHT);
            // 压缩
            Imgproc.resize(crop, crop, RESIZE);
            // 识别
            List<Rect> rects = detector.detect(crop);
            //释放
            crop.release();
            // 坐标变换
            List<Rect> faces = new ArrayList<>(rects.size());
            for (int i = 0; i < rects.size(); i++) {
                Rect rect = rects.get(i);
                int centerX = (int)(rect.centerX() * resize) + cropLeft;
                int centerY = (int)(rect.centerY() * resize) + cropTop;
                int halfEdge = (int) Math.ceil(Math.max(rect.width(), rect.height()) * resize/ 2);
                faces.add(new Rect(centerX - halfEdge, centerY - halfEdge,
                        centerX+halfEdge, centerY+halfEdge));
            }
            MatPool rgbaPool = MatPool.obtain(framePool.getRgba());
            //获取人脸最大矩阵，找到最大人脸
            Rect maxRect = FaceUtils.getMaxRect(faces);
            //处理年龄
            if(isDealAge){
                //获取最大人脸的年龄数据
                picInput = getInputArray2Model(rgbaPool,maxRect);
            }
            //回调
            Rect finalMaxRect = maxRect;
            HandlerUtils.getMain().post(() -> {
                if(faceDetectorCallback != null) faceDetectorCallback.onDetectFace(rgbaPool,faces, finalMaxRect,
                        picInput, DETECT_WIDTH/2 + cropLeft);
            });
            framePool.recycle();
        }
    }

    /**
     * 获取模型输入数据
     * @param matPool
     * @param position
     * @return
     */
    private float[] getInputArray2Model(MatPool matPool, Rect position){
        Bitmap bitmap = null;
        if(position != null){
            int width = position.width();
            int height = position.height();
            Mat rgba = matPool.getMat();
            //这里是去对比的图片，区域面积padding 30%
            int startCol = (int) Math.max(position.left - width * 0.3,0);
            int endCol = (int) Math.min(position.right + width*0.3,rgba.width());
            int startRow = (int) Math.max(position.top - height*0.3,0);
            int endRow = (int) Math.min(position.bottom + height*0.3,rgba.height());
            Mat mat = rgba.colRange(startCol,endCol).rowRange(startRow,endRow);
            //压缩
            Imgproc.resize(mat,mat,new Size(64,64));
            bitmap = FaceUtils.matToBitmap(mat,0,mat.width(),0,mat.height());
            mat.release();
        }
        if(bitmap != null){
            return bitmapToRGB(bitmap);
        }
        return null;
    }

    /**
     * 模型需要的是rgb，这里把bmp转为rgb
     * @param bitmap
     * @return
     */
    private float[] bitmapToRGB(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] intArray = new int[width*height];
        float[] floatArray = new float[width*height*3];
        bitmap.getPixels(intArray,0,width,0,0,width,height);
        for (int i = 0; i < intArray.length; i++) {
            int pixel = intArray[i];
            floatArray[i*3] = Color.red(pixel);
            floatArray[i*3+1] = Color.green(pixel);
            floatArray[i*3+2] = Color.blue(pixel);
        }
        bitmap.recycle();
        return floatArray;
    }
}
