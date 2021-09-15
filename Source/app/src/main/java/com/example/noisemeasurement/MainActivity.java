package com.example.noisemeasurement;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.noisemeasurement.objects.NoiseObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    NoiseObject noiseObject;
    TextView tvCurrentTime, tvrecordingDurationTime, tvRecording;
    ImageButton btnSave, btnShowHistory,btnRecording;
    Timer updateCurrentTime, updateAverageNoise;
    boolean isStartRecording = false;

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
    private void StartRecoding(){

    }
    private void StopRecording(){

    }
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
}