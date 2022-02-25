package com.hammad.whatsappstatussaver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Activity activity;

    //shortcut Alt+Shift+F
    private int REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing the activity reference
        activity=this;

        checkPermissions();
    }

    private void checkPermissions() {
        String[] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(activity,permission[0]) != PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(activity, permission[1]) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,permission,REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE && grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {}
        else
        {
            Toast.makeText(activity, "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }
}