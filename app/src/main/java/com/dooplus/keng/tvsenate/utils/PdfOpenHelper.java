package com.dooplus.keng.tvsenate.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;

import org.reactivestreams.Subscription;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PdfOpenHelper {

    private static String TAG = "PdfOpenHelper";

    public static void openPdfFromUrl(final String pdfUrl, final Activity activity) {
        Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                try {
                    URL url = new URL(pdfUrl);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    // download the file
                    InputStream input = new BufferedInputStream(connection.getInputStream());
                    File dir = new File(activity.getFilesDir(), "/shared_pdf");
                    dir.mkdir();
                    File file = new File(dir, "temp.pdf");
                    OutputStream output = new FileOutputStream(file);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                    return file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(Disposable s) {
                        Log.d(TAG, "onSubscribe: " + s.toString());
                    }

                    @Override
                    public void onNext(File file) {
                        Log.d(TAG, "onNext");
                        String authority = activity.getApplicationContext().getPackageName() + ".fileprovider";
                        Uri uriToFile = FileProvider.getUriForFile(activity, authority, file);

                        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
                        shareIntent.setDataAndType(uriToFile, "application/pdf");
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        if (shareIntent.resolveActivity(activity.getPackageManager()) != null) {
                            activity.startActivity(shareIntent);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.d(TAG, "onError: " + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }
}
