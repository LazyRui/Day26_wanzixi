package com.bawei.day25_wanzixi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {

    private EditText mPh;
    private EditText mPw;
    private Button mButRegister;
    private Button mButLogin;
    private Retrofit mBuild;

    Handler mHandler = new Handler(){
        private int i = 3;
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (i==0){
                ProgressDialogUtil.dismiss();
                Toast.makeText(Main2Activity.this, "登录成功", Toast.LENGTH_SHORT).show();

                finish();
            }else {
                i--;
                mHandler.sendEmptyMessageDelayed(1,1000);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mPh = findViewById(R.id.ph);
        mPw = findViewById(R.id.pw);
        mButRegister = findViewById(R.id.but_register);
        mButLogin = findViewById(R.id.but_login);



        mBuild = new Retrofit.Builder()
                .baseUrl("http://mobile.bwstudent.com/")
                .client(new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        mButLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ph = mPh.getText().toString();
                String pw = mPw.getText().toString();
                if (TextUtils.isEmpty(ph)) {
                    return;
                }
                if (TextUtils.isEmpty(pw)) {
                    return;
                }
                ProgressDialogUtil.showProgressDialog(Main2Activity.this,"加载中……");

                mBuild.create(ApiService.class)
                        .getLoginData(ph, pw)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<UserEntity>() {
                            @Override
                            public void accept(UserEntity userEntity) throws Exception {
                                if (userEntity != null) {
                                    if (userEntity.getMessage().equals("登录成功")) {

                                        EventBus.getDefault().postSticky(userEntity);

                                        mHandler.sendEmptyMessageDelayed(1,1000);

                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                                Toast.makeText(Main2Activity.this, ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
        mButRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ph = mPh.getText().toString();
                String pw = mPw.getText().toString();
                if (TextUtils.isEmpty(ph)) {
                    return;
                }
                if (TextUtils.isEmpty(pw)) {
                    return;
                }


                mBuild.create(ApiService.class)
                        .getregisterData(ph, pw)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<UserEntity>() {
                            @Override
                            public void accept(UserEntity userEntity) throws Exception {
                                if (userEntity != null) {
                                    if (userEntity.getMessage().equals("注册成功")) {
                                        Toast.makeText(Main2Activity.this, "注册成功", Toast.LENGTH_SHORT).show();

                                        finish();

                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(this);
    }
}
