package ru.gdgkazan.popularmovies.network;

import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.gdgkazan.popularmovies.model.response.MoviesResponse;
import ru.gdgkazan.popularmovies.model.response.ReviewsResponse;
import ru.gdgkazan.popularmovies.model.response.VideosResponse;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public interface MovieService {

    @GET("popular/")
    Observable<MoviesResponse> popularMovies();

    @GET("{movie_id}/reviews")
    Observable<ReviewsResponse> movieReviews(@Path("movie_id") int movie_id);

    @GET("{movie_id}/videos")
    Observable<VideosResponse> movieVideos(@Path("movie_id") int movie_id);

}
