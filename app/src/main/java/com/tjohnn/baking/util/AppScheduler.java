package com.tjohnn.baking.util;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AppScheduler {

    private static AppScheduler appScheduler;

    public static AppScheduler getInstance(){
        if(appScheduler == null) appScheduler = new AppScheduler();
        return appScheduler;
    }

    public Scheduler io(){
        return Schedulers.io();
    }

    public Scheduler main(){
        return AndroidSchedulers.mainThread();
    }


    public Scheduler trampoline(){
        return Schedulers.trampoline();
    }



}
