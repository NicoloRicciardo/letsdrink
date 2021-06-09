package it.unimib.letsdrink.ui.profile;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.unimib.letsdrink.domain.Cocktail;

public class CustomDrinkAdapter extends RecyclerView.Adapter<CustomDrinkItemView> {
    private List<Cocktail> listOfCocktails;
    private CustomDrinkAdapter.OnItemClickListener listener;
    private Context context;

    public CustomDrinkAdapter(List<Cocktail> listOfCocktails, Context context) {
        this.listOfCocktails = listOfCocktails;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(CustomDrinkAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomDrinkItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomDrinkItemView(parent, listener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomDrinkItemView holder, int position) {
        holder.bind(listOfCocktails.get(position));
    }

    @Override
    public int getItemCount() {
        if (listOfCocktails != null)
            return listOfCocktails.size();
        return 0;
    }

}
