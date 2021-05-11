package it.unimib.letsdrink.ui.drinks.drinks_with_login;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailAdapter;

public class CocktailItemView_With_Login extends RecyclerView.ViewHolder {
    private TextView name;
    private ImageView image;
    private Context context;
    private ImageButton imgBtn;

    CocktailItemView_With_Login (ViewGroup parent, final CocktailAdapter_With_Login.OnItemClickListener listener, Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.card_view_cocktail_with_login, parent, false));

        this.context=context;
        name = itemView.findViewById(R.id.text_cocktail_with_login);
        image = itemView.findViewById(R.id.image_cocktail_with_login);
        imgBtn = itemView.findViewById(R.id.love_cocktail_card);

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

        imgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                imgBtn.setColorFilter(Color.RED);
                Log.d("cuore", "cuore premuto");
            }
        });
    }
    void bind(Cocktail cocktail){
        name.setText(cocktail.getName());
        Glide.with(context).load(cocktail.getImageUrl()).into(image);
    }

}
