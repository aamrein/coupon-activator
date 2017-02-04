package couponactivator.filter;

import couponactivator.Coupon;

import java.util.function.Predicate;

public class ExceptTypeFilter implements CouponFilter{

    private final String type;

    public ExceptTypeFilter(String filter) {
        this.type = filter;
    }

    @Override
    public Predicate<Coupon> getCouponFilterFunction() {
        return coupon -> coupon.getType().equals(this.type);
    }

}
