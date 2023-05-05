package fr.siomd.ludo.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

import fr.siomd.ludo.entity.Mot;
import fr.siomd.ludo.entity.Theme;

public class DicoDatabase extends SQLiteOpenHelper {

    public DicoDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // création des tables
        String strReq = "CREATE TABLE theme (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT NOT NULL" +
                ");";
        db.execSQL(strReq);


        strReq = "CREATE TABLE mot (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "contenu TEXT NOT NULL, " +
                "nbPoints INTEGER NOT NULL, " +
                "idTheme INTEGER," +
                "FOREIGN KEY(idTheme) REFERENCES theme(id));";
        db.execSQL(strReq);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insererXML(XmlPullParser monXmlPullParser ) {
        // vérifier qu'il n'y a pas déjà des lignes dans la base
        SQLiteDatabase maDbRead = this.getReadableDatabase(); // pour pouvoir lire la base de données
        Cursor resultThemes = maDbRead.query ("theme", null, null, null, null, null, null,
                null);
        // si nombre de thèmes = 0 alors on insère les données à partir du fichier XML
        if (resultThemes.getCount() == 0) {
            SQLiteDatabase maDb = this.getWritableDatabase(); // pour pouvoir mettre à jour la base de données
            int idTheme = 0; // variable qui sert à récupérer l'id du thème inséré
            try {
                // lecture du fichier xml
                // création d'un enregistrement (une ligne, un n-uplet) theme par noeud 'theme' dans le document xml
                // création d'un enregistrement mot par noeud 'mot' dans le document xml
                // tant que ce n'est pas la fin du document XML
                while (monXmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    // s'il s'agit d'un élément début (balise début d'élément)
                    if (monXmlPullParser.getEventType() == XmlPullParser.START_TAG) {
                        if (monXmlPullParser.getName().equals("theme")) {
                            // le nom du thème correspond au premier attribut donc index 0
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("nom", monXmlPullParser.getAttributeValue(0));
                            Log.i("INSERTION BDD", "nom theme = " +
                                    contentValues.getAsString("nom"));
                            idTheme = (int) maDb.insert("theme", null, contentValues);
                        } else if (monXmlPullParser.getName().equals("mot")) {
                            // attribut d'index 0 = contenu du mot, attribut d'index 1 = nombre de points du mot
                            ContentValues contentValuesMot = new ContentValues();
                            contentValuesMot.put("contenu",
                                    monXmlPullParser.getAttributeValue(0));
                            contentValuesMot.put("nbPoints",
                                    Integer.parseInt(monXmlPullParser.getAttributeValue(1)));
                            contentValuesMot.put("idTheme", idTheme);
                            Log.i("INSERTION BDD", "contenu = " +
                                    contentValuesMot.getAsString("contenu"));
                            maDb.insert("mot", null, contentValuesMot);
                        }
                    }
                    monXmlPullParser.next();
                }
            } catch (Exception e) {
                Log.i("DICO", "Erreur = " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Theme> getLesThemes() {
        ArrayList<Theme> lesThemes = new ArrayList<Theme>();
        Theme unTheme;
        SQLiteDatabase maDbRead = getReadableDatabase();
        String[] listCol = {"contenu", "nbPoints"}; // colonnes de la table Mot à lire
        String whereCond = "idTheme=?"; // condition where pour la lecture de la table Mot
        Cursor resultThemes = maDbRead.query ("theme", null, null, null, null, null, null,
                null);
        int nbLignes = resultThemes.getCount();
        Log.i("LECTURE","nb lignes = " + String.valueOf(nbLignes));
        if (nbLignes > 0) {
            resultThemes.moveToFirst();
            while (!resultThemes.isAfterLast()) {
                // ajouter le thème à la liste
                unTheme = new Theme(resultThemes.getString(1));
                // lire les mots du thème
                int idTheme = resultThemes.getInt(0);
                String[] whereArg = {Integer.toString(idTheme)};
                Cursor resultMots = maDbRead.query ("mot", listCol, whereCond, whereArg,
                        null, null, null, null);
                int nbLignesMots = resultMots.getCount();
                Log.i("LECTURE","nb lignes mots du thème = " +
                        String.valueOf(nbLignesMots));
                if (nbLignesMots > 0) {
                    resultMots.moveToFirst();
                    while (!resultMots.isAfterLast()) {
                        unTheme.ajouterMot(new Mot(resultMots.getString(0),
                                resultMots.getInt(1)));
                        // passer au mot suivant
                        resultMots.moveToNext();
                    }
                }
                // fermer le curseur sur les mots
                resultMots.close();
                // ajouter le thème à la liste
                lesThemes.add(unTheme);
                // passer au thème suivant
                resultThemes.moveToNext();
            }
        }
        // fermer le curseur sur les mots
        resultThemes.close();
        return lesThemes;
    }









}
