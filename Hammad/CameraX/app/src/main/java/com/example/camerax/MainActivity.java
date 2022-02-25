package com.example.camerax;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.impl.VideoCaptureConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import com.google.common.util.concurrent.ListenableFuture;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    Executor executor;

    PreviewView mPreviewView;
    ImageView imageViewCapture;
    Button btn_image,btn_video;

    private ImageCapture imageCapture;
    private VideoCapture videoCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreviewView=findViewById(R.id.camera_preview);
        imageViewCapture=findViewById(R.id.image_view);

        btn_image=findViewById(R.id.click_picture);
        btn_video=findViewById(R.id.record_video);

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                        if(multiplePermissionsReport.areAllPermissionsGranted())
                        {
                            Toast.makeText(MainActivity.this, "Permissions granted", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                    }
                })
                .check();

        cameraProviderFuture=ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider=cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(this));

        executor=Executors.newSingleThreadExecutor();


        btn_image.setOnClickListener(view -> {
            takePicture();
        });

        btn_video.setOnClickListener(v -> {

            if(btn_video.getText().toString().equals("Start Recording"))
            {
                btn_video.setText("Stop Recording");
                recordVideo();
            }
            else {
                btn_video.setText("Start Recording");
                videoCapture.stopRecording();
            }

        });

    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        CameraSelector cameraSelector=new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        Preview preview=new Preview.Builder().build();
        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());

        imageCapture=new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();

        videoCapture=new VideoCaptureConfig.Builder()
                .setVideoFrameRate(30).build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector,preview,imageCapture,videoCapture);
    }

    private void recordVideo() {
        if(videoCapture != null)
        {
            SimpleDateFormat mSimpleDateFormat=new SimpleDateFormat("yyyyy.MMMM.dd GGG hh:mm aaa", Locale.getDefault());
            File file=new File(getBatchDirectoryName("/camerax/videos"),mSimpleDateFormat.format(new Date())+ ".mp4");

            videoCapture.startRecording(file, executor, new VideoCapture.OnVideoSavedCallback() {
                @Override
                public void onVideoSaved(@NonNull File file) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "video saved!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Video err Code: "+videoCaptureError+"\nMessage: "+message.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

    }

    private void takePicture() {
        SimpleDateFormat mSimpleDateFormat=new SimpleDateFormat("yyyyy.MMMM.dd GGG hh:mm aaa", Locale.getDefault());
        File file=new File(getBatchDirectoryName("/camerax/images"),mSimpleDateFormat.format(new Date())+ ".jpg");

        ImageCapture.OutputFileOptions outputFileOptions=new ImageCapture.OutputFileOptions.Builder(file).build();

        imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "image saved!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Image error: "+exception.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

    }

    private String getBatchDirectoryName(String directoryName) {
        String folder_path="";

        folder_path=Environment.getExternalStorageDirectory().toString()+directoryName;

        File dir=new File(folder_path);

        if(!dir.exists() && !dir.mkdirs())
        {
            dir.mkdir();
        }

        return folder_path;
    }

}