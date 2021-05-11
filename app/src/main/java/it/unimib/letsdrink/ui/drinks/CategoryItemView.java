package it.unimib.letsdrink.ui.drinks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Category;

public class CategoryItemView extends RecyclerView.ViewHolder {


    private TextView name;
    private ImageView image;
    private Context context;

    CategoryItemView (ViewGroup parent, final CategoryAdapter.OnItemClickListener listener, Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.card_view_category, parent, false));

        this.context=context;
        name = itemView.findViewById(R.id.text_category);
        image = itemView.findViewById(R.id.image_category);

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
    void bind(Category category){
        name.setText(category.getName());
        Glide.with(context).load(category.getImageUrl()).into(image);
    }
}

