package couponactivator;

import couponactivator.cli.CliArgs;
import couponactivator.cli.ClientFactory;
import couponactivator.filter.CouponFilter;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

public class CouponService {
    private static final Logger logger = LogManager.getLogger(OpenIdLoginService.class);
    private static final String TOKEN_NAME = "name=\"_csrf\"";

    private static final String START_TOKEN = "content=\"";
    private static final String END_TOKEN = "\"";

    private final CliArgs cliArgs;

    private OkHttpClient client;
    private Map<String, Coupon> coupons = new HashMap<>();

    public static final String COUPON_STATE_ACTIVE = "ACTIVE";
    public static final String COUPON_STATE_INACTIVE = "INACTIVE";
    public static final String COUPON_STATE_REDEEMED = "REDEEMED";

    CouponService(CliArgs cliArgs) {
        this.cliArgs = cliArgs;
        this.client = ClientFactory.createClient();
    }

    public Collection<Coupon> getCoupons(){
        if (coupons.size() == 0) {
            coupons = reloadCoupons();
        }
        return new ArrayList<>(coupons.values());
    }

    public Collection<Coupon> getFilteredCouponList(Collection<CouponFilter> filters){
        Collection<Coupon> coupons = getCoupons();
        for (CouponFilter filter : filters) {
            coupons.removeIf(filter.getCouponFilterFunction());
        }
        return coupons;
    }

    public Map<String, Coupon> reloadCoupons() {
        Map<String, Coupon> coupons = new HashMap<>();

        login();
        openIdLogin();

        HttpUrl url = HttpUrl
                .parse("https://www.migros.ch/mgb-rest/dirac-cms-core/components/couponOverview.json")
                .newBuilder()
                .addQueryParameter("method", "getCoupons")
                .addQueryParameter("lang", "de")
                .addQueryParameter("_", "" + (System.currentTimeMillis()))
                .build();

        Request request = RequestFactory.createGetRequest(url);

        try (Response response = client.newCall(request).execute()){
            String body = response.body().string();
            JSONArray data = new JSONArray(body);
            coupons = CouponFactory.createCoupons(data);
            response.close();
        } catch (IOException | JSONException e) {
            logger.error(e);
        }

        return coupons;
    }

    public boolean activate(Coupon coupon) {
        return changeCouponState(coupon, "activate");
    }

    public boolean deactivate(Coupon coupon){
        return changeCouponState(coupon, "deactivate");
    }

    private boolean changeCouponState(Coupon coupon, String newState) {
        HttpUrl httpUrl = HttpUrl.parse("https://www.migros.ch/mgb-rest/dirac-cms-core/components/couponOverview.json")
                .newBuilder()
                .addQueryParameter("method", newState)
                .addQueryParameter("lang", "de")
                .addQueryParameter("id", coupon.getId())
                .build();
        RequestBody body = new FormBody.Builder().build();
        Request request = RequestFactory.createPostRequest(httpUrl, body);

        boolean status = executeRequest(request) == 200;
        if(status) {
            coupon.setState(newState.equals("activate") ? COUPON_STATE_ACTIVE : COUPON_STATE_INACTIVE);
        }

        return status;
    }

    private boolean login() {
        String csrf = getCSRFToken();

        HttpUrl httpUrl = HttpUrl.parse("https://login.migros.ch/login");
        RequestBody body = new FormBody.Builder()
                .add("_csrf", csrf)
                .add("username", cliArgs.getUsername())
                .add("password", cliArgs.getPassword())
                .add("remember-me", "0")
                .build();

        executeRequest(RequestFactory.createPostRequest(httpUrl, body));
        executeRequest(RequestFactory.createGetRequest("https://www.migros.ch/de.html"));
        return true;
    }

    private void openIdLogin() {
        OpenIdLoginService openIdLogin = new OpenIdLoginService(client);
        openIdLogin.authenticate();
    }


    private String getCSRFToken() {
        String csrf = "";
        HttpUrl httpUrl = HttpUrl.parse("https://login.migros.ch/login");
        Request request = RequestFactory.createGetRequest(httpUrl);

        try (Response response = client.newCall(request).execute()){
            String[] body = response.body().string().split(System.getProperty("line.separator"));

            for (String line: body) {
                if (line.contains(TOKEN_NAME)) {
                    int startIndex = line.indexOf(START_TOKEN) + START_TOKEN.length();
                    int endIndes = line.lastIndexOf(END_TOKEN);
                    csrf = line.substring(startIndex, endIndes);
                    break;
                }
            }

            response.close();
        } catch (IOException e) {
            logger.error(e);
        }
        return csrf;
    }

    private int executeRequest(Request request) {
        int errorCode = 0;
        try (Response response = client.newCall(request).execute()){
            errorCode = response.code();
            response.close();
        } catch (IOException e) {
            logger.error(e);
            return errorCode;
        }
        return errorCode;
    }
}
