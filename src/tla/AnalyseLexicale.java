package tla;

import java.util.ArrayList;
import java.util.List;

public class AnalyseLexicale {
    /*
    Table de transition de l'analyse lexicale
     */
    private static Integer TRANSITIONS[][] = {
            //           0    1    2       3            4              5     6     7    8    9   10    11  12    13
            //           *    -    )    chiffre  lettre+caractères     [     ]     &    |    >    <    +    (   null
            /*  0 */   {101, 102, 103,     1,           2,           106,  107,    3,   4, 110, 111, 112, 113,   0},
            /*  1 */   {104, 104, 104,     1,         104,           104,  104,  104, 104, 104, 104, 104, 104, 104},
            /*  2 */   {105,   2,   2,     2,           2,             2,    2,    2,   2,   2,   2,   2,   2, 105}, //Ligne qui sert à constituer un texte. On continue d'ajouter au texte sauf si il y a un *
            /*  3 */   {105,   2,   2,     2,           2,             2,    2,  108,   2,   2,   2,   2,   2,   2}, //Ligne qui permet d'accepter &&
            /*  4 */   {105,   2,   2,     2,           2,             2,    2,    2, 109,   2,   2,   2,   2,   2}, //Ligne qui permet d'accepter ||

            // 101 acceptation d'un *
            // 102 acceptation d'un -
            // 103 acceptation d'un )
            // 104 acceptation d'un entier (retourArriere)
            // 105 acceptation d'un texte, sert aussi pour les identifiants (retourArriere)
            // 106 acceptation d'un [
            // 107 acceptation d'un ]
            // 108 acceptation d'un &&
            // 109 acceptation d'un ||
            // 110 acceptation d'un >
            // 111 acceptation d'un <
            // 112 acceptation d'un +
            // 113 acceptation d'un (

    };

    private static final int ETAT_INITIAL = 0;

    private String entree;
    private int pos;

    /*
    Effectue l'analyse lexicale et retourne une liste de Token
     */
    public List<Token> analyse(String entree) throws Exception {

        this.entree = entree;
        pos = 0;

        List<Token> tokens = new ArrayList<>();
        String buf = "";
        Integer etat = ETAT_INITIAL;

        Character c;
        ;
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
                    //Si les premiers mots d'un stringVal sont PV ou Random, ils sont identifiés comme des mots clés
                    if (buf.length() >= 2 && buf.substring(0, 2).equalsIgnoreCase("PV")) {
                        tokens.add(new Token(TypeDeToken.PV));
                        String reste = buf.substring(2, buf.length());
                        if (reste.length() > 0) {
                            AnalyseLexicale al = new AnalyseLexicale();
                            List<Token> tokens2 = al.analyse(reste); //On réanalyse la partie restante
                            tokens.addAll(tokens2); //Puis on ajoute à la liste des tokens le résultat de cette analyse
                        }
                    } else if (buf.length() >= 6 && buf.substring(0, 6).equalsIgnoreCase("Random")) {
                        tokens.add(new Token(TypeDeToken.Random));
                        String reste = buf.substring(6, buf.length());
                        if (reste.length() > 0) {
                            AnalyseLexicale al=new AnalyseLexicale();
                            List<Token> tokens2 = al.analyse(reste); //On réanalyse la partie restante
                            tokens.addAll(tokens2);
                        }
                    } else {
                        tokens.add(new Token(TypeDeToken.stringVal, buf));
                    }
                    retourArriere();
                } else if (e == 106) {
                    tokens.add(new Token(TypeDeToken.crochetGauche));
                } else if (e == 107) {
                    tokens.add(new Token(TypeDeToken.crochetDroit));
                } else if (e == 108) {
                    tokens.add(new Token(TypeDeToken.etLogique));
                } else if (e == 109) {
                    tokens.add(new Token(TypeDeToken.ouLogique));
                } else if (e == 110) {
                    tokens.add(new Token(TypeDeToken.superieur));
                } else if (e == 111) {
                    tokens.add(new Token(TypeDeToken.inferieur));
                } else if (e == 112) {
                    tokens.add(new Token(TypeDeToken.plus));
                } else if (e == 113) {
                    tokens.add(new Token(TypeDeToken.parentheseGauche));
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

    /*
    Permet de lire le caractère à la position pos
     */
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
    Pour chaque symbole terminal acceptable en entrée de l'analyse syntaxique
    retourne un indice identifiant soit un symbole, soit une classe de symbole
     */
    private static int indiceSymbole(Character c) throws IllegalCharacterException {
        if (c == null) return 13;
        if (c == '*') return 0;
        if (c == '-') return 1;
        if (c == ')') return 2;
        if (Character.isDigit(c)) return 3;
        if (c == '[') return 5;
        if (c == ']') return 6;
        if (c == '&') return 7;
        if (c == '|') return 8;
        if (c == '>') return 9;
        if (c == '<') return 10;
        if (c == '+') return 11;
        if (c == '(') return 12;
        if (estStringVal(c)) return 4;
        System.out.println("Symbole inconnu : " + c);
        throw new IllegalCharacterException(c.toString());
    }

    /*
    Les textes de l'aventure peuvent contenir des symboles, tels que des parenthèses.
    Cette fonction permet de définir ce qui est acceptable dans les String de nos lieux/propositions.
    Elle accepte &+-!$%^&()"'{}_[]|\?/<>,.;: en plus des lettres et des espaces
    Renvoie true si c'est un caractère qu'on accepte, false sinon
     */
    private static boolean estStringVal(Character c) {
        boolean estStringVal = false;
        String caracteresSpeciaux = "&+-!$%^&()\"\'{}_[]|\\?/<>,.;:";
        if (caracteresSpeciaux.indexOf(c) != -1)
            estStringVal = true;
        if (Character.isLetter(c))
            estStringVal = true;
        if (Character.isWhitespace(c))
            estStringVal = true;
        return estStringVal;
    }

}

