package com.example.noisemeasurement.objects;

import java.util.ArrayList;

public class NoiseObject  extends  RecordingObject{
    double noiseAverageValue;
    String measurementaddress;
    ArrayList<Double>noiseValues;
    public double getNoiseAverageValue() {
        return noiseAverageValue;
    }

    public void setNoiseAverageValue(double noiseAverageValue) {
        this.noiseAverageValue = noiseAverageValue;
    }

    public String getMeasurementaddress() {
        return measurementaddress;
    }

    public void setMeasurementaddress(String measurementaddress) {
        this.measurementaddress = measurementaddress;
    }

    public ArrayList<Double> getNoiseValues() {
        return noiseValues;
    }

    public void setNoiseValues(ArrayList<Double> noiseValues) {
        this.noiseValues = noiseValues;
    }
}
