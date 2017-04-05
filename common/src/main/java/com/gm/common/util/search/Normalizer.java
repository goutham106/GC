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

package com.gm.common.util.search;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Name       : Gowtham
 * Created on : 20/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

public final class Normalizer {

    private static final Pattern PATTERN_DIACRITICS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    private static final Pattern PATTERN_NON_LETTER_DIGIT_TO_SPACES = Pattern.compile("[^\\p{L}\\p{Nd}]");

    private Normalizer() {
        //no instance
    }

    public static final String forSearch(CharSequence searchTerm) {
        if (searchTerm == null) {
            return null;
        }
        String result = java.text.Normalizer.normalize(searchTerm, java.text.Normalizer.Form.NFD);
        result = PATTERN_DIACRITICS.matcher(result).replaceAll("");
        result = PATTERN_NON_LETTER_DIGIT_TO_SPACES.matcher(result).replaceAll(" ");
        return result.toLowerCase(Locale.ROOT);
    }
}
