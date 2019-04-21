package com.example.animo.gita;

import com.example.animo.gita.retrofit.NotificationClient;

/**
 * Created by animo on 21/12/17.
 */

public class TestAsyncClass {
    public void test(){
        MyApiInterface s = NotificationClient.createService(MyApiInterface.class,null);
        /*MyCall<List<WebHookRegister>> t =s.getAllHooks("fadsfsa","fadfas","fadsfasd");
        t.enque(new MyCallBack<List<WebHookRegister>>() {
            @Override
            public void callBackOnSuccess(MyCall<List<WebHookRegister>> myCall) {

            }

            @Override
            public void callBackOnFailure(Exception e) {

            }
        });*/


    }
}
