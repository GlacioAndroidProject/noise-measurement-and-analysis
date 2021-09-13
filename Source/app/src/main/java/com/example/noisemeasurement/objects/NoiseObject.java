package com.example.noisemeasurement.objects;

public class NoiseObject  extends  RecordingObject{
    double noiseValue;
    String measurementaddress;

    public double getNoiseValue() {
        return noiseValue;
    }

    public void setNoiseValue(double noiseValue) {
        this.noiseValue = noiseValue;
    }

    public String getMeasurementaddress() {
        return measurementaddress;
    }

    public void setMeasurementaddress(String measurementaddress) {
        this.measurementaddress = measurementaddress;
    }
}
