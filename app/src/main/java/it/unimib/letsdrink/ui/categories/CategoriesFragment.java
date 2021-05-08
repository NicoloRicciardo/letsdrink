package it.unimib.letsdrink.ui.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Category;

public class CategoriesFragment extends Fragment {

    FirebaseFirestore db;
    ArrayList<Category> categorie;
    //CategoriesViewModel categoriesViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /* View root = inflater.inflate(R.layout.fragment_categories, container, false);
        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);
        return root; */

        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.categories_recycler);
        final FragmentManager fm = requireActivity().getSupportFragmentManager();
        new FirebaseDBCategories().readCategories(new FirebaseDBCategories.DataStatus() {
            @Override
            public void dataIsLoaded(List<Category> listOfCategories) {
                new RecyclerCategories().setConfiguration(recyclerView, getContext(),listOfCategories, fm);
            }
        });
        return root;
    }

    /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView categoryRecycler = view.findViewById(R.id.categories_recycler);
        CategoryCardAdapter adapter = new CategoryCardAdapter();
        categoryRecycler.setAdapter(adapter);
        categorie = new ArrayList<>();

        if (savedInstanceState != null) {
            categorie = savedInstanceState.getParcelableArrayList("categorie");
            Log.d("prova", categorie.get(0).getName());
        } else {
            db.collection("Categorie").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    Category categoria = document.toObject(Category.class);
                                    categorie.add(categoria);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }


        adapter.setDati(getContext(), categorie);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        categoryRecycler.setLayoutManager(layoutManager);

        adapter.setListener(new CategoryCardAdapter.Listener() {
            public void onClick(int position) {
                Category categoria = categorie.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("categoria", categoria);
                Navigation.findNavController(categoryRecycler).navigate(R.id.action_navigation_categories_to_cocktailsFragment, bundle);
            }
        });

    } */


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}