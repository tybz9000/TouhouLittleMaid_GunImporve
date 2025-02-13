package com.github.tartaricacid.touhoulittlemaid.ai.service.fishaudio;

import com.github.tartaricacid.touhoulittlemaid.ai.service.Service;
import com.github.tartaricacid.touhoulittlemaid.ai.service.fishaudio.request.TTSRequest;
import com.github.tartaricacid.touhoulittlemaid.ai.service.fishaudio.response.TTSCallback;
import com.squareup.okhttp.*;

import java.util.function.Consumer;

public class TTSClient {
    private final OkHttpClient httpClient;
    private String baseUrl = "";
    private String apiKey = "";
    private TTSRequest request;

    public static TTSClient create(final OkHttpClient httpClient) {
        return new TTSClient(httpClient);
    }

    private TTSClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public TTSClient baseUrl(final String baseUrl) {
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }
        return this;
    }

    public TTSClient apiKey(final String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public TTSClient request(TTSRequest request) {
        this.request = request;
        return this;
    }

    public void handle(Consumer<byte[]> consumer) {
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, Service.GSON.toJson(request));
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + this.apiKey)
                .post(requestBody)
                .url(this.baseUrl + TTSRequest.getUrl())
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new TTSCallback(consumer));
    }
}
