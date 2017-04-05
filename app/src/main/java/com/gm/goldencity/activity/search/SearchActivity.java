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

package com.gm.goldencity.activity.search;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.gm.common.util.ImeUtils;
import com.gm.glog.library.GLog;
import com.gm.goldencity.ApplicationComponent;
import com.gm.goldencity.GoldenCityApplication;
import com.gm.goldencity.R;
import com.gm.goldencity.base.basic.BaseActivity;
import com.gm.goldencity.domain.model.Search;
import com.gm.goldencity.fragment.search.SearchFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Name       : Gowtham
 * Created on : 17/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

public class SearchActivity extends BaseActivity {
    private static final String TAG = "SearchActivity";
    public static final String TAG_SEARCH_FRAGMENT = "search_fragment";

    @BindView(R.id.searchback)
    ImageButton searchBack;
    @BindView(R.id.searchback_container)
    ViewGroup searchBackContainer;
    @BindView(R.id.search_view)
    SearchView searchview;
    @BindView(R.id.search_background)
    View searchBackground;
    @BindView(R.id.search_toolbar)
    ViewGroup searchToolbar;

    CompositeDisposable subscriptions;
    private SearchFragment searchFragment;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        bundle = savedInstanceState;
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            if (intent.hasExtra(SearchManager.QUERY)) {
                String query = intent.getStringExtra(SearchManager.QUERY);
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
                suggestions.saveRecentQuery(query, null);
                if (!TextUtils.isEmpty(query)) {
                    searchview.setQuery(query, false);
                }
            }
        }


        initSearchFragment();
    }

    @Override
    public void onEnterAnimationComplete() {
        // focus the search view once the enter transition finishes
        searchview.requestFocus();
        ImeUtils.showIme(searchview);
    }

    private void initSearchFragment() {
        if (null == bundle) {
            searchFragment = SearchFragment.newInstance();
            attachFragments();
        } else {
            searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag(TAG_SEARCH_FRAGMENT);
        }
    }

    @Override
    protected void injectDependencies(GoldenCityApplication application, ApplicationComponent component) {
        component.inject(this);
    }

    @Override
    protected void releaseSubComponents(GoldenCityApplication application) {
        application.releaseSearchSubComponent();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null == subscriptions || subscriptions.isDisposed())
            subscriptions = new CompositeDisposable();

        subscriptions.addAll(
                searchFragment.characterObservable()
                        .subscribe(this::showCharacter),
                searchFragment.messageObservable()
                        .subscribe(this::showMessage),
                searchFragment.offlineObservable()
                        .subscribe(this::showOfflineMessage)

        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscriptions.dispose();
    }

    private void attachFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.search_fragment, searchFragment, TAG_SEARCH_FRAGMENT);
        fragmentTransaction.commitAllowingStateLoss();
    }


    public void showMessage(String message) {
        GLog.e("Showing Message: %s", message);

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showOfflineMessage(boolean isCritical) {

//        Snackbar.make(toolbar, R.string.offline_message, Snackbar.LENGTH_LONG)
//                .setAction(R.string.go_online, v -> startActivity(new Intent(
//                        Settings.ACTION_WIFI_SETTINGS)))
//                .setActionTextColor(Color.GREEN)
//                .show();
    }

    public void showCharacter(Search character) {
        //startActivity(CharacterActivity.newIntent(this, character));
    }

    @OnClick({R.id.scrim, R.id.searchback})
    protected void dismiss() {
        // clear the background else the touch ripple moves with the translation which looks bad
        searchBack.setBackground(null);
        finishAfterTransition();
    }

    public void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        // hint, inputType & ime options seem to be ignored from XML! Set in code
        searchview.setQueryHint(getString(R.string.search_hint));
        searchview.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchview.setImeOptions(searchview.getImeOptions() | EditorInfo.IME_ACTION_SEARCH |
                EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);

    }

    public SearchView getSearchview() {
        return searchview != null ? searchview : null;
    }


}
