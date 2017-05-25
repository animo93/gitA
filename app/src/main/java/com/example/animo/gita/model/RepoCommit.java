package com.example.animo.gita.model;

import com.example.animo.gita.model.Commit;

/**
 * Created by animo on 23/5/17.
 */

public class RepoCommit {
    private String sha;
    private Commit commit;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }
}
