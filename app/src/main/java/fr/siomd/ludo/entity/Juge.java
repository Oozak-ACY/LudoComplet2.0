package fr.siomd.ludo.entity;

import java.util.ArrayList;
import java.util.Random;

import fr.siomd.ludo.R;
import fr.siomd.ludo.dataaccess.DicoXml;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.preference.PreferenceManager;


public class Juge {
    private static final String TAG = "Juge";

    private Random leHasard = new Random();

    private int score = 0;

    private ArrayList<Theme> lesThemes;

    private int numeroThemeSelectionne;

    public Juge(ArrayList<Theme> lesThemes)   {
        this.lesThemes = lesThemes;
    }

    public void setNumeroThemeSelectionne(int unNumeroTheme) {

        numeroThemeSelectionne = unNumeroTheme;
        lireThemes();

    }
    private void lireThemes() {
        Theme unTheme = lesThemes.get(numeroThemeSelectionne);
    }


    public void ajouterBoutonsThemes(ViewGroup parent, View.OnClickListener listener) {
        Context context = parent.getContext();
        for (int i = 0; i < lesThemes.size(); i++) {
            Theme theme = lesThemes.get(i);
            Button button = new Button(context);
            button.setText(theme.getNom());
            button.setId(i);
            button.setOnClickListener(listener);
            parent.addView(button);
        }
    }


    // retourne la liste des noms de thèmes
    public ArrayList<String> getLesNomsThemes() {
        ArrayList<String> nomTheme = new ArrayList<String>();
        for (Theme unTheme : lesThemes) {
            nomTheme.add(unTheme.getNom());
        }
        return nomTheme;
    }


    // retourne le score
    public int getScore() {
        return score;

    }


    // retourne le numéro de thème sélectionné
    public int getNumeroThemeSelectionne() {return this.numeroThemeSelectionne;}


    //  si aucun thème sélectionné, sélectionner un thème au hasard
    //  donner un mot au hasard dans le thème (sélectionné ou pris au hasard)
    public Mot donnerMot()  {
        Theme unTheme = lesThemes.get(this.numeroThemeSelectionne);
        ArrayList<Mot> lesMots = unTheme.getLesMots();
        int idMotRandom = leHasard.nextInt(4);
        Mot leMot = lesMots.get(idMotRandom);
        return leMot;
    }


    // ajouter nbPoints au score
    public void ajouterScore(int unNbPoints) {

        score = score + unNbPoints;

    }
}