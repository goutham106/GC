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

package com.gm.goldencity;

import android.app.Application;
import android.content.Context;

import com.gm.goldencity.fragment.search.SearchModule;
import com.gm.goldencity.fragment.login.LoginModule;
import com.gm.goldencity.fragment.login.LoginSubComponent;
import com.gm.goldencity.fragment.search.SearchSubComponent;

/**
 * Name       : Gowtham
 * Created on : 28/2/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */
public abstract class GoldenCityApplication extends Application {

    private static ApplicationComponent component;
    private LoginSubComponent loginSubComponent;
    private SearchSubComponent searchSubComponent;

    public static ApplicationComponent getComponent() {
        return component;
    }

    public static GoldenCityApplication get(Context context) {
        return (GoldenCityApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initApplication();

        component = createComponent();
    }

    public ApplicationComponent createComponent() {
        return DaggerApplicationComponent.builder()
                .androidModule(new AndroidModule(this))
                .build();
    }

    public abstract void initApplication();

    // SubComponents Implementations

    public LoginSubComponent getLoginSubComponent() {
        if (null == loginSubComponent)
            createLoginSubComponent();

        return loginSubComponent;
    }

    public LoginSubComponent createLoginSubComponent() {
        loginSubComponent = component.plus(new LoginModule());
        return loginSubComponent;
    }

    public SearchSubComponent getSearchSubComponent() {
        if (null == searchSubComponent)
            createSearchSubComponent();

        return searchSubComponent;
    }

    public SearchSubComponent createSearchSubComponent() {
        searchSubComponent = component.plus(new SearchModule());
        return searchSubComponent;
    }



    public void releaseLoginSubComponent() {
        loginSubComponent = null;
    }
    public void releaseSearchSubComponent() {
        searchSubComponent = null;
    }


}