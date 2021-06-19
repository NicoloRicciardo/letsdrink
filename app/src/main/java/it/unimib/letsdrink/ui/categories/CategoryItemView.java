package it.unimib.letsdrink.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.adapters.CategoryAdapter;
import it.unimib.letsdrink.domain.Category;

public class CategoryItemView extends RecyclerView.ViewHolder {

    private final TextView name;
    private final ImageView image;
    private final Context context;

    public CategoryItemView (ViewGroup parent, final CategoryAdapter.OnItemClickListener listener, Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.card_view_category, parent, false));

        this.context=context;
        name = itemView.findViewById(R.id.text_category);
        image = itemView.findViewById(R.id.image_category);

        itemView.setOnClickListener(v -> {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position, v);
                }
            }
        });
    }
    public void bind(Category category){
        name.setText(category.getName());
        Glide.with(context).load(category.getImageUrl()).into(image);
    }
}

