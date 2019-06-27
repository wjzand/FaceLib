package com.startdt.android.magicfacelib;

import android.graphics.Rect;
import android.support.annotation.NonNull;

import org.opencv.core.Mat;

import java.util.List;

/**
 * @author fhp
 * @date 2018/6/22
 */
public interface Detector {
    List<Rect> detect(@NonNull Mat mat);
    void release();
}
