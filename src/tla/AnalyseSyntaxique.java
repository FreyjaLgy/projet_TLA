package tla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalyseSyntaxique {

	private List<Token> tokens;
	private int pos;
	private int PV;
	
	
	/**Méthode permettant d'obtenir le type de token à lire.*/	
	
	public TypeDeToken getTypeDeToken() {
		if (pos >= tokens.size()) {
			return null;
		}
		else {
			Token t = tokens.get(pos);
			return t.getTypeDeToken();
		}		
	}
	
	/**Méthode permettant d'obtenir la valeur des tokens de type intVal et stringVal.
	 * 
	 * Les tokens intVal et stringVal sont en réalité tous deux des chaînes de caractères.
	 * Si le token est de l'un de ces deux types, la méthode renvoie la valeur de la chaîne de caractère.
	 */

	public String getValeurIntVal() {
		if (pos >= tokens.size()) {
			return null;
		}
		else {
			if ((getTypeDeToken() == TypeDeToken.intVal) || (getTypeDeToken() == TypeDeToken.stringVal)) {
				return tokens.get(pos).getValeur();
			}
			else
				return null;
		}		
	}
	
	
	/**Méthode permettant de lire un token et de positionner le pointeur devant le token suivant.*/
	
	public Token lireToken() {
		Token t = tokens.get(pos);
		System.out.println(t);
		pos += 1;
		return t;
	}


	/**Méthode permettant de créer une HashMap de Lieu.
	 * 
	 * On crée une liste de Lieu, et à chaque fois que l'on trouve un intVal suivi d'un tiret, on ajoute un lieu à cette liste.*/
	
	private HashMap<Integer, Lieu> S() {
		//S → Lieu
		
		HashMap<Integer, Lieu> listeLieux = new HashMap<Integer, Lieu>();
		
		return Lieu();
	}
	
	
	/**Méthode permettant de créer un Lieu.
	 * 
	 *On lit un delimiteur, un stringVal (la description du lieu), puis un autre delimiteur.
	 *On cherche ensuite les propositions associées au lieu.
	 *Enfin, on crée le lieu.
	 * 
	 */
	
	private HashMap<Integer, Lieu> Lieu() {
		//Lieu → intVal-*stringVal Num *S

		Lieu lieu = null;
		HashMap<Integer, Lieu> listeLieux = new HashMap<Integer, Lieu>();
		List<Proposition> listePropositions = new ArrayList<Proposition>();
		
		if (getTypeDeToken() == TypeDeToken.intVal) {
			lireToken();
			
			while (getTypeDeToken() == TypeDeToken.tiret) {
				pos -= 1;
				String numLieu = getValeurIntVal();
				int numEndroit = Integer.parseInt(numLieu);
				pos += 1;
				//System.out.println("num lieu = " + numEndroit);
				
				lireToken();
				if (getTypeDeToken() == TypeDeToken.delimiteur) {
					//System.out.print("-*");
					lireToken();
					if (getTypeDeToken() == TypeDeToken.stringVal) {
						String description = lireToken().getValeur();
						
						listePropositions = Num();
						
						lireToken();
						
						lieu = new Lieu(description, listePropositions);
						listeLieux.put(numEndroit, lieu);
					}
				}
			}
		}
		
		return listeLieux;
	}
	
	
	/**Méthode permettant de créer une liste de propositions.
	 * 
	 * On crée une liste de propositions, et à chaque fois que l'on trouve un intVal et une parentheseDroite, on récupère le contenu de la
	 * proposition et le numéro du lieu vers lequel elle renvoie.
	 * A partir de là, on crée une proposition qu'on ajoute à la liste.
	 */
	
	private List<Proposition> Num() {
		//Num → A’ Propo Objet A
		List<Proposition> listePropositions = new ArrayList<Proposition>();
		
		A_prime();
		
		pos -= 1;
		
		while (getTypeDeToken() == TypeDeToken.parentheseDroite) {
			lireToken();
			
			String contenuProposition = Propo();
			
			int bonusMalus = Objet();
			
			String numLieu = A();
			int idLieu = Integer.parseInt(numLieu);
			Proposition proposition = new Proposition(contenuProposition, idLieu, bonusMalus);
			
			listePropositions.add(proposition);	
			
			A_prime();

			pos -= 1;
		}
		return listePropositions;
	}
	
	
	/**Méthode permettant de récupérer le contenu d'une proposition.
	 * 
	 * On lit un delimiteur et on renvoie la valeur du stringVal qui suit.
	 */
	
	
	private String Propo() {
		//Propo → *stringVal
		
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			//System.out.print(")*");
			lireToken();
		
			if (getTypeDeToken() == TypeDeToken.stringVal) {
				return lireToken().getValeur();
			}
		}
		return null;
	}
	
	
	private int Objet() {
		//Objet → *Nom*Effet*Condition
		
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			//System.out.print("contenuPropo*");
			lireToken();
			
			Nom();
				
			if (getTypeDeToken() == TypeDeToken.delimiteur) {
				//System.out.print("nomObjet*");
				lireToken();
					
				int bonusMalus = Effet();
					
				if (getTypeDeToken() == TypeDeToken.delimiteur) {
					//System.out.print("Effet*");
					lireToken();
						
					Condition();
					
					return bonusMalus;
				}
			}
		}
		return 0;
	}
	
	private void Nom() {
		//Nom → stringVal || Ɛ

		if (getTypeDeToken() == TypeDeToken.stringVal) {
			lireToken();
		}		
	}
	
	
	private int Effet() {
		//Effet → Signe intVal Identifiant’
		
		int bonusMalus = 0;
		
		Signe();
		
		if (getTypeDeToken() == TypeDeToken.intVal) {
			Token points = lireToken();
			int nbPoints =  Integer.parseInt(points.getValeur());
			
			pos -=2;
			if (getTypeDeToken() == TypeDeToken.plus) {
				bonusMalus = nbPoints;
				PV += nbPoints;
			}
			
			if (getTypeDeToken() == TypeDeToken.tiret) {
				bonusMalus = -nbPoints;
				PV -= nbPoints;
			}
			pos +=2;
			
			
			Identifiant_prime();
		}
		
		return bonusMalus;
	}
	
	
	private void Signe() {
		//Signe → +
		if (getTypeDeToken() == TypeDeToken.plus) {
			lireToken();
		}

		//Signe → -
		if (getTypeDeToken() == TypeDeToken.tiret) {
			lireToken();
		}
	}
	
	private void Condition() {
		
		
		//Condition → T
		
		if ((getTypeDeToken() == TypeDeToken.PV) || (getTypeDeToken() == TypeDeToken.Random)) {
			Token[] tokenCondition = T();
		}
		
		
		//Condition → (Condition)
		
		if (getTypeDeToken() == TypeDeToken.parentheseGauche) {
			lireToken();
			Condition();
			
			if (getTypeDeToken() == TypeDeToken.intVal) {
				lireToken();
			}
			
			if (getTypeDeToken() == TypeDeToken.parentheseDroite) {
				lireToken();
			}
		}

		
		//Condition → Condition Op Condition
		
		if (getTypeDeToken() == TypeDeToken.intVal) {
			lireToken();
		}
		
		if ((getTypeDeToken() == TypeDeToken.ouLogique) || (getTypeDeToken() == TypeDeToken.etLogique)) {
			lireToken();
			
			if ((getTypeDeToken() == TypeDeToken.PV) || (getTypeDeToken() == TypeDeToken.Random) || (getTypeDeToken() == TypeDeToken.parentheseGauche)) {
				Condition();
			}
		}
		
		
		return tokenCondition +
	}
	
	private Token[] T() {
		//T → Identifiant > intVal | Identifiant < intVal
		
		Token ident = Identifiant();
		Token symbole = null;
			
		if ((getTypeDeToken() == TypeDeToken.inferieur) || (getTypeDeToken() == TypeDeToken.superieur)) {
			symbole = lireToken();
		}
		
		Token tokenCondition[] = new Token[2];
		
		tokenCondition[0] = ident;
		tokenCondition[1] = symbole;
		
		return tokenCondition;
		
		//T → Possède stringVal
	}
	
	
	private Token Identifiant() {
		//Identifiant → PV | Random
		
		if ((getTypeDeToken() == TypeDeToken.PV) || (getTypeDeToken() == TypeDeToken.Random)) {
			Token t = lireToken();
			
			System.out.println("PV = " + getPV());
			
			return t;
		}
		
		return null;
	}
	

	private void Identifiant_prime() {
		//Identifiant’ → PV
		
		if (getTypeDeToken() == TypeDeToken.PV) {
			lireToken();
		}
	}

	
	
	/**Méthode permettant de récupérer le numéro du lieu vers lequel une proposition renvoie.
	 * 
	 * On lit un delimiteur, un crocheGauche, un intVal et un crochetDroit. On renvoie la valeur de l'intVal.
	 */
	
	private String A() {
		//A → *[intval] Num
		String numLieu = null;
		
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			//System.out.print("condition*");
			lireToken();
			
			if (getTypeDeToken() == TypeDeToken.crochetGauche) {
				lireToken();
				
				if (getTypeDeToken() == TypeDeToken.intVal){
					numLieu = lireToken().getValeur();
					
					if (getTypeDeToken() == TypeDeToken.crochetDroit) {
						lireToken();
					}
				}
			
			}
		}
			
		return numLieu ;
	}
	
	/**Méthode permettant de lire le delimiteur et l'intVal qui précède le contenu d'une proposition. 
	 */
	
	private void A_prime() {
		//A’ → *intVal)
		
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
			if (getTypeDeToken() == TypeDeToken.intVal) {
				lireToken();
				if (getTypeDeToken() == TypeDeToken.parentheseDroite) {
					//System.out.print("lecture a_prime");
					lireToken();
				}
			}
		}
	}
	

	/**Méthode retournant une map de Lieu créée à partir d'une liste de tokens.
	 */

	public HashMap<Integer, Lieu> analyse(List<Token> tokens) throws Exception {
		this.pos = 0;
		this.tokens = tokens;
		
		return S();
	}
	
	
	public int getPV() {
		return PV;
	}
	
	
	
}
