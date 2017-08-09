package couponactivator

import org.json.JSONArray
import org.json.JSONObject

import java.util.*

class CouponFactory {

    fun createCoupons(couponsJson: JSONArray): Map<String, Coupon> {
        val coupons = HashMap<String, Coupon>()

        for (i in 0 until couponsJson.length()) {
            val couponJson = couponsJson.getJSONObject(i)
            val coupon = createCoupon(couponJson)
            coupons.put("$coupon.id$coupon.state", coupon)
        }

        return coupons
    }

    fun createCoupon(data: JSONObject): Coupon {
        val coupon = Coupon(
            id = getString(data, "id"),
            couponId = data.getInt("couponId"),
            name = getString(data, "name"),
            nameWeb = getString(data, "nameWeb"),
            nameApp = getString(data, "nameApp"),
            subTitle = getString(data, "subTitle"),
            group = getString(data, "group"),
            minimumPurchase = getString(data, "minimumPurchase"),
            discountAmount = getString(data, "discountAmount"),
            disclaimerLeadActive = getString(data, "disclaimerLeadActive"),
            disclaimerLeadInactive = getString(data, "disclaimerLeadInactive"),
            disclaimerBody = getString(data, "disclaimerBody"),
            promotionNumber = getString(data, "promotionNumber"),
            finePrint = getString(data, "finePrint"),
            quantity = data.getInt("quantity"),
            isStationaryRedeemable = data.getBoolean("stationaryRedeemable"),
            isOnlineRedeemable = data.getBoolean("onlineRedeemable"),
            ean = getString(data, "ean"),
            state = getString(data, "state"),
            type = getString(data, "type"),
            isNewCoupon = data.getBoolean("newCoupon"),
            expirationDateText = getString(data, "expirationDateText"),
            redeemedDateText = getString(data, "redeemedDateText"),
            redeemedText = getString(data, "redeemedText"),
            activationDisabledText = getString(data, "activationDisabledText"),
            expirationDate = getString(data, "expirationDate"),
            redeemedDate = getString(data, "redeemedDate")
        )

        return coupon
    }

    private fun getString(data: JSONObject, key: String): String {
        return if (data.isNull(key)) "" else data.getString(key)
    }
}
