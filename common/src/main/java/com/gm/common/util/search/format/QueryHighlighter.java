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

package com.gm.common.util.search.format;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.gm.common.util.search.Normalizer;

import java.util.Locale;
import java.util.Objects;

/**
 * Name       : Gowtham
 * Created on : 20/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

public final class QueryHighlighter {

    public enum Mode {
        CHARACTERS, WORDS
    }

    public static abstract class QueryNormalizer {

        public static final QueryNormalizer FOR_SEARCH = new QueryNormalizer() {
            @Override
            public CharSequence normalize(CharSequence source) {
                return Normalizer.forSearch(source);
            }
        };

        public static final QueryNormalizer CASE = new QueryNormalizer() {
            @Override
            public CharSequence normalize(CharSequence source) {
                if (TextUtils.isEmpty(source)) {
                    return source;
                }
                return source.toString().toUpperCase(Locale.ROOT);
            }
        };

        public static final QueryNormalizer NONE = new QueryNormalizer() {
            @Override
            public CharSequence normalize(CharSequence source) {
                return source;
            }
        };

        public abstract CharSequence normalize(CharSequence source);
    }

    private CharacterStyle mHighlightStyle = new StyleSpan(Typeface.BOLD);
    private QueryNormalizer mQueryNormalizer = QueryNormalizer.NONE;
    private Mode mMode = Mode.WORDS;

    public QueryHighlighter setHighlightStyle(CharacterStyle highlightStyle) {
        mHighlightStyle = Objects.requireNonNull(highlightStyle, "highlightStyle cannot be null");
        return this;
    }

    public QueryHighlighter setQueryNormalizer(QueryNormalizer queryNormalizer) {
        mQueryNormalizer = Objects.requireNonNull(queryNormalizer, "queryNormalizer cannot be null");
        return this;
    }

    public QueryHighlighter setMode(Mode mode) {
        mMode = Objects.requireNonNull(mode, "mode cannot be null");
        return this;
    }

    public CharSequence apply(CharSequence text, CharSequence wordPrefix) {
        final CharSequence normalizedText = mQueryNormalizer.normalize(text);
        final CharSequence normalizedWordPrefix = mQueryNormalizer.normalize(wordPrefix);

        final int index = indexOfQuery(normalizedText, normalizedWordPrefix);
        if (index != -1) {
            final SpannableString result = new SpannableString(text);
            result.setSpan(mHighlightStyle, index, index + normalizedWordPrefix.length(), 0);
            return result;
        } else {
            return text;
        }
    }

    public void setText(TextView view, CharSequence text, CharSequence query) {
        view.setText(apply(text, query));
    }

    private int indexOfQuery(CharSequence text, CharSequence query) {
        if (query == null || text == null) {
            return -1;
        }

        final int textLength = text.length();
        final int queryLength = query.length();

        if (queryLength == 0 || textLength < queryLength) {
            return -1;
        }

        for (int i = 0; i <= textLength - queryLength; i++) {
            // Only match word prefixes
            if (mMode == Mode.WORDS && i > 0 && text.charAt(i - 1) != ' ') {
                continue;
            }

            int j;
            for (j = 0; j < queryLength; j++) {
                if (text.charAt(i + j) != query.charAt(j)) {
                    break;
                }
            }
            if (j == queryLength) {
                return i;
            }
        }

        return -1;
    }


}
