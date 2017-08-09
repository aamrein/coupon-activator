package couponactivator;

import couponactivator.cli.CliArgs;
import couponactivator.cli.CliParser;
import couponactivator.filter.ExceptTypeFilter;
import couponactivator.filter.OnlyStateFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class CouponActivator {

    private static final Logger logger = LogManager.getLogger(CouponActivator.class);

    public static void main(String ... args) {
        CliArgs cliArgs = CliParser.parse(args);
        CouponService couponService = new CouponService(cliArgs);

        ArrayList<Predicate<Coupon>> couponFilters = new ArrayList<>();
        couponFilters.add(new OnlyStateFilter(couponService.getCOUPON_STATE_INACTIVE()));
        if(cliArgs.hasTypeFilter()) {
            couponFilters.add(new ExceptTypeFilter(cliArgs.getTypeFilter()));
        }

        activate(couponService, couponFilters);
        printCouponStates(couponService);
    }

    private static void activate(CouponService couponService, ArrayList<Predicate<Coupon>> couponFilters) {
        Collection<Coupon> deactivatedCoupons = couponService.getFilteredCouponList(couponFilters);
        for (Coupon coupon : deactivatedCoupons) {
            logger.info("Activate coupon {}", coupon.getCouponId());
            couponService.activate(coupon);
        }
    }

    private static void printCouponStates(CouponService couponService) {
        Collection<Coupon> coupons = couponService.getCoupons();
        for (Coupon coupon : coupons) {
            System.out.println(
                    "State: " + coupon.getState() +
                    ",\tType: " + coupon.getType() +
                    ",\tName: " + coupon.getName());
        }
    }
}
