package fr.siomd.ludo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

import fr.siomd.ludo.databinding.ActivityBatailleBinding;
import fr.siomd.ludo.entity.Carte;

public class BatailleActivity extends AppCompatActivity {

    // layout
    private ActivityBatailleBinding ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mettre en place le layout (mise en page)
        ui = ActivityBatailleBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());

        // créer le jeu de 52 cartes à partir des couleurs et des figures
        int indCarte = 0;
        for (String couleur : tbCouleurs) {
            for (String figure:tbFigure) {
                tbJeu[indCarte] = new Carte(couleur, figure);
                indCarte++;
            }
        }
        afficherJeu();
        demarrerJeu();
    }

    private static final String TAG="Bataille";
    private static final int NB_COUPS=26;

    private String[] tbCouleurs = {"Coeur","Carreau", "Pique", "Trèfle"};
    private String[] tbFigure = {"As", "Roi", "Dame", "Valet", "2","3","4","5","6","7","8","9","10"};

    private Carte[] tbJeu = new Carte[NB_COUPS*2];

    private Carte[] tbJoueurUn = new Carte[NB_COUPS];
    private Carte[] tbJoueurDeux = new Carte[NB_COUPS];

    private int numCoup = 0;
    private int indiceAtout = 0;
    private String atout = tbCouleurs[indiceAtout];
    private int scoreUn = 0;
    private int scoreDeux = 0;

    private int nbPointsCoup=0;
    private int numJoueurGagnantCoup=0;
    private int nbReponsesCorrectes=0;

    private void afficherJeu(){
        for (Carte uneCarte: tbJeu){
            Log.i(TAG, "carte = "+uneCarte.getNom());
        }
    }

    private void demarrerJeu(){
        // mélanger le jeu de cartes
        Carte carteTemp;
        Random leHasard= new Random(); // générateur de nombre peseudo-aléatoires
        for (int i=0; i< tbJeu.length; i++) {
            // on tire une position au hasard entre 0 et
            // méthode next retourne un nombre aléatoire non négatif, inférieur au nombre maximal spécifié
            int indHasard=leHasard.nextInt(tbJeu.length -1);
            carteTemp = tbJeu[indHasard];
            tbJeu[indHasard]=tbJeu[i];
            tbJeu[i]=carteTemp;
        }

        // distribuer le jeu de carte a 2 joueurs
        int indJoueur=0;
        for (int i=0; i < tbJeu.length; i+=2) {
            tbJoueurUn[indJoueur]=tbJeu[i];
            tbJoueurDeux[indJoueur]=tbJeu[i+1];
            indJoueur++;
        }

        numCoup = -1;
        scoreUn = 0;
        scoreDeux = 0;
        nbPointsCoup=0;
        numJoueurGagnantCoup=0;
        nbReponsesCorrectes=0;

        // afficher les textes de départ
        ui.imgCarte1.setImageResource(R.drawable.back);
        ui.imgCarte2.setImageResource(R.drawable.back);
        ui.tvInfosCarte1.setText(" ");
        ui.tvInfosCarte2.setText(" ");
        ui.tvScore1.setText(String.format(Locale.getDefault(), "%d point", scoreUn));
        ui.tvScore2.setText(String.format(Locale.getDefault(), "%d point", scoreDeux));
        //effacer la zone de saisie du nombre de points
        ui.etNbPoints.setText(" ");
        // désactiver la zone de saisie du nb de points
        ui.etNbPoints.setEnabled(false);
        // effacer le résultat des réponses de l'utilisateur
        ui.tvNbRepCorrectes.setText(String.format(Locale.getDefault(), "%d / %d", 0, NB_COUPS));

        // désactiver les boutons de réponse
        ui.btJoueur1.setEnabled(false);
        ui.btJoueur2.setEnabled(false);
        ui.btAucunJoueur.setEnabled(false);
        ui.btAtout.setEnabled(true);

    }

    public void onClickbtNouveauJeu(View view) {
        demarrerJeu();
    }

    public void onClickbtAtout(View view) {
        Context contexte=getApplicationContext();

        indiceAtout=(indiceAtout+1)%4;
        atout=tbCouleurs[indiceAtout];
        switch (indiceAtout){
            case 0:
                ui.btAtout.setBackground(ContextCompat.getDrawable(contexte, R.drawable.coeur));
                break;
            case 1:
                ui.btAtout.setBackground(ContextCompat.getDrawable(contexte, R.drawable.carreau));
                ui.btAtout.setBackground(ContextCompat.getDrawable(contexte, R.drawable.carreau));
                break;
            case 2:
                ui.btAtout.setBackground(ContextCompat.getDrawable(contexte, R.drawable.pic));
                break;
            case 3:
                ui.btAtout.setBackground(ContextCompat.getDrawable(contexte, R.drawable.trefle));
                break;
        }
    }

    private int getCarteResource(String nomImgCarte){
        int resId = getResources().getIdentifier(nomImgCarte, "drawable", "fr.siomc.ludo");
        if (resId!=0){
            return resId;
        }
        return R.drawable.back;
    }

    public void onClickbtJouer(View view) {
        if (numCoup==-1){
            ui.btAtout.setEnabled(false);
            ui.etNbPoints.setEnabled(true);
            ui.btJoueur1.setEnabled(true);
            ui.btJoueur2.setEnabled(true);
            ui.btAucunJoueur.setEnabled(true);
            numCoup=0;
        }
        if (numCoup<NB_COUPS){
            // jouer 1 coups avec calcul scores
            nbPointsCoup = tbJoueurUn[numCoup].getValeur() + tbJoueurDeux[numCoup].getValeur();
            if (tbJoueurUn[numCoup].isAtout(atout) == tbJoueurDeux[numCoup].isAtout(atout)){
                if (tbJoueurUn[numCoup].getValeur()>tbJoueurDeux[numCoup].getValeur()){
                    scoreUn=scoreUn+nbPointsCoup;
                    numJoueurGagnantCoup=1;
                } else {
                    if (tbJoueurUn[numCoup].getValeur()<tbJoueurDeux[numCoup].getValeur()){
                        scoreDeux=scoreDeux+nbPointsCoup;
                        numJoueurGagnantCoup=2;
                    } else {
                        numJoueurGagnantCoup = 3; // égalité
                    }
                }
            } else {
                if (tbJoueurUn[numCoup].isAtout(atout)){
                    scoreUn=scoreUn+nbPointsCoup;
                    numJoueurGagnantCoup=1;
                } else {
                    scoreDeux=scoreDeux+nbPointsCoup;
                    numJoueurGagnantCoup=2;
                }
            }

            //afficher les informations du coup
            ui.imgCarte1.setImageResource(getCarteResource(tbJoueurUn[numCoup].getNomImg()));
            ui.imgCarte2.setImageResource(getCarteResource(tbJoueurDeux[numCoup].getNomImg()));
            ui.tvInfosCarte1.setText(String.format(Locale.getDefault(), "%s : %d points", tbJoueurUn[numCoup].getNom(), tbJoueurUn[numCoup].getValeur()));
            ui.tvInfosCarte2.setText(String.format(Locale.getDefault(), "%s : %d points", tbJoueurDeux[numCoup].getNom(), tbJoueurDeux[numCoup].getValeur()));
            // passer au coup suivant
            numCoup++;
            ui.etNbPoints.setText("");

        } else {
            String message = " ";
            if (scoreUn>scoreDeux){
                message = String.format(Locale.getDefault(), "Le joueur Un a gagné avec %d points contre %d points", scoreUn, scoreDeux);
            } else {
                if (scoreDeux>scoreUn){
                    message = String.format(Locale.getDefault(), "Le joueur Deux a gagné avec %d points contre %d points", scoreDeux, scoreUn);
                } else {
                    message = String.format(Locale.getDefault(), "Les joueurs ont gagnés avec %d points chacun", scoreUn);
                }
            }
            ui.tvResultat.setText(message);
        }

        // mettre à jour le score du joueur dans les préférences
        // récupérer le stockage des préférences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        // début de la transaction
        SharedPreferences.Editor editeur = prefs.edit();
        // modification du score
        editeur.putString("prefs_bataille_score", Integer.toString(nbReponsesCorrectes) + " / " + NB_COUPS);
        // fin de la transaction
        editeur.commit();
    }

    public void onClickbtJoueur1(View view) {
        traitementReponse(1);
    }

    public void onClickbtJoueur2(View view) {
        traitementReponse(2);
    }
    public void onClickbtAucunJoueur(View view) {
        traitementReponse(3);
    }

    private void  traitementReponse(int numClickReponse){
        // aficher les scores des 2 joueurs
        ui.tvScore1.setText(String.format(Locale.getDefault(), "%d points", scoreUn));
        ui.tvScore2.setText(String.format(Locale.getDefault(), "%d points", scoreDeux));

        // traiter les réponses des 2 joueurs
        String repJoueur="";
        int nbRepCorrectes=0;
        if (numClickReponse==numJoueurGagnantCoup){
            repJoueur=String.format(Locale.getDefault(), "OUI, Joueur%d !", numJoueurGagnantCoup);
            nbRepCorrectes++;
        } else {
            repJoueur=String.format(Locale.getDefault(), "NON, c'est Joueur%d !", numJoueurGagnantCoup);
        }

        String strNbPoints=ui.etNbPoints.getText().toString();
        int nbPoints=0;
        if (!TextUtils.isEmpty(strNbPoints)){
            nbPoints= Integer.parseInt(strNbPoints);
        }
        String repNbPoints = "";
        if (nbPoints==nbPointsCoup){
            repNbPoints=String.format(Locale.getDefault(), "OUI, %d points", nbPointsCoup);
            nbRepCorrectes++;
        } else {
            repNbPoints=String.format(Locale.getDefault(), "NON, c'est %d points", nbPointsCoup);
        }

        // mise à jour des réponses utilisateurs
        if (nbRepCorrectes==2){
            nbReponsesCorrectes++;
            ui.tvNbRepCorrectes.setText(String.format(Locale.getDefault(), "%d / %d", nbReponsesCorrectes, NB_COUPS));
        }
        Toast toast = Toast.makeText(getApplicationContext(), repNbPoints+" "+repJoueur, Toast.LENGTH_LONG);
        toast.show();
    }
}