package it.unimib.letsdrink.ui.categories;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import it.unimib.letsdrink.R;


public class DrinksFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drinks,container, false);
        ViewPager viewPager = view.findViewById(R.id.view_pager_drinks);

        TabLayoutAdapter adapter = new TabLayoutAdapter(getChildFragmentManager());
        adapter.addFragment(new CategoriesFragment(), "Categorie");
        adapter.addFragment(new CocktailsFragment(), "Cocktail");
        viewPager.setAdapter(adapter);

        TabLayout tabs = view.findViewById(R.id.tab_drinks);
        tabs.setupWithViewPager(viewPager);
        return view;
    }


}