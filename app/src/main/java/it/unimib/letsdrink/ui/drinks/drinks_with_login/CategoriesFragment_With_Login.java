package it.unimib.letsdrink.ui.drinks.drinks_with_login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.ui.drinks.CategoryAdapter;
import it.unimib.letsdrink.ui.drinks.FirebaseDBCategories;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailsCategoryFragment;


public class CategoriesFragment_With_Login extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.categories_recycler);
        setHasOptionsMenu(true);
        new FirebaseDBCategories().readCategories(new FirebaseDBCategories.DataStatus() {
            @Override
            public void dataIsLoaded(List<Category> listOfCategories) {
                CategoryAdapter categoryAdapter = new CategoryAdapter(listOfCategories, getContext());
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(categoryAdapter);

                categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Fragment cocktailsCategoryFragment_With_Login = CocktailsCategoryFragment_With_Login.newInstance(listOfCategories.get(position).getName(), listOfCategories.get(position).getImageUrl(),
                                listOfCategories.get(position).getDrinks());
                        Bundle bundle = new Bundle();
                        bundle.putString("name", listOfCategories.get(position).getName());
                        Navigation.findNavController(getView())
                                .navigate(R.id.action_navigation_categories_to_cocktailsCategoryFragment_With_Login, bundle);


                    }
                });
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}