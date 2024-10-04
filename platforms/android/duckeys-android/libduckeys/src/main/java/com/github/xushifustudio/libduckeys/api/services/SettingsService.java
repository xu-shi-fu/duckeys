package com.github.xushifustudio.libduckeys.api.services;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.settings.apps.AppSettingBank;
import com.github.xushifustudio.libduckeys.settings.users.UserSettingBank;

public final class SettingsService {

    public final static String URI = "uri://local/settings";

    public static class Request {
        public UserSettingBank users;
        public AppSettingBank apps;
    }

    public static class Response {
        public UserSettingBank users;
        public AppSettingBank apps;
    }


    public static Want encode(Request req) {
        return new Want();
    }

    public static Have encode(Response resp) {
        return new Have();
    }


    public static Request decode(Want req) {
        return new Request();
    }

    public static Response decode(Have resp) {
        return new Response();
    }
}
