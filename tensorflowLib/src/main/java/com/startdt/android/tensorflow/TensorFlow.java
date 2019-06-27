package com.startdt.android.tensorflow;

import android.content.res.AssetManager;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by wjz on 2019/1/3
 */
public class TensorFlow {
    private static volatile TensorFlow instance;
    private int IN_COL = 64;
    private int IN_ROW = 64;
    private int OUT_COL = 1;
    private int OUT_ROW = 1;
    private int dim = 3;
    private String intputName = "input_1";
    private String outputName = "output_node0";
    private TensorFlowInferenceInterface tensorFlowInferenceInterface;
    private String[] outputNames;
    private float[] outputs;

    public static TensorFlow getInstance() {
        if(instance == null){
            synchronized (TensorFlow.class){
                if(instance == null){
                    instance = new TensorFlow();
                }
            }
        }
        return instance;
    }

    public void init(String nameInput,String nameOut,int...dim){
        this.intputName = nameInput;
        this.outputName = nameOut;
        this.IN_COL = dim[0];
        this.IN_ROW = dim[1];
        this.OUT_COL = dim[2];
        this.OUT_ROW = dim[3];
        this.dim = dim[4];
        outputNames = new String[]{outputName};
        outputs = new float[]{OUT_COL*OUT_ROW};
    }


    public void init(String nameInput,String nameOut,int in_col,int in_row,
                     int out_col,int out_row,int dim){
        this.intputName = nameInput;
        this.outputName = nameOut;
        this.IN_COL = in_col;
        this.IN_ROW = in_row;
        this.OUT_COL = out_col;
        this.OUT_ROW = out_row;
        this.dim = dim;
        outputNames = new String[]{outputName};
        outputs = new float[]{OUT_COL*OUT_ROW};
    }

    public void create(File file){
        try {
            tensorFlowInferenceInterface = new TensorFlowInferenceInterface(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void create(AssetManager assetManager, String model){
        tensorFlowInferenceInterface = new TensorFlowInferenceInterface(assetManager,model);
    }

    public float[] getResult(float[] input){
        if(input == null || tensorFlowInferenceInterface == null)
            return null;
        //将数据feed给tensorflow的输入节点
        tensorFlowInferenceInterface.feed(intputName, input, 1,IN_COL,IN_ROW,dim);
        //运行tensorflow
        tensorFlowInferenceInterface.run(outputNames);
        ///获取输出节点的输出信息
        tensorFlowInferenceInterface.fetch(outputName, outputs);
        return outputs;
    }
}
