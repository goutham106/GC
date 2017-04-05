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

package com.gm.goldencity.fragment.login;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gm.goldencity.GoldenCityApplication;
import com.gm.goldencity.R;
import com.gm.goldencity.activity.DashBoardActivity;
import com.gm.goldencity.base.basic.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Name       : Gowtham
 * Created on : 6/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

public class LoginFragment extends BaseFragment implements LoginView {

    @Inject
    Context context;
    @Inject
    Resources resources;

    @Inject
    LoginPresenter presenter;

    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.edt_mobileNo)
    EditText edt_mobileNo;
    @BindView(R.id.edt_pwd)
    EditText edt_pwd;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter.bind(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }

    @Override
    protected void injectDependencies(GoldenCityApplication application) {
        application.getLoginSubComponent().inject(this);
    }

    @OnClick(R.id.btn_login)
    void onLoginClick(View view) {
        startActivity(new Intent(context, DashBoardActivity.class));
        getActivity().finish();
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showOfflineMessage(boolean isCritical) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
