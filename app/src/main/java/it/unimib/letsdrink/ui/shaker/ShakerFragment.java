package it.unimib.letsdrink.ui.shaker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.firebaseDB.FirebaseDBCocktails;
import it.unimib.letsdrink.ui.home.CocktailDetailFragment;

//fragment dello shaker
public class ShakerFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor acelerometerSensor;
    private boolean isAceletometerSensorAvaiable, isNotFirstTime = false;
    private float lastX;
    private float lastY;
    private float lastZ;
    private int random;

    /*all' onCreateView istanziamo il sensor manager per accedere ai sensori
    a noi interessa l'accelerometro e appena lo riceviamo lo settiamo a disponibile
    facciamo l'inflate del file xml dello shaker*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            acelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAceletometerSensorAvaiable = true;
        } else {
            isAceletometerSensorAvaiable = false;
        }

        return inflater.inflate(R.layout.fragment_shaker, container, false);
    }


    /*all'onSensorChanged abbiamo le variazioni dei parametri relativi allo spostamento
     del cellulare all'interno di un sistema cartesiano a 3 dimensioni (x,y,z),
     se quest'ultime variano in base al movimento del dispositivo e lo spostamento risulta essere
     di almeno 5 unità ci sposteremo nel fragment relativo al singolo cocktail randomico*/
    @Override
    public void onSensorChanged(SensorEvent event) {

        float currentX = event.values[0];
        float currentY = event.values[1];
        float currentZ = event.values[2];

        //la prima volta non vengono effettuate le differenze delle posizioni
        if (isNotFirstTime) {

            float xDifference = Math.abs(lastX - currentX);
            float yDifference = Math.abs(lastY - currentY);
            float zDifference = Math.abs(lastZ - currentZ);

            float shakeThreshold = 5f;
            if ((xDifference > shakeThreshold && yDifference > shakeThreshold) ||
                    (xDifference > shakeThreshold && zDifference > shakeThreshold) ||
                    (yDifference > shakeThreshold && zDifference > shakeThreshold)) {

                FirebaseDBCocktails db = new FirebaseDBCocktails();
                db.readCocktails(listOfCocktails -> {
                    random = (int) (Math.random() * listOfCocktails.size());

                    CocktailDetailFragment.newInstance(listOfCocktails.get(random).getName(), listOfCocktails.get(random).getMethod(),
                            listOfCocktails.get(random).getIngredients(), listOfCocktails.get(random).getImageUrl());
                    Navigation.findNavController(requireView()).navigate(R.id.action_navigation_shaker_to_cocktailDetailFragment2);

                });

                //questo if serve per evitare una volta mostrato il cocktail, scuotendo il telefono cerchi un altro cocktail
                //e tenti di passare al coktailDetailFragment con un'altra action. Si toglie la registrazione del listener all'evento
                if (isAceletometerSensorAvaiable) {
                    sensorManager.unregisterListener(this);
                }

            }

        }

        //salva le posizioni correnti
        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;

        isNotFirstTime = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*quando il fragment è nella fase onResume del suo ciclo di vita, bisogna registrare il sensore
    al proprio listener in modo tale che sia nuovamente disponibile alla prossima chiamata*/
    @Override
    public void onResume() {
        super.onResume();

        if (isAceletometerSensorAvaiable) {
            sensorManager.registerListener(this, acelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /* quando il fragment è nella fase onPause del suo ciclo di vita, bisogna rilasciare il sensore
    al proprio listener in modo tale che non sprechi batteria inutilmente visto che rimarrebbe inutilizzato
    essendo il fragment appunto in onPause*/
    @Override
    public void onPause() {
        super.onPause();

        if (isAceletometerSensorAvaiable) {
            sensorManager.unregisterListener(this);

        }
    }
}