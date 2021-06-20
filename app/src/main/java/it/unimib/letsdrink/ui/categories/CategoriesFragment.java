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

//fragment relativo alle categorie
public class CategoriesFragment extends Fragment {

    //metodo per creare la gerarchia della view associata al fragment
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.categories_recycler);
        FirebaseDBCategories firebaseCategories= new FirebaseDBCategories();
        //visualizzazione a schermo delle categorie ottenute da db tramite l'adapter
        firebaseCategories.readCategories(listOfCategories -> {
            CategoryAdapter categoryAdapter = new CategoryAdapter(listOfCategories, getContext());
            //settiamo il tipo di layoutManager (Gridlayout)
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            //al recyclerView settiamo l'adapter che deve utilizzare
            recyclerView.setAdapter(categoryAdapter);
            //al click delle cardview
            categoryAdapter.setOnItemClickListener((position, v) -> {
                //impostazione delle variabili statiche del cocktailsCategoryFragment
                Fragment cocktailsCategoryFragment = CocktailsCategoryFragment.newInstance(listOfCategories.get(position).getName(), listOfCategories.get(position).getImageUrl(),
                        listOfCategories.get(position).getDrinks());
                Bundle bundle = new Bundle();
                //salviamo nel bundle il nome della categoria cliccata
                bundle.putString("name", listOfCategories.get(position).getName());
                //navigazione dalle categorie ai cocktail della relativa categoria
                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_categories_to_cocktailsCategoryFragment, bundle);

            });
        });
        //restituzione della vista
        return root;
    }


}