package it.unimib.letsdrink.ui.categories;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.unimib.letsdrink.domain.Category;


public class FirebaseDBCategories {

    private FirebaseFirestore db;
    private CollectionReference collezione;
    private List<Category> listOfCategories = new ArrayList<>();

    public interface DataStatus{
        void dataIsLoaded(List<Category> listOfCategories);
    }

    public FirebaseDBCategories() {
        db = FirebaseFirestore.getInstance();
        collezione = db.collection("Categorie");
    }

    public void readCategories(final DataStatus dataStatus){

        db.collection("Categorie").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Category categoria = document.toObject(Category.class);
                                listOfCategories.add(categoria);
                            }

                            dataStatus.dataIsLoaded(listOfCategories);
                        }
                    }
                });


        /* reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listOfEvents.clear();
                final List<String> listOfKeysEvents = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    EventItem event = keyNode.getValue(EventItem.class);

                    Date currentDate = Calendar.getInstance().getTime();

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    assert event != null;
                    String dateEventString = event.getDateString() + " " + event.getOpeningHours().substring(0, 5);

                    try {
                        Date dateEvent = simpleDateFormat.parse(dateEventString);
                        assert dateEvent != null;
                        if(dateEvent.after(currentDate)){
                            listOfKeysEvents.add(keyNode.getKey());
                            listOfEvents.add(event);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                dataStatus.dataIsLoaded(listOfEvents, listOfKeysEvents);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        }); */
    }
}
