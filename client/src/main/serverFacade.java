import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.util.Collection;
import java.util.HashMap;
import java.lang.*;

public class serverFacade {

    private final String serverUrl;

    public serverFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String clearApplication() {
        var path = "/db";
        // return this.makeRequest("DELETE", path, pet, Pet.class);

        // String method, String path, Object request, Class<T> responseClass
        String method = "DELETE";

        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            //writeBody(request, http);
            http.connect();
            var temp = readBody(http);
            return "pass";
        } catch (Exception ex) {
            System.out.println(ex);
            return "pass";
        }
    }

//    public String register(String[] params) {
//        var path = "/user";
//        return this.makeRequest("POST", path);
//    }


//    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass){
//        try {
//            URL url = (new URI(serverUrl + path)).toURL();
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            http.setRequestMethod(method);
//            http.setDoOutput(true);
//
//            writeBody(request, http);
//            http.connect();
//            throwIfNotSuccessful(http);
//            return readBody(http, responseClass);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

//    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
//        T response = null;
//        if (http.getContentLength() < 0) {
//            try (InputStream respBody = http.getInputStream()) {
//                InputStreamReader reader = new InputStreamReader(respBody);
//                if (responseClass != null) {
//                    response = reader;
//                }
//            }
//        }
//        return response;
//    }

    private static InputStreamReader readBody(HttpURLConnection http) throws IOException {
        InputStreamReader response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                response = reader;
            }
        }
        return response;
    }

}



