package ru.gdgkazan.popularmovies.model.content;

import java.util.ArrayList;
import java.util.List;


public class DataHolder {
    private List<Review> reviewList;
    private List<Video> videoList;
    private boolean hasError;

    public DataHolder(boolean hasError) {
        reviewList = new ArrayList<>();
        videoList = new ArrayList<>();
        this.hasError = false;
    }

    public DataHolder(List<Review> reviewList, List<Video> videoList) {
        this.reviewList = reviewList;
        this.videoList = videoList;
        hasError = false;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public boolean hasError() {
        return hasError;
    }

    public void setError(boolean hasError) {
        this.hasError = hasError;
    }
}
