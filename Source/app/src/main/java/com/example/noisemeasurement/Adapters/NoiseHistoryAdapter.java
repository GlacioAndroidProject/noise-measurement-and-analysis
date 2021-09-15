package com.example.noisemeasurement.Adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noisemeasurement.objects.RecordingObject;

import java.util.ArrayList;

public class NoiseHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<RecordingObject> recordingObject;
    Context mContext;
    public NoiseHistoryAdapter(Context context, ArrayList<RecordingObject> recordingObject){
        this.mContext = context;
        this.recordingObject = recordingObject;
        //this.listenerUser = (ListenerUser) context;
        //sessionManager = new SessionManager(mContext);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
