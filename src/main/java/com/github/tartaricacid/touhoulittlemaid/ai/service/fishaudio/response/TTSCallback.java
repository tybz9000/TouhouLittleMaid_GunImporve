package com.github.tartaricacid.touhoulittlemaid.ai.service.fishaudio.response;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.function.Consumer;

public class TTSCallback implements Callback {
    private final Consumer<byte[]> consumer;

    public TTSCallback(Consumer<byte[]> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        TouhouLittleMaid.LOGGER.error("Request failed: ", e);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        try (ResponseBody body = response.body()) {
            if (response.isSuccessful()) {
                consumer.accept(body.bytes());
            } else {
                TouhouLittleMaid.LOGGER.error("Request failed: {}", response.code());
            }
        } catch (JsonSyntaxException e) {
            TouhouLittleMaid.LOGGER.error("JSON Syntax Exception: ", e);
        }
    }
}
