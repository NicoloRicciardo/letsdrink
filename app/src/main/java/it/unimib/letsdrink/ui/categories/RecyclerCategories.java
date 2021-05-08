package it.unimib.letsdrink.ui.categories;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.letsdrink.domain.Category;

public class RecyclerCategories {
    void setConfiguration(RecyclerView recyclerView, final Context context, final List<Category> listOfCategories, final FragmentManager fm){
        CategoryAdapter categoryAdapter = new CategoryAdapter (listOfCategories, context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(categoryAdapter);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                /* DialogFragment fragment = ExpandedEvent.newInstance(listOfEvent.get(position).getTitle(), listOfEvent.get(position).getDescription(),
                        listOfEvent.get(position).getImg(), listOfEvent.get(position).getDateString(), listOfEvent.get(position).getOpeningHours(),
                        listOfEvent.get(position).getStreet(), listOfEvent.get(position).getTag(), listOfEvent.get(position).getEventType());
                fragment.show(fm, "ExpandedEvent"); */


            }
        });
    }
}