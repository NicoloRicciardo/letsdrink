package it.unimib.letsdrink.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.profile.CustomDrinkItemView;

public class CustomDrinkAdapter extends RecyclerView.Adapter<CustomDrinkItemView> {
    private List<Cocktail> listOfCocktails;
    private CustomDrinkAdapter.OnItemClickListener listener;
    private final Context context;

    public CustomDrinkAdapter(List<Cocktail> listOfCocktails, Context context) {
        this.listOfCocktails = listOfCocktails;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
        void onDeleteClick(int position, View v);
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

    public void setListOfCocktails(List <Cocktail> listOfCocktails) {
        this.listOfCocktails = listOfCocktails;
    }

}
