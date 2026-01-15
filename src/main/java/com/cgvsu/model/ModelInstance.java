package com.cgvsu.model;

public class ModelInstance {
    private Model model;
    private TransformationParameters transformationParams;

    public ModelInstance(Model model) {
        this.model = model;
        this.transformationParams = new TransformationParameters();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public TransformationParameters getTransformationParams() {
        return transformationParams;
    }

    public void setTransformationParams(TransformationParameters params) {
        this.transformationParams = params;
    }
}
