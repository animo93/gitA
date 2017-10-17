package com.example.animo.gita.retrofit;

import com.example.animo.gita.Constants;
import com.example.animo.gita.model.Access;
import com.example.animo.gita.model.Commit;
import com.example.animo.gita.model.Event;
import com.example.animo.gita.model.Files;
import com.example.animo.gita.model.RepoCommit;
import com.example.animo.gita.model.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by animo on 11/5/17.
 */

public interface ApiInterface {

    @GET(Constants.REPOSITORIES)
    @Headers("Accept: application/json")
    Call<List<Repository>> getRepos();

    @GET(Constants.COMMITS)
    Call<List<RepoCommit>> getCommits(@Path("user") String userId, @Path("repo") String repoName);

    @GET(Constants.CONTENTS)
    Call<List<Files>> getContents(@Path("user") String userId, @Path("repo") String repoName, @Path("path") String path);

    @GET(Constants.COMMIT)
    Call<Commit> getCommit(@Path("user") String userId,
                             @Path("repo") String repoName,
                             @Path("sha") String sha);


    @GET(Constants.EVENTS)
    Call<List<Event>> getEventStatus(@Path("user") String userId,
                               @Path("repo") String repoName,
                               @Header("If-None-Match") String Etag);

    @GET(Constants.EVENTS)
    Call<List<Event>> getEventTag(@Path("user") String userId,@Path("repo") String repoName);

    @POST(Constants.ACCESS_TOKEN)
    @FormUrlEncoded
    @Headers("Accept: application/json")
    Call<Access> getAccessToken(@Field("code") String code,
                                @Field("client_id") String clientId,
                                @Field("client_secret") String clientSecret);

    @GET(Constants.REPO_DETAIL)
    Call<Repository> getRepoDetail(@Path("user") String user,
                                   @Path("repo") String repo);




}
