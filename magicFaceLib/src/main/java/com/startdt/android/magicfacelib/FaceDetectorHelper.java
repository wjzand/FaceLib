package com.startdt.android.magicfacelib;


import org.opencv.core.Mat;

import timber.log.Timber;

/**
 * @author guohelong on 2018/1/11.
 */

public final class FaceDetectorHelper {
    private boolean isCanIdentify;
    private final FaceDetector faceDetector;
    private long startTime = 0;
    private long INTERVAL = 400;

    public FaceDetectorHelper() {
        isCanIdentify = true;
        faceDetector =new FaceDetector();
    }

    public FaceDetectorHelper(boolean isDealAge) {
        isCanIdentify = true;
        faceDetector =new FaceDetector(isDealAge);
    }

    public FaceDetectorHelper(int leftMargin,int topMargin,int detectWidth,int detctHeight) {
        isCanIdentify = true;
        faceDetector =new FaceDetector(leftMargin,topMargin,detectWidth,detctHeight,true);
    }

    public FaceDetectorHelper(int leftMargin,int topMargin,int detectWidth,int detctHeight,boolean isDealAge) {
        isCanIdentify = true;
        faceDetector =new FaceDetector(leftMargin,topMargin,detectWidth,detctHeight,isDealAge);
    }


    public boolean isDealAge(){
        return faceDetector.isDealAge();
    }

    public void setIntervalTime(long intervalTime) {
        INTERVAL = intervalTime;
    }

    public void setCanIdentify(boolean isCanIdentify) {
        this.isCanIdentify = isCanIdentify;
        faceDetector.setCanIdentify(isCanIdentify);
    }

    public boolean isCanIdentify() {
        return isCanIdentify;
    }

    public void detectFace(Mat gray, Mat rgba) {
        if (System.currentTimeMillis() - startTime > INTERVAL) {
            if (isCanIdentify) {
                faceDetector.detectFace(gray,rgba);
                startTime = System.currentTimeMillis();
            }
        }
    }

    public void setFaceDetectorCallBack(FaceDetectorCallback callback){
        faceDetector.setFaceDetectorCallback(callback);
    }

    public void release(){
        faceDetector.release();
    }
}
