package com.testing.otp.mobileotp;

public class userProfile {
    public String userProduct;
    public String userQuantity;
    public String userid;
    public String userPrice;

    public userProfile(){

    }
    public userProfile(String userProduct, String userQuantity,String userid,String userPrice) {
        this.userProduct = userProduct;
        this.userQuantity = userQuantity;
        this.userid=userid;
        this.userPrice=userPrice;
    }

    public String getUserProduct() {
        return userProduct;
    }

    public void setUserProduct(String userProduct) {
        this.userProduct = userProduct;
    }

    public String getUserQuantity() {
        return userQuantity;
    }

    public void setUserQuantity(String userQuantity) {
        this.userQuantity = userQuantity;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserPrice() {
        return userPrice;
    }

    public void setUserPrice(String userPrice) {
        this.userPrice = userPrice;
    }
}
