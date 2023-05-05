package fr.siomd.ludo.dataaccess;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser; import java.util.ArrayList;

import fr.siomd.ludo.entity.Mot; import fr.siomd.ludo.entity.Theme;

public class DicoXml {
    public static ArrayList<Theme> getLesthemes(XmlPullParser monXmlPullParser ) {
        ArrayList<Theme> lesThemes = new ArrayList<Theme>();
        Theme themeLu = null;
        try {
            // lecture du fichier xml
            // création d'un objet Theme par noeud 'theme' dans le document xml
            // création d'un objet Mot par noeud 'mot' dans le document xml
            // tant que ce n'est pas la fin du document XML
            while (monXmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                // s'il s'agit d'un élément début (balise début d'élément)
                if (monXmlPullParser.getEventType() == XmlPullParser.START_TAG) {
                    if (monXmlPullParser.getName().equals("theme")) {
                        // le nom du thème correspond au premier attribut donc index 0
                        themeLu = new Theme(monXmlPullParser.getAttributeValue(0));
                        lesThemes.add(themeLu);
                    } else if (monXmlPullParser.getName().equals("mot")) {

                        // attribut d'index 0 = contenu du mot, attribut d'index 1 = nombre  de points du mot
                        Mot unMot = new Mot(monXmlPullParser.getAttributeValue(0), Integer.parseInt(monXmlPullParser.getAttributeValue(1)));
                        themeLu.ajouterMot(unMot);
                    }
                }
                monXmlPullParser.next();
            }
        } catch (Exception e) {
            Log.i("DICO", "Erreur = " + e.getMessage()); e.printStackTrace();
        }
        return lesThemes; }
}