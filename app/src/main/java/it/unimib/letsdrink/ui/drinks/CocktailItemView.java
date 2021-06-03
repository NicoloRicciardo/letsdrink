package it.unimib.letsdrink.ui.drinks;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;

public class CocktailItemView extends RecyclerView.ViewHolder{

    private TextView name;
    private ImageView image;
    private Context context;
    private ImageButton imgBtn;
    private FirebaseUser currentUser;

    CocktailItemView (ViewGroup parent, final CocktailAdapter.OnItemClickListener listener, Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.card_view_cocktail, parent, false));

        this.context=context;
        name = itemView.findViewById(R.id.text_cocktail);
        image = itemView.findViewById(R.id.image_cocktail);
        imgBtn = itemView.findViewById(R.id.love_cocktail_card);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

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
                if(currentUser!=null) {
                    imgBtn.setColorFilter(Color.RED);
                    Log.d("cuore", "cuore premuto");
                }else{
                    Toast.makeText(context,"Devi essere loggato per salvare un cocktail", Toast.LENGTH_SHORT ).show();
                }
            }
        });
    }
    void bind(Cocktail cocktail){
        name.setText(cocktail.getName());
        Glide.with(context).load(cocktail.getImageUrl()).into(image);
    }

}
