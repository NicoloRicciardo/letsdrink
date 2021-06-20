package it.unimib.letsdrink.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.profile.CustomDrinkItemView;

//Adapter per il recyclerView delle cardview di CustomDrink
public class CustomDrinkAdapter extends RecyclerView.Adapter<CustomDrinkItemView> {
    private List<Cocktail> listOfCocktails;
    private CustomDrinkAdapter.OnItemClickListener listener;
    private final Context context;

    public CustomDrinkAdapter(List<Cocktail> listOfCocktails, Context context) {
        this.listOfCocktails = listOfCocktails;
        this.context = context;
    }

    //L'interfaccia per il click sulle cardview e il click sull'icona del cestino
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
        void onDeleteClick(int position, View v);
    }

    public void setOnItemClickListener(CustomDrinkAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    //Metodo che crea per ogni cardview di Customdrink un CustomDrinkItemView (ossia un ViewHolder)
    @NonNull
    @Override
    public CustomDrinkItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomDrinkItemView(parent, listener, context);
    }

    //Metodo che associa a ogni ViewHolder il proprio Customdrink
    @Override
    public void onBindViewHolder(@NonNull CustomDrinkItemView holder, int position) {
        holder.bind(listOfCocktails.get(position));
    }

    //Metodo che restituisce la dimensione della lista di Customdrinks da associare ai viewholder
    @Override
    public int getItemCount() {
        if (listOfCocktails != null)
            return listOfCocktails.size();
        return 0;
    }

    public void setListOfCocktails(List <Cocktail> listOfCocktails) {
        this.listOfCocktails = listOfCocktails;
    }

}
