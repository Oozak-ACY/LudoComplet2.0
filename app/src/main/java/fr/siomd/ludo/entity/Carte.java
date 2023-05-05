package fr.siomd.ludo.entity;

public class Carte {


    private String couleur;

    private String figure;


    /**
     * @param pCouleur
     * @param pFigure
     */
    public Carte(String pCouleur, String pFigure){
        this.couleur = pCouleur;
        this.figure = pFigure;
    }

    /**
     *
     * @return la couleur da la carte
     */
    public String getCouleur() {
        return couleur;
    }

    public String getFigure() {
        return figure;
    }

    public String getNom(){
        return figure + " de " + couleur;
    }

    public boolean isAtout( String atout){
        if (couleur.equals(atout)){
            return true;
        }else {
            return false;
        }
    }

    public int getValeur(){
        int value = 0;
        switch (figure){
            case "As":
                value =  14;
                break;
            case "Roi":
                value = 13;
                break;
            case "Dame":
                value = 12;
                break;
            case "Valet":
                value = 11;
                break;
            default:
                value = Integer.parseInt(figure);
                break;
        }
        return value;
    }

    public String getNomImg(){
        String laCouleur="";
        switch (couleur){
            case "Coeur":
                laCouleur="co";
                break;
            case "Carreau":
                laCouleur="ca";
                break;
            case "Pique":
                laCouleur="p";
                break;
            case "Trèfle":
                laCouleur="t";
                break;
        }
        return String.format("%s%d",laCouleur,getValeur());
    }

}
