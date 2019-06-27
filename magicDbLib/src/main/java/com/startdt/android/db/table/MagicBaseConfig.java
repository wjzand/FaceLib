package com.startdt.android.db.table;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by wjz on 2019/2/22
 */
@Entity
public class MagicBaseConfig {
    @Id(autoincrement = true)
    private Long id;

    private int activityType;
    /**
     * 补光灯
     */
    private boolean isOpenFillLight = false;
    /**
     * 暖光值
     */
    private String warmValue = "32";
    /**
     * 冷光值
     */
    private String codeValue = "16";
    /**
     * 摄像头镜像
     */
    private boolean isOPenCameraMirror = false;
    /**
     * 摄像头旋转角度
     */
    private int cameraRotateValue = -1;
    /**
     * 摄像头曝光值
     */
    private int exposure = 0;

    /**
     * 语音
     */
    private boolean isOPenVoice = true;
    /**
     * 音量
     */
    private int voiceValue;

    /**
     * 音效
     */
    private boolean isOPenSoundType = true;
    /**
     * 音效值
     */
    private int soundTypeValue;

    /**
     * 最小人脸高度 max = 1920 default = 50%
     */
    private int minFaceHeight = 960;
    /**
     * 最小人脸 max = 800 default = 5%
     */
    private int minFace = 40;
    /**
     * 最大人脸 max = 800 default = 100%
     */
    private int maxFace = 800;
    /**
     * 人脸左边距 max = 540 default = 65%
     */
    private int faceLeftMargin = 351;
    /**
     * 人脸右边距 max = 351 default = 65%
     */
    private int faceRightMargin = 351;
    @Generated(hash = 37193291)
    public MagicBaseConfig(Long id, int activityType, boolean isOpenFillLight,
            String warmValue, String codeValue, boolean isOPenCameraMirror,
            int cameraRotateValue, int exposure, boolean isOPenVoice,
            int voiceValue, boolean isOPenSoundType, int soundTypeValue,
            int minFaceHeight, int minFace, int maxFace, int faceLeftMargin,
            int faceRightMargin) {
        this.id = id;
        this.activityType = activityType;
        this.isOpenFillLight = isOpenFillLight;
        this.warmValue = warmValue;
        this.codeValue = codeValue;
        this.isOPenCameraMirror = isOPenCameraMirror;
        this.cameraRotateValue = cameraRotateValue;
        this.exposure = exposure;
        this.isOPenVoice = isOPenVoice;
        this.voiceValue = voiceValue;
        this.isOPenSoundType = isOPenSoundType;
        this.soundTypeValue = soundTypeValue;
        this.minFaceHeight = minFaceHeight;
        this.minFace = minFace;
        this.maxFace = maxFace;
        this.faceLeftMargin = faceLeftMargin;
        this.faceRightMargin = faceRightMargin;
    }
    @Generated(hash = 1767064031)
    public MagicBaseConfig() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getActivityType() {
        return this.activityType;
    }
    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }
    public boolean getIsOpenFillLight() {
        return this.isOpenFillLight;
    }
    public void setIsOpenFillLight(boolean isOpenFillLight) {
        this.isOpenFillLight = isOpenFillLight;
    }
    public String getWarmValue() {
        return this.warmValue;
    }
    public void setWarmValue(String warmValue) {
        this.warmValue = warmValue;
    }
    public String getCodeValue() {
        return this.codeValue;
    }
    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
    public boolean getIsOPenCameraMirror() {
        return this.isOPenCameraMirror;
    }
    public void setIsOPenCameraMirror(boolean isOPenCameraMirror) {
        this.isOPenCameraMirror = isOPenCameraMirror;
    }
    public int getCameraRotateValue() {
        return this.cameraRotateValue;
    }
    public void setCameraRotateValue(int cameraRotateValue) {
        this.cameraRotateValue = cameraRotateValue;
    }
    public int getExposure() {
        return this.exposure;
    }
    public void setExposure(int exposure) {
        this.exposure = exposure;
    }
    public boolean getIsOPenVoice() {
        return this.isOPenVoice;
    }
    public void setIsOPenVoice(boolean isOPenVoice) {
        this.isOPenVoice = isOPenVoice;
    }
    public int getVoiceValue() {
        return this.voiceValue;
    }
    public void setVoiceValue(int voiceValue) {
        this.voiceValue = voiceValue;
    }
    public boolean getIsOPenSoundType() {
        return this.isOPenSoundType;
    }
    public void setIsOPenSoundType(boolean isOPenSoundType) {
        this.isOPenSoundType = isOPenSoundType;
    }
    public int getSoundTypeValue() {
        return this.soundTypeValue;
    }
    public void setSoundTypeValue(int soundTypeValue) {
        this.soundTypeValue = soundTypeValue;
    }
    public int getMinFaceHeight() {
        return this.minFaceHeight;
    }
    public void setMinFaceHeight(int minFaceHeight) {
        this.minFaceHeight = minFaceHeight;
    }
    public int getMinFace() {
        return this.minFace;
    }
    public void setMinFace(int minFace) {
        this.minFace = minFace;
    }
    public int getMaxFace() {
        return this.maxFace;
    }
    public void setMaxFace(int maxFace) {
        this.maxFace = maxFace;
    }
    public int getFaceLeftMargin() {
        return this.faceLeftMargin;
    }
    public void setFaceLeftMargin(int faceLeftMargin) {
        this.faceLeftMargin = faceLeftMargin;
    }
    public int getFaceRightMargin() {
        return this.faceRightMargin;
    }
    public void setFaceRightMargin(int faceRightMargin) {
        this.faceRightMargin = faceRightMargin;
    }

    @Override
    public String toString() {
        return "MagicBaseConfig{" +
                "id=" + id +
                ", activityType=" + activityType +
                ", isOpenFillLight=" + isOpenFillLight +
                ", warmValue='" + warmValue + '\'' +
                ", codeValue='" + codeValue + '\'' +
                ", isOPenCameraMirror=" + isOPenCameraMirror +
                ", cameraRotateValue=" + cameraRotateValue +
                ", exposure=" + exposure +
                ", isOPenVoice=" + isOPenVoice +
                ", voiceValue=" + voiceValue +
                ", isOPenSoundType=" + isOPenSoundType +
                ", soundTypeValue=" + soundTypeValue +
                ", minFaceHeight=" + minFaceHeight +
                ", minFace=" + minFace +
                ", maxFace=" + maxFace +
                ", faceLeftMargin=" + faceLeftMargin +
                ", faceRightMargin=" + faceRightMargin +
                '}';
    }
}
