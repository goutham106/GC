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

import com.gm.goldencity.domain.model.SearchResults;
import com.gm.goldencity.util.SchedulerProvider;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

/**
 * Name       : Gowtham
 * Created on : 21/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

class SearchPresenterImpl implements SearchPresenter {

    @Inject
    SearchInteractor interactor;

    private Searchview view;
    private Disposable subscription = Disposables.empty();
    private SchedulerProvider scheduler;

    @Inject
    public SearchPresenterImpl(SchedulerProvider scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void bind(Searchview view) {
        this.view = view;
    }

    @Override
    public void unbind() {
        if (subscription != null && !subscription.isDisposed())
            subscription.dispose();

        interactor.unbind();

        view = null;
    }

//    @Override
//    public void doSearch(boolean isConnected, SearchView searchView) {
//        if (null != view) {
//            view.showProgress();
//        }
//
//
////        subscription = interactor.loadCharacter(query)
////                // check if result code is OK
////                .map(charactersResponse -> {
////                    if (Constants.CODE_OK == charactersResponse.getCode())
////                        return charactersResponse;
////                    else
////                        throw Exceptions.propagate(new ApiResponseCodeException(charactersResponse.getCode(), charactersResponse.getStatus()));
////                })
////                // check if is there any result
////                .map(charactersResponse -> {
////                    if (charactersResponse.getData().getCount() > 0)
////                        return charactersResponse;
////                    else
////                        throw Exceptions.propagate(new NoSuchCharacterException());
////                })
////                // map CharacterResponse to CharacterModel
////                .map(Mapper::mapCharacterResponseToCharacter)
////                // cache data on database
////                .map(character -> {
////                    try {
////                        databaseHelper.addCharacter(character);
////                    } catch (SQLException e) {
////                        throw Exceptions.propagate(e);
////                    }
////
////                    return character;
////                })
////                .observeOn(scheduler.mainThread())
////                .subscribe(character -> {
////                            if (null != view) {
////                                view.hideProgress();
////                                view.showCharacter(character);
////
////                                if (!isConnected)
////                                    view.showOfflineMessage(false);
////                            }
////                        },
////                        // handle exceptions
////                        throwable -> {
////                            if (null != view) {
////                                view.hideProgress();
////
////                                if (isConnected) {
////                                    if (throwable instanceof ApiResponseCodeException)
////                                        view.showServiceError((ApiResponseCodeException) throwable);
////                                    else if (throwable instanceof NoSuchCharacterException)
////                                        view.showQueryNoResult();
////                                    else
////                                        view.showRetryMessage(throwable);
////
////                                } else {
////                                    view.showOfflineMessage(true);
////                                }
////                            }
////                        });
//
//
//
//
//    }

    @Override
    public Observable<SearchResults> searchSuggestion(String searchTerm) {
        if (null != view) {
            view.showProgress();
        }
        return interactor.loadCharacter(searchTerm);
    }

    @Override
    public Observable<SearchResults> fullSearch(String search) {
        if (null != view) {
            view.showProgress();
        }
        return interactor.loadCharacter(search);
    }

    @Override
    public void getObservableEnd(Boolean end) {
        if(end){
            if (null != view) {
                view.hideProgress();
            }
        }
    }

    @Override
    public boolean isQueryValid(String query) {
        return null != query && !query.isEmpty();
    }

    @Override
    public void loadCharactersCachedData() {

    }
}
