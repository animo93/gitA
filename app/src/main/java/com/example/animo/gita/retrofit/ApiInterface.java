package com.example.animo.gita.retrofit;

import com.example.animo.gita.Constants;
import com.example.animo.gita.model.Access;
import com.example.animo.gita.model.Commit;
import com.example.animo.gita.model.Event;
import com.example.animo.gita.model.Files;
import com.example.animo.gita.model.RepoCommit;
import com.example.animo.gita.model.RepoRegister;
import com.example.animo.gita.model.RepoRegisterOutput;
import com.example.animo.gita.model.Repository;
import com.example.animo.gita.model.WebHookRegister;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
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

    @POST(Constants.REGISTER_REPO)
    @Headers("Accept: application/json")
    Call<RepoRegisterOutput> registerFavRepo(@Body RepoRegister repoRegister);

    @POST(Constants.REGISTER_WEBHOOK)
    @Headers("Accept: application/json")
    Call<WebHookRegister> registerWebHook(@Path("user") String user,
                                          @Path("repo") String repo,
                                          @Body WebHookRegister webHookRegister);

    @DELETE(Constants.DEREGISTER_WEBHOOK)
    @Headers("Accept: application/json")
    Call<Void> deleteWebHook(@Path("user") String user,
                             @Path("repo") String repo,
                             @Path("id") String id);

    @POST(Constants.DEREGISTER_REPO)
    @Headers("Accept: application/json")
    Call<RepoRegisterOutput> deRegisterFavRepo(@Body RepoRegister repoRegister);

    @GET(Constants.REGISTER_WEBHOOK)
    @Headers("Accept: application/json")
    Call<List<WebHookRegister>> listFavRepos(@Path("user") String user,
                                             @Path("repo") String repo);

    @PATCH(Constants.PATCH_WEBHOOK)
    @Headers("Accept: application/json")
    Call<WebHookRegister> editWebHook(@Path("user") String user,
                                      @Path("repo") String repo,
                                      @Path("id") String id,
                                      @Body WebHookRegister webHookRegister);


}
