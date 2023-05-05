package fr.siomd.ludo.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Chaine {

    private String contenu;

    /**
     * @param pContenu
     */
    public Chaine(String pContenu){
        this.contenu = pContenu;
    }

    public String getContenu() { return contenu;}

    public int getVoyelles() {
        int count = 0;
        for (int i = 0; i < contenu.length(); i++) {
            char c = contenu.toLowerCase().charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y') {
                count++;
            }
        }
        return count;
    }

    public int getConsonnes() {
        int count = 0;
        for (int i = 0; i < contenu.length(); i++) {
            char c = contenu.toLowerCase().charAt(i);
            if (c != 'a' && c != 'e' && c != 'i' && c != 'o' && c != 'u' && c != 'y' && c != ' ' && c != '-') {
                count++;
            }
        }
        return count;
    }

    public int getTaille() {
        String newContenu = contenu.replace(" ", "");
        return newContenu.length();
    }
}
