package kz.demo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import okhttp3.MediaType;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class PdfToGPT3Processor {

    private static final String PDF_FILE_PATH = "C:\\Users\\Azamat\\OneDrive\\Рабочий стол\\Diagnos.pdf";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    public static void main(String[] args) {
        try {
            String text = extractTextFromPdf(PDF_FILE_PATH);
            String simplifiedText = simplifyText(text);
//            System.out.println(simplifiedText);
//            String shortenedText = removeLastLines(text, 1000);
            String response = sendRequestToGPT("Что не так с этим анализом и дай мне основные диагнозы предварительные и дай мне советы" + text);
//            System.out.println("Ответ от ChatGPT: " + response);
            generatePdf(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String extractTextFromPdf(String filePath) throws IOException {
        PDDocument document = PDDocument.load(new File(filePath));
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();
        return text;
    }
    public static void generatePdf(String text) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType0Font font = PDType0Font.load(document, new File("C:\\Users\\Azamat\\IdeaProjects\\KeremetProject\\src\\main\\resources\\static\\LiberationSans-Italic.ttf"));

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(font, 12);  // Используйте загруженный шрифт
                contentStream.setLeading(14.5f);
                contentStream.beginText();
                contentStream.newLineAtOffset(25, page.getMediaBox().getHeight() - 50);
                String[] lines = text.split("\n");
                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
                contentStream.endText();
            }

            document.save("output.pdf");  // сохраните PDF в файл
        }
    }
    public static String sendRequestToGPT(String userContent) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .connectTimeout(180, TimeUnit.SECONDS)
                .build();


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
        RequestBody body = RequestBody.Companion.create(requestBody.toString(), mediaType);
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
    public static String removeLastLines(String text, int numLinesToRemove) {
        String[] lines = text.split("\n");
        int newLength = Math.max(0, lines.length - numLinesToRemove);
        String[] newLines = Arrays.copyOf(lines, newLength);
        return String.join("\n", newLines);
    }

    public static String simplifyText(String text) {
        String simplifiedText = text.replaceAll("\n", " ").replaceAll("\\s+", " ");
        return simplifiedText;
    }

    private static class ChatGPTResponse {
        private ChatGPTCompletion[] choices;
    }

    private static class ChatGPTCompletion {
        private JsonObject message;
    }
}
