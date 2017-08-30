package ru.gdgkazan.popularmovies.screen.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.gdgkazan.popularmovies.R;
import ru.gdgkazan.popularmovies.model.content.DataHolder;
import ru.gdgkazan.popularmovies.model.content.Movie;
import ru.gdgkazan.popularmovies.model.content.Review;
import ru.gdgkazan.popularmovies.model.content.Video;
import ru.gdgkazan.popularmovies.screen.loading.LoadingDialog;
import ru.gdgkazan.popularmovies.screen.loading.LoadingView;
import ru.gdgkazan.popularmovies.utils.Images;
import rx.Subscription;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String MAXIMUM_RATING = "10";

    public static final String IMAGE = "image";
    public static final String EXTRA_MOVIE = "extraMovie";
    private static final int LOADER_ID = 1;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.image)
    ImageView mImage;

    @BindView(R.id.title)
    TextView mTitleTextView;

    @BindView(R.id.overview)
    TextView mOverviewTextView;

    @BindView(R.id.rating)
    TextView mRatingTextView;

    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;

    @BindView(R.id.rvVideos)
    RecyclerView rvVideos;

    private LoadingView loadingView;
    private ReviewsAdapter reviewAdapter = new ReviewsAdapter();
    private VideosAdapter videoAdapter = new VideosAdapter();
    private Movie movie;

    public static void navigate(@NonNull AppCompatActivity activity, @NonNull View transitionImage,
                                @NonNull Movie movie) {
        Intent intent = new Intent(activity, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, IMAGE);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareWindowForAnimation();
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar), IMAGE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));
        rvVideos.setAdapter(videoAdapter);
        loadingView = LoadingDialog.view(getSupportFragmentManager());

         movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        showMovie(movie);
        loadingView.showLoadingIndicator();

        LoaderManager.LoaderCallbacks<DataHolder> callbacks = new DetailsCallback();
        getSupportLoaderManager().initLoader(LOADER_ID, Bundle.EMPTY, callbacks);


        /*Observable<List<Review>> reviewObservable = ApiFactory.getMoviesService()
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

        moviesSubscription = Observable.combineLatest(reviewObservable, videoObservable, (reviews, videos) -> new DataHolder(reviews, videos))
                .doOnSubscribe(loadingView::showLoadingIndicator)
                .doAfterTerminate(loadingView::hideLoadingIndicator)
                .cache()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dataHolder -> {
                    showReviews(dataHolder.getReviewList());
                    showTrailers(dataHolder.getVideoList());
                }, throwable -> showError());
*/
        /**
         * TODO : task
         *
         * Load movie trailers and reviews and display them
         *
         * 1) See http://docs.themoviedb.apiary.io/#reference/movies/movieidtranslations/get?console=1
         * http://docs.themoviedb.apiary.io/#reference/movies/movieidtranslations/get?console=1
         * for API documentation
         *
         * 2) Add requests to {@link ru.gdgkazan.popularmovies.network.MovieService} for trailers and videos
         *
         * 3) Execute requests in parallel and show loading progress until both of them are finished
         *
         * 4) Save trailers and videos to Realm and use cached version when error occurred
         *
         * 5) Handle lifecycle changes any way you like
         */
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareWindowForAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void showMovie(@NonNull Movie movie) {
        String title = getString(R.string.movie_details);
        mCollapsingToolbar.setTitle(title);
        mCollapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        Images.loadMovie(mImage, movie, Images.WIDTH_780);

        String year = movie.getReleasedDate().substring(0, 4);
        mTitleTextView.setText(getString(R.string.movie_title, movie.getTitle(), year));
        mOverviewTextView.setText(movie.getOverview());

        String average = String.valueOf(movie.getVoteAverage());
        average = average.length() > 3 ? average.substring(0, 3) : average;
        average = average.length() == 3 && average.charAt(2) == '0' ? average.substring(0, 1) : average;
        mRatingTextView.setText(getString(R.string.rating, average, MAXIMUM_RATING));
    }

    private void showTrailers(@NonNull List<Video> videos) {
        videoAdapter.setData(videos);
    }

    private void showReviews(@NonNull List<Review> reviews) {
        reviewAdapter.setData(reviews);
    }

    private void showError() {
        Toast.makeText(this, "Can`t load data", Toast.LENGTH_SHORT).show();
    }

    private class DetailsCallback implements LoaderManager.LoaderCallbacks<DataHolder> {


        @Override
        public Loader<DataHolder> onCreateLoader(int id, Bundle args) {
            return new DetailsLoader(MovieDetailsActivity.this,movie);
        }

        @Override
        public void onLoadFinished(Loader<DataHolder> loader, DataHolder data) {
            loadingView.hideLoadingIndicator();
            if(!data.hasError()) {
                showTrailers(data.getVideoList());
                showReviews(data.getReviewList());
            }else{
                showError();
            }
        }

        @Override
        public void onLoaderReset(Loader<DataHolder> loader) {

        }
    }
}
