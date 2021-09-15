package com.example.noisemeasurement;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.noisemeasurement.ProccessBarUI.SoundLevelView;

import java.io.IOException;


public class RecordingManager {
    Context mContext;
    protected int bitsPerSamples = 16;

    public  RecordingManager(Context context){
        mContext = context;

    }
    // This file is used to record voice
    static final private double EMA_FILTER = 0.6;
    static final int RECORD_AUDIO =0;

    private MediaRecorder mRecorder = null;
    private double mEMA = 0.0;

    public void start(Activity activity) {

        if(!checkPermission(activity))
            requestPermission(activity);

        if (mRecorder == null) {

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            try {
                mRecorder.prepare();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            mRecorder.start();
            mEMA = 0.0;
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude()/2700.0);
        else
            return 0;

    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }
    public void requestPermission(Activity context){
        if(ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.RECORD_AUDIO))
        {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);
            Toast.makeText(context, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        }
        else{
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);
        }
    }
    public  boolean checkPermission(Activity mContext){
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO);
        if(result == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

}