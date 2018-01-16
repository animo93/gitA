package com.example.animo.gita.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.animo.gita.Constants;
import com.example.animo.gita.MyApiInterface;
import com.example.animo.gita.MyApiResponse;
import com.example.animo.gita.MyAsyncTask;
import com.example.animo.gita.MyCall;
import com.example.animo.gita.MyCallBack;
import com.example.animo.gita.MyRequestBean;
import com.example.animo.gita.R;
import com.example.animo.gita.Utility;
import com.example.animo.gita.data.RepoContract;
import com.example.animo.gita.model.Config;
import com.example.animo.gita.model.RepoRegister;
import com.example.animo.gita.model.RepoRegisterOutput;
import com.example.animo.gita.model.Repository;
import com.example.animo.gita.model.WebHookRegister;
import com.example.animo.gita.retrofit.ApiClient;
import com.example.animo.gita.retrofit.ApiInterface;
import com.example.animo.gita.retrofit.NotificationClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by animo on 6/12/17.
 */

public class MarkFavouriteDialogFragment extends DialogFragment {

    public static final String LOG_TAG = MarkFavouriteDialogFragment.class.getSimpleName();

    List<Integer> mSelectedItems;
    Repository repo;
    Context mContext;

    public interface MarkFavouriteDialogListener {
        public void onMarkFavouritePositiveClick(DialogFragment dialog, List<Integer> mSelectedItems);
        public void onMarkFavouriteNegativeClick(DialogFragment dialog);
    }

    MarkFavouriteDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        Bundle bundle = getArguments();
        String repoJson = bundle.getString("repoJson");
        repo = new Gson().fromJson(repoJson,Repository.class);

