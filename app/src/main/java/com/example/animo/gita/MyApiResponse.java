package com.example.animo.gita;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * Created by animo on 23/12/17.
 */

public class MyApiResponse {

    public static <S> S createApi(Class<S> clazz){
        ClassLoader loader = clazz.getClassLoader();
        Class[] interfaces = new Class[]{clazz};

        Object object = Proxy.newProxyInstance(loader, interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Annotation annotation = method.getAnnotation(RESPONSE_TYPE.class);
                RESPONSE_TYPE response_type = (RESPONSE_TYPE) annotation;

                MyRequestBean<Object> myRequestBean = new MyRequestBean<>();
                myRequestBean.setAccessToken((String) args[0]);
                myRequestBean.setUrl((String) args[1]);
                myRequestBean.setRequestType(response_type.type());
                if(response_type.type().equals(HTTP_METHOD.POST) ||
                        response_type.type().equals(HTTP_METHOD.PATCH)){
                    if(!(args.length==3)){
                        throw new Exception("No request Body found ");
                    }else{
                        myRequestBean.setRequestObject(args[2]);
                    }
                }
                Type returnType = method.getGenericReturnType();
                Class<?> clazz = MyCall.class;
                Object object = clazz.newInstance();
                MyCall myCall = (MyCall) object;
                myCall.setRequestBean(myRequestBean);
                Type type =  method.getGenericReturnType();
                if(type instanceof ParameterizedType){
                    ParameterizedType pType = (ParameterizedType) type;
                    for(Type t:pType.getActualTypeArguments()){
                        myCall.setType(t);
                    }
                }
                else
                    myCall.setType(type);

                return myCall;
            }
        });

        return (S) object;
    }


}
