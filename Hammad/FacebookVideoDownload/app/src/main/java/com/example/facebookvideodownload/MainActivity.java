package com.example.facebookvideodownload;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editTextLink;
    Button buttonDownload, buttonPasteLink;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLink = findViewById(R.id.edt_txt_link);

        //initializing activity instance
        activity = this;

        buttonDownload = findViewById(R.id.btn_download);
        buttonPasteLink = findViewById(R.id.btn_paste_link);

        buttonPasteLink.setOnClickListener(view -> {
            if (pasteLink() != null) {
                editTextLink.setText(pasteLink());
                hideSoftInputKeyboard(view);
            } else if (pasteLink() == null) {
                Toast.makeText(activity, "No data in clipboard", Toast.LENGTH_SHORT).show();
            }

        });

        buttonDownload.setOnClickListener(v -> checkPermission());
    }


    private void checkPermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, 1);
        } else {
            downloadVideo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            downloadVideo();
        } else {
            Toast.makeText(activity, "permission denied", Toast.LENGTH_SHORT).show();
        }

    }

    private String pasteLink() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (clipData == null) {
            return null;
        } else {
            ClipData.Item item = clipData.getItemAt(0);
            return item.getText().toString();
        }
    }

    private void downloadVideo() {

        try {
            URL url = new URL(editTextLink.getText().toString());
            String host = url.getHost();

            if (host.contains("fb.watch")) {
                new FacebookDownloadAsyncTask().execute(editTextLink.getText().toString());
            } else {
                Toast.makeText(activity, "Please paste Facebook video link", Toast.LENGTH_SHORT).show();
                editTextLink.setText("");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

    private void hideSoftInputKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    class FacebookDownloadAsyncTask extends AsyncTask<String, Void, Document> {

        Document facebookDoc;

        @Override
        protected Document doInBackground(String... strings) {
            try {
                facebookDoc = Jsoup.connect(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return facebookDoc;
        }

        @Override
        protected void onPostExecute(Document document) {
            String videoUrl = document.select("meta[property=\"og:video\"]")
                    .last().attr("content");

            if (videoUrl != null) {
                Util.downloadFacebookVideo(videoUrl, Util.ROOT_DIRECTORY_FACEBOOK, activity,
                        "facebook_" + System.currentTimeMillis() + ".mp4");
            }
        }
    }
}