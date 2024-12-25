package com.app.mygame.network;

import com.app.mygame.usePre.requestVo.LoginRequest;
import com.app.mygame.usePre.requestVo.SendOtpRequest;
import com.app.mygame.usePre.requestVo.User;
import com.app.mygame.usePre.responseVo.LoginResponse;
import com.app.mygame.usePre.responseVo.OtpSuccessResponse;
import com.app.mygame.usePre.responseVo.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("users/registeruser")
    Call<RegisterResponse> registerUser(@Body User user);

    @POST("users/login/sendotp")
    Call<User> sendOtp(@Body SendOtpRequest sendOtpRequest);

    @POST("users/login/verifyotp")
    Call<OtpSuccessResponse> verifyOtp(@Body SendOtpRequest sendOtpRequest);

    @POST("users/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

}
