package couponactivator.cli;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ClientFactory {

    public static OkHttpClient createClient(){

        return new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<String, HashMap<String, Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        HashMap<String, Cookie> currentCookies = cookieStore.containsKey(url.host()) ?
                                cookieStore.get(url.host()) : new HashMap<>();
                        for (Cookie cookie: cookies) {
                            currentCookies.put(cookie.name(), cookie);
                        }
                        cookieStore.put(url.host(), currentCookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = new ArrayList<>(cookieStore.containsKey(url.host()) ?
                                cookieStore.get(url.host()).values() : new ArrayList<>());
                        return cookies;
                    }
                })
                .build();
    }

}
