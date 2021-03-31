package com.sampleassignment.developertext.Remote;

import com.sampleassignment.developertext.Model.ResultModel;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IMyApi {
    @GET("json/{ip}")
    Observable<ResultModel> getDetails(@Path ("ip") String ip);
}
