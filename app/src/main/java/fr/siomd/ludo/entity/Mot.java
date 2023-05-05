package fr.siomd.ludo.entity;
public class Mot {
    private String contenu; private int nbPoints;
    public Mot(String pContenu, int pNbPoints) { contenu = pContenu;
        nbPoints = pNbPoints;
    }
    public String getContenu() { return contenu;
    }
    public void setContenu(String contenu) { contenu = contenu;
    }
    public int getNbPoints() { return nbPoints;
    }
    public void setNbPoints(int nbpoints) {
        nbPoints = nbpoints; }
}