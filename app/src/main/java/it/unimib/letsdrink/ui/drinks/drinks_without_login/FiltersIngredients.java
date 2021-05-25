package it.unimib.letsdrink.ui.drinks.drinks_without_login;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import it.unimib.letsdrink.R;

public class FiltersIngredients extends DialogFragment {

    private boolean modeAnanas, modeArancia, modeCognac, modeGin, modeLime, modeMenta, modePesca, modeRum, modeSoda, modeVodka;
    private String ananas, arancia, cognac, gin, lime, menta, pesca, rum, soda, vodka;
    private FilterInterface filter;

    public FiltersIngredients(String ananas, String arancia, String cognac,
                              String gin, String lime, String menta, String pesca, String rum,
                              String soda, String vodka, FilterInterface filter) {
        this.ananas = ananas;
        this.arancia = arancia;
        this.cognac = cognac;
        this.gin = gin;
        this.lime = lime;
        this.menta = menta;
        this.pesca = pesca;
        this.rum = rum;
        this.soda = soda;
        this.vodka = vodka;
        this.filter = filter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = requireActivity().getLayoutInflater().inflate(R.layout.filters, new LinearLayout(requireActivity()), false);
        final SwitchMaterial sAnanas = v.findViewById(R.id.switchAnanas);
        final SwitchMaterial sArancia = v.findViewById(R.id.switchArancia);
        final SwitchMaterial sCognac = v.findViewById(R.id.switchCognac);
        final SwitchMaterial sGin = v.findViewById(R.id.switchGin);
        final SwitchMaterial sLime = v.findViewById(R.id.switchLime);
        final SwitchMaterial sMenta = v.findViewById(R.id.switchMenta);
        final SwitchMaterial sPesca = v.findViewById(R.id.switchPesca);
        final SwitchMaterial sRum = v.findViewById(R.id.switchRum);
        final SwitchMaterial sSoda = v.findViewById(R.id.switchSoda);
        final SwitchMaterial sVodka = v.findViewById(R.id.switchVodka);

        if (ananas.equals("Ananas"))
            sAnanas.setChecked(true);
        if (arancia.equals("Arancia"))
            sArancia.setChecked(true);
        if (cognac.equals("Cognac"))
            sCognac.setChecked(true);
        if (gin.equals("Gin"))
            sGin.setChecked(true);
        if (lime.equals("Lime"))
            sLime.setChecked(true);
        if (menta.equals("Menta"))
            sMenta.setChecked(true);
        if (pesca.equals("Pesca"))
            sPesca.setChecked(true);
        if (rum.equals("Rum"))
            sRum.setChecked(true);
        if (soda.equals("Soda"))
            sSoda.setChecked(true);
        if (vodka.equals("Vodka"))
            sVodka.setChecked(true);

        Context c = getContext();
        assert c != null;

        return new MaterialAlertDialogBuilder(requireActivity(), R.style.DialogTheme)
                .setView(v)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setModeAnanas(sAnanas.isChecked());
                        setModeArancia(sArancia.isChecked());
                        setModeCognac(sCognac.isChecked());
                        setModeGin(sGin.isChecked());
                        setModeLime(sLime.isChecked());
                        setModeMenta(sMenta.isChecked());
                        setModePesca(sPesca.isChecked());
                        setModeRum(sRum.isChecked());
                        setModeSoda(sSoda.isChecked());
                        setModeVodka(sVodka.isChecked());
                        dialog.dismiss();
                        filter.okButtonClick(isModeAnanas(), isModeArancia(), isModeCognac(),
                                isModeGin(), isModeLime(), isModeMenta(), isModePesca(), isModeRum(),
                                isModeSoda(), isModeVodka());
                    }
                })
                .setBackground(new ColorDrawable(Color.TRANSPARENT))
                .create();
    }

    public boolean isModeAnanas() {
        return modeAnanas;
    }

    public void setModeAnanas(boolean modeAnanas) {
        this.modeAnanas = modeAnanas;
    }

    public boolean isModeArancia() {
        return modeArancia;
    }

    public void setModeArancia(boolean modeArancia) {
        this.modeArancia = modeArancia;
    }

    public boolean isModeCognac() {
        return modeCognac;
    }

    public void setModeCognac(boolean modeCognac) {
        this.modeCognac = modeCognac;
    }

    public boolean isModeGin() {
        return modeGin;
    }

    public void setModeGin(boolean modeGin) {
        this.modeGin = modeGin;
    }

    public boolean isModeLime() {
        return modeLime;
    }

    public void setModeLime(boolean modeLime) {
        this.modeLime = modeLime;
    }

    public boolean isModeMenta() {
        return modeMenta;
    }

    public void setModeMenta(boolean modeMenta) {
        this.modeMenta = modeMenta;
    }

    public boolean isModePesca() {
        return modePesca;
    }

    public void setModePesca(boolean modePesca) {
        this.modePesca = modePesca;
    }

    public boolean isModeRum() {
        return modeRum;
    }

    public void setModeRum(boolean modeRum) {
        this.modeRum = modeRum;
    }

    public boolean isModeSoda() {
        return modeSoda;
    }

    public void setModeSoda(boolean modeSoda) {
        this.modeSoda = modeSoda;
    }

    public boolean isModeVodka() {
        return modeVodka;
    }

    public void setModeVodka(boolean modeVodka) {
        this.modeVodka = modeVodka;
    }
}