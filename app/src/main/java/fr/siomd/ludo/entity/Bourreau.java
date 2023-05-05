package fr.siomd.ludo.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bourreau {
    public static final int MAX_REBUT = 8;    // nombre maximum de lettres au rebut
    private Juge leJuge;           // le juge
    private Mot leMot;             // mot à chercher de type Mot
    private String leMotCherche;   // contenu du mot à chercher en majuscules
    private String leMotEnCours;  // mot avec des _ à la place de chaque lettre non trouvée
    private ArrayList<Character> lesLettresAuRebut = new ArrayList<Character>();  // liste des lettres au rebut

    private String TAG = "bourreau";
    public Bourreau(Juge unJuge) {
        leJuge = unJuge;
        demarrer();
    }

    // initialisation des variables de travail utilisées pour la gestion d'un mot
    public void demarrer() {
        leMot =  leJuge.donnerMot();
        leMotCherche = leMot.getContenu().toUpperCase();
        lesLettresAuRebut.clear();
        // définir leMotEnCours : autant de caractères _ que de lettres dans le mot à trouver
        leMotEnCours = "";
        int longueurMot = leMotCherche.length();
        for (int i = 0; i < longueurMot; i++) {
            leMotEnCours = leMotEnCours + "_";
        }

    }

    // vrai si toutes les lettres du mot ont été trouvées
    public boolean isGagne() {
        if (Objects.equals(leMotCherche, leMotEnCours)){
            leJuge.ajouterScore(leMot.getNbPoints());
        }
        return Objects.equals(leMotCherche, leMotEnCours);
    }

    // vrai si maximum de lettres au rebut atteint
    public boolean isPerdu() {
        return lesLettresAuRebut.size() == MAX_REBUT;
    }

    // retourne le mot en cours (avec des _ ) qui est à afficher
    public String getLeMotEnCours() {
        return leMotEnCours;
    }

    public String getLeMotEntier() {
        return leMotCherche;
    }

    // retourne les lettres au rebut (qui peut être affichée)
    public String getLesLettresAuRebut () {

        String toutesLesLettres = "";
        for (Character laLettre : lesLettresAuRebut) {
            toutesLesLettres =  toutesLesLettres+" "+laLettre+",";
        }
        return toutesLesLettres;

    }




    // chercher la lettre dans le mot à trouver
    //     si la lettre est trouvée, le mémoriser
    //     sinon, mettre la lettre au rebut
    // si mot entièrement trouvé, alors mettre à jour le score
    public void executer(char uneLettre) {
        List<Integer> placeLettre = new ArrayList<>();
        for (int i = 0; i < leMotCherche.length(); i++) {
            if (leMotCherche.charAt(i) == uneLettre) {
                placeLettre.add(i);
                leMotEnCours = leMotEnCours.substring(0, i) + uneLettre + leMotEnCours.substring(i + 1);
            }
        }
        if (placeLettre.size() == leMotCherche.length()) {
            leJuge.ajouterScore(leMot.getNbPoints());
        } else if (placeLettre.isEmpty()) {
            lesLettresAuRebut.add(uneLettre);
        }
    }
}