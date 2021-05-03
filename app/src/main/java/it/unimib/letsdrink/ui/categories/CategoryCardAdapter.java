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

import it.unimib.letsdrink.R;

public class CategoryCardAdapter extends RecyclerView.Adapter<CategoryCardAdapter.ViewHolder>{

    private Listener listener;
    private ArrayList<String> nomiCategorie;
    private Context context;
    private ArrayList<Category> categorie;

    interface Listener {
        void onClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_category);
        }
    }

    @Override
    public int getItemCount(){
        return categorie.size();
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public CategoryCardAdapter(Context context, ArrayList<Category> categorie){
        this.context = context;
        this.categorie = categorie;
    }

    @Override
    public CategoryCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(context).inflate(R.layout.card_view_category, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position){
        //Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), null);
        //imageView.setImageDrawable(drawable);
        //imageView.setContentDescription(captions[position]);
        Glide.with(context).load(categorie.get(position).getImageUrl()).into(holder.imageView);
        /*
        TextView textView = (TextView)cardView.findViewById(R.id.text_category);
        textView.setText(nomiCategorie.get(position));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
         */
    }


}

