package it.unimib.letsdrink.adapters;

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
import it.unimib.letsdrink.ui.home.CocktailItemView;

//Adapter per il RecyclerView di cardview di cocktail
public class CocktailAdapter extends RecyclerView.Adapter<CocktailItemView> implements Filterable {
    private List<Cocktail> listOfCocktails;
    private final List<Cocktail> listOfAllCocktails;
    private CocktailAdapter.OnItemClickListener listener;
    private final Context context;

    public CocktailAdapter(List<Cocktail> listOfCocktails, Context context) {
        this.listOfCocktails = listOfCocktails;
        this.context = context;
        listOfAllCocktails = new ArrayList<>();
        listOfAllCocktails.addAll(listOfCocktails);
    }

    //L'interfaccia per il click sulle cardview e il click sull'icona del cuore
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
        void onSaveClick(int position, View v);
    }

    public void setOnItemClickListener(CocktailAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    //Gestione ricerca cocktail per nome
    Filter filter = new Filter() {
        //Automatic on background thread
        //Metodo per eseguire il filtraggio
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Cocktail> filteredList = new ArrayList<>();
            //Se l'utente non inserisce alcuna stringa si aggiunge all'array di cocktail filtrati tutti i cocktail
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(listOfAllCocktails);
            }
            else {
                //Se l'utente inserisce una stringa si aggiunge all'array di cocktail filtrati i cocktail che hanno nel nome quella stringa
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
        //Metodo che aggiunge alla lista di cocktail, la lista di cocktail filtrati e notifica l'adapter che i dati sono cambiati
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listOfCocktails.clear();
            listOfCocktails.addAll((Collection<? extends Cocktail>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    //Metodo che crea per ogni cardview di Cocktail un CocktailItemView (ossia un ViewHolder)
    @NonNull
    @Override
    public CocktailItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CocktailItemView(parent, listener, context);
    }

    //Metodo che associa a ogni ViewHolder il proprio cocktail
    @Override
    public void onBindViewHolder(@NonNull CocktailItemView holder, int position) {
        holder.bind(listOfCocktails.get(position));
    }

    //Metodo che restituisce la dimensione della lista di cocktails da associare ai viewholder
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
