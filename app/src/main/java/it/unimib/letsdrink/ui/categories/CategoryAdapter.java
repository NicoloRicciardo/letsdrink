package it.unimib.letsdrink.ui.categories;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.letsdrink.domain.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryItemView> {
    private List<Category> listOfCategories;
    private OnItemClickListener listener;
    private Context context;

    CategoryAdapter (List<Category> listOfCategories, Context context) {
        this.listOfCategories = listOfCategories;
        this.context=context;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryItemView (parent, listener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryItemView holder, int position) {
        holder.bind(listOfCategories.get(position));
    }

    @Override
    public int getItemCount() {
        return listOfCategories.size();
    }
}
