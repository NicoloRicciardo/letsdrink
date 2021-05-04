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
import it.unimib.letsdrink.domain.Cocktail;

public class CocktailCardAdapter extends RecyclerView.Adapter<CocktailCardAdapter.ViewHolder>{

    private Listener listener;
    private Context context;
    private ArrayList<Cocktail> bevande;

    public interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }

    @Override
    public int getItemCount(){
        return bevande.size();
    }

    public void setListener(CocktailCardAdapter.Listener listener){
        this.listener = listener;
    }

    public CocktailCardAdapter(){
    }

    public void setDati(Context context, ArrayList<Cocktail>  bevande){
        this.context = context;
        this.bevande = bevande;
    }

    @Override
    public CocktailCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(context).inflate(R.layout.card_view_cocktail, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.image_cocktail);
        Glide.with(context).load(bevande.get(position).getImageUrl()).into(imageView);
        TextView textView = cardView.findViewById(R.id.text_cocktail);
        textView.setText(bevande.get(position).getNome());
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
