package com.example.animo.gita;

import com.example.animo.gita.model.DeviceRegisterOutput;
import com.example.animo.gita.model.RepoRegister;
import com.example.animo.gita.model.RepoRegisterOutput;
import com.example.animo.gita.model.WebHookRegister;

import java.util.List;

/**
 * Created by animo on 23/12/17.
 */

public interface MyApiInterface {

    @RESPONSE_TYPE(url = Constants.GET_WEBHOOKS,type = HTTP_METHOD.GET)
    MyCall<Void,List<WebHookRegister>> getAllHooks(String accessToken,String url);

    @RESPONSE_TYPE(url=Constants.PATCH_WEBHOOK,type = HTTP_METHOD.PATCH)
    MyCall<WebHookRegister,WebHookRegister> patchHook(String accessToken,String url,WebHookRegister webHookRegister);

    @RESPONSE_TYPE(url = Constants.REGISTER_WEBHOOK,type = HTTP_METHOD.POST)
    MyCall<WebHookRegister,WebHookRegister> createWebHook(String accessToken,String url,WebHookRegister webHookRegister);

    @RESPONSE_TYPE(url = Constants.DEREGISTER_WEBHOOK,type = HTTP_METHOD.DELETE)
    MyCall<Void,Void> deleteWebHook(String accessToken,String url);

    @RESPONSE_TYPE(url = Constants.REGISTER_REPO,type = HTTP_METHOD.POST)
    MyCall<RepoRegister,RepoRegisterOutput> markFavRepo(String accessToken,String url,RepoRegister repoRegister);

    @RESPONSE_TYPE(url = Constants.DEREGISTER_REPO,type = HTTP_METHOD.POST)
    MyCall<RepoRegister,RepoRegisterOutput> unmarkFavRepo(String accessToken,String url,RepoRegister repoRegister);

    @RESPONSE_TYPE(url= Constants.FETCH_KEYS,type=HTTP_METHOD.POST)
    MyCall<RepoRegister,DeviceRegisterOutput> fetchKeys(String accessToken,String url,RepoRegister repoRegister);


}
