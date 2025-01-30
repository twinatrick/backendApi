package com.example.backedapi.Util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
//import com.google.gson.Gson;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
@Getter@Setter
public class ClientSecret {
//    private static final Gson GSON = new Gson();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private String client_id;
    private String project_id;
    private String auth_uri;
    private String token_uri;
    private String auth_provider_x509_cert_url;
    private String client_secret;
    private String[] redirect_uris;
    private String currentRedirectUri;
    private ClientSecret(){}
    public static ClientSecret readFromPath(String path) throws IOException {
        try(
                FileReader fileReader = new FileReader(path);
        ){
            ClientSecret clientSecret = OBJECT_MAPPER.convertValue(
                    OBJECT_MAPPER.readValue(fileReader, Map.class).get("web"),
                    ClientSecret.class);
            /* TODO: 2023/11/21
             * 目前先以系統參數為主，
             * 因為 1.發出請求的ip domain要和redirect url一致，
             *     2.有被登入在gcp憑證設定裡，
             * 但是目前取不到當前系統host origin
             */
            clientSecret.currentRedirectUri = System.getenv("REDIRECT_URL");
            return clientSecret;
        }
    }


//    public String getAuthorizationCodeBody(String code){
//
//        HashMap<String, String> data = new HashMap<>();
//        data.put("client_id", client_id);
//        data.put("client_secret", client_secret);
//        data.put("grant_type", "authorization_code");
//        data.put("redirect_uri", currentRedirectUri);
//        data.put("code", code);
//        return GSON.toJson(data);
//    }

    public HashMap<String, String> getRefreshTokenBody(String refreshToken){
        HashMap<String, String> data = new HashMap<>();
        data.put("client_id", client_id);
        data.put("client_secret", client_secret);
        data.put("grant_type", "refresh_token");
        data.put("refresh_token", refreshToken);
        return data;
    }
}
