package ru.gdgkazan.popularmovies.model.content;

import java.util.List;


public class DataHolder {
    private List<Review> reviewList;
    private List<Video> videoList;

    public DataHolder(List<Review> reviewList, List<Video> videoList) {
        this.reviewList = reviewList;
        this.videoList = videoList;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public List<Video> getVideoList() {
        return videoList;
    }
}
