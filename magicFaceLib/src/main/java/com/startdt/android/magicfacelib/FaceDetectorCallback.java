package com.startdt.android.magicfacelib;

import android.graphics.Rect;
import android.support.annotation.UiThread;

import java.util.List;

/**
 * Created by wjz on 2019/1/3
 */
public interface FaceDetectorCallback {
    /**
     * @param matPool  人脸的mat矩阵
     * @param rectList   人脸的坐标集合
     * @param maxRect  最大的人脸
     * @param picInput  年龄
     * @param baseCenterLine  识别的中间线
     */
    @UiThread
    void onDetectFace(MatPool matPool, List<Rect> rectList,Rect maxRect, float[] picInput, int baseCenterLine);
}
