package com.example.animo.gita.model;

import java.util.List;

/**
 * Created by animo on 23/5/17.
 */

public class Commit {

    private Committer committer;
    private String message;

    public List<Files> getFiles() {
        return files;
    }

    public void setFiles(List<Files> files) {
        this.files = files;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

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

    private List<Files> files;

    private Stats stats;
}