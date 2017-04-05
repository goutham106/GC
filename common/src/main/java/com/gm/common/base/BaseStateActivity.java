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

package com.gm.common.base;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gm.common.R;
import com.gm.common.ui.statelayout.StatefulLayout;

/**
 * Name       : Gowtham
 * Created on : 23/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

public class BaseStateActivity extends AppCompatActivity {

    protected FrameLayout baseLayout;
    protected StatefulLayout statefulLayout;
    private LinearLayout linearLayout;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        baseLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.activity_state_base_layout, null);

        statefulLayout = (StatefulLayout) baseLayout.findViewById(R.id.stateLayout);
        linearLayout = (LinearLayout) baseLayout.findViewById(R.id.stateLayoutChild);
        getLayoutInflater().inflate(layoutResID, linearLayout, true);

        super.setContentView(baseLayout);
    }
}
