package couponactivator.filter;

import couponactivator.Coupon;

import java.util.function.Predicate;

public class OnlyStateFilter implements CouponFilter {

    public final String state;

    public OnlyStateFilter(String state) {
        this.state = state;
    }

    @Override
    public Predicate<Coupon> getCouponFilterFunction() {
        return coupon -> !coupon.getState().equals(state);
    }
}
