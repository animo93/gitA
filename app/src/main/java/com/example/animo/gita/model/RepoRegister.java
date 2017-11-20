package com.example.animo.gita.model;

/**
 * Created by animo on 24/10/17.
 */

public class RepoRegister {

    private String reg_token;
    private String repo_id;

    public RepoRegister(String reg_token, String repo_id) {
        this.reg_token = reg_token;
        this.repo_id = repo_id;
    }

    public String getReg_token() {
        return reg_token;
    }

    public void setReg_token(String reg_token) {
        this.reg_token = reg_token;
    }

    public String getRepo_id() {
        return repo_id;
    }

    public void setRepo_id(String repo_id) {
        this.repo_id = repo_id;
    }
}
