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
		System.out.println(t);
		pos += 1;
		return t;
	}
	
	/*
			Num => intVal-Lieu
			Lieu => *stringVal*Num’
			Num’ => intVal) Propo A A’
			Propo => *stringVal A’
			A => *[intval] | S
			A’ =>*intVal) Propo*/


	/**Méthode permettant de créer une HashMap de Lieu.*/
	
	public HashMap<Integer, Lieu> S() {
		//production S => Num | E
		
		HashMap<Integer, Lieu> listeLieux = new HashMap<Integer, Lieu>();
		
		if (getTypeDeToken() == TypeDeToken.intVal) {
			lireToken();

			TypeDeToken typeToken = getTypeDeToken();

			while (typeToken == TypeDeToken.tiret) {
				pos -= 1;
				String numLieu = getValeurIntVal();
				int numEndroit = Integer.parseInt(numLieu);
				pos += 1;
				
				lireToken();
				
				Lieu endroit = Lieu();
				
				listeLieux.put(numEndroit, endroit);
				
				numLieu += 1;
				
				typeToken = getTypeDeToken();
			}
		}
		return listeLieux;
	}
	
	public Lieu Lieu() {
		//production Lieu => intVal-*stringVal*Proposition
		
		Lieu lieu = null;
		List<Proposition> listePropositions = new ArrayList<Proposition>();
		
		
				
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
			
			if (getTypeDeToken() == TypeDeToken.stringVal) {
				//lireToken();
				String description = lireToken().getValeur();
						
				if (getTypeDeToken() == TypeDeToken.delimiteur) {
					//System.out.println("Description lieu = " + description);
						
					lireToken();
					listePropositions = Num();
						
					for (Proposition propo : listePropositions) {
					    //System.out.println("action propo = " + propo.texte + " ; numero lieu = " + propo.numeroLieu); 
					}
							
					//System.out.println("Nb propo (lieu) = " + listePropositions.size());
							
					lieu = new Lieu(description, listePropositions);
				}
			}
		}
		
		return lieu;
	}
	
	public List<Proposition> Num() {
		//production Lieu => intVal-*stringVal*Proposition
		List<Proposition> listePropositions = new ArrayList<Proposition>();
		
		if (getTypeDeToken() == TypeDeToken.intVal) {
			lireToken();
			
			TypeDeToken typeToken = getTypeDeToken();

			while (typeToken == TypeDeToken.parentheseDroite) {
				//System.out.println(typeToken);
				//System.out.println("entree");
				
				lireToken();
				
				String actionProposition = Proposition();
				String numLieu = A_prime();
				int idLieu = Integer.parseInt(numLieu);
				Proposition proposition = new Proposition(actionProposition, idLieu);
				listePropositions.add(proposition);
				
				//System.out.println("Nb propo = " + listePropositions.size());
				
				//Proposition();
				
				
				typeToken = A();							
			}
		} 
		return listePropositions;
	}
	
	
	public String Proposition() {
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
		
			if (getTypeDeToken() == TypeDeToken.stringVal) {
				String actionProposition = lireToken().getValeur();
				
				//System.out.println("Action = " + actionProposition);
				
				return actionProposition;
			}
		}
		return null;
	}
	
	
	public TypeDeToken A() {
		//production A' => Proposition | S | E
		TypeDeToken typeToken = null;
		if (getTypeDeToken() == TypeDeToken.delimiteur) {
			lireToken();
			if (getTypeDeToken() == TypeDeToken.intVal) {
				lireToken();
				typeToken = getTypeDeToken();
			}
		}
		return typeToken;
		
	}
	
	public String A_prime() {
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
	

	public HashMap<Integer, Lieu> analyse(List<Token> tokens) throws Exception {
		this.pos = 0;
		this.tokens = tokens;
		
		return S();
	}
	
}
