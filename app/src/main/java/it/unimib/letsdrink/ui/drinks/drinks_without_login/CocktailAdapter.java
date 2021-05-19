package it.unimib.letsdrink.ui.drinks.drinks_without_login;

import android.content.Context;
import android.util.Log;
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

public class CocktailAdapter extends RecyclerView.Adapter<CocktailItemView> implements Filterable {
    private List<Cocktail> listOfCocktails;
    private CocktailAdapter.OnItemClickListener listener;
    private Context context;
    private List<Cocktail> allListOfCocktails;

    CocktailAdapter(List<Cocktail> listOfCocktails, Context context) {
        this.listOfCocktails = listOfCocktails;
        this.context = context;
        allListOfCocktails = new ArrayList<>();
        allListOfCocktails.addAll(listOfCocktails);
    }


    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(CocktailAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<Cocktail> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(allListOfCocktails);
            } else {
                for (Cocktail cocktail: allListOfCocktails) {
                    if (cocktail.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(cocktail);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listOfCocktails.clear();
            listOfCocktails.addAll((Collection<? extends Cocktail>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public CocktailItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CocktailItemView(parent, listener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailItemView holder, int position) {
        holder.bind(listOfCocktails.get(position));
    }

    @Override
    public int getItemCount() {
        return listOfCocktails.size();
    }
}
