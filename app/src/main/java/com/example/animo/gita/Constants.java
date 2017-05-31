package com.example.animo.gita;

import android.content.res.Resources;

/**
 * Created by animo on 10/5/17.
 */

public class Constants {


    //public static final String AUTH_TOKEN= Resources.getSystem().getString(android.R.string.unknownName);

    public static final String ROOT_URL="https://api.github.com";

    public static final String USER="/user";

    public static final String REPOSITORIES = USER+"/repos";

    public static final String COMMITS = "/repos/{user}/{repo}/commits";

    public static final String REPO = "Repos";
    public static final String OWNER = "Owner";
    public static final String URL = "Url";

    public static final String CONTENTS = "/repos/{user}/{repo}/contents/{path}";
    public static final String PATH = "path";
    public static final String REPOS = "repositoryList";
    public static final String TITLE = "Title";
    public static final String SOURCE = "Source";
    public static final String DESC = "Description";
    public static final String LANG = "Language";
    public static final String ISSUE_COUNT = "issuesCount";
    public static final String STARGAZERS = "stargazers";
    public static final String FORKS = "forksCount";

    public static final String COMMIT = COMMITS+"/{sha}";
    public static final String SHA = "sha";
}
