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

package com.gm.goldencity.domain.client;

import com.gm.goldencity.domain.model.SearchResults;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Name       : Gowtham
 * Created on : 28/2/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */
public interface TestApi {

    @GET("/")
    Observable<SearchResults> getSearchResults(@Query("s") String query,
                                               @Query("plot") String plot,
                                               @Query("type") String type,
                                               @Query("r") String format);
}
