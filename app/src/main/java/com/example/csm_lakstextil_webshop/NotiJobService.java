package com.example.csm_lakstextil_webshop;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class NotiJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        new NotificationHandler(getApplicationContext()).sendNoti("Check out our new collection!","We add new stuff to our webshop every Monday. Don't miss the opportunity!");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
