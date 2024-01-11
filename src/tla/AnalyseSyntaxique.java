package tla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalyseSyntaxique {

	private List<Token> tokens;
	private int pos;
	private int PV;
	private int bonusMalus = 0;
	private ArrayList<Condition> conditions;
	
	
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


	/**Méthode correspondant à la production S → Lieu
	 * 
	 */
	
	private HashMap<Integer, Lieu> S() {
		HashMap<Integer, Lieu> listeLieux = new HashMap<Integer, Lieu>();
		
		return Lieu();
	}
	
	
	/**Méthode correspondant à la production Lieu → intVal-*stringVal Num *S
	 * 
	 */
	
	private HashMap<Integer, Lieu> Lieu() {
		//

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
				
				lireToken();
				if (getTypeDeToken() == TypeDeToken.delimiteur) {

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
	
	
	/**Méthode correspondant à la production Num → A’ Propo Objet A
	 * 
	 */
	
	private List<Proposition> Num() {
		List<Proposition> listePropositions = new ArrayList<Proposition>();
		
		A_prime();
		
		pos -= 1;
		
		while (getTypeDeToken() == TypeDeToken.parentheseDroite) {
			lireToken();
			
			String contenuProposition = Propo();
			
			bonusMalus = 0 ; //Initialisation du bonusMalus pour la proposition.
			conditions = new ArrayList<Condition>(); //Initialisation de la liste des conditions.
			Objet();
			
			String numLieu = A();
			int idLieu = Integer.parseInt(numLieu);
			Proposition proposition = new Proposition(contenuProposition, idLieu, bonusMalus, conditions);
			
			listePropositions.add(proposition);	
			
			A_prime();

			pos -= 1;
		}
		return listePropositions;
	}
	
	
	/**Méthode correspondant à la production Propo → *stringVal
	 * 
	 */
	
	
	private String Propo() {
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
		
			if (getTypeDeToken() == TypeDeToken.stringVal) {
				return lireToken().getValeur();
			}
		}
		return null;
	}
	
	
	/**Méthode correspondant à la production Objet → *Nom*Effet*Condition
	 * 
	 */
	
	private void Objet() {
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
			
			Nom();
				
			if (getTypeDeToken() == TypeDeToken.delimiteur) {
				lireToken();
					
				Effet();
					
				if (getTypeDeToken() == TypeDeToken.delimiteur) {
					lireToken();
						
					Condition();
				}
			}
		}
	}
	
	/**Méthode correspondant à la production Nom → stringVal || Ɛ
	 * 
	 */
	
	private void Nom() {
		if (getTypeDeToken() == TypeDeToken.stringVal) {
			lireToken();
		}		
	}
	
	
	/**Méthode correspondant à la production Effet → Signe intVal Identifiant’
	 * 
	 */
	
	private void Effet() {
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
	}
	
	
	/**Méthode correspondant à la production Signe → + | -
	 * 
	 */
	
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
	
	/**Méthode correspondant à la production Condition → (Condition) | Condition Op Condition | T
	 * 
	 */
	
	private void Condition() {
		//Condition → T
		
		if ((getTypeDeToken() == TypeDeToken.PV) || (getTypeDeToken() == TypeDeToken.Random)) {
			T();
			
			//Créer une condition.
			pos -= 3;
			TypeDeToken ident = getTypeDeToken();
			pos++;
			TypeDeToken symbole = getTypeDeToken();
			pos++;
			int val = Integer.parseInt(getValeurIntVal());
			pos++;
			
			TypeDeToken operateur = null; 
			
			if ((getTypeDeToken() == TypeDeToken.ouLogique) || (getTypeDeToken() == TypeDeToken.etLogique)) {
				operateur = getTypeDeToken();
			}
			
			Condition c = new Condition(ident, symbole, val, operateur);
			
			conditions.add(c);
			
			
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
		
		if ((getTypeDeToken() == TypeDeToken.ouLogique) || (getTypeDeToken() == TypeDeToken.etLogique)) {
			lireToken();
			
			if ((getTypeDeToken() == TypeDeToken.PV) || (getTypeDeToken() == TypeDeToken.Random) || (getTypeDeToken() == TypeDeToken.parentheseGauche)) {
				Condition();
			}
		}
		
	}
	
	/**Méthode correspondant à la production T → Identifiant > intVal | Identifiant < intVal | Possède stringVal
	 * 
	 */
	
	private void T() {
		//T → Identifiant > intVal | Identifiant < intVal
		
		Identifiant();
		Token symbole = null;
			
		if ((getTypeDeToken() == TypeDeToken.inferieur) || (getTypeDeToken() == TypeDeToken.superieur)) {
			symbole = lireToken();

			if (getTypeDeToken() == TypeDeToken.intVal) {
				lireToken();
			}
			
		}
		
		//T → Possède stringVal
	}
	
	
	/**Méthode correspondant à la production Identifiant → PV | Random
	 * 
	 */
	
	private void Identifiant() {
		if ((getTypeDeToken() == TypeDeToken.PV) || (getTypeDeToken() == TypeDeToken.Random)) {
			lireToken();
		}
	}
	

	/**Méthode correspondant à la production Identifiant’ → PV
	 * 
	 */
	
	private void Identifiant_prime() {
		if (getTypeDeToken() == TypeDeToken.PV) {
			lireToken();
		}
	}

	
	
	/**Méthode correspondant à la production A → *[intval] Num
	 * 
	 */
	
	private String A() {
		String numLieu = null;
		
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
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
	
	/**Méthode correspondant à la production A’ → *intVal)
	 * 
	 */
	
	private void A_prime() {
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
			if (getTypeDeToken() == TypeDeToken.intVal) {
				lireToken();
				if (getTypeDeToken() == TypeDeToken.parentheseDroite) {
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
