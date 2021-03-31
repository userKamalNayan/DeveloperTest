package com.sampleassignment.developertext;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sampleassignment.developertext.Common.Common;
import com.sampleassignment.developertext.Events.StopServiceEvent;
import com.sampleassignment.developertext.Model.ResultModel;
import com.sampleassignment.developertext.Remote.IMyApi;
import com.sampleassignment.developertext.Remote.RetrofitClient;
import com.sampleassignment.developertext.Services.RepeatService;

import org.greenrobot.eventbus.EventBus;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    MaterialButton btn_startStop;


    IMyApi iMyApi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_startStop = findViewById(R.id.btn_start_service);
        Retrofit retrofit = RetrofitClient.getInstance();
        iMyApi = retrofit.create(IMyApi.class);


        btn_startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WAKE_LOCK};
                Dexter.withContext(MainActivity.this).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (btn_startStop.getText() == getResources().getString(R.string.start_service)) {

                            startService(new Intent(MainActivity.this, RepeatService.class));

                            btn_startStop.setText(getResources().getString(R.string.stop_service));

                        } else {
                            btn_startStop.setText(getResources().getString(R.string.start_service));
                           // EventBus.getDefault().post(new StopServiceEvent(true));
                         //   Common.stopService = true;
                            Common.mObservable.onNext(true);
                            stopService(new Intent(MainActivity.this, RepeatService.class));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

            }
        });
    }


}