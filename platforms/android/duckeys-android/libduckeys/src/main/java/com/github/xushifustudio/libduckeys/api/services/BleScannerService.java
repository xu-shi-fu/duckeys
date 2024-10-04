package com.github.xushifustudio.libduckeys.api.services;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;

public final class BleScannerService {


    public final static String URI_BLE_SCANNER_START = "uri:///ble/scanner/start";
    public final static String URI_BLE_SCANNER_STOP = "uri:///ble/scanner/stop";
    public final static String URI_BLE_SCANNER_RESULTS = "uri:///ble/scanner/results";
    public final static String URI_BLE_SCANNER_WAIT = "uri:///ble/scanner/wait";


    public static class Request {
    }

    public static class Response {
    }

    public static class Holder {
        public Request request;
        public Response response;
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
