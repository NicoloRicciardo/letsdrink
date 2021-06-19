package it.unimib.letsdrink.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import java.util.HashSet;
import java.util.Set;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.interfaces.FilterInterface;

public class FiltersIngredients extends DialogFragment {

    private boolean[] filtri;
    private static FilterInterface filter;
    SwitchMaterial sAnanas, sArancia, sCognac, sGin, sLime, sMenta, sPesca, sRum, sSoda, sVodka;

    public static FiltersIngredients newInstance(FilterInterface filter) {
        FiltersIngredients fragment = new FiltersIngredients();
        FiltersIngredients.filter = filter;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = requireActivity().getLayoutInflater().inflate(R.layout.filters, new LinearLayout(requireActivity()), false);
        sAnanas = v.findViewById(R.id.switchAnanas);
        sArancia = v.findViewById(R.id.switchArancia);
        sCognac = v.findViewById(R.id.switchCognac);
        sGin = v.findViewById(R.id.switchGin);
        sLime = v.findViewById(R.id.switchLime);
        sMenta = v.findViewById(R.id.switchMenta);
        sPesca = v.findViewById(R.id.switchPesca);
        sRum = v.findViewById(R.id.switchRum);
        sSoda = v.findViewById(R.id.switchSoda);
        sVodka = v.findViewById(R.id.switchVodka);
        filtri = new boolean[10];
        loadToogle();
        Context c = getContext();
        assert c != null;


        return new MaterialAlertDialogBuilder(requireActivity(), R.style.DialogTheme)
                .setView(v)
                .setPositiveButton("Ok", (dialog, which) -> {
                    filtri[0] = sAnanas.isChecked();
                    filtri[1] = sArancia.isChecked();
                    filtri[2] = sCognac.isChecked();
                    filtri[3] = sGin.isChecked();
                    filtri[4] = sLime.isChecked();
                    filtri[5] = sMenta.isChecked();
                    filtri[6] = sPesca.isChecked();
                    filtri[7] = sRum.isChecked();
                    filtri[8] = sSoda.isChecked();
                    filtri[9] = sVodka.isChecked();
                    setFiltri(filtri);
                    saveToogle();
                    dialog.dismiss();
                    filter.okButtonClick(getFiltri());
                })
                .setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss())
                .setBackground(new ColorDrawable(Color.TRANSPARENT))
                .create();
    }


    private void saveToogle() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("Filtri", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> checkedCheckboxSet = new HashSet<>();
        if (sAnanas.isChecked()) {
            checkedCheckboxSet.add("Ananas");
        }
        if (sArancia.isChecked()) {
            checkedCheckboxSet.add("Arancia");
        }
        if (sCognac.isChecked()) {
            checkedCheckboxSet.add("Cognac");
        }
        if (sGin.isChecked()) {
            checkedCheckboxSet.add("Gin");
        }
        if (sLime.isChecked()) {
            checkedCheckboxSet.add("Lime");
        }
        if (sMenta.isChecked()) {
            checkedCheckboxSet.add("Menta");
        }
        if (sPesca.isChecked()) {
            checkedCheckboxSet.add("Pesca");
        }
        if (sRum.isChecked()) {
            checkedCheckboxSet.add("Rum");
        }
        if (sSoda.isChecked()) {
            checkedCheckboxSet.add("Soda");
        }
        if (sVodka.isChecked()) {
            checkedCheckboxSet.add("Vodka");
        }

        editor.putStringSet("Filtri_selezionati", checkedCheckboxSet);
        editor.apply();
    }


    private void loadToogle() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("Filtri", Context.MODE_PRIVATE);
        Set<String> checkedCheckboxSet = sharedPref.getStringSet("Filtri_selezionati", null);
        if (checkedCheckboxSet != null) {
            if (checkedCheckboxSet.contains("Ananas")) {
                sAnanas.setChecked(true);
            }
            if (checkedCheckboxSet.contains("Arancia")) {
                sArancia.setChecked(true);
            }
            if (checkedCheckboxSet.contains("Cognac")) {
                sCognac.setChecked(true);
            }
            if (checkedCheckboxSet.contains("Gin")) {
                sGin.setChecked(true);
            }
            if (checkedCheckboxSet.contains("Lime")) {
                sLime.setChecked(true);
            }
            if (checkedCheckboxSet.contains("Menta")) {
                sMenta.setChecked(true);
            }
            if (checkedCheckboxSet.contains("Pesca")) {
                sPesca.setChecked(true);
            }
            if (checkedCheckboxSet.contains("Rum")) {
                sRum.setChecked(true);
            }
            if (checkedCheckboxSet.contains("Soda")) {
                sSoda.setChecked(true);
            }
            if (checkedCheckboxSet.contains("Vodka")) {
                sVodka.setChecked(true);
            }

        }
    }

    public boolean[] getFiltri() {
        return filtri;
    }

    public void setFiltri(boolean[] filtri) {
        this.filtri = filtri;
    }
}