package fr.siomd.ludo.entity;
import java.util.ArrayList;
public class Theme {
    private String nom;
    private ArrayList<Mot> lesMots;
    public Theme(String pNom) { nom = pNom;
        lesMots = new ArrayList<Mot>();
    }
    public String getNom() {
        return nom; }
    public void setNom(String nom) { nom = nom;
    }
    public ArrayList<Mot> getLesMots() { return lesMots;
    }
    public void setLesMots(ArrayList<Mot> lesMots) { lesMots = lesMots;
    }
    public void ajouterMot(Mot pMot) { lesMots.add(pMot);
    }
}