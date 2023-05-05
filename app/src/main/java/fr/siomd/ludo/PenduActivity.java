package fr.siomd.ludo;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;

import fr.siomd.ludo.dataaccess.DicoXml;
import fr.siomd.ludo.databinding.ActivityPenduBinding;
import fr.siomd.ludo.entity.Bourreau;
import fr.siomd.ludo.entity.Juge;
import fr.siomd.ludo.entity.Theme;

public class PenduActivity extends AppCompatActivity {

    private ActivityPenduBinding ui;     // ui : interface utilisateur

    private Juge leJuge;

    private Bourreau leBourreau;

    private int tours = 0;

    int[] penduImages = { R.drawable.pendu1, R.drawable.pendu2, R.drawable.pendu3, R.drawable.pendu4, R.drawable.pendu5, R.drawable.pendu6, R.drawable.pendu7 };




    @Override

    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        // mettre en place le layout (la mise en page)

        ui = ActivityPenduBinding.inflate(getLayoutInflater());

        setContentView(ui.getRoot());

        // créer le juge et le bourreau
        leJuge = new Juge(DicoXml.getLesthemes(getResources().getXml(R.xml.dico)));

        ui.fenetreFin.setVisibility(View.GONE);
        ui.fenetreJeu.setVisibility(View.GONE);
        ui.fenetreAccueil.setVisibility(View.VISIBLE);
        ViewGroup parent = findViewById(R.id.themesLayout);
        leJuge.ajouterBoutonsThemes(parent, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button clickedButton = (Button) v;
                leJuge.setNumeroThemeSelectionne(clickedButton.getId());

                //création de l'instance et ainsi début du jeu
                leBourreau = new Bourreau(leJuge);
                // afficher le mot en cours (avec des _ pour chaque lettre non trouvée)
                ui.tvMot.setText(leBourreau.getLeMotEnCours());
                ui.fenetreJeu.setVisibility(View.VISIBLE);
                ui.fenetreAccueil.setVisibility(View.GONE);

            }
        });
    }

    public void onClickbtEnvoyer(View laVue){
        String oldMotEnCours = leBourreau.getLeMotEnCours();

        String text = String.valueOf(ui.txtLettreMots.getText());
        int longueurText = text.length();
        if (longueurText == 1){
            leBourreau.executer(Character.toUpperCase(text.charAt(0)));
        }
        String newMotEnCours = leBourreau.getLeMotEnCours();

        if (newMotEnCours == oldMotEnCours && longueurText == 1){
            ui.tvRebu.setText(leBourreau.getLesLettresAuRebut());
            tours ++;
            if (tours < penduImages.length){
                ui.imagePendu.setImageResource(penduImages[tours]);
            }
        }

        if (newMotEnCours != oldMotEnCours && longueurText == 1){
            ui.tvMot.setText(leBourreau.getLeMotEnCours());
        }

        ui.txtLettreMots.setText("");

        if (leBourreau.isGagne()){
            ui.fenetreFin.setVisibility(View.VISIBLE);
            ui.fenetreJeu.setVisibility(View.GONE);
            ui.imagePenduFin.setImageResource(R.drawable.winpendu);
            ui.tvTitreFin.setText("VOUS AVEZ GAGNÉ !");
            String score = String.valueOf(leJuge.getScore());

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            // début de la transaction
            SharedPreferences.Editor editeur = prefs.edit();
            // modification du score
            editeur.putString("prefs_pendu_score", score + " points");
            // fin de la transaction
            editeur.commit();
            ui.tvNbPoints.setText("vous obtenez " +score+" pts");

        }
        if (leBourreau.isPerdu()){
            ui.fenetreFin.setVisibility(View.VISIBLE);
            ui.fenetreJeu.setVisibility(View.GONE);
            ui.imagePenduFin.setImageResource(R.drawable.die);
            ui.tvTitreFin.setText("VOUS AVEZ PERDU !");
            ui.tvNbPoints.setText("le mot caché était " +leBourreau.getLeMotEntier());
            ui.tvNbPoints.setVisibility(View.VISIBLE);
        }


    }

    public void onClickbtrestart(View laVue) {

        tours = 0;

        ui.fenetreFin.setVisibility(View.GONE);
        ui.fenetreJeu.setVisibility(View.VISIBLE);


        // créer le juge et le bourreau
        leJuge = new Juge(DicoXml.getLesthemes(getResources().getXml(R.xml.dico)));

        leBourreau = new Bourreau(leJuge);

        // afficher le mot en cours (avec des _ pour chaque lettre non trouvée)

        ui.tvMot.setText(leBourreau.getLeMotEnCours());
        ui.imagePendu.setImageResource(R.drawable.pendu);
        ui.tvRebu.setText("");


    }

    public void onClickbtreturnchoixtheme(View laVue) {

        tours = 0;

        ui.fenetreFin.setVisibility(View.GONE);
        ui.fenetreAccueil.setVisibility(View.VISIBLE);


    }

}