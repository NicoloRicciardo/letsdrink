package it.unimib.letsdrink.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.adapters.CategoryAdapter;
import it.unimib.letsdrink.firebaseDB.FirebaseDBCategories;

public class CategoriesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.categories_recycler);
        setHasOptionsMenu(true);
        new FirebaseDBCategories().readCategories(listOfCategories -> {
            CategoryAdapter categoryAdapter = new CategoryAdapter(listOfCategories, getContext());
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            recyclerView.setAdapter(categoryAdapter);

            categoryAdapter.setOnItemClickListener((position, v) -> {
                Fragment cocktailsCategoryFragment = CocktailsCategoryFragment.newInstance(listOfCategories.get(position).getName(), listOfCategories.get(position).getImageUrl(),
                        listOfCategories.get(position).getDrinks());
                Bundle bundle = new Bundle();
                bundle.putString("name", listOfCategories.get(position).getName());
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_categories_to_cocktailsCategoryFragment, bundle);

            });
        });
        return root;
    }


}