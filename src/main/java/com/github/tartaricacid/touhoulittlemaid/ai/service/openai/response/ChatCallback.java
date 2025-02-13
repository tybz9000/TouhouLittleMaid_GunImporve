package com.github.tartaricacid.touhoulittlemaid.ai.service.openai.response;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.Service;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.function.Consumer;

public class ChatCallback implements Callback {
    private final Consumer<ChatCompletionResponse> consumer;

    public ChatCallback(Consumer<ChatCompletionResponse> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        TouhouLittleMaid.LOGGER.error("Request failed: ", e);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        try (ResponseBody body = response.body()) {
            String string = body.string();
            if (response.isSuccessful()) {
                ChatCompletionResponse chatCompletionResponse = Service.GSON.fromJson(string, ChatCompletionResponse.class);
                consumer.accept(chatCompletionResponse);
            } else {
                TouhouLittleMaid.LOGGER.error("Request failed: {}", string);
            }
        } catch (JsonSyntaxException e) {
            TouhouLittleMaid.LOGGER.error("JSON Syntax Exception: ", e);
        }
    }
}
