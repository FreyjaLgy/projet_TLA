package tla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalyseSyntaxique {

	private List<Token> tokens;
	private int pos;

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
		//System.out.println(t);
		pos += 1;
		return t;
	}


	/**Méthode permettant de créer une HashMap de Lieu.
	 * 
	 * On crée une liste de Lieu, et à chaque fois que l'on trouve un intVal suivi d'un tiret, on ajoute un lieu à cette liste.*/
	
	public HashMap<Integer, Lieu> S() {
		HashMap<Integer, Lieu> listeLieux = new HashMap<Integer, Lieu>();
		
		if (getTypeDeToken() == TypeDeToken.intVal) {
			lireToken();

			while (getTypeDeToken() == TypeDeToken.tiret) {
				pos -= 1;
				String numLieu = getValeurIntVal();
				int numEndroit = Integer.parseInt(numLieu);
				pos += 1;
				
				lireToken();
				
				Lieu endroit = Lieu();
				
				listeLieux.put(numEndroit, endroit);
				
				numLieu += 1;
			}
		}
		return listeLieux;
	}
	
	
	/**Méthode permettant de créer un Lieu.
	 * 
	 *On lit un delimiteur, un stringVal (la description du lieu), puis un autre delimiteur.
	 *On cherche ensuite les propositions associées au lieu.
	 *Enfin, on crée le lieu.
	 * 
	 */
	
	public Lieu Lieu() {
		Lieu lieu = null;
		List<Proposition> listePropositions = new ArrayList<Proposition>();
				
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
			
			if (getTypeDeToken() == TypeDeToken.stringVal) {
				String description = lireToken().getValeur();
						
				if (getTypeDeToken() == TypeDeToken.delimiteur) {
					lireToken();
					listePropositions = Num();
							
					lieu = new Lieu(description, listePropositions);
				}
			}
		}
		
		return lieu;
	}
	
	
	/**Méthode permettant de créer une liste de propositions.
	 * 
	 * On crée une liste de propositions, et à chaque fois que l'on trouve un intVal et une parentheseDroite, on récupère le contenu de la
	 * proposition et le numéro du lieu vers lequel elle renvoie.
	 * A partir de là, on crée une proposition qu'on ajoute à la liste.
	 */
	
	public List<Proposition> Num() {
		List<Proposition> listePropositions = new ArrayList<Proposition>();
		
		if (getTypeDeToken() == TypeDeToken.intVal) {
			lireToken();
			
			while (getTypeDeToken() == TypeDeToken.parentheseDroite) {				
				lireToken();
				
				String contenuProposition = Proposition();
				String numLieu = A();
				int idLieu = Integer.parseInt(numLieu);
				Proposition proposition = new Proposition(contenuProposition, idLieu);
				listePropositions.add(proposition);				
				
				A_prime();							
			}
		} 
		return listePropositions;
	}
	
	
	/**Méthode permettant de récupérer le contenu d'une proposition.
	 * 
	 * On lit un delimiteur et on renvoie la valeur du stringVal qui suit.
	 */
	
	
	public String Proposition() {
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
		
			if (getTypeDeToken() == TypeDeToken.stringVal) {
				return lireToken().getValeur();
			}
		}
		return null;
	}
	
	
	/**Méthode permettant de récupérer le numéro du lieu vers lequel une proposition renvoie.
	 * 
	 * On lit un delimiteur, un crocheGauche, un intVal et un crochetDroit. On renvoie la valeur de l'intVal.
	 */
	
	public String A() {
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
	
	/**Méthode permettant de lire le delimiteur et l'intVal qui précède le contenu d'une proposition. 
	 */
	
	public void A_prime() {
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
			if (getTypeDeToken() == TypeDeToken.intVal) {
				lireToken();
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
	
}
