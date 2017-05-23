package com.example.animo.gita;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by animo on 11/5/17.
 */

public interface ApiInterface {

    @GET(Constants.REPOSITORIES)
    Call<List<Repository>> getRepos();

    @GET(Constants.COMMITS)
    Call<List<RepoCommit>> getCommits(@Path("user") String userId, @Path("repo") String repoName);


}
