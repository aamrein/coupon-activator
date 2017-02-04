package couponactivator;

import okhttp3.*;

import java.util.List;
import java.util.Map;

public class RequestFactory {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static Request createGetRequest(String url) {
        return createGetRequest(HttpUrl.parse(url));
    }

    public static Request createGetRequest(HttpUrl url) {
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    public static Request createPostRequest(String url, RequestBody body){
        return createPostRequest(HttpUrl.parse(url), body);
    }

    public static Request createPostRequest(HttpUrl url, RequestBody body){
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

}
