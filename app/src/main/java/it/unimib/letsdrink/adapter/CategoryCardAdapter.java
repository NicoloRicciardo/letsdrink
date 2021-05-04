package it.unimib.letsdrink.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Category;

public class CategoryCardAdapter extends RecyclerView.Adapter<CategoryCardAdapter.ViewHolder> {

    private Listener listener;
    private Context context;
    private ArrayList<Category> categorie = new ArrayList<Category>();

    public interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public int getItemCount() {
        return categorie.size();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public CategoryCardAdapter() {
    }

    public void setDati(Context context, ArrayList<Category> categorie) {
        this.context = context;
        this.categorie = categorie;

    }

    @Override
    public CategoryCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(context).inflate(R.layout.card_view_category, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.image_category);
        Glide.with(context).load(categorie.get(position).getImageUrl()).into(imageView);
        TextView textView = cardView.findViewById(R.id.text_category);
        textView.setText(categorie.get(position).getNome());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }


}

