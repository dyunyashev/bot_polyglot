package botpolyglot.translate;

import botpolyglot.AppProp;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class YandexTranslate implements TranslationService {

    private final String YANDEX_TRANSLATE_URL = AppProp.getProperty("YANDEX_TRANSLATE_URL");
    private final String HTTP_METHOD = AppProp.getProperty("HTTP_METHOD");
    private final String IAM_TOKEN = AppProp.getProperty("IAM_TOKEN");
    private final String FOLDER_ID = AppProp.getProperty("FOLDER_ID");

    @Override
    public String translateText(String targetLanguageCode, String textToTranslate) throws IOException {
        String resultStr = "";
        URL url = new URL(YANDEX_TRANSLATE_URL);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod(HTTP_METHOD);

        //headers
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + IAM_TOKEN);
        con.setDoOutput(true);

        Gson g = new Gson();

        Request req = new Request();
        req.targetLanguageCode = targetLanguageCode;
        req.folderId = FOLDER_ID;
        req.texts = new String[]{textToTranslate};
        String jsonInputString = g.toJson(req);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        //post
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            Response res = g.fromJson(response.toString(), Response.class);
            if (res.translations.size() > 0) {
                resultStr = res.translations.get(0).text;
            }

        }

        return resultStr;
    }

    private static class Response{
        public List<Translation> translations;
    }

    private static class Translation{
        public String text;
    }

    private static class Request{
        String targetLanguageCode;
        String folderId;
        String[] texts;
    }
}
