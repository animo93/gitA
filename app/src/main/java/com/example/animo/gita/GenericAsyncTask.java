package com.example.animo.gita;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by animo on 10/5/17.
 */

public class GenericAsyncTask extends AsyncTask<String,Void,List<Object>> {

    AsyncProcessOutput processOutput;

    public GenericAsyncTask(AsyncProcessOutput processOutput) {
        this.processOutput = processOutput;
    }

    @Override
    protected void onPostExecute(List<Object> objects) {
        super.onPostExecute(objects);
        processOutput.onComplete(objects);

    }

    @Override
    protected List doInBackground(String... strings) {
        return null;
    }
}
