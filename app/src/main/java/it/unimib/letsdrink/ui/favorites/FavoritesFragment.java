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
import com.google.firebase.auth.FirebaseUser;
import org.jetbrains.annotations.NotNull;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.adapters.CocktailAdapter;
import it.unimib.letsdrink.firebaseDB.FirebaseDBFavorites;
import it.unimib.letsdrink.ui.home.CocktailDetailFragment;

//fragment dei cocktail preferiti
public class FavoritesFragment extends Fragment {
    private TextView mNoFav;
    private CocktailAdapter mFavoriteCocktailAdapter;

    //chiamato per creare la gerarchia di viste associata al fragment
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    //chiamato quando la vista è già stata creata
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNoFav = view.findViewById(R.id.text_favorites_none);
        TextView mNotLogged = view.findViewById(R.id.text_favorites_not_logged);
        RecyclerView recyclerView = view.findViewById(R.id.favorites_recycler);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null) {
            FirebaseDBFavorites db = new FirebaseDBFavorites(FirebaseAuth.getInstance().getCurrentUser().getUid());

            //visualizzazione a schermo dei cocktail preferiti nel caso siano presenti (altrimenti verrà visualizzato un messaggio)
            db.readCocktails(listOfFavorites -> {
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
                            CocktailDetailFragment.newInstance(listOfFavorites.get(position).getName(), listOfFavorites.get(position).getMethod(),
                                    listOfFavorites.get(position).getIngredients(), listOfFavorites.get(position).getImageUrl());
                            Navigation.findNavController(requireView()).navigate(R.id.action_navigation_favorites_to_cocktailDetailFragment);
                        }

                        @Override
                        public void onSaveClick(int position, View v) {
                            db.deleteFavoriteCocktail(listOfFavorites.get(position), listOfFavoritesCocktail -> {
                                //modifichiamo la lista di cocktail dell'adapter (1 in meno se eliminato)
                                mFavoriteCocktailAdapter.setListOfCocktails(listOfFavoritesCocktail);
                                //puliamo la recyclerView
                                recyclerView.getRecycledViewPool().clear();
                                mFavoriteCocktailAdapter.notifyDataSetChanged();
                                //refresh della pagina
                                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_favorites_self);
                            });
                        }
                    });
                }

            });
        } else {
            mNotLogged.setVisibility(View.VISIBLE);
        }
    }
}