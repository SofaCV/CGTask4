package com.cgvsu.model;

public class TransformationParameters {
    private float translationX = 0;
    private float translationY = 0;
    private float translationZ = 0;
    private float rotationX = 0;
    private float rotationY = 0;
    private float rotationZ = 0;
    private float scaleX = 1;
    private float scaleY = 1;
    private float scaleZ = 1;
    private float alpha = 0;
    private float beta = 0;
    private float gamma = 0;

    public float getTranslationX() { return translationX; }
    public float getTranslationY() { return translationY; }
    public float getTranslationZ() { return translationZ; }
    
    public float getRotationX() { return rotationX; }
    public float getRotationY() { return rotationY; }
    public float getRotationZ() { return rotationZ; }
    
    public float getScaleX() { return scaleX; }
    public float getScaleY() { return scaleY; }
    public float getScaleZ() { return scaleZ; }
    
    public float getAlpha() { return alpha; }
    public float getBeta() { return beta; }
    public float getGamma() { return gamma; }

    public void setTranslationX(float value) { this.translationX = value; }
    public void setTranslationY(float value) { this.translationY = value; }
    public void setTranslationZ(float value) { this.translationZ = value; }
    
    public void setRotationX(float value) { this.rotationX = value; }
    public void setRotationY(float value) { this.rotationY = value; }
    public void setRotationZ(float value) { this.rotationZ = value; }
    
    public void setScaleX(float value) { this.scaleX = value; }
    public void setScaleY(float value) { this.scaleY = value; }
    public void setScaleZ(float value) { this.scaleZ = value; }
    
    public void setAlpha(float value) { this.alpha = value; }
    public void setBeta(float value) { this.beta = value; }
    public void setGamma(float value) { this.gamma = value; }

    public void setTranslationX(double value) { this.translationX = (float) value; }
    public void setTranslationY(double value) { this.translationY = (float) value; }
    public void setTranslationZ(double value) { this.translationZ = (float) value; }
    
    public void setRotationX(double value) { this.rotationX = (float) value; }
    public void setRotationY(double value) { this.rotationY = (float) value; }
    public void setRotationZ(double value) { this.rotationZ = (float) value; }
    
    public void setScaleX(double value) { this.scaleX = (float) value; }
    public void setScaleY(double value) { this.scaleY = (float) value; }
    public void setScaleZ(double value) { this.scaleZ = (float) value; }
    
    public void setAlpha(double value) { this.alpha = (float) value; }
    public void setBeta(double value) { this.beta = (float) value; }
    public void setGamma(double value) { this.gamma = (float) value; }

    public void setAlpha(int value) { this.alpha = (float) value; }
    public void setBeta(int value) { this.beta = (float) value; }
    public void setGamma(int value) { this.gamma = (float) value; }
}
