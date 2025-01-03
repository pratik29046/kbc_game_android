package com.app.mygame.network;

import com.app.mygame.usePre.requestVo.LoginRequest;
import com.app.mygame.usePre.requestVo.SendOtpRequest;
import com.app.mygame.usePre.requestVo.User;
import com.app.mygame.usePre.responseVo.LoginResponse;
import com.app.mygame.usePre.responseVo.OtpSuccessResponse;
import com.app.mygame.usePre.responseVo.ProfileResponse;
import com.app.mygame.usePre.responseVo.RegisterResponse;
import com.app.mygame.userPost.requestVo.StartRoundRequest;
import com.app.mygame.userPost.requestVo.SubmitAnswerRequest;
import com.app.mygame.userPost.requestVo.TableWinnerRequest;
import com.app.mygame.userPost.responseVo.AllTournamentsResponse;
import com.app.mygame.userPost.responseVo.BannerResponse;
import com.app.mygame.userPost.responseVo.PlansResponse;
import com.app.mygame.userPost.responseVo.StartRoundResponse;
import com.app.mygame.userPost.responseVo.TableWinnerResponse;
import com.app.mygame.userPost.responseVo.TournamentsRegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("users/registeruser")
    Call<RegisterResponse> registerUser(@Body User user);

    @POST("users/login/sendotp")
    Call<User> sendOtp(@Body SendOtpRequest sendOtpRequest);

    @POST("users/login/verifyotp")
    Call<OtpSuccessResponse> verifyOtp(@Body SendOtpRequest sendOtpRequest);

    @POST("users/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("users/profile")
    Call<ProfileResponse> profile();

    @GET("userpay/plan/all/active")
    Call<PlansResponse> plans();

    @GET("tournaments/banners/active/all")
    Call<BannerResponse> banner();

    @GET("tournaments/active/all")
    Call<AllTournamentsResponse> tournaments();
    @POST("tournaments/{id}/register")
    Call<TournamentsRegisterResponse> tournamentsRegister(@Path("id") long id);

    @POST("tournaments/{id}/deregister")
    Call<TournamentsRegisterResponse> tournamentsDeRegister(@Path("id") long id);

    @GET("tournaments/userregistered/latest")
    Call<AllTournamentsResponse>  topThreeUserTournamentsRegister();

    @POST("questions/startround")
    Call<StartRoundResponse> startRound(@Body StartRoundRequest startRoundRequest);

    @POST("questions/submitanswer")
    Call<StartRoundResponse> submitAnswerRound(@Body SubmitAnswerRequest submitAnswerRequest);

    @POST("questions/levelwinnercalculate")
    Call<TableWinnerResponse> checkTableRoundWinner(@Body TableWinnerRequest tableWinnerRequest);

}
