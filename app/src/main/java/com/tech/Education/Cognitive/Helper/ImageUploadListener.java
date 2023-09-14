package com.tech.Education.Cognitive.Helper;

/**
 * Created by bodacious on 10/2/18.
 */

public interface ImageUploadListener {

    void onComplete(String imageUrl);
    void onError(String message);
}
