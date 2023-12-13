package tla;

import java.util.ArrayList;
import java.util.List;

public class AnalyseLexicale {
    /*
    Table de transition de l'analyse lexicale
     */
    private static Integer TRANSITIONS[][] = {
            //Là où il y a un null c'est que je sais pas encore quoi mettre
            //           0    1    2       3            4
            //           *    -    )    chiffre  lettre+caractères
            /*  0 */   {  2, 102, 103,   1,   2},
            /*  1 */   {104, 104, 104,   1, 104},
            /*  2 */   {105,   2,   2,   2,   2}, //Ligne qui sert à constituer un texte. On continue d'ajouter au texte sauf si il y a un *

            // 101 acceptation d'un *
            // 102 acceptation d'un -
            // 103 acceptation d'un )
            // 104 acceptation d'un entier (retourArriere)
            // 105 acceptation d'un texte (retourArriere)

    };

    private static final int ETAT_INITIAL = 0;

    private String entree;
    private int pos;

    /*
    Effectue l'analyse lexicale et retourne une liste de Token
     */
    public List<Token> analyse(String entree) throws Exception {

        this.entree=entree;
        pos = 0;

        List<Token> tokens = new ArrayList<>();
        String buf = "";
        Integer etat = ETAT_INITIAL;

        Character c;
        do {
            c = lireCaractere();
            Integer e = TRANSITIONS[etat][indiceSymbole(c)];
            if (e == null) {
                System.out.println("pas de transition depuis état " + etat + " avec symbole " + c);
                throw new LexicalErrorException("pas de transition depuis état " + etat + " avec symbole " + c);
            }
            // cas particulier lorsqu'un état d'acceptation est atteint
            if (e >= 100) {
                if (e == 101) {
                    tokens.add(new Token(TypeDeToken.delimiteur));
                } else if (e == 102) {
                    tokens.add(new Token(TypeDeToken.tiret));
                } else if (e == 103) {
                    tokens.add(new Token(TypeDeToken.parentheseDroite));
                } else if (e == 104) {
                    tokens.add(new Token(TypeDeToken.intVal, buf));
                    retourArriere();
                } else if (e == 105) {
                    tokens.add(new Token(TypeDeToken.stringVal, buf));
                    retourArriere();
                }
                // un état d'acceptation ayant été atteint, retourne à l'état 0
                etat = 0;
                // reinitialise buf
                buf = "";
            } else {
                // enregistre le nouvel état
                etat = e;
                // ajoute le symbole qui vient d'être examiné à buf
                buf += c;
            }

        } while (c != null);

        return tokens;
    }

    private Character lireCaractere() {
        Character c;
        try {
            c = entree.charAt(pos);
            pos = pos + 1;
        } catch (StringIndexOutOfBoundsException ex) {
            c = null;
        }
        return c;
    }

    private void retourArriere() {
        pos = pos - 1;
    }

    /*
    TODO : A Modifier
    Pour chaque symbole terminal acceptable en entrée de l'analyse syntaxique
    retourne un indice identifiant soit un symbole, soit une classe de symbole :
     */
    private static int indiceSymbole(Character c) throws IllegalCharacterException {
        if (c == null || c == '*') return 0;
        if (c == '-') return 1;
        if (c == ')') return 2;
        if (Character.isDigit(c)) return 3;
        if (Character.isLetter(c)) return 4;
        System.out.println("Symbole inconnu : " + c);
        throw new IllegalCharacterException(c.toString());
    }

}

