package it.unimib.letsdrink.ui.profile;

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

public class CustomDrinkItemView extends RecyclerView.ViewHolder{

    private TextView name;
    private ImageView image;
    private ImageButton delete;
    private Context context;

    public CustomDrinkItemView (ViewGroup parent, final CustomDrinkAdapter.OnItemClickListener listener, Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.card_view_custom_drinks, parent, false));

        this.context=context;
        name = itemView.findViewById(R.id.text_custom_drink);
        image = itemView.findViewById(R.id.image_custom_drink);
        delete = itemView.findViewById(R.id.delete_custom_drink_card);

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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position, v);
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
