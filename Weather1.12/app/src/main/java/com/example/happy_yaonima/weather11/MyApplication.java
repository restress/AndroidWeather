package com.example.happy_yaonima.weather11;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 * Created by Happy_yaonima on 2016/10/10.
 */

public class MyApplication extends Application {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate();
        ApiStoreSDK.init(this, " 2c238fba1b1153bf8894d13f0858ccc4");
        /*ApiStoreSDK.init(this, "88aa7f34cf0f138a60937bdb1d1fc2b6");*/
    }
}
