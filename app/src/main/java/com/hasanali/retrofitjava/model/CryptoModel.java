package com.hasanali.retrofitjava.model;

import com.google.gson.annotations.SerializedName;

public class CryptoModel {

    @SerializedName("currency") // JSON dosyasındaki key
    public String currency;

    @SerializedName("price") // JSON dosyasındaki key
    public Float price;
}
