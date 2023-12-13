package tla;

import java.util.ArrayList;
import java.util.List;

public class AnalyseSyntaxique {

	private List<Token> tokens;
	private int pos;

	public TypeDeToken getTypeDeToken() {
		if (pos >= tokens.size()) {
			return null;
		}
		else {
			Token t = tokens.get(pos);
			return t.getTypeDeToken();
		}		
	}
	
	public Token lireToken() {
		Token t = tokens.get(pos);
		//System.out.println(t);
		pos += 1;
		return t;
	}
	
	public boolean finAtteinte() {
		return pos >= tokens.size();
	}
	
	/*S => Lieu
	Lieu => intVal- *stringVal*Proposition
	Proposition => intVal)*stringVal*A'
	A' => Proposition | S | E*/

	
	public void S() {
		Lieu();		
	}
	
	public void Lieu() {
		//production Lieu => intVal-*stringVal*Proposition
		
		
		List<Proposition> listePropositions = new ArrayList<Proposition>();
		
		if (getTypeDeToken() == TypeDeToken.intVal) {
			lireToken();

			if (getTypeDeToken() == TypeDeToken.tiret) {
				lireToken();
				
				if (getTypeDeToken() == TypeDeToken.delimiteur) {
					lireToken();
					
					if (getTypeDeToken() == TypeDeToken.stringVal) {
						//lireToken();
						String description = lireToken().getValeur();
						
						if (getTypeDeToken() == TypeDeToken.delimiteur) {
							lireToken();
							listePropositions = Proposition();
							System.out.println(listePropositions.size());

							System.out.println("Description = " + description);
							
							Lieu lieu = new Lieu(description, listePropositions);						
						}
					}
				}
			}
		}
	}
	
	public List<Proposition> Proposition() {
		//production Lieu => intVal-*stringVal*Proposition
		List<Proposition> listePropositions = new ArrayList<Proposition>();
		
		if (getTypeDeToken() == TypeDeToken.intVal) {
			lireToken();

			if (getTypeDeToken() == TypeDeToken.parentheseDroite) {
				lireToken();
				
				if (getTypeDeToken() == TypeDeToken.delimiteur) {
					lireToken();
				
					if (getTypeDeToken() == TypeDeToken.stringVal) {
						String actionProposition = lireToken().getValeur();
						
						Proposition proposition = new Proposition(actionProposition, 1);
						listePropositions.add(proposition);
					
						if (getTypeDeToken() == TypeDeToken.delimiteur) {
							lireToken();
							actionProposition = A();
							listePropositions.add(proposition);
							
							return listePropositions;
						}
					}
				}
			}
		} 
		return null;
	}
	
	public String A() {
		//production A' => Proposition | S | E
		if (getTypeDeToken() == TypeDeToken.intVal) {
			pos +=1 ;
			//System.out.println(getTypeDeToken());
			
			if (getTypeDeToken() == TypeDeToken.parentheseDroite) {
				pos -= 1 ;
				return Proposition();
			}				
			
			if (getTypeDeToken() == TypeDeToken.tiret) {
				pos -= 1 ;
				Lieu();
			}
			
			
		}
		return null;
		
	}
	
	/*Problèmes :
	 * - Récupérer toutes les propositions.
	 * - Récupérer le num du lieu d'après. 
	 */
	
	

	public void analyse(List<Token> tokens) throws Exception {
		this.pos = 0;
		this.tokens = tokens;
		
		S();
	}
	
}
