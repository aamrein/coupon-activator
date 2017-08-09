package couponactivator.filter;

import couponactivator.Coupon;

import java.util.function.Predicate;

public class OnlyStateFilter implements Predicate<Coupon> {

    public final String state;

    public OnlyStateFilter(String state) {
        this.state = state;
    }

    @Override
    public boolean test(Coupon coupon) {
        return !coupon.getState().equals(state);
    }
}
