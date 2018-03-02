package com.bitcoinapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcoinapi.NetworkUtils.BitcoinApi;
import com.bitcoinapi.Object.BitcoinPrice;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private AppCompatSpinner spinner;
    private TextView currency_textview;
    private static final String BASE_URL = "https://apiv2.bitcoinaverage.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.symbol_spinner);
        currency_textview = findViewById(R.id.currency_textview);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.symbol_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();






        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                      String symbol = adapterView.getItemAtPosition(i).toString();
                      displayCurrency(symbol);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void displayCurrency(String currency){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(BuildConfig.DEBUG){
            client.addInterceptor(interceptor);
        }



        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build());

        Retrofit retrofit = builder.build();
        BitcoinApi bitcoin = retrofit.create(BitcoinApi.class);
        Call<BitcoinPrice> call = bitcoin.getPrice("BTC"+currency);

        call.enqueue(new Callback<BitcoinPrice>() {
            @Override
            public void onResponse(Call<BitcoinPrice> call, Response<BitcoinPrice> response) {
                String currency = response.body().getBid();
                currency_textview.setText(currency);
            }

            @Override
            public void onFailure(Call<BitcoinPrice> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Cant connect to the Api", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
