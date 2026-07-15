package com.mohit.moviedbmachine.data.remote

import com.mohit.moviedbmachine.data.dto.movie.MovieResponse
import com.mohit.moviedbmachine.data.dto.detail.MovieDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
//    curl --request GET \
//    --url 'https://api.themoviedb.org/3/movie/popular?language=en-US&page=1' \
//    --header 'Authorization: Bearer ••••' \
//    --header 'accept: application/json'
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language : String = "en-US",
        @Query("page") page : Int
    ): MovieResponse

//    curl --request GET \
//    --url 'https://api.themoviedb.org/3/search/movie?query=jungle&include_adult=false&language=en-US&page=1' \
//    --header 'Authorization: Bearer ••••' \
//    --header 'accept: application/json'

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") includeAdult : Boolean = false,
        @Query("language") language : String = "en-US",
        @Query("page") page : Int
    ): MovieResponse


//    curl --request GET \
//    --url 'https://api.themoviedb.org/3/movie/1339713?language=en-US' \
//    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiZGE5NTQ3ZTcxZGU3NzEwNTE1ZDcxZmNmY2FmYTA0MSIsIm5iZiI6MTY5NjQ5MDMxOC43MDIwMDAxLCJzdWIiOiI2NTFlNjM0ZWVhODRjNzAwZWI5YmM0ZTgiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.XeA-Ma40ANDj9DmLb1fctN7afdbSc-iGCG8DEwcNZjE' \
//    --header 'accept: application/json'

    @GET("movie/{movieId}")
    suspend fun getMovieDetail(
        @Path("movieId") movieId: Int,
        @Query("language") language : String = "en-US",
        ): MovieDetailsResponse










}
