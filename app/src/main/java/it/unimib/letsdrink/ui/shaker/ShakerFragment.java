package it.unimib.letsdrink.ui.shaker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.List;
import java.util.Objects;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.FirebaseDBCocktails;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailDetailFragment;


public class ShakerFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor acelerometerSensor;
    private boolean isAceletometerSensorAvaiable, isNotFirstTime = false;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ, xDifference, yDifference, zDifference;
    private float shakeThreshold = 5f;
    private int random;
    private FirebaseDBCocktails db;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            acelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAceletometerSensorAvaiable = true;
        } else {
            Log.d("fragment shaker", "l'acelerometro non funzia");
            isAceletometerSensorAvaiable = false;
        }

        View root = inflater.inflate(R.layout.fragment_shaker, container, false);

        return root;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("attivo evento 1", event.values[0] + "m/s2 su asse x");
        Log.d("attivo evento 2", event.values[1] + "m/s2 su asse y");
        Log.d("attivo evento 3", event.values[2] + "m/s2 su asse z");

        currentX = event.values[0];
        currentY = event.values[1];
        currentZ = event.values[2];

        if (isNotFirstTime) {

            xDifference = Math.abs(lastX - currentX);
            yDifference = Math.abs(lastY - currentY);
            zDifference = Math.abs(lastZ - currentZ);

            Log.d("prova", String.valueOf(xDifference));

            if ((xDifference > shakeThreshold && yDifference > shakeThreshold) ||
                    (xDifference > shakeThreshold && zDifference > shakeThreshold) ||
                    (yDifference > shakeThreshold && zDifference > shakeThreshold)) {

                Log.d("shaker attivo", "stai shakerando");

                db = new FirebaseDBCocktails();
                db.readCocktails(new FirebaseDBCocktails.DataStatus() {
                    @Override
                    public void dataIsLoaded(List<Cocktail> listOfCocktails) {
                        random = (int) (Math.random() * listOfCocktails.size());
                        Log.d("random", listOfCocktails.get(random).getName());

                        Fragment cocktailDetail = CocktailDetailFragment.newInstance(listOfCocktails.get(random).getName(), listOfCocktails.get(random).getMethod(),
                                listOfCocktails.get(random).getIngredients(), listOfCocktails.get(random).getImageUrl());
                        //Navigation.findNavController(getView()).navigate(R.id.action_navigation_shaker_to_cocktailDetailFragment);
                        //Navigation.findNavController(requireView()).navigate(R.id.action_navigation_shaker_to_cocktailDetailFragment2);

                    }
                });

            }

        }

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;

        isNotFirstTime = true;
        Log.d("prova", String.valueOf(isNotFirstTime));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();

        if (isAceletometerSensorAvaiable) {
            sensorManager.registerListener(this, acelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (isAceletometerSensorAvaiable) {
            sensorManager.unregisterListener(this);
        }
    }
}