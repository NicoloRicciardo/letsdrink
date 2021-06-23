package it.unimib.letsdrink.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.adapters.CocktailAdapter;
import it.unimib.letsdrink.firebaseDB.FirebaseDBFavorites;

//classe che rappresenta la cardview di un cocktail
public class CocktailItemView extends RecyclerView.ViewHolder {

    private final TextView name;
    private final ImageView image;
    private final Context context;
    private final ImageButton imgBtn;
    private final FirebaseUser currentUser;
    FirebaseDBFavorites db;
    int color = 0;

    public CocktailItemView(ViewGroup parent, final CocktailAdapter.OnItemClickListener listener, Context context) {
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

        //click sulla card
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position, v);
                }
            }
        });

        //click sul cuore
        imgBtn.setOnClickListener(view -> {
            if (currentUser != null) {
                if (listener != null) {
                    int position = getAbsoluteAdapterPosition();
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
            } else { // se non sei loggato appare la dialog che ti esorta a farlo
                new MaterialAlertDialogBuilder(context, R.style.DialogTheme)
                        .setTitle(R.string.title_add_fav_not_logged)
                        .setMessage(R.string.message_add_fav_not_logged)
                        .setPositiveButton(R.string.button_ok, null)
                        .setBackground(new ColorDrawable(Color.TRANSPARENT))
                        .show();
            }
        });

    }

    //associazione dei dati alla cardview
    public void bind(Cocktail cocktail) {
        name.setText(cocktail.getName());
        Glide.with(context).load(cocktail.getImageUrl()).into(image);
        if(currentUser != null) {
            //colorazione del cuore della card del cocktail presente nei preferiti
            db.readCocktails(cocktailList -> {
                for (int i = 0; i < cocktailList.size(); i++) {
                    if (cocktailList.get(i).getName().equals(cocktail.getName())) {
                        imgBtn.setImageResource(R.drawable.ic_favorites_black_24dp);
                        imgBtn.setColorFilter(Color.RED);
                        color = 1;
                        return;
                    }
                }
                imgBtn.setImageResource(R.drawable.ic_heart_black_line_24_dp);
                imgBtn.setColorFilter(Color.WHITE);
                color = 0;
            });
        }
    }

}
