package it.unimib.letsdrink.ui.categories;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.domain.Cocktail;

public class CocktailAdapter extends RecyclerView.Adapter<CocktailItemView> {
    private List<Cocktail> listOfCocktails;
    private CocktailAdapter.OnItemClickListener listener;
    private Context context;

    CocktailAdapter(List<Cocktail> listOfCocktails, Context context) {
        this.listOfCocktails = listOfCocktails;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(CocktailAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

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
