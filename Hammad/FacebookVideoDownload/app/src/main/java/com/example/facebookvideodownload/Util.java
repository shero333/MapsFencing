package com.example.facebookvideodownload;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class Util {

    public static String ROOT_DIRECTORY_FACEBOOK = "/Facebook Video Downloader/Video/";

    public static void downloadFacebookVideo(String downloadPath, String destPath, Context context, String fileName) {
        Toast.makeText(context, "Downloading started", Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse(downloadPath);

        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileName);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, destPath + fileName);
        ((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
    }
}
