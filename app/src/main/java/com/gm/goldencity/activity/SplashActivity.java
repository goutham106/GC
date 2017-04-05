/*
 * Copyright (c) 2017 Gowtham Parimelazhagan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gm.goldencity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gm.glog.library.GLog;
import com.gm.goldencity.ApplicationComponent;
import com.gm.goldencity.GoldenCityApplication;
import com.gm.goldencity.R;
import com.gm.goldencity.base.basic.BaseActivity;
import com.gm.goldencity.util.AppConstants;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class SplashActivity extends BaseActivity {

    // injecting dependencies via Dagger
    @Inject
    Context context;

    // Thread to process splash screen events
    private Thread splashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        // The thread to wait for splash screen events
        splashThread = new Thread() {
            @Override
            public void run() {

                try {
                    synchronized (this) {
                        // Wait given period of time or exit on touch
                        wait(AppConstants.SPLASH_TIMEOUT_SEC);
                    }
                } catch (InterruptedException ex) {
                    GLog.e("Splash thread interrupted!", ex.getMessage());
                }

                finish();

                Intent mainActivityIntent = new Intent();
                mainActivityIntent.setClass(context, LoginBaseActivity.class);
                startActivity(mainActivityIntent);
            }
        };

        splashThread.start();

    }

    @Override
    protected void injectDependencies(GoldenCityApplication application, ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void releaseSubComponents(GoldenCityApplication application) {

    }
}
