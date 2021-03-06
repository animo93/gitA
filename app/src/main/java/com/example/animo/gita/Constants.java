package com.example.animo.gita;

/**
 * Created by animo on 10/5/17.
 */

public class Constants {



    public static final String ROOT_URL="https://api.github.com";

    public static final String USER="/user";

    public static final String REPOSITORIES = USER+"/repos";

    public static final String COMMITS = "/repos/{user}/{repo}/commits";

    public static final String EVENTS = "/repos/{user}/{repo}/events";

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
    public static final String DIFF_CONTENT = "diff_content";
    public static final int NOTIFICATION_ID = 0;
    public static final String ETAG = "ETag";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String PUSH_EVENT = "PushEvent";
    public static final java.lang.String OATH2_URL = "http://github.com/login/oauth/authorize?";
    public static final java.lang.String REDIRECT_URL = "com.example.animo.gita://user/oAuth";
    public static final String ACCESS_TOKEN = "https://github.com/login/oauth/access_token";
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String AUTH_CODE = "code";
    public static final String AUTH_STATE = "state";
    public static final CharSequence ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
    public static final String RANDOM_CODE = "afewf2ef234sd";
    public static final String FILES_ADDED = "added";
    public static final String FILES_RENAMED = "renamed";
    public static final String FILES_DELETD = "modified";
    public static final String REPO_DETAIL = "/repos/{user}/{repo}";
    public static final String REPO_ALREADY_MARKED = "Repo is Already Marked as Favourite";
    public static final String MARK_REPO_FAVOURITE = "Repo is Marked as Favourite";
    public static final String REGISTER_REPO = "/register_device";
    public static final String NOTIF_ROOT_URL = "https://gitanotif.herokuapp.com";
    public static final String REGISTER_WEBHOOK = "/repos/{user}/{repo}/hooks";
    public static final String WEBHOOK_URL = "/send";
    public static final String DEREGISTER_WEBHOOK = "/repos/{user}/{repo}/hooks/{id}";
    public static final String DEREGISTER_REPO = "/de_register_device";
    public static final String UNMARK_REPO_FAVOURITE_SUCCESS = "Repo is UnMarked as Favourite";
    public static final String SERVER_UNREACHABLE = "Server is unreachable";
    public static final String PATCH_WEBHOOK = "/repos/{user}/{repo}/hooks/{id}";
    public static final String GET_WEBHOOKS = "/repos/{user}/{repo}/hooks";
    public static final int RETRY_COUNT = 10;
    public static final String FETCH_KEYS = "/fetch_keys";
}
