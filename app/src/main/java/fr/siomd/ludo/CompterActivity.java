package fr.siomd.ludo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import fr.siomd.ludo.databinding.ActivityMainBinding;
import fr.siomd.ludo.entity.Chaine;

public class CompterActivity extends AppCompatActivity {
    private ActivityMainBinding ui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ui = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());

        setContentView(R.layout.activity_compter);


    }
    private int indexChaineActuel;

    private int nbtours = 0;
    private int nbtoursmax = 10;
    private int nbPoints = 0;
    private int valeurEditText = -1;
    private int valeurAttendue;

    private String [] tbContenuChaines;

    private Chaine[] tbChaines;



    private void genererJeu() {
        tbContenuChaines = new String[] {"Sanguinius", "Guilliman", "Abaddon", "Mortarion" , "Magnus", "Dorn", "Fulgrim", "Calgar", "Vashtorr", "Farsight", "Shadowsun", "Emperor of Mankind", "Belisarius Cole", "Lion el jonson", "Horus Lupercal"};
        tbChaines = new Chaine[tbContenuChaines.length];
        nbPoints = 0;
        indexChaineActuel = 0;
        TextView tvNbPoints = findViewById(R.id.tvNbPoints);
        tvNbPoints.setText(String.valueOf(nbPoints));

    }

    public void onClickbtDemarrerJeu(View view) {
        LinearLayout fenetreFinale = findViewById(R.id.fenetreFinDePartie);
        LinearLayout fenetreJeu = findViewById(R.id.fenetreJeu);
        LinearLayout fenetreAccueil = findViewById(R.id.fenetreAccueil);
        fenetreAccueil.setVisibility(View.GONE);
        fenetreFinale.setVisibility(View.GONE);
        fenetreJeu.setVisibility(View.VISIBLE);
        int indChaine = 0;
        genererJeu();
        Collections.shuffle(Arrays.asList(tbContenuChaines));
        for (String contenuChaine : tbContenuChaines) {
            tbChaines[indChaine] = new Chaine(contenuChaine);
            indChaine++;

        }
        indexChaineActuel = 0;
        nbtours = 0;
        nbPoints = 0;
        Jeu();
    }



    private void Jeu() {
        afficherPersonnage();
    }

    private void afficherPersonnage() {
        if (nbtours < nbtoursmax ) {
            String contenuChaineActuelle = tbChaines[indexChaineActuel].getContenu();

            String questionChaine = "";

            Random random = new Random();
            int nombreAleatoire = random.nextInt(3);

            switch (nombreAleatoire) {
                case 0:
                    questionChaine = "Combien y'a-t-il de lettres ?";
                    valeurAttendue = tbChaines[indexChaineActuel].getTaille();
                    break;
                case 1:
                    questionChaine = "Combien y'a-t-il de voyelles ?";
                    valeurAttendue = tbChaines[indexChaineActuel].getVoyelles();
                    break;
                case 2:
                    questionChaine = "Combien y'a-t-il de consonnes ?";
                    valeurAttendue = tbChaines[indexChaineActuel].getConsonnes();
                    break;

            }
            TextView tvQuestion = findViewById(R.id.tvQuestion);
            tvQuestion.setText(questionChaine);

            TextView tvChaine = findViewById(R.id.tvChaine);
            tvChaine.setText(contenuChaineActuelle);
        }else {
            Log.i("Fin de la partie", "Le score du joueur est : " + nbPoints);

            LinearLayout fenetreFinale = findViewById(R.id.fenetreFinDePartie);
            LinearLayout fenetreJeu = findViewById(R.id.fenetreJeu);
            LinearLayout fenetreAccueil = findViewById(R.id.fenetreAccueil);
            TextView tvScoreFinal = findViewById(R.id.tvScoreFinal);
            fenetreJeu.setVisibility(View.GONE);
            fenetreAccueil.setVisibility(View.GONE);
            tvScoreFinal.setText("Score du joueur : " + nbPoints);
            fenetreFinale.setVisibility(View.VISIBLE);
        }

    }


    private void nextTurn() {
        String msgToast = null;
        TextView tvNbPoints = findViewById(R.id.tvNbPoints);
        if (nbtours < nbtoursmax) {
            if (valeurEditText != -1) {
                if (valeurEditText==valeurAttendue) {
                    nbPoints += 3;

                    msgToast = "Oui bonne réponse ! +3 point";
                }else {
                    int diffValeur = 0;
                    if (valeurEditText < valeurAttendue) {
                        diffValeur = valeurAttendue - valeurEditText;
                    }else {
                        diffValeur = valeurEditText - valeurAttendue;
                    }
                    nbPoints -= diffValeur;
                    if (nbPoints < 0 ) {
                        nbPoints = 0;
                    }
                    msgToast = "Dommage, c'était " + valeurAttendue + ", vous perdez " + diffValeur + " points";
                }
                tvNbPoints.setText(String.valueOf(nbPoints));
            }
            EditText etNbPoints = findViewById(R.id.etNbPoints);
            etNbPoints.setText("");

            Toast toast = Toast.makeText(getApplicationContext(), msgToast, Toast.LENGTH_LONG);
            toast.show();
            nbtours += 1;
            indexChaineActuel += 1;
            // mettre à jour le score du joueur dans les préférences
            // récupérer le stockage des préférences
            SharedPreferences prefs =
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            // début de la transaction
            SharedPreferences.Editor editeur = prefs.edit();
            // modification du score
            editeur.putString("prefs_compter_score", Integer.toString(nbPoints) + " points au tour " + nbtours);
            // fin de la transaction
            editeur.commit();


        }
        afficherPersonnage();
    }


    public void onClickbtValider(View view) {
        EditText etNbPoints = findViewById(R.id.etNbPoints);
        if (etNbPoints.getText().toString().equals("")) {
            valeurEditText = 0;
        }else{
            valeurEditText = Integer.parseInt(etNbPoints.getText().toString());
        }

        nextTurn();

    }

}