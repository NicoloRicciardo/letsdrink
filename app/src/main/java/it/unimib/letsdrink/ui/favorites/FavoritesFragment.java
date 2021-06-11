package it.unimib.letsdrink.ui.favorites;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.CocktailAdapter;
import it.unimib.letsdrink.ui.profile.CustomDrinkDetailFragment;
import it.unimib.letsdrink.ui.profile.FirebaseDBCustomDrink;

public class FavoritesFragment extends Fragment {
    private TextView mNoFav;
    private CocktailAdapter mFavoriteCocktailAdapter;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoFav = view.findViewById(R.id.text_favorites_none);
        RecyclerView recyclerView = view.findViewById(R.id.favorites_recycler);

        FirebaseDBFavorites db = new FirebaseDBFavorites(FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.readCocktails(new FirebaseDBFavorites.DataStatus() {
            @Override
            public void dataIsLoaded(List<Cocktail> listOfFavorites) {
                if(listOfFavorites.size() == 0) {
                    mNoFav.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    mFavoriteCocktailAdapter = new CocktailAdapter(listOfFavorites, getContext());
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    recyclerView.setAdapter(mFavoriteCocktailAdapter);

                    mFavoriteCocktailAdapter.setOnItemClickListener(new CocktailAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Fragment cocktailDetail = CustomDrinkDetailFragment.newInstance(listOfFavorites.get(position).getName(), listOfFavorites.get(position).getMethod(),
                                    listOfFavorites.get(position).getIngredients(), listOfFavorites.get(position).getImageUrl());
                            Navigation.findNavController(getView()).navigate(R.id.action_navigation_favorites_to_cocktailDetailFragment);
                        }

                        @Override
                        public void onSaveClick(int position, View v) {
                            db.deleteFavoriteCocktail(listOfFavorites.get(position), new FirebaseDBFavorites.DataStatus() {
                                @Override
                                public void dataIsLoaded(List<Cocktail> listOfFavoritesCocktail) {
                                    mFavoriteCocktailAdapter.setListOfCocktails(listOfFavoritesCocktail);
                                    recyclerView.getRecycledViewPool().clear();
                                    mFavoriteCocktailAdapter.notifyDataSetChanged();
                                    Navigation.findNavController(getView()).navigate(R.id.action_navigation_favorites_self);
                                }
                            });
                        }
                    });
                }

            }
        });
    }
}