package ru.gdgkazan.popularmovies.screen.details;

import android.content.Context;
import android.support.v4.content.Loader;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.gdgkazan.popularmovies.model.content.DataHolder;
import ru.gdgkazan.popularmovies.model.content.Movie;
import ru.gdgkazan.popularmovies.model.content.Review;
import ru.gdgkazan.popularmovies.model.content.Video;
import ru.gdgkazan.popularmovies.model.response.ReviewsResponse;
import ru.gdgkazan.popularmovies.model.response.VideosResponse;
import ru.gdgkazan.popularmovies.network.ApiFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DetailsLoader extends Loader<DataHolder> {
    private Movie movie;
    private Subscription subscription;

    public DetailsLoader(Context context, Movie movie) {
        super(context);
        this.movie = movie;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        loadData();

    }

    @Override
    protected void onReset() {
        super.onReset();
        subscription.unsubscribe();
    }

    private void loadData() {
        Observable<List<Review>> reviewObservable = ApiFactory.getMoviesService()
                .movieReviews(movie.getId())
                .map(ReviewsResponse::getReviews)
                .flatMap(reviews -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(Review.class);
                        realm.insert(reviews);
                    });
                    return Observable.just(reviews);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Review> results = realm.where(Review.class).findAll();
                    return Observable.just(realm.copyFromRealm(results));
                });

        Observable<List<Video>> videoObservable = ApiFactory.getMoviesService()
                .movieVideos(movie.getId())
                .map(VideosResponse::getVideos)
                .flatMap(videos -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(Video.class);
                        realm.insert(videos);
                    });
                    return Observable.just(videos);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Video> results = realm.where(Video.class).findAll();
                    return Observable.just(realm.copyFromRealm(results));
                });

        subscription = Observable.combineLatest(reviewObservable, videoObservable, DataHolder::new)
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::deliverResult,
                        throwable -> deliverResult(new DataHolder(true)));
    }


}
