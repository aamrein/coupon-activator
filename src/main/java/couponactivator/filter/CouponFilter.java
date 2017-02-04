package couponactivator.filter;

import couponactivator.Coupon;

import java.util.function.Predicate;

public interface CouponFilter {

    public Predicate<Coupon> getCouponFilterFunction();

}
