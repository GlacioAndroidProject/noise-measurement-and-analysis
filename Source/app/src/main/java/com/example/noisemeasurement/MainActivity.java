package com.example.noisemeasurement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.noisemeasurement.ProccessBarUI.SoundLevelView;
import com.example.noisemeasurement.objects.NoiseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    NoiseObject noiseObject;
    TextView tvCurrentTime, tvrecordingDurationTime, tvRecording,tvCurentNoiseLevel, tvAverageNoiseLevel;
    ImageButton btnSave, btnShowHistory,btnRecording;
    Timer updateCurrentTime, updateAverageNoise;
    boolean isStartRecording = false;
    private SoundLevelView mDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FindByID();
        SetEventListener();
        UpdateCurrentTimeViaTimer();

    }
    private void FindByID(){
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvrecordingDurationTime = findViewById(R.id.tvrecordingDurationTime);
        tvRecording = findViewById(R.id.tvRecording);
        btnSave = findViewById(R.id.btnSave);
        btnRecording = findViewById(R.id.btnRecording);
        btnShowHistory = findViewById(R.id.btnShowHistory);
        mDisplay = (SoundLevelView) findViewById(R.id.volume);
        tvCurentNoiseLevel = findViewById(R.id.tvCurentNoiseLevel);
        tvAverageNoiseLevel = findViewById(R.id.tvAverageNoiseLevel);

    }

    private void SetEventListener(){
        btnRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartRecording=!isStartRecording;
                ChangeStatusButtonSave(isStartRecording);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveRecording();
            }
        });
        btnShowHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHistory();
            }
        });
    }
    private void ChangeStatusButtonSave(boolean isStartRecording){
        if(isStartRecording)
        {
            btnRecording.setBackgroundResource(R.drawable.oval_size_80_boder_white);
            btnRecording.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange, getTheme())));
            tvRecording.setText(getResources().getString(R.string.recording));
            StartRecoding();
        }
        else {
            btnRecording.setBackgroundResource(R.drawable.oval_size_80_boder_white);
            btnRecording.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray, getTheme())));
            tvRecording.setText(getResources().getString(R.string.stoped_recording));
            StopRecording();
        }


    }
    private void SaveRecording(){

    }
    private void ViewHistory(){

    }
    @SuppressLint("InvalidWakeLockTag")
    private void StartRecoding(){
        recordingManager = new RecordingManager(getApplicationContext());
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON , "NoiseAlert");
        initializeApplicationConstants();
        mDisplay.setLevel(0, mThreshold);

        if (!mRunning) {
            mRunning = true;
            start();
        }
    }
    private void StopRecording(){
        //Stop noise monitoring
        Log.i("Noise", "==== Stop Noise Monitoring===");
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        mHandlerGetNoise.removeCallbacks(mSleepTask);
        mHandlerGetNoise.removeCallbacks(mPollTask);
        recordingManager.stop();
        mDisplay.setLevel(0,0);
        updateDisplay("stopped...", 0.0);
        mRunning = false;    }
    private void SaveNoiseRecording(){

    }
    private void NoiseAnalysic(){

    }
    private void AddNoiseitems(NoiseObject noiseObject){

    }
    private double getNoiseAverage(ArrayList<NoiseObject> noises){
        return 0.0;
    }
    private void UpdateRecordingDurationTimeView(NoiseObject noiseObject){ // in second
        tvrecordingDurationTime.setText(noiseObject.getRecodringDuration());
    }
    private void UpdateCurrentTimeViaTimer(){
        updateCurrentTime = new Timer();
        final int FPS = 40;
        TimerTask updateTimer = new UpdateCurrentTime();
        updateCurrentTime.schedule(updateTimer, 0, 1000);
    }
    private String GetCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        if(tvCurrentTime!=null){
            tvCurrentTime.setText(currentDateandTime);
        }
        return  currentDateandTime;
    }
    class UpdateCurrentTime extends TimerTask {
        public void run() {
            //calculate the new position of myBall
            GetCurrentTime();
        }
    }

    private void DestroyTimer(){
        if(updateCurrentTime!=null)
        {
            try {
                updateCurrentTime.cancel();
                updateCurrentTime = null;
            }
            catch (Exception e){

            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DestroyTimer();
    }

    private PowerManager.WakeLock mWakeLock;
    private Handler mHandlerGetNoise = new Handler();
    private int mThreshold;
    private boolean mRunning = false;
    private static final int POLL_INTERVAL = 300;
    private RecordingManager recordingManager;

    private Runnable mSleepTask = new Runnable() {
        public void run() {
            //Log.i("Noise", "runnable mSleepTask");

            start();
        }
    };

    // Create runnable thread to Monitor Voice
    private Runnable mPollTask = new Runnable() {
        public void run() {

            double amp = recordingManager.getAmplitude();
            //Log.i("Noise", "runnable mPollTask");
            updateDisplay("Monitoring Voice...", amp);

            if ((amp > mThreshold)) {
                callForHelp();
                //Log.i("Noise", "==== onCreate ===");

            }

            // Runnable(mPollTask) will again execute after POLL_INTERVAL
            mHandlerGetNoise.postDelayed(mPollTask, POLL_INTERVAL);

        }
    };
    private void start() {
        //Log.i("Noise", "==== start ===");

        recordingManager.start(this);
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }

        //Noise monitoring start
        // Runnable(mPollTask) will execute after POLL_INTERVAL
        mHandlerGetNoise.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void initializeApplicationConstants() {
        // Set Noise Threshold
        mThreshold = 8;

    }

    private void updateDisplay(String status, double signalEMA) {
        tvCurentNoiseLevel.setText(String.valueOf(signalEMA));
        mDisplay.setLevel((int)signalEMA, mThreshold);
    }
    private void callForHelp() {

        //stop();

        // Show alert when noise thersold crossed
        Toast.makeText(getApplicationContext(), "Noise Thersold Crossed, do here your stuff.",
                Toast.LENGTH_LONG).show();
    }
}