        mSelectedItems = new ArrayList<>();
        final String accessToken = new Utility().getAccessToken(getContext());
        final String regToken = new Utility().getRegToken(getContext());
        Log.e(LOG_TAG,"Access Token "+accessToken);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //mListener.onMarkFavouritePositiveClick(MarkFavouriteDialogFragment.this,mSelectedItems);


            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //mListener.onMarkFavouriteNegativeClick(MarkFavouriteDialogFragment.this);
            }
        });
        builder.setMultiChoiceItems(R.array.favourite_events, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                        if(isChecked){
                            mSelectedItems.add(which);
                        }else{
                            mSelectedItems.remove(which);
                        }

                    }
                });


        return builder.create();
    }

    private void backendWebhookRegister(final String hookId, String regToken) {
        ApiInterface notifService = NotificationClient.createService(ApiInterface.class,null);
        Log.d(LOG_TAG,"Onwer "+repo.getOwner().getLogin()+" and repo "+repo.getName());

        MyApiInterface myApiInterface = MyApiResponse.createApi(MyApiInterface.class);
        MyCall<RepoRegister,RepoRegisterOutput> call = myApiInterface.markFavRepo(null,Constants.NOTIF_ROOT_URL+Constants.REGISTER_REPO,new RepoRegister(regToken,repo.getId().toString()));
        call.callMeNow(new MyCallBack<RepoRegister, RepoRegisterOutput>() {
            @Override
            public void callBackOnSuccess(MyCall<RepoRegister, RepoRegisterOutput> myCall) {
                Log.i(LOG_TAG,"inside onResponse");
                ContentValues repoValues = new ContentValues();
                //repoValues.put(RepoContract.FavRepos.COLUMN_ETAG,eTag);
                repoValues.put(RepoContract.FavRepos.COLUMN_REPO_ID,repo.getId());
                repoValues.put(RepoContract.FavRepos.COLUMN_REPO_OWNER,repo.getOwner().getLogin());
                repoValues.put(RepoContract.FavRepos.COLUMN_TITLE,repo.getName());
                repoValues.put(RepoContract.FavRepos.COLUMN_HOOK_ID,hookId);
                //repoValues.put(RepoContract.FavRepos.COLUMN_UPDATED_DATE,lastModified);

                if(myCall.getResponseCode() == 200){
                    Uri insertUri=mContext.getContentResolver().insert(RepoContract.FavRepos.CONTENT_URI,repoValues);
                    if(insertUri!=null){
                        Log.d(LOG_TAG,"Repo inserted and Uri is" +insertUri.toString());
                        dismiss();
                        new Utility().createToast(mContext,Constants.MARK_REPO_FAVOURITE,Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void callBackOnFailure(Exception e) {
                Log.e(LOG_TAG,"Could not insert favourite repos ",e);
                dismiss();
                new Utility().createToast(mContext,Constants.SERVER_UNREACHABLE,Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setCancelable(false);

        final String accessToken = new Utility().getAccessToken(getContext());
        final String regToken = new Utility().getRegToken(getContext());
        Log.e(LOG_TAG,"Access Token "+accessToken);
        AlertDialog alertDialog = (AlertDialog) getDialog();
        final Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectedItems!=null && mSelectedItems.size()>0){
                    final Type type = new TypeToken<List<WebHookRegister>>(){}.getType();
                    final MyApiInterface myInterface = MyApiResponse.createApi(MyApiInterface.class);
                    MyCall<Void,List<WebHookRegister>> myCall = myInterface.getAllHooks(accessToken,Constants.ROOT_URL+"/repos/"+repo.getOwner().getLogin()+"/"+repo.getName()+"/hooks");
                    myCall.callMeNow(new MyCallBack<Void, List<WebHookRegister>>() {
                        @Override
                        public void callBackOnSuccess(MyCall<Void, List<WebHookRegister>> myCall) {
                            if(myCall.getResponseCode() == 200){
                                Log.d(LOG_TAG,"response body "+myCall.getResponseBody());
                                Boolean webHookExists = false;
                                if(myCall.getResponseBody().size() > 0 ){
                                    for(final WebHookRegister hook : myCall.getResponseBody()) {
                                        if (hook.getConfig().getUrl().equals(Constants.NOTIF_ROOT_URL + Constants.WEBHOOK_URL)) {
                                            webHookExists=true;
                                            //Patch Existing hook
                                            Log.d(LOG_TAG,"id is "+hook.getId());
                                            MyApiInterface myService = MyApiResponse.createApi(MyApiInterface.class);
                                            MyCall<WebHookRegister, WebHookRegister> myCall2 = myService.patchHook(accessToken,Constants.ROOT_URL+"/repos/"+repo.getOwner().getLogin()+"/"+repo.getName()+"/hooks/"+hook.getId(),createWebHookPayload(mSelectedItems));
                                            myCall2.callMeNow(new MyCallBack<WebHookRegister, WebHookRegister>() {
                                                @Override
                                                public void callBackOnSuccess(MyCall<WebHookRegister, WebHookRegister> myCall) {
                                                    if(myCall.getResponseCode() == 200){
                                                        backendWebhookRegister(hook.getId(),regToken);

                                                    }
                                                }

                                                @Override
                                                public void callBackOnFailure(Exception e) {
                                                    Log.e(LOG_TAG,"Could not edit webhook"+e.getMessage());
                                                    dismiss();
                                                    //new Utility().createToast(mContext,Constants.SERVER_UNREACHABLE,Toast.LENGTH_LONG);
                                                }
                                            });
                                            break;

                                        }
                                    }
                                }
                                //Web hook doesn't exist ..Create new
                                if(!webHookExists){
                                    //No webhook exists , create a new webhook
                                    MyCall<WebHookRegister,WebHookRegister> call = myInterface.createWebHook(accessToken,Constants.ROOT_URL+"/repos/"+repo.getOwner().getLogin()+"/"+repo.getName()+"/hooks",createWebHookPayload(mSelectedItems));
                                    call.callMeNow(new MyCallBack<WebHookRegister, WebHookRegister>() {
                                        @Override
                                        public void callBackOnSuccess(MyCall<WebHookRegister, WebHookRegister> myCall) {
                                            Log.i(LOG_TAG, "inside onResponse of Webhook Callback");
                                            if (myCall.getResponseCode() == 201) {
                                                backendWebhookRegister(myCall.getResponseBody().getId(), regToken);
                                                dismiss();
                                            }
                                        }

                                        @Override
                                        public void callBackOnFailure(Exception e) {
                                            Log.e(LOG_TAG, "Could not register WebHooks", e);
                                            dismiss();
                                            new Utility().createToast(mContext, Constants.SERVER_UNREACHABLE, Toast.LENGTH_LONG);
                                        }
                                    });
                                }
                            }

                        }

                        @Override
                        public void callBackOnFailure(Exception e) {
                            Log.e(LOG_TAG,"Unable to fetch repo List "+e.getMessage());
                            dismiss();
                            new Utility().createToast(mContext,Constants.SERVER_UNREACHABLE,Toast.LENGTH_LONG);
                        }
                    });
                }
            }
        });
    }

    private WebHookRegister createWebHookPayload(List<Integer> mSelectedItems) {
        WebHookRegister webHookRegister = new WebHookRegister();
        webHookRegister.setActive(true);
        webHookRegister.setName("web");
        Config config = new Config();
        config.setContent_type("json");
        config.setUrl(Constants.NOTIF_ROOT_URL+Constants.WEBHOOK_URL);
        webHookRegister.setConfig(config);
        List<String> eventList = new ArrayList<>();
        String[] events = mContext.getResources().getStringArray(R.array.favourite_events);
        for(Integer i:mSelectedItems){
            eventList.add(events[i]);
        }
        //eventList.add("push");
        webHookRegister.setEvents(eventList);

        return webHookRegister;
    }
}
