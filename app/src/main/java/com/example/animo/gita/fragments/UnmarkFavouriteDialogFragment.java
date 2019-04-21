package com.example.animo.gita.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.animo.gita.Constants;
import com.example.animo.gita.MyApiInterface;
import com.example.animo.gita.MyApiResponse;
import com.example.animo.gita.MyCall;
import com.example.animo.gita.MyCallBack;
import com.example.animo.gita.R;
import com.example.animo.gita.Utility;
import com.example.animo.gita.data.RepoContract;
import com.example.animo.gita.model.RepoRegister;
import com.example.animo.gita.model.RepoRegisterOutput;
import com.example.animo.gita.model.Repository;
import com.google.gson.Gson;

/**
 * Created by animo on 6/12/17.
 */

public class UnmarkFavouriteDialogFragment extends DialogFragment {

    public static final String LOG_TAG = UnmarkFavouriteDialogFragment.class.getSimpleName();

    public interface UnmarkFavouriteDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    UnmarkFavouriteDialogListener mListener;
    Repository repo;
    Context mContext;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        Bundle bundle = getArguments();
        String repoJson = bundle.getString("repoJson");
        repo = new Gson().fromJson(repoJson,Repository.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.unmark_dialog_title)
                .setMessage(R.string.unmark_favourite_message);
        builder.setCancelable(false);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Send the event to host activity
                //mListener.onDialogNegativeClick(UnmarkFavouriteDialogFragment.this);
            }
        });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCancelable(false);
        final String accessToken = new Utility().getAccessToken(getContext());
        final String regToken = new Utility().getRegToken(getContext());
        AlertDialog alertDialog = (AlertDialog) getDialog();
        final Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MyApiInterface myService = MyApiResponse.createApi(MyApiInterface.class);
                String hookId = getHookIdFromRepo(repo);
                if(hookId!=null){
                    // Make Api Call to github to delete hook

                    MyCall<Void,Void> myCall = myService.deleteWebHook(accessToken,Constants.ROOT_URL+"/repos/"+repo.getOwner().getLogin()+"/"+repo.getName()+"/hooks/"+hookId);
                    myCall.callMeNow(new MyCallBack<Void, Void>() {
                        @Override
                        public void callBackOnSuccess(MyCall<Void, Void> myCall) {
                            //Log.i(LOG_TAG,"inside onResponse of WebhookCallback");

                            //if response code is deleted or not found
                            if(myCall.getResponseCode() == 204 || myCall.getResponseCode() == 404){
                                //Make Api Call to backend server to delete entry
                                MyCall<RepoRegister,RepoRegisterOutput> call2 = myService.unmarkFavRepo(null,Constants.NOTIF_ROOT_URL+Constants.DEREGISTER_REPO,new RepoRegister(regToken,repo.getId().toString()));
                                call2.callMeNow(new MyCallBack<RepoRegister, RepoRegisterOutput>() {
                                    @Override
                                    public void callBackOnSuccess(MyCall<RepoRegister, RepoRegisterOutput> myCall) {
                                        int deletedRows = mContext.getContentResolver().delete(
                                                RepoContract.FavRepos.CONTENT_URI,
                                                RepoContract.FavRepos.COLUMN_REPO_ID + "=?",
                                                new String[]{repo.getId().toString()}
                                        );
                                        //Log.d(LOG_TAG,"deleted Rows "+deletedRows);
                                        if(deletedRows>0){
                                            dismiss();
                                            new Utility().createToast(mContext,Constants.UNMARK_REPO_FAVOURITE_SUCCESS,Toast.LENGTH_LONG);
                                        }
                                    }

                                    @Override
                                    public void callBackOnFailure(Exception e) {
                                        //Log.e(LOG_TAG,"unable to unmark repo",e);
                                        dismiss();
                                        new Utility().createToast(mContext,Constants.SERVER_UNREACHABLE,Toast.LENGTH_LONG);

                                    }
                                });
                            }
                        }

                        @Override
                        public void callBackOnFailure(Exception e) {
                            //Log.e(LOG_TAG,"unable to unmark repo",e);
                            dismiss();
                            new Utility().createToast(mContext,Constants.SERVER_UNREACHABLE,Toast.LENGTH_LONG);
                        }
                    });
                }
            }
        });
    }

    private String getHookIdFromRepo(Repository repo) {
        Cursor repoCursor = getContext().getContentResolver().query(
                RepoContract.FavRepos.buildRepoUriFromId(String.valueOf(repo.getId())),
                RepoContract.FavRepos.REPO_COLUMNS,
                null,
                null,
                null
        );
        if(repoCursor.moveToFirst()){
            String hookId=repoCursor.getString(RepoContract.FavRepos.COL_HOOK_ID);
            repoCursor.close();
            return hookId;
        }
        return null;
    }

}
