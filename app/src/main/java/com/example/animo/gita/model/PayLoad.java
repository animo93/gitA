package com.example.animo.gita.model;

import java.util.List;

/**
 * Created by animo on 9/6/17.
 */

public class PayLoad {
    private int size;
    private List<RepoCommit> commits;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<RepoCommit> getCommits() {
        return commits;
    }

    public void setCommits(List<RepoCommit> commits) {
        this.commits = commits;
    }
}
