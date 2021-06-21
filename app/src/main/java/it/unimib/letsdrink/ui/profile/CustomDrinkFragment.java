package it.unimib.letsdrink.ui.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;

//fragment per la creazione dei custom drink
public class CustomDrinkFragment extends Fragment {

    private LinearLayout mLayoutIngredientsList;
    private EditText mNameCustomDrink;
    private EditText mMethodCustomDrink;
    private ImageView mCustomDrinkImage;
    private final ArrayList<String> mIngredientsCustomDrink = new ArrayList<>();
    private final Cocktail mCustomDrink = new Cocktail();
    private CollectionReference collezione;
    private StorageReference mStorageReference;

    public CustomDrinkFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar= ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.title_custom_drink);

        return inflater.inflate(R.layout.fragment_custom_drink, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collezione = db.collection("Utenti");

        mStorageReference = FirebaseStorage.getInstance().getReference();

        mLayoutIngredientsList = view.findViewById(R.id.layout_ingredients_list);
        ImageView mAddIngredient = view.findViewById(R.id.image_add_ingredient);

        mNameCustomDrink = view.findViewById(R.id.edit_text_custom_drink_name);
        mMethodCustomDrink = view.findViewById(R.id.edit_text_custom_drink_method);

        mCustomDrinkImage = view.findViewById(R.id.image_custom_drink_added_from_user);

        //setta l'immagine di default del custom drink (su firebase)
        setDefaultCustomDrinkImage();

        ImageView mSetDrinkPhoto = view.findViewById(R.id.image_custom_drink_camera);
        mSetDrinkPhoto.setOnClickListener(v -> {
            //intent implicito che serve per aprire la galleria del telefono
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });

        //al click del simbolo "+" aggiunge gli ingredienti
        mAddIngredient.setOnClickListener(v -> addView());

        FloatingActionButton mCreateCustomDrink = view.findViewById(R.id.floating_action_button_custom_drink_add);
        mCreateCustomDrink.setOnClickListener(v -> {

            if(controlParameters()) {
                //inserisce i dati all'interno dell'oggetto mCustomDrink che fa riferimento alla classe Cocktail
                mCustomDrink.setIngredients(mIngredientsCustomDrink);
                mCustomDrink.setName(mNameCustomDrink.getText().toString());
                mCustomDrink.setMethod(mMethodCustomDrink.getText().toString());

                //inserisce il custom drink all'interno della sub-collection dentro il documento Utente (dello specifico current user)
                collezione.document(currentUser).collection("customDrink").add(mCustomDrink);

                //una volta creato riporta l'utente sulla pagina del profilo
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_customDrinkFragment_to_profileFragment);
                //se i parametri non sono corretti allora verrá visualizzato un messaggio di errore
            } else {
                Toast.makeText(getContext(), R.string.toast_insert_all_data, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //controllo parametri di inserimento del custom drink
    private boolean controlParameters() {
        mIngredientsCustomDrink.clear();
        boolean result = true;

        //controllo della lista degli ingredienti
        for(int i = 0; i < mLayoutIngredientsList.getChildCount(); i++){

            View ingredientsListView = mLayoutIngredientsList.getChildAt(i);
            EditText ingredientInsert = ingredientsListView.findViewById(R.id.edit_text_ingredient_list);

            //i campi non devono essere nulli
            if(!ingredientInsert.getText().toString().equals("")) {
                mIngredientsCustomDrink.add(ingredientInsert.getText().toString());
            } else {
                result = false;
                break;
            }
        }

        //se non hai mai cliccato sul "+" per inserire gli ingredienti: ERRORE
        if(mIngredientsCustomDrink.size() == 0){
            result = false;
        }

        //i campi non devono essere nulli
        if(mNameCustomDrink.getText().toString().equals("")) {
            result = false;
        }

        //i campi non devono essere nulli
        if(mMethodCustomDrink.getText().toString().equals("")) {
            result = false;
        }

        return result;
    }

    //permette di inserire il layout per inserire un nuovo ingrediente
    private void addView() {

        @SuppressLint("InflateParams")
        View ingredientsListView = getLayoutInflater().inflate(R.layout.ingredients_list, null,false);

        ImageView imageClose = ingredientsListView.findViewById(R.id.image_delete_ingredient);

        imageClose.setOnClickListener(v -> removeView(ingredientsListView));

        mLayoutIngredientsList.addView(ingredientsListView);

    }

    //permette di rimuovere il layout di un nuovo ingrediente
    private void removeView(View view) {
        mLayoutIngredientsList.removeView(view);
    }

    //gestice l'intent della galleria
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK) {
                assert data != null;
                //sovrascrive l'immagine di default rossa nella pagina con la foto inserita dall'utente
                Uri imageUri = data.getData();
                mCustomDrinkImage.setImageURI(imageUri);

                //carica l'immagine inserita su firebase
                uploadDrinkImageToFirebase(imageUri);
            }
        }
    }

    //carica l'immagine inserita su firebase
    private void uploadDrinkImageToFirebase(Uri imageUri) {
        //entra nella cartella corretta su firebase storage e inserisce l'immagine dandole il nome del custom drink scelto dall'utente
        StorageReference fileRef = mStorageReference
                .child("UserImage/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() + "/"
                        + mNameCustomDrink.getText().toString()+".jpg");
        //permette di avere un feedback nel caso l'immagine sia stata caricata correttamente o no
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(getContext(), R.string.toast_changed_user_image, Toast.LENGTH_SHORT).show();
            //setta l'url dell'oggetto custom drink
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> mCustomDrink.setImageUrl(uri.toString()));
        }).addOnFailureListener(e -> Toast.makeText(getContext(), R.string.toast_NOT_changed_user_image, Toast.LENGTH_SHORT).show());
    }

    //metodo che setta l'immagine di default al custom drink (se non viene inserita alcuna immagine dall'utente)
    private void setDefaultCustomDrinkImage() {
        Random random = new Random();
        int ranNum = random.nextInt(8);
        switch(ranNum) {
            case 0:
                //blue
                mCustomDrink.setImageUrl("https://firebasestorage.googleapis.com/v0/b/let-s-" +
                        "drink-5630b.appspot.com/o/UserImage%2FDefaultImage%2Fdefault_custom" +
                        "_drink_blue.png?alt=media&token=628fbc6e-6331-4b0d-9e1e-a1beee2a53eb");
                break;
            case 1:
                //light_blue
                mCustomDrink.setImageUrl("https://firebasestorage.googleapis.com/v0/b/let-s-" +
                        "drink-5630b.appspot.com/o/UserImage%2FDefaultImage%2Fdefault" +
                        "_custom_drink_lightblue.png?alt=media&token=cacdd010" +
                        "-5e2e-4534-8879-2903e5c4d80d");
                break;
            case 2:
                //dark_blue
                mCustomDrink.setImageUrl("https://firebasestorage.googleapis.com/v0/b/let-s" +
                        "-drink-5630b.appspot.com/o/UserImage%2FDefaultImage%2Fdefault_custom" +
                        "_drink_darkblue.png?alt=media&token=0aae353f-4f5e-4552-9154-f728d2750acd");
                break;
            case 3:
                //purple
                mCustomDrink.setImageUrl("https://firebasestorage.googleapis.com/v0/b/let-s" +
                        "-drink-5630b.appspot.com/o/UserImage%2FDefaultImage%2Fdefault_custom" +
                        "_drink_purple.png?alt=media&token=d5ea1ca6-8281-4f7a-a1f1-38ca9ec86731");
                break;
            case 4:
                //light_purple
                mCustomDrink.setImageUrl("https://firebasestorage.googleapis.com/v0/b/let-s" +
                        "-drink-5630b.appspot.com/o/UserImage%2FDefaultImage%2Fdefault" +
                        "_custom_drink_lightpurple.png?alt=media&token=076a6879" +
                        "-5b42-4c38-a57b-1a0d1f9422f2");
                break;
            case 5:
                //red
                mCustomDrink.setImageUrl("https://firebasestorage.googleapis.com/v0/b/let-s" +
                        "-drink-5630b.appspot.com/o/UserImage%2FDefaultImage%2Fdefault_custom" +
                        "_drink_red.png?alt=media&token=abe5e5ca-a5cb-4419-bea0-7fc9c5c7dcd4");
                break;
            case 6:
                //green
                mCustomDrink.setImageUrl("https://firebasestorage.googleapis.com/v0/b/let-s" +
                        "-drink-5630b.appspot.com/o/UserImage%2FDefaultImage%2Fdefault_custom" +
                        "_drink_green.png?alt=media&token=814f471c-b60e-4a23-87d3-ebcad56f7c97");
                break;
            case 7:
                //grey
                mCustomDrink.setImageUrl("https://firebasestorage.googleapis.com/v0/b/let-s" +
                        "-drink-5630b.appspot.com/o/UserImage%2FDefaultImage%2Fdefault_custom" +
                        "_drink_grey.png?alt=media&token=cb201cb6-6558-4bac-bbbc-eafedea611d2");
                break;
        }
    }

}