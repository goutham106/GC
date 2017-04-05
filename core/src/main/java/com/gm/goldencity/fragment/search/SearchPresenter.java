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

import com.gm.goldencity.base.BasePresenter;
import com.gm.goldencity.domain.model.SearchResults;

import io.reactivex.Observable;


/**
 * Name       : Gowtham
 * Created on : 21/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

public interface SearchPresenter extends BasePresenter<Searchview> {

    Observable<SearchResults> searchSuggestion(String searchTerm);

    Observable<SearchResults> fullSearch(String search);

    void getObservableEnd(Boolean end);

    boolean isQueryValid(String query);

    void loadCharactersCachedData();
}
