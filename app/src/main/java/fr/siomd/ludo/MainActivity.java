package fr.siomd.ludo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import fr.siomd.ludo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // mettre en place le layout (la mise en page)
        ActivityMainBinding ui = ActivityMainBinding.inflate(getLayoutInflater());
        // afficher Bonjour nom_joueur
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nomJoueur = prefs.getString("prefs_nom_joueur", "Yann");
        String msg = "";
        if (!nomJoueur.equals(getString(R.string.prefs_non_renseigne))) {
            msg = getString(R.string.txtSalut) + " " + nomJoueur + " ! ";
        } else {
            msg = getString(R.string.txtSalut) + " ! \n" + getString(R.string.txtPremiereUtilisation);
        }
        ui.tvSalut.setText(msg);

        // appliquer le thème choisi
        boolean themeVertRose = prefs.getBoolean("prefs_theme", false);
        if (themeVertRose) {
            // à faire après super.onCreate() et avant setContentView()
            setTheme(R.style.Theme_VertRose);
        }
        setContentView(ui.getRoot());
    }

    //méthode qui ajoute le menu à la barre d’action
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ludo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // la méthode qui réagit aux clics sur les éléments du menu
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bataille:
                Intent bataille = new Intent(this, BatailleActivity.class);
                startActivity(bataille);
                return true;
            case R.id.menu_compter:
                Intent compterCalculer = new Intent(this, CompterActivity.class);
                startActivity(compterCalculer);
                return true;
            case R.id.menu_pendu:
                Intent pendu = new Intent(this, PenduActivity.class);
                startActivity(pendu);
                return true;
            case R.id.menu_parametres:
                Toast.makeText(getApplicationContext(), "Pas de paramètres pour l'instant ...",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_chercher:
                Toast.makeText(getApplicationContext(), "Recherche indisponible",
                        Toast.LENGTH_LONG).show();
                return true;
            default:
                // l'action de l'utilisateur n'a pas été reconnue
                // appeler la méthode de la classe de base pour le gérer
                return super.onOptionsItemSelected(item);
        }
    }
}