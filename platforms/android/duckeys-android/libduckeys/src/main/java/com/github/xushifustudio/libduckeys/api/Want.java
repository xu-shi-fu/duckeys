package com.github.xushifustudio.libduckeys.api;


public class Want {


    // 定义方法名称
    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";


    public String url;
    public String method;
    public final Headers headers = new Headers();
    public Entity entity;

}
