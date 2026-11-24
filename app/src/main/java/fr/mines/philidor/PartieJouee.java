package fr.mines.philidor;

import java.util.Date;

public class PartieJouee {

    public enum Resultat {VICTOIRE, DEFAITE, NULLE}

    public enum Fin {MAT, PAT, NULLE, ABANDON}

    private final Date date;
    private final boolean blancs;
    private final Resultat resultat;
    private final Fin fin;
    private final int nbCoups;

    public PartieJouee(Date date, boolean blancs, Resultat resultat, Fin fin, int nbCoups) {
        this.date = date;
        this.blancs = blancs;
        this.resultat = resultat;
        this.fin = fin;
        this.nbCoups = nbCoups;
    }

    public Date getDate() {
        return date;
    }

    public boolean isBlancs() {
        return blancs;
    }

    public Resultat getResultat() {
        return resultat;
    }

    public Fin getFin() {
        return fin;
    }

    public int getNbCoups() {
        return nbCoups;
    }
}
