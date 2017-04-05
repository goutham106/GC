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

import com.gm.goldencity.activity.DashBoardActivity;
import com.gm.goldencity.activity.LoginBaseActivity;
import com.gm.goldencity.activity.SplashActivity;
import com.gm.goldencity.activity.search.SearchActivity;
import com.gm.goldencity.fragment.search.SearchModule;
import com.gm.goldencity.domain.ApiModule;
import com.gm.goldencity.domain.ClientModule;
import com.gm.goldencity.fragment.login.LoginModule;
import com.gm.goldencity.fragment.login.LoginSubComponent;
import com.gm.goldencity.fragment.search.SearchSubComponent;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Name       : Gowtham
 * Created on : 6/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

@Singleton
@Component(modules = {
        AndroidModule.class,
        ApplicationModule.class,
        ApiModule.class,
        ClientModule.class
})
public interface ApplicationComponent {

    void inject(SplashActivity activity);

    void inject(LoginBaseActivity activity);

    void inject(DashBoardActivity activity);

    void inject(SearchActivity activity);

    LoginSubComponent plus(LoginModule module);

    SearchSubComponent plus(SearchModule module);

}