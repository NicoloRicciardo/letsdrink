package it.unimib.letsdrink.ui.categories;

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
import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.domain.Cocktail;

public class CocktailItemView extends RecyclerView.ViewHolder{

    private TextView name;
    private ImageView image;
    private Context context;

    CocktailItemView (ViewGroup parent, final CocktailAdapter.OnItemClickListener listener, Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.card_view_cocktail, parent, false));

        this.context=context;
        name = itemView.findViewById(R.id.text_cocktail);
        image = itemView.findViewById(R.id.image_cocktail);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, v);
                    }
                }
            }
        });
    }
    void bind(Cocktail cocktail){
        name.setText(cocktail.getName());
        Glide.with(context).load(cocktail.getImageUrl()).into(image);
    }

}
