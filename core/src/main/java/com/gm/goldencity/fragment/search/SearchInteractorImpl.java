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

import com.gm.goldencity.domain.client.TestApi;
import com.gm.goldencity.domain.model.SearchResults;
import com.gm.goldencity.util.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.ReplaySubject;


/**
 * Name       : Gowtham
 * Created on : 21/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

@Search
class SearchInteractorImpl implements SearchInteractor {

    private TestApi api;
    private SchedulerProvider scheduler;

    private ReplaySubject<SearchResults> characterSubject;
    private Disposable characterSubscription;

    @Inject
    SearchInteractorImpl(TestApi api, SchedulerProvider scheduler) {
        this.api = api;
        this.scheduler = scheduler;
    }

    @Override
    public Observable<SearchResults> loadCharacter(String query) {
        if (characterSubscription == null || characterSubscription.isDisposed()) {
            characterSubject = ReplaySubject.create();

            api.getSearchResults(query, "short", "", "json")
                    .subscribeOn(scheduler.backgroundThread())
                    .subscribe(characterSubject);
        }

        return characterSubject.hide();
    }


    @Override
    public void unbind() {
        if (characterSubscription != null && !characterSubscription.isDisposed())
            characterSubscription.dispose();
    }

}

