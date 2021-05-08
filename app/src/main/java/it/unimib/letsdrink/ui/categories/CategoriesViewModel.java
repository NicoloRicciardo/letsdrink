package it.unimib.letsdrink.ui.categories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.unimib.letsdrink.adapter.CategoryCardAdapter;
import it.unimib.letsdrink.domain.Category;

public class CategoriesViewModel extends ViewModel {

   private MutableLiveData<ArrayList<Category>> categories;
    private FirebaseFirestore db;

    public CategoriesViewModel() {
        categories = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
    }

    private void setCategories(CategoryCardAdapter adapter) {

        ArrayList<Category> categoriesArray = new ArrayList<>();
        db.collection("Categorie").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Category categoria = document.toObject(Category.class);
                                categoriesArray.add(categoria);
                            }
                            categories.setValue(categoriesArray);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    public LiveData<ArrayList<Category>> getCategories(CategoryCardAdapter adapter){
        setCategories(adapter);
        return categories;
    }

    private void setCategories() {

        ArrayList<Category> categoriesArray = new ArrayList<>();
        db.collection("Categorie").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Category categoria = document.toObject(Category.class);
                                categoriesArray.add(categoria);
                            }
                            categories.setValue(categoriesArray);
                        }
                    }
                });

    }

    public LiveData<ArrayList<Category>> getCocktailsCategories(){
        setCategories();
        return categories;
    }

    public LiveData<String> getCategory(int position) {
        setCategories();
        ArrayList<Category> categoriesArray = categories.getValue();
        MutableLiveData<String> nameCategory = null;
        nameCategory.setValue(categoriesArray.get(position).getName());
        return nameCategory;
    }


}