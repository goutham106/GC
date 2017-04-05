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

package com.gm.common.util.glide;

import android.graphics.Bitmap;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;

/**
 * Name       : Gowtham
 * Created on : 10/3/17.
 * Email      : goutham.gm11@gmail.com
 * GitHub     : https://github.com/goutham106
 */

public class GlideUtils {

    private GlideUtils() {
    }

    public static Bitmap getBitmap(GlideDrawable glideDrawable) {
        if (glideDrawable instanceof GlideBitmapDrawable) {
            return ((GlideBitmapDrawable) glideDrawable).getBitmap();
        } else if (glideDrawable instanceof GifDrawable) {
            return ((GifDrawable) glideDrawable).getFirstFrame();
        }
        return null;
    }
}