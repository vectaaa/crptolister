package com.example.cryptolister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    //Here we create the arraylist while linking the Modal class
    private ArrayList<CurrencyRVModal> currencyRVModalArrayList;
    // We create the context.
    private Context context;
    //Here we create the variable responsible for displaying the last 2 digits after the decimal points.
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public CurrencyAdapter(ArrayList<CurrencyRVModal> currencyRVModalArrayList, Context context) {
        this.currencyRVModalArrayList = currencyRVModalArrayList;
        this.context = context;
    }

    //We create the filterlist method here
    public void filterList(ArrayList<CurrencyRVModal> filteredList){
        currencyRVModalArrayList = filteredList;
        notifyDataSetChanged();

    }
    @NonNull
    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //This is were we inflate the layout from the currency_rv_item xml file.
        View view = LayoutInflater.from(context).inflate(R.layout.currency_rv_item, parent, false);
        return new CurrencyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.ViewHolder holder, int position) {
        CurrencyRVModal currencyRVModal = currencyRVModalArrayList.get(position);
        holder.currencyNameTV.setText(currencyRVModal.getName());
        holder.symbolTV.setText(currencyRVModal.getSymbol());
        holder.rateTV.setText("$ " +df2.format(currencyRVModal.getPrice()));
    }

    @Override
    public int getItemCount() {
        return currencyRVModalArrayList.size();
    }


    //We create constructor
    protected class ViewHolder extends RecyclerView.ViewHolder{
        //We assign our views here new variables.
        private TextView currencyNameTV, symbolTV, rateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Now we declare and attach them.
            currencyNameTV = itemView.findViewById(R.id.idCurrencyName);
            symbolTV = itemView.findViewById(R.id.idTVSymbol);
            rateTV = itemView.findViewById(R.id.idRateTv);
        }
    }
}
