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
            state = getString(data, "state"),
            type = getString(data, "type")
        )

        return coupon
    }

    private fun getString(data: JSONObject, key: String): String {
        return if (data.isNull(key)) "" else data.getString(key)
    }
}
