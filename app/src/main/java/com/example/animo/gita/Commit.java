package com.example.animo.gita;

/**
 * Created by animo on 23/5/17.
 */

public class Commit {

    private Committer committer;
    private String message;

    public Committer getCommitter() {
        return committer;
    }

    public void setCommitter(Committer committer) {
        this.committer = committer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
