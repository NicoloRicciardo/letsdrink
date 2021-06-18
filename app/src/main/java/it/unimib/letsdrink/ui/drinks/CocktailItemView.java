package it.unimib.letsdrink.ui.drinks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.favorites.FirebaseDBFavorites;

public class CocktailItemView extends RecyclerView.ViewHolder {

    private TextView name;
    private ImageView image;
    private Context context;
    private ImageButton imgBtn;
    private FirebaseUser currentUser;
    FirebaseDBFavorites db;
    int color = 0;

    CocktailItemView(ViewGroup parent, final CocktailAdapter.OnItemClickListener listener, Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.card_view_cocktail, parent, false));

        this.context = context;
        name = itemView.findViewById(R.id.text_cocktail);
        image = itemView.findViewById(R.id.image_cocktail);
        imgBtn = itemView.findViewById(R.id.love_cocktail_card);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null) {
            String id = currentUser.getUid();
            db = new FirebaseDBFavorites(id);
        }

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

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onSaveClick(position, view);
                            if (color == 0) {
                                imgBtn.setImageResource(R.drawable.ic_favorites_black_24dp);
                                imgBtn.setColorFilter(Color.RED);
                                color = 1;
                            } else {
                                imgBtn.setImageResource(R.drawable.ic_heart_black_line_24_dp);
                                imgBtn.setColorFilter(Color.WHITE);
                                color = 0;
                            }
                        }
                    }
                } else {
                    Log.d("CocktailItemView", "loggati");
                    new MaterialAlertDialogBuilder(context, R.style.DialogTheme)
                            .setTitle("Errore")
                            .setMessage("Devi prima loggarti!")
                            .setPositiveButton("OK", null)
                            .setBackground(new ColorDrawable(Color.TRANSPARENT))
                            .show();
                }
            }
        });

    }

    void bind(Cocktail cocktail) {
        name.setText(cocktail.getName());
        Glide.with(context).load(cocktail.getImageUrl()).into(image);
        if(currentUser != null) {
            db.readCocktails(new FirebaseDBFavorites.DataStatus() {
                @Override
                public void dataIsLoaded(List<Cocktail> cocktailList) {
                    for (int i = 0; i < cocktailList.size(); i++) {
                        if (cocktailList.get(i).getName().equals(cocktail.getName())) {
                            imgBtn.setImageResource(R.drawable.ic_favorites_black_24dp);
                            imgBtn.setColorFilter(Color.RED);
                            color = 1;
                        }
                    }
                }
            });
        }


    }

}
