package it.unimib.letsdrink.ui.drinks.drinks_with_login;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailAdapter;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailItemView;

public class CocktailAdapter_With_Login extends RecyclerView.Adapter<CocktailItemView_With_Login>{
    private List<Cocktail> listOfCocktails;
    private CocktailAdapter_With_Login.OnItemClickListener listener;
    private Context context;

    CocktailAdapter_With_Login(List<Cocktail> listOfCocktails, Context context) {
        this.listOfCocktails = listOfCocktails;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(CocktailAdapter_With_Login.OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CocktailItemView_With_Login onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CocktailItemView_With_Login(parent, listener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CocktailItemView_With_Login holder, int position) {
        holder.bind(listOfCocktails.get(position));
    }

    @Override
    public int getItemCount() {
        return listOfCocktails.size();
    }
}
