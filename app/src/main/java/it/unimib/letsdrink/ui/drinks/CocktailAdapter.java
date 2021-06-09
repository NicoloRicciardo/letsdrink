package it.unimib.letsdrink.ui.drinks;

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

public class CocktailAdapter extends RecyclerView.Adapter<CocktailItemView> implements Filterable {
    private List<Cocktail> listOfCocktails, listOfAllCocktails;
    private CocktailAdapter.OnItemClickListener listener;
    private Context context;
    boolean noCocktailsFiltered;

    public CocktailAdapter(List<Cocktail> listOfCocktails, Context context) {
        this.listOfCocktails = listOfCocktails;
        this.context = context;
        listOfAllCocktails = new ArrayList<>();
        listOfAllCocktails.addAll(listOfCocktails);
        noCocktailsFiltered = false;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(CocktailAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public boolean getNoCocktailsFiltered(){
        return noCocktailsFiltered;
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
                filteredList.addAll(listOfAllCocktails);
            }
            else {
                for (Cocktail cocktail: listOfAllCocktails) {
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
            if(listOfCocktails.size() == 0)
                noCocktailsFiltered = true;
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
        if (listOfCocktails != null)
            return listOfCocktails.size();
        return 0;
    }

    public void setListOfCocktails(List<Cocktail> listOfCocktails) {
        this.listOfCocktails = listOfCocktails;
    }

    public List<Cocktail> getListOfCocktails() {
        return listOfCocktails;
    }
}
