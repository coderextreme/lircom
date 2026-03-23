package com.example;

import okhttp3.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TranslationApp {
    private static final String SERVER_URL = "http://127.0.0.1:8000/translate";
    // Keep these static so they are reused for every chat message (much faster!)
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    // Changed from main() to a reusable method that returns a String
    public static String translate(String textToTranslate, String sourceLang, String targetLang) {
        try {
            ObjectNode payloadNode = mapper.createObjectNode();
            payloadNode.put("text", textToTranslate);
            payloadNode.put("source_lang", sourceLang);
            payloadNode.put("target_lang", targetLang);

            String jsonPayload = mapper.writeValueAsString(payloadNode);
            RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(SERVER_URL)
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonNode jsonNode = mapper.readTree(response.body().string());
                    if (jsonNode.has("translation")) {
			String result = jsonNode.get("translation").asText();
			System.err.println("Translated properly: "+result);
                        textToTranslate = result;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textToTranslate; // Fallback to original text if translation fails
    }
}
