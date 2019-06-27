package com.startdt.android.magicfacelib;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;

import com.startdt.android.common.util.Utils;

import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import timber.log.Timber;

/**
 * Created by wjz on 2019/1/2
 */
public class FaceUtils {
    public static final String[] outFileName = new String[]{"det1.bin","det2.bin","det3.bin",
            "det1.param","det2.param","det3.param"};
    public static final Comparator<Rect> COMPARE_BY_SIZE =
            (rect1, rect2) -> {
                int rect1Width=rect1.right-rect1.left;
                int rect1Height=rect1.bottom-rect1.top;
                int rect2Width=rect2.right-rect2.left;
                int rect2Height=rect2.bottom-rect2.top;
                int signum=Long.signum(
                        rect2Width * rect2Height - rect1Width * rect1Height
                );
                return signum;
            };

    public static void copyBigDataToSD() {
        try {
            for(String outFile:outFileName){
                copyMtcnnDataToSD(outFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void copyMtcnnDataToSD(String strOutFileName) throws IOException {
        Timber.d("start copy file %s", strOutFileName);
        String sdPath = Environment.getExternalStorageDirectory() + File.separator +  "mtcnn";
        File outFile = new File(sdPath);
        if (!outFile.exists()) {
            outFile.mkdir();
        }
        String tmpFile = outFile.getAbsolutePath() + File.separator + strOutFileName;
        File f = new File(tmpFile);
        if (f.exists()) {
            Timber.d( "file exists %s", strOutFileName);
            return;
        }
        InputStream myInput;
        FileOutputStream myOutput = new FileOutputStream(tmpFile);
        myInput = Utils.getApplication().getAssets().open("mtcnn/" + strOutFileName);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
        Timber.d("end copy file %s", strOutFileName);
    }

    /**
     * 获取最大的矩阵
     * @param faces
     * @return
     */
    public static Rect getMaxRect(List<Rect> faces){
        if(faces == null || faces.size() == 0){
            return null;
        }
        Collections.sort(faces,COMPARE_BY_SIZE);
        return faces.get(0);
    }

    /**
     * mat中截取指定区域并且转为bitmap
     * @param rgba
     * @param startCol
     * @param endCol
     * @param startRow
     * @param endRow
     * @return
     */
    public static Bitmap matToBitmap(Mat rgba, int startCol, int endCol, int startRow, int endRow){
        Bitmap bitmap = Bitmap.createBitmap(endCol - startCol, endRow - startRow,
                Bitmap.Config.ARGB_8888);

        org.opencv.android.Utils.matToBitmap(rgba.colRange(startCol, endCol)
                .rowRange(startRow, endRow), bitmap);

        return bitmap;
    }



}
