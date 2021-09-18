package com.example.noisemeasurement;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.noisemeasurement.objects.NoiseObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class DataManager {
    public static String dataFile ="NoiseHistory.txt";
    static final int RECORD_AUDIO =0, WRITE_EXTENAL_STORAGE = 1;

    public static boolean WriteNoiseHistoryToFile(ArrayList<NoiseObject> noiseObjects, Activity context){
        if(noiseObjects == null)
            return false;
        //JSONArray jsArray = new JSONArray(noiseObjects);
        String jsonText =  new Gson().toJson(noiseObjects);
        Type listType = new TypeToken<List<NoiseObject>>() {}.getType();

        List<NoiseObject> myModelList = new Gson().fromJson(jsonText , listType);
        return writeToFile(jsonText, context);
    }
    public static ArrayList<NoiseObject> ReadNoiseHistoryFromFile(Activity context){
        ArrayList<NoiseObject> noiseObjects = new ArrayList<>();
        String dataString = readFromFile(context);
        if (dataString== null || dataString.isEmpty())
            return null;

        Type listType = new TypeToken<List<NoiseObject>>() {}.getType();

        List<NoiseObject> myModelList = new Gson().fromJson(dataString, listType);

        noiseObjects.addAll(myModelList);

        return noiseObjects;
    }

    public static boolean writeToFile(String data, Activity context) {
        if(!checkStorePermission(context)){
            requestStorePermission(context);
        }
        requestStorePermission(context);

        try {
            String folderPath = GetDefaultFolderPath(context);
            File folder = new File(folderPath);
            if(!folder.isDirectory() || !folder.exists())  // check parent folder exits
            {
                boolean result = folder.mkdir();
                folder.setWritable(true);
            }
            File logFile_ = new File(folderPath, dataFile);

            String file_path= logFile_.getPath();

            FileOutputStream fileout =new FileOutputStream (logFile_,false);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            //outputWriter.append(data);
            outputWriter.write(data);
            outputWriter.close();
            //clear logdata when It's size larger 100MB
            if(logFile_.length()/(1024*1024)> 100) {
                PrintWriter writer = new PrintWriter(file_path);
                writer.print("");
                writer.close();
            }
            Toast.makeText(context, "Write file successful", Toast.LENGTH_LONG).show();
            return true;
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            return false;
        }
    }
    public static String readFromFile(Activity context) {

        String data = "";

        if(!checkStorePermission(context)){
            requestStorePermission(context);
        }
        try {
            String folderPath = GetDefaultFolderPath(context);
            File file = new File(folderPath, dataFile);
            if(!file.exists()){
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            BufferedReader in = null;

            try {
                in = new BufferedReader(new FileReader(file));
                while ((line = in.readLine()) != null) stringBuilder.append(line);
                return stringBuilder.toString();
            }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            }
        }
            catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        return null;
    }

    public static void requestRecoderPermission(Activity context){
        if(ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.RECORD_AUDIO))
        {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);
            Toast.makeText(context, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        }
        else{
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO);
        }
    }
    public static boolean checkRecoderPermission(Activity mContext){
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO);
        if(result == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }
    public static boolean checkStorePermission(Activity mContext){
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED)
            return true;
        else return false;
    }



    public static void requestStorePermission(Activity context){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(context, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        }else{
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTENAL_STORAGE);
        }
    }
    public static String GetDefaultFolderPath(Activity mContext){
        if(!checkStorePermission(mContext)){
            requestStorePermission(mContext);
        }
        String folderPath = "";
        ApplicationInfo applicationInfo = mContext.getApplicationInfo();
        int appNameID = applicationInfo.labelRes;
        String AppName = appNameID == 0 ? applicationInfo.nonLocalizedLabel.toString() : mContext.getString(appNameID);

        folderPath =mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();

        return  folderPath;
    }
    public static String ConvertMinisecondToDateTime(int minisecondTime){

        int secondtemp = minisecondTime/1000;
        int hour = secondtemp/3600;
        int minute = (secondtemp - hour*3600)/60;
        int second = secondtemp - hour*3600 - minute*60;
        String time = String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second);
        return  time;
    }
    public static String ConvertMinisecondDuration(int minisecondTime){

        int secondtemp = minisecondTime/1000;
        int hour = secondtemp/3600;
        int minute = (secondtemp - hour*3600)/60;
        int second = secondtemp - hour*3600 - minute*60;
        String time = String.format("%02d", hour) + "h" + String.format("%02d", minute) + "m" + String.format("%02d", second) + "s";
        return  time;
    }

}
