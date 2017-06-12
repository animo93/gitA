package com.example.animo.gita.retrofit;

import com.example.animo.gita.Constants;
import com.example.animo.gita.model.Commit;
import com.example.animo.gita.model.Event;
import com.example.animo.gita.model.Files;
import com.example.animo.gita.model.RepoCommit;
import com.example.animo.gita.model.Repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by animo on 11/5/17.
 */

public interface ApiInterface {

    @GET(Constants.REPOSITORIES)
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


}
