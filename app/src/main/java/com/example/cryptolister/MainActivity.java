package com.example.cryptolister;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText searchEdit;
    private RecyclerView currenciesRV;
    private ProgressBar loadingPB;
    private ArrayList<CurrencyRVModal> currencyRVModalArrayList;
    private CurrencyAdapter currencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assigning values to each controls in the layout.
        searchEdit = findViewById(R.id.edit);
        currenciesRV = findViewById(R.id.recycle);
        loadingPB = findViewById(R.id.progressBar);
        currencyRVModalArrayList = new ArrayList<>();
        currencyAdapter = new CurrencyAdapter(currencyRVModalArrayList, this);
        //We set adapter to recyclerView
        currenciesRV.setLayoutManager(new LinearLayoutManager(this));
        //Here we create the currency recyclerView Adapter.
        currenciesRV.setAdapter(currencyAdapter);
        //We pass the get currency data here.
        getCurrencyData();

        //Here we link the search EditText to the filterCurrencies
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //
                filterCurrencies(s.toString());

            }
        });


    }
    //Now we work on the Search for crypto feature.
    private void filterCurrencies(String currency){
        ArrayList<CurrencyRVModal> filteredList = new ArrayList<>();
        //Now we create a for loop to help search through the arraylist to get the right crypto.
        for(CurrencyRVModal item : currencyRVModalArrayList) {
            if(item.getName().toLowerCase().contains(currency.toLowerCase())){
                filteredList.add(item);
            }
        }

        //We check if the filtered list is empty or not
        if(filteredList.isEmpty()){
            Toast.makeText(MainActivity.this, "No currency found", Toast.LENGTH_SHORT).show();
        }
        //We go to our CuurencyAdapter class to create a public method that will help filter the list.
        else{
            //We now call filterList here
            currencyAdapter.filterList(filteredList);
        }
    }
    //Here we create a method to get the currency data from the api
    private void getCurrencyData(){
        //We make the loading progressbar visible
        loadingPB.setVisibility(View.VISIBLE);
        //Step 1: Pass in the API url
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        //Step 2: USe volley to get the requestqueue.
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //From our postman checking of the API we can see that it came in as a JSOn so we create a JSOn object Request.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //On this side we set the visibility as gone.
                loadingPB.setVisibility(View.GONE);
                //Here what we are executing is wor
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObject = dataArray.getJSONObject(i);
                        //So here we try to get the exact data using their corresponding keys
                        String name = dataObject.getString("name");
                        String symbol = dataObject.getString("symbol");
                        //Here we try to narrow our search down to the exact thing we need which is the price of the coin through the key value pairs.
                        JSONObject quote = dataObject.getJSONObject("quote");
                        JSONObject USD = quote.getJSONObject("USD");
                        double price = USD.getDouble("price");
                        currencyRVModalArrayList.add(new CurrencyRVModal(name, symbol, price));
                    }
                    //This is responsible for dataset change accross the recyclerview
                    currencyAdapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Fail to extract json data", Toast.LENGTH_SHORT).show();
                }

            }
            //We also create the Response.ErrorListener
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //On this side we set the visibility as gone.
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Fail to get data", Toast.LENGTH_SHORT).show();


            }
        }){
            @Override
            public Map<String, String> getHeaders(){
                HashMap<String, String > headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY", "8c0d4188-b02d-4459-be33-3ec813428c3e");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }
}