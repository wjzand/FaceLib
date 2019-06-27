package com.startdt.android.magicfacelib;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.startdt.android.face.MTCNN;

import org.opencv.core.Mat;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


import timber.log.Timber;

import static org.opencv.android.Utils.matToBitmap;

/**
 * @author Frank Kwok on 2018/4/25.
 */
public class MtcnnDetector implements Detector {
    private MTCNN mtcnn;
    private Bitmap bitmap;
    private ByteBuffer buffer;
    private long time = 0;



    public MtcnnDetector(int width, int height, int faceSize, String modelPath) {
        mtcnn = new MTCNN(width, height, modelPath);
        mtcnn.setMinFaceSize(faceSize);
    }


    @NonNull
    @Override
    public List<Rect> detect(@NonNull Mat mat) {
        int width = mat.width();
        int height = mat.height();
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } else if (width != bitmap.getWidth() || height != bitmap.getHeight()) {
            throw new IllegalStateException("Mat size is " + width + "*" + height +
                    ", Bitmap size is " + bitmap.getWidth() + "*" + bitmap.getHeight());
        }
        if (buffer == null) {
            buffer = ByteBuffer.allocate(bitmap.getByteCount());
        }

        try {
            matToBitmap(mat, bitmap);
            bitmap.copyPixelsToBuffer(buffer);
            byte[] pixels = buffer.array();
            buffer.clear();
            float[] faceInfo = mtcnn.detectFace(pixels,  4);
            List<Rect> rects = new ArrayList<>();

          /*  if(time == 0){
                time = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() - time >= 2000){
                String base64 = bitmapToBase64(bitmap,50);
                Timber.e("base64:" + base64);
            }*/
            if (faceInfo.length > 0) {
                int faceNum = (int) faceInfo[0];
                for (int i = 0; i < faceNum; i++) {
                    int left, top, right, bottom;
                    left = (int) faceInfo[2 + 18 * i];
                    top = (int) faceInfo[3 + 18 * i];
                    right = (int) faceInfo[4 + 18 * i];
                    bottom = (int) faceInfo[5 + 18 * i];
                    rects.add(new Rect(left, top, right, bottom));
                }
            }
            return rects;
        } catch (Exception e) {
            Timber.e(e);
            return new ArrayList<>();
        }
    }

    @Override
    public void release() {
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
        }
        mtcnn.release();
    }
}
