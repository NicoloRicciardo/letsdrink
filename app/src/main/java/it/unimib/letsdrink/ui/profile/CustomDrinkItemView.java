package it.unimib.letsdrink.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.adapters.CustomDrinkAdapter;
import it.unimib.letsdrink.domain.Cocktail;

//classe che rappresenta la cardview di un custom drink
public class CustomDrinkItemView extends RecyclerView.ViewHolder{

    private final TextView name;
    private final ImageView image;
    private final Context context;

    public CustomDrinkItemView (ViewGroup parent, final CustomDrinkAdapter.OnItemClickListener listener, Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.card_view_custom_drinks, parent, false));

        this.context=context;
        name = itemView.findViewById(R.id.text_custom_drink);
        image = itemView.findViewById(R.id.image_custom_drink);
        ImageButton delete = itemView.findViewById(R.id.delete_custom_drink_card);

        //click sulla cardview
        itemView.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position, v);
                }
            }
        });

        //click sul cestino
        delete.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(position, v);
                }
            }
        });

    }

    //associazione dei dati alla cardview
    public void bind(Cocktail cocktail){
        name.setText(cocktail.getName());
        Glide.with(context).load(cocktail.getImageUrl()).into(image);
    }

}
