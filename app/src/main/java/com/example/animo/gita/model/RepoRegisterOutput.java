package com.example.animo.gita.model;

/**
 * Created by animo on 27/10/17.
 */

public class RepoRegisterOutput {
    private String success;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
