package com.github.tartaricacid.touhoulittlemaid.ai.service.openai;

import com.github.tartaricacid.touhoulittlemaid.ai.service.Service;
import com.github.tartaricacid.touhoulittlemaid.ai.service.openai.request.ChatCompletion;
import com.github.tartaricacid.touhoulittlemaid.ai.service.openai.response.ChatCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.openai.response.ChatCompletionResponse;
import com.squareup.okhttp.*;

import java.util.function.Consumer;

public final class ChatClient {
    private final OkHttpClient httpClient;
    private String baseUrl = "";
    private String apiKey = "";
    private ChatCompletion chatCompletion;

    public static ChatClient create(final OkHttpClient httpClient) {
        return new ChatClient(httpClient);
    }

    private ChatClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public ChatClient baseUrl(final String baseUrl) {
        if (baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }
        return this;
    }

    public ChatClient apiKey(final String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public ChatClient chat(final ChatCompletion chatCompletion) {
        this.chatCompletion = chatCompletion;
        return this;
    }

    public void handle(Consumer<ChatCompletionResponse> consumer) {
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, Service.GSON.toJson(chatCompletion));
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + this.apiKey)
                .post(requestBody)
                .url(this.baseUrl + ChatCompletion.getUrl())
                .build();
        Call call = httpClient.newCall(request);
        call.enqueue(new ChatCallback(consumer));
    }
}
