package couponactivator.filter;

import couponactivator.Coupon;

import java.util.function.Predicate;

public class ExceptTypeFilter implements Predicate<Coupon>{

    private final String type;

    public ExceptTypeFilter(String filter) {
        this.type = filter;
    }

    @Override
    public boolean test(Coupon coupon) {
        return coupon.getType().equals(this.type);
    }
}
