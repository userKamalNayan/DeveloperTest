package com.sampleassignment.developertext.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sampleassignment.developertext.Common.Common;
import com.sampleassignment.developertext.Events.StopServiceEvent;
import com.sampleassignment.developertext.Model.ResultModel;
import com.sampleassignment.developertext.Remote.IMyApi;
import com.sampleassignment.developertext.Remote.RetrofitClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import retrofit2.Retrofit;

public class RepeatService extends Service {

    IMyApi iMyApi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    PowerManager.WakeLock mWakeLock;
    Disposable disposable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Retrofit retrofit = RetrofitClient.getInstance();
        iMyApi = retrofit.create(IMyApi.class);
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire(15 * 60 * 1000L /*15 minutes*/);


        Subject<Boolean> mObservable = PublishSubject.create();
        // Observable<Boolean> stopObservable = Observable.defer(()-> {return Observable.just(Common.stopService); });


        getData();

        disposable = Observable.interval(10, TimeUnit.SECONDS)
                .doOnNext(n -> getData()).subscribe();
        

        method();
        super.onCreate();
    }


    public void method() {
        Common.mObservable.map(value -> {
            if (value) stopService(value);
            return String.valueOf(value);
        }).subscribe();
    }

    private void getData() {
        String ipAddress = "24.48.0.1";

        File rootStorage = new File(String.valueOf(getApplicationContext().getExternalFilesDir("storage/emulated/0")));

        compositeDisposable
                .add(iMyApi.getDetails(ipAddress)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResultModel>() {
                            @Override
                            public void accept(ResultModel resultModel) throws Exception {
                                //System.out.println(" result = " + resultModel.getCity() + " " + resultModel.getRegionName());
                                String fileName = "sampleDeveloper.txt";

                                File actualFile = new File(rootStorage.getAbsolutePath() + "/" + fileName);
                                Date date = new Date();

                                /*
                                *  "query": "24.48.0.1",
  "status": "success",
  "country": "Canada",
  "countryCode": "CA",
  "region": "QC",
  "regionName": "Quebec",
  "city": "Montreal",
  "zip": "H1S",
  "lat": 45.5808,
  "lon": -73.5825,
  "timezone": "America/Toronto",
  "isp": "Le Groupe Videotron Ltee",
  "org": "Videotron Ltee",
  "as": "AS5769 Videotron Telecom Ltee"
                                * */

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MM:YYYY");

                                String contents = new StringBuilder("\n\n" + simpleDateFormat.format(date).toString() + " : " + ipAddress).append("\nquery : ").append(resultModel.getQuery()).append("\nstatus : ").append(resultModel.getStatus()).append("\ncountry : ").append(resultModel.getCountry())
                                        .append("\ncountryCode : ").append(resultModel.getCountryCode()).append("\nregion : ").append(resultModel.getRegion())
                                        .append("\nregionName : ").append(resultModel.getRegionName())
                                        .append("\ncity : ").append(resultModel.getCity())
                                        .append("\nzip : ").append(resultModel.getZip())
                                        .append("\nlat : ").append(resultModel.getLat())
                                        .append("\nlon : ").append(resultModel.getLon())
                                        .append("\ntimezone : ").append(resultModel.getTimezone())
                                        .append("\nisp : ").append(resultModel.getIsp())
                                        .append("\norg : ").append(resultModel.getOrg())
                                        .append("\nas : ").append(resultModel.getAs())
                                        .append("\n\n")
                                        .toString();


                                updateTextFile(actualFile.getAbsolutePath(), contents);

                            }
                        }));
    }


    public void updateTextFile(String fileName, String contents) {
        try {

            File textFile = new File(fileName);

            if (textFile.exists()) {
                System.out.println("File path exists = " + textFile.getAbsolutePath());

                // set to true if you want to append contents to text file
                // set to false if you want to remove preivous content of text
                // file
                FileWriter textFileWriter = new FileWriter(textFile, true);

                BufferedWriter out = new BufferedWriter(textFileWriter);

                // create the content string
                String contentString = new String(contents);

                // write the updated content
                out.write(contentString);
                out.close();


            } else {
                createTextFile(fileName);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTextFile(String actualFile) {
        try {

            File file = new File(actualFile);

            if (!file.exists()) {
                System.out.println("File path = " + file.getAbsolutePath());
                if (file.createNewFile()) {
                    Toast.makeText(this, "File Created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "File not Created", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restart = new Intent(getApplicationContext(), this.getClass());
        restart.setPackage(getPackageName());
        startService(restart);
        super.onTaskRemoved(rootIntent);

    }


    @Override
    public void onDestroy() {

        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        stopSelf();
        super.onDestroy();

    }

    public void stopService(boolean stopService) {
        System.out.println("service stopped");
        if (stopService) {
            if (!disposable.isDisposed())
                disposable.dispose();

            stopSelf();

        }
    }
}
