package tla;

public class AnalyseLexicale {
    /*
    Table de transition de l'analyse lexicale
     */
    private static Integer TRANSITIONS[][] = {
            //Là où il y a un null c'est que je sais pas encore quoi mettre
            //           0    1    2       3            4
            //           *    -    )    chiffre  lettre+caractères
            /*  0 */   {  2, null, null, null, 2},
            /*  1 */   {105, 2, 2, 2, 2}, //Ligne qui sert à constituer un texte. On continue d'ajouter au texte sauf si il y a un *

            // 101 acceptation d'un *
            // 102 acceptation d'un -
            // 103 acceptation d'un )
            // 104 acceptation d'un entier (retourArriere)
            // 105 acceptation d'un texte (retourArriere)

    };

    private static final int ETAT_INITIAL = 0;
}
