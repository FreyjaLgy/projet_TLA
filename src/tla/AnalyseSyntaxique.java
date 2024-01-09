package tla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalyseSyntaxique {

	private List<Token> tokens;
	private int pos;

	/**M�thode permettant d'obtenir le type de token � lire.*/	
	
	public TypeDeToken getTypeDeToken() {
		if (pos >= tokens.size()) {
			return null;
		}
		else {
			Token t = tokens.get(pos);
			return t.getTypeDeToken();
		}		
	}
	
	/**M�thode permettant d'obtenir la valeur des tokens de type intVal et stringVal.
	 * 
	 * Les tokens intVal et stringVal sont en r�alit� tous deux des cha�nes de caract�res.
	 * Si le token est de l'un de ces deux types, la m�thode renvoie la valeur de la cha�ne de caract�re.
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
	
	
	/**M�thode permettant de lire un token et de positionner le pointeur devant le token suivant.*/
	
	public Token lireToken() {
		Token t = tokens.get(pos);
		//System.out.println(t);
		pos += 1;
		return t;
	}


	/**M�thode permettant de cr�er une HashMap de Lieu.
	 * 
	 * On cr�e une liste de Lieu, et � chaque fois que l'on trouve un intVal suivi d'un tiret, on ajoute un lieu � cette liste.*/
	
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
	
	
	/**M�thode permettant de cr�er un Lieu.
	 * 
	 *On lit un delimiteur, un stringVal (la description du lieu), puis un autre delimiteur.
	 *On cherche ensuite les propositions associ�es au lieu.
	 *Enfin, on cr�e le lieu.
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
	
	
	/**M�thode permettant de cr�er une liste de propositions.
	 * 
	 * On cr�e une liste de propositions, et � chaque fois que l'on trouve un intVal et une parentheseDroite, on r�cup�re le contenu de la
	 * proposition et le num�ro du lieu vers lequel elle renvoie.
	 * A partir de l�, on cr�e une proposition qu'on ajoute � la liste.
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
	
	
	/**M�thode permettant de r�cup�rer le contenu d'une proposition.
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
	
	
	/**M�thode permettant de r�cup�rer le num�ro du lieu vers lequel une proposition renvoie.
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
	
	/**M�thode permettant de lire le delimiteur et l'intVal qui pr�c�de le contenu d'une proposition. 
	 */
	
	public void A_prime() {
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
			if (getTypeDeToken() == TypeDeToken.intVal) {
				lireToken();
			}
		}
	}
	
	
	/**M�thode retournant une map de Lieu cr��e � partir d'une liste de tokens.
	 */

	public HashMap<Integer, Lieu> analyse(List<Token> tokens) throws Exception {
		this.pos = 0;
		this.tokens = tokens;
		
		return S();
	}
	
}
