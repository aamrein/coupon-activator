package couponactivator

import couponactivator.cli.CliArgs
import couponactivator.cli.ClientFactory
import okhttp3.*
import org.apache.logging.log4j.LogManager
import org.json.JSONArray
import org.json.JSONException

import java.io.IOException
import java.util.*
import java.util.function.Predicate

class CouponService internal constructor(private val cliArgs: CliArgs) {

    private val logger = LogManager.getLogger(OpenIdLoginService::class.java)
    private val TOKEN_NAME = "name=\"_csrf\""

    private val START_TOKEN = "content=\""
    private val END_TOKEN = "\""

    private val couponFactory = CouponFactory()
    private val client: OkHttpClient = ClientFactory.createClient()
    private var coupons: Map<String, Coupon> = HashMap()

    val COUPON_STATE_ACTIVE = "ACTIVE"
    val COUPON_STATE_INACTIVE = "INACTIVE"
    val COUPON_STATE_REDEEMED = "REDEEMED"

    fun getCoupons(): Collection<Coupon> {
        if (coupons.size == 0) {
            coupons = reloadCoupons()
        }
        return ArrayList(coupons.values)
    }

    fun getFilteredCouponList(filters: Collection<Predicate<Coupon>>): Collection<Coupon> {
        val coupons = getCoupons()
        var filteredCoupons = coupons
        for (filter in filters) {
            filteredCoupons = filteredCoupons.filter{ filter.test(it) }
        }
        return filteredCoupons
    }

    fun reloadCoupons(): Map<String, Coupon> {
        var coupons: Map<String, Coupon> = HashMap()

        login()
        openIdLogin()

        val url = HttpUrl
                .parse("https://www.migros.ch/mgb-rest/api/dirac-cms-core/components/couponOverview.json")
                .newBuilder()
                .addQueryParameter("method", "getCoupons")
                .addQueryParameter("lang", "de")
                .addQueryParameter("_", "" + System.currentTimeMillis())
                .build()

        val request = RequestFactory.createGetRequest(url)

        try {
            client.newCall(request).execute().use { response ->
                val body = response.body().string()
                val data = JSONArray(body)
                coupons = couponFactory.createCoupons(data)
                response.close()
            }
        } catch (e: IOException) {
            logger.error(e)
        } catch (e: JSONException) {
            logger.error(e)
        }

        return coupons
    }

    fun activate(coupon: Coupon): Boolean {
        return changeCouponState(coupon, "activate")
    }

    fun deactivate(coupon: Coupon): Boolean {
        return changeCouponState(coupon, "deactivate")
    }

    private fun changeCouponState(coupon: Coupon, newState: String): Boolean {
        val httpUrl = HttpUrl.parse("https://www.migros.ch/mgb-rest/dirac-cms-core/components/couponOverview.json")
                .newBuilder()
                .addQueryParameter("method", newState)
                .addQueryParameter("lang", "de")
                .addQueryParameter("id", coupon.id)
                .build()
        val body = FormBody.Builder().build()
        val request = RequestFactory.createPostRequest(httpUrl, body)

        val status = executeRequest(request) == 200
        if (status) {
            coupon.state = if (newState == "activate") COUPON_STATE_ACTIVE else COUPON_STATE_INACTIVE
        }

        return status
    }

    private fun login(): Boolean {
        val csrf = csrfToken

        val httpUrl = HttpUrl.parse("https://login.migros.ch/login")
        val body = FormBody.Builder()
                .add("_csrf", csrf)
                .add("username", cliArgs.username)
                .add("password", cliArgs.password)
                .add("remember-me", "0")
                .build()

        executeRequest(RequestFactory.createPostRequest(httpUrl, body))
        executeRequest(RequestFactory.createGetRequest("https://www.migros.ch/de.html"))
        return true
    }

    private fun openIdLogin() {
        val openIdLogin = OpenIdLoginService(client)
        openIdLogin.authenticate()
    }


    private val csrfToken: String
        get() {
            var csrf = ""
            val httpUrl = HttpUrl.parse("https://login.migros.ch/login")
            val request = RequestFactory.createGetRequest(httpUrl)

            try {
                client.newCall(request).execute().use { response ->
                    val body = response.body().string().split(System.getProperty("line.separator").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                    for (line in body) {
                        if (line.contains(TOKEN_NAME)) {
                            val startIndex = line.indexOf(START_TOKEN) + START_TOKEN.length
                            val endIndes = line.lastIndexOf(END_TOKEN)
                            csrf = line.substring(startIndex, endIndes)
                            break
                        }
                    }

                    response.close()
                }
            } catch (e: IOException) {
                logger.error(e)
            }

            return csrf
        }

    private fun executeRequest(request: Request): Int {
        var errorCode = 0
        try {
            client.newCall(request).execute().use { response ->
                errorCode = response.code()
                response.close()
            }
        } catch (e: IOException) {
            logger.error(e)
            return errorCode
        }

        return errorCode
    }

}
