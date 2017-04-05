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

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;

import com.gm.goldencity.ApplicationComponent;
import com.gm.goldencity.GoldenCityApplication;
import com.gm.goldencity.R;
import com.gm.goldencity.base.basic.BaseActivity;
import com.gm.goldencity.fragment.login.LoginFragment;

import butterknife.ButterKnife;

public class LoginBaseActivity extends BaseActivity {

    public static final String TAG_LOGIN_FRAGMENT = "login_fragment";

    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_base);

        ButterKnife.bind(this);
//        statefulLayout.showEmpty();
//        new Handler().postDelayed(() -> {
//            statefulLayout.showLoading();
//            new Handler().postDelayed(() -> statefulLayout.showContent(), 5000);
//        }, 10000);


        if (null == savedInstanceState) {
            loginFragment = LoginFragment.newInstance();
            attachFragments();
        } else {
            loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(TAG_LOGIN_FRAGMENT);
        }
    }

    private void attachFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.base_container, loginFragment, TAG_LOGIN_FRAGMENT);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    protected void injectDependencies(GoldenCityApplication application, ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void releaseSubComponents(GoldenCityApplication application) {
        application.releaseLoginSubComponent();
    }
}
