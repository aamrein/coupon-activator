package couponactivator;

import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OpenIdLoginService {
    private static final Logger logger = LogManager.getLogger(OpenIdLoginService.class);

    private OkHttpClient client;

    OpenIdLoginService(OkHttpClient client){
        this.client = client;
    }

    public void authenticate() {
        Map<String, String> data = initiateAuthentication();

        data = sendFormData("https://login.migros.ch/server", data);
        data = sendFormData("https://www.migros.ch/service/mconnect/login/immediate?is_return=true", data);
        sendFormData("https://www.migros.ch/j_spring_openid_security_check", data);

        logger.info("Login performed for user {}", data.get("openid.ext1.value.email"));
    }

    private Map<String, String> initiateAuthentication(){
        HttpUrl url = HttpUrl.parse("https://www.migros.ch/service/mconnect/login/immediate");
        Request request = RequestFactory.createGetRequest(url);
        return executeRequest(request);
    }

    private Map<String, String> sendFormData(String url, Map<String, String> data) {
        RequestBody body = createFromBody(data);
        Request request = RequestFactory.createPostRequest(url, body);
        return executeRequest(request);
    }

    private Map<String, String> executeRequest(Request request) {
        Map<String, String> parameters = new HashMap<>();

        try (Response response = client.newCall(request).execute()){
            String body = response.body().string();
            parameters = parseHtml(body);
            response.close();
        } catch (IOException e) {
            logger.error(e);
        }

        return parameters;
    }

    private Map<String, String> parseHtml(String body) {
        Map<String, String> parameters = new HashMap<>();

        Document document = Jsoup.parse(body);
        Elements inputs = document.select("input");

        for (Element element: inputs) {
            parameters.put(element.attr("name"), element.attr("value"));
        }

        return parameters;
    }

    private RequestBody createFromBody(Map<String, String> data){
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key: data.keySet()) {
            formBodyBuilder.add(key, data.get(key));
        }
        return formBodyBuilder.build();
    }
}
