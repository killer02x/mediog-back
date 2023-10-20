package kz.demo;

import okhttp3.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Scanner;

public class GPT3Request {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите ваш запрос:");
        String userContent = scanner.nextLine();

        try {
            String response = sendRequestToGPT(userContent);
            System.out.println("Ответ от ChatGPT: " + response);
        } catch (IOException e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }

    private static String sendRequestToGPT(String userContent) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", "You are a helpful assistant.");

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", userContent);

        JsonArray messages = new JsonArray();
        messages.add(systemMessage);
        messages.add(userMessage);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo");
        requestBody.add("messages", messages);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        if (response.isSuccessful()) {
            // Парсинг ответа
            Gson gson = new Gson();
            ChatGPTResponse chatGPTResponse = gson.fromJson(responseBody, ChatGPTResponse.class);
            if (chatGPTResponse.choices != null && chatGPTResponse.choices.length > 0) {
                return chatGPTResponse.choices[0].message.get("content").getAsString();
            }
            return "No response from ChatGPT.";
        } else {
            return "Error from ChatGPT: " + response.code() + " " + response.message() + "\n" + responseBody;
        }

    }

    private static class ChatGPTResponse {
        private ChatGPTCompletion[] choices;
    }

    private static class ChatGPTCompletion {
        private JsonObject message;
    }
}
