package tla;

import java.util.ArrayList;

/*
 * Proposition faite au joueur pour poursuivre dans l'aventure.
 * 
 * Une proposition mène à un nouveau lieu, identifié par un numéro.
 */
public class Proposition {
    String texte;
    int numeroLieu;
    int bonusMalus;
    //ArrayList<Condition> conditions;

    //public Proposition(String texte, int numeroLieu, int bonusMalus, ArrayList<Condition> conditions) {
    public Proposition(String texte, int numeroLieu, int bonusMalus) {
        this.texte = texte;
        this.numeroLieu = numeroLieu;
        this.bonusMalus = bonusMalus;
        //this.conditions = conditions;
    }
}
