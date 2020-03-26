package com.bawei.day25_wanzixi;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @ClassName: ApiService
 * @Description: Java类的作用
 * @Author: LazyRui
 * @CreateDate: 2020/3/25 13:45
 */
public interface ApiService {

    @POST("small/user/v1/login")
    @FormUrlEncoded
    Observable<UserEntity> getLoginData(@Field("phone") String ph, @Field("pwd") String pw);

    @POST("small/user/v1/register")
    @FormUrlEncoded
    Observable<UserEntity> getregisterData(@Field("phone") String ph, @Field("pwd") String pw);

}
