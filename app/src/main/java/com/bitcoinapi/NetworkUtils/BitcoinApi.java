package com.bitcoinapi.NetworkUtils;

import com.bitcoinapi.Object.BitcoinPrice;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Korisnik on 02.03.2018.
 */

public interface BitcoinApi {

    @GET("/indices/global/ticker/{symbol}")
    Call<BitcoinPrice> getPrice(@Path("symbol")String symbol);
}
