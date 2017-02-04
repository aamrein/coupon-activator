package couponactivator;

import org.json.JSONObject;

public class Coupon {

    private String id;
    private int couponId;
    private String name = "";
    private String nameWeb = "";
    private String nameApp = "";
    private String subTitle = "";
    private String group;
    private String minimumPurchase = "";
    private String discountAmount = "";
    private String disclaimerLeadActive = "";
    private String disclaimerLeadInactive = "";
    private String disclaimerBody = "";
    private String promotionNumber = "";
    private String finePrint = "";
    private int quantity;
    private boolean stationaryRedeemable = false;
    private boolean onlineRedeemable = false;
    private String ean;
    private String state = "";
    private String type = "";
    private boolean newCoupon;
    private String expirationDateText = "";
    private String redeemedDateText = "";
    private String redeemedText = "";
    private String activationDisabledText = "";
    private String expirationDate = "";
    private String redeemedDate = "";

    public Coupon (JSONObject data) {
        this.id = data.getString("id");
        this.couponId = data.getInt("couponId");
        this.name = data.getString("name");
        this.nameWeb = data.getString("nameWeb");
        this.nameApp = data.getString("nameApp");
        this.subTitle = data.isNull("subTitle") ? "" : data.getString("subTitle");
        this.group = data.getString("group");
        this.minimumPurchase = data.getString("minimumPurchase");
        this.discountAmount = data.getString("discountAmount");
        this.disclaimerLeadActive = data.getString("disclaimerLeadActive");
        this.disclaimerLeadInactive = data.getString("disclaimerLeadInactive");
        this.disclaimerBody = data.getString("disclaimerBody");
        this.promotionNumber = data.getString("promotionNumber");
        this.finePrint = data.getString("finePrint");
        this.quantity = data.getInt("quantity");
        this.stationaryRedeemable = data.getBoolean("stationaryRedeemable");
        this.onlineRedeemable = data.getBoolean("onlineRedeemable");
        this.ean = data.getString("ean");
        this.state = data.getString("state");
        this.type = data.getString("type");
        this.newCoupon = data.getBoolean("newCoupon");
        this.expirationDateText = data.getString("expirationDateText");
        this.redeemedDateText = data.isNull("redeemedDateText") ? "" : data.getString("redeemedDateText");
        this.redeemedText = data.isNull("redeemedText") ? "" : data.getString("redeemedText");
        this.activationDisabledText = data.getString("activationDisabledText");
        this.expirationDate = data.getString("expirationDate");
        this.redeemedDate = data.isNull("redeemedDate") ? "" : data.getString("redeemedDate");
    }

    public String  getId() {
        return id;
    }

    public int getCouponId() {
        return couponId;
    }

    public String getName() {
        return name;
    }

    public String getNameWeb() {
        return nameWeb;
    }

    public String getNameApp() {
        return nameApp;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getGroup() {
        return group;
    }

    public String getMinimumPurchase() {
        return minimumPurchase;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public String getDisclaimerLeadActive() {
        return disclaimerLeadActive;
    }

    public String getDisclaimerLeadInactive() {
        return disclaimerLeadInactive;
    }

    public String getDisclaimerBody() {
        return disclaimerBody;
    }

    public String getPromotionNumber() {
        return promotionNumber;
    }

    public String getFinePrint() {
        return finePrint;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isStationaryRedeemable() {
        return stationaryRedeemable;
    }

    public boolean isOnlineRedeemable() {
        return onlineRedeemable;
    }

    public String getEan() {
        return ean;
    }

    public String getState() { return state; }

    public String getType() {
        return type;
    }

    public boolean isNewCoupon() {
        return newCoupon;
    }

    public String getExpirationDateText() {
        return expirationDateText;
    }

    public String getRedeemedDateText() {
        return redeemedDateText;
    }

    public String getRedeemedText() {
        return redeemedText;
    }

    public String getActivationDisabledText() {
        return activationDisabledText;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getRedeemedDate() {
        return redeemedDate;
    }

    public void setState(String state) { this.state = state; }
}
