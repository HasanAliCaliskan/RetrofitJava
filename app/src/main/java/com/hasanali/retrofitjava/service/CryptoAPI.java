package com.hasanali.retrofitjava.service;

import com.hasanali.retrofitjava.model.CryptoModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CryptoAPI {

    // GET, POST, UPDATE, DELETE
    // URL BASE --> www.website.com  (https://api.nomics.com/v1)
    // GET --> price?key=xxx  (prices?key=b8dc22aa51465dfe705cd7cc1e17ff71fe6c3f5e)

    @GET("prices?key=b8dc22aa51465dfe705cd7cc1e17ff71fe6c3f5e")
    Observable<List<CryptoModel>> getData();
    /* Observable gözlemlenebilir objedir ve yayın yapar.
       Veri setinde bir değişiklik olduğunda bunu gözleyen objeye bildirir.
     */

    // Call<List<CryptoModel>> getData();
}
