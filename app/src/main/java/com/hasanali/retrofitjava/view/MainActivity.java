package com.hasanali.retrofitjava.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasanali.retrofitjava.R;
import com.hasanali.retrofitjava.adapter.RecyclerViewAdapter;
import com.hasanali.retrofitjava.model.CryptoModel;
import com.hasanali.retrofitjava.service.CryptoAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // https://api.nomics.com/v1/prices?key=b8dc22aa51465dfe705cd7cc1e17ff71fe6c3f5e

    private String BASE_URL = "https://api.nomics.com/v1/";
    ArrayList<CryptoModel> cryptoModels;
    Retrofit retrofit;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        // Gson : JSON ‘dan objeye ya da objeden json ‘a döndürme kütüphanesidir.
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        loadData();
    }

    private void loadData() {
        CryptoAPI cryptoAPI = retrofit.create(CryptoAPI.class);

        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(cryptoAPI.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse));

        /* disposible: RxJava'da yaptığımız işlemleri tutan bir çöp torbasıdır.
           subscribeOn: Gözlemleme işlemini yapacağımız thread [(IO: network,database),(UI:kullanıcı arayüz),(Default:CPU)]
           observeOn: Alınan sonuçlar gözlemlenir.
           subscribe: Alınan sonuçlar işlenir.
         */

        /*
        Call<List<CryptoModel>> call = cryptoAPI.getData();

        call.enqueue(new Callback<List<CryptoModel>>() {
            @Override
            public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                if (response.isSuccessful()) {
                    List<CryptoModel> responseList = response.body();
                    cryptoModels = new ArrayList<>(responseList);
                    setRecyclerView_Adapter();
                }
            }
            @Override
            public void onFailure(Call<List<CryptoModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        */
    }

    private void handleResponse(List<CryptoModel> cryptoModelList) {
        cryptoModels = new ArrayList<>(cryptoModelList);
        Collections.sort(cryptoModels, new Comparator<CryptoModel>() {
            @Override
            public int compare(CryptoModel cryptoModel, CryptoModel t1) {
                return Float.compare(cryptoModel.price,t1.price);
            }
        });
        Collections.reverse(cryptoModels);
        setRecyclerView_Adapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void setRecyclerView_Adapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(cryptoModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}