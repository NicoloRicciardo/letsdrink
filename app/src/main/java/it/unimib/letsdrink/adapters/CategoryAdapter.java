package it.unimib.letsdrink.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.ui.categories.CategoryItemView;

//Adapter per il RecyclerView di cardview di categorie
public class CategoryAdapter extends RecyclerView.Adapter<CategoryItemView> {
    private final List<Category> listOfCategories;
    private OnItemClickListener listener;
    private final Context context;

    public CategoryAdapter (List<Category> listOfCategories, Context context) {
        this.listOfCategories = listOfCategories;
        this.context=context;
    }

    //L'interfaccia per il click sulle cardview
    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    //Metodo che crea per ogni cardview di Category un CategoryItemView (ossia un ViewHolder)
    @NonNull
    @Override
    public CategoryItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryItemView (parent, listener, context);
    }

    //Metodo che associa a ogni ViewHolder la propria categoria
    @Override
    public void onBindViewHolder(@NonNull CategoryItemView holder, int position) {
        holder.bind(listOfCategories.get(position));
    }

    //Metodo che restituisce la dimensione della lista di categorie da associare ai viewholder
    @Override
    public int getItemCount() {
        if (listOfCategories != null)
            return listOfCategories.size();
        return 0;
    }
}