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

package com.gm.goldencity.fragment.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.gm.common.ui.transition.CircularReveal;
import com.gm.common.util.ImeUtils;
import com.gm.common.util.TransitionUtils;
import com.gm.glog.library.GLog;
import com.gm.goldencity.GoldenCityApplication;
import com.gm.goldencity.R;
import com.gm.goldencity.activity.search.SearchActivity;
import com.gm.goldencity.base.basic.BaseFragment;
import com.gm.goldencity.domain.model.Search;
import com.gm.goldencity.domain.model.SearchResults;
import com.gm.goldencity.util.SchedulerProvider;
import com.gm.grecyclerview.GmRecyclerView;
import com.jakewharton.rxbinding2.widget.RxSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static android.content.Context.SEARCH_SERVICE;

/**
 * Name       : Gowtham
 * Created on : 21/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

public class SearchFragment extends BaseFragment implements Searchview {
    private static final String TAG = "SearchFragment";

    @Inject
    Context context;
    @Inject
    Resources resources;
    @Inject
    SearchPresenter presenter;
    @Inject
    SchedulerProvider scheduler;

    @BindView(android.R.id.empty)
    ProgressBar progress;
    @BindView(R.id.search_results)
    GmRecyclerView results;
    @BindView(R.id.container)
    ViewGroup container;
    @BindView(R.id.results_container)
    ViewGroup resultsContainer;

    PublishSubject<Search> notifyCharacter = PublishSubject.create();
    PublishSubject<String> notifyMessage = PublishSubject.create();
    PublishSubject<Boolean> notifyOffline = PublishSubject.create();

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter.bind(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        ((SearchActivity)getActivity()).setupSearchView();
        doSearch(((SearchActivity)getActivity()).getSearchview());
        setupTransitions();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    protected void injectDependencies(GoldenCityApplication application) {
        application.getSearchSubComponent().inject(this);
    }




    private void doSearch(SearchView searchView) {

        RxSearchView.queryTextChangeEvents(searchView)
                .skip(1)
                .doOnNext(charSequence -> GLog.e("Search", "Searching: " + charSequence))
                .throttleLast(100, TimeUnit.MILLISECONDS)
                .debounce(300, TimeUnit.MILLISECONDS)
                // .onBackpressureLatest()
                .observeOn(scheduler.mainThread())
                .filter(searchViewQueryTextEvent -> {
                    final boolean empty = TextUtils.isEmpty(searchViewQueryTextEvent.queryText());
                    if (empty) {
                        GLog.e("Search", "empty view");
                        clearAdapter();
                    }
                    return !empty;
                })
                .switchMap(searchViewQueryTextEvent -> {
                    GLog.e("Search", "requesting " + searchViewQueryTextEvent.queryText().toString());
                    String searchTerm = searchViewQueryTextEvent.queryText().toString();
                    boolean submitted = searchViewQueryTextEvent.isSubmitted();
                    //Show Loading and Hide RecyclerView
                    Observable<SearchResults> observable;
                    if (submitted) {
                        observable = presenter.fullSearch(searchTerm);
                    } else {
                        if (TextUtils.isEmpty(searchTerm)) {
                            clearResults();
                        }
                        observable = presenter.searchSuggestion(searchTerm);
                    }
                    //Hide Loading and show RecyclerView
                    return observable.compose(scheduler.normalSchedulers())
                            .doOnComplete(this::showContent);
                })
                .doOnNext(charSequence -> GLog.e("Search", "got data"))
                .subscribe(this::showSearchResponse
                        , throwable -> {
                            throwable.printStackTrace();
                            showEmptyErrorView(throwable.getMessage());
                        });


        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                return true;
            }
        });

    }


    private void showContent() {
        presenter.getObservableEnd(true);
    }

    private Observable<SearchResults> getFullSearch(String searchTerm) {
        return null;
    }

    private void clearAdapter() {

    }

    public void showSearchResponse(SearchResults list) {
        // adapter.swap(list);
        GLog.e(TAG, " " + list.getSearch());
    }

    private void showEmptyErrorView(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    void clearResults() {
        results.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
    }

    @Override
    public void showOfflineMessage(boolean isCritical) {

    }

    @Override
    public void showQueryError(Throwable throwable) {

    }

    @Override
    public void showCharacter(Search character) {

    }

    @Override
    public void showRetryMessage(Throwable throwable) {

    }

    @Override
    public void showQueryNoResult() {

    }

    @Override
    public void setCharactersCachedData(List<Search> characters) {

    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showServiceError(ApiResponseCodeException throwable) {

    }

    public Observable<Search> characterObservable() {
        return notifyCharacter.hide();
    }

    public Observable<String> messageObservable() {
        return notifyMessage.hide();
    }

    public Observable<Boolean> offlineObservable() {
        return notifyOffline.hide();
    }


    private void setupTransitions() {
        // grab the position that the search icon transitions in *from*
        // & use it to configure the return transition
        setEnterSharedElementCallback(new android.support.v4.app.SharedElementCallback() {
            @Override
            public void onSharedElementStart(
                    List<String> sharedElementNames,
                    List<View> sharedElements,
                    List<View> sharedElementSnapshots) {
                if (sharedElements != null && !sharedElements.isEmpty()) {
                    View searchIcon = sharedElements.get(0);
                    if (searchIcon.getId() != R.id.searchback) return;
                    int centerX = (searchIcon.getLeft() + searchIcon.getRight()) / 2;
                    CircularReveal hideResults = (CircularReveal) TransitionUtils.findTransition(
                            (TransitionSet) getActivity().getWindow().getReturnTransition(),
                            CircularReveal.class,
                            R.id.results_container);
                    if (hideResults != null) {
                        hideResults.setCenter(new Point(centerX, 0));
                    }
                }
            }
        });
    }

}
