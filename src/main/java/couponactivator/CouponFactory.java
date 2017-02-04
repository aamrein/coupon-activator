package couponactivator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class CouponFactory {

    public static Map<String, Coupon> createCoupons(JSONArray couponsJson){
        Map<String, Coupon> coupons = new HashMap<>();

        for (int i = 0; i < couponsJson.length(); i++) {
            JSONObject couponJson = couponsJson.getJSONObject(i);
            Coupon coupon = createCoupon(couponJson);
            coupons.put(coupon.getId() + coupon.getState(), coupon);
        }

        return coupons;
    }

    public static Coupon createCoupon(JSONObject data) {
        return new Coupon(data);
    }
}
