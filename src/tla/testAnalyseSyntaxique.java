package tla;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class testAnalyseSyntaxique {


	public static void main(String[] args) {
		testAnalyseSyntaxique("1-*Chambre*1)*Ranger.*[19]*2)*Sortir*[23]*2-*TestLieux*3)*Propo*[56]");
	}

	/*
	effectue l'analyse lexicale de la chaine entree,
	affiche la liste des tokens reconnus
	 */
	private static void testAnalyseSyntaxique(String entree) {
		System.out.println("test de l'analyse syntaxique sur l'entrée " + entree);
		AnalyseLexicale al = new AnalyseLexicale();
		List<Token> listToken = null;
		try {
			listToken = al.analyse(entree);
			
			for (Token t : listToken) {
				//System.out.print(t.toString());
			}
			System.out.println();

			AnalyseSyntaxique as = new AnalyseSyntaxique();
			
			HashMap<Integer, Lieu> listeLieux = as.analyse(listToken);
			
			Iterator iterator = listeLieux.entrySet().iterator();
			
			while (iterator.hasNext()) {
				Map.Entry mapentry = (Map.Entry) iterator.next();
				System.out.print("Num lieu = " + mapentry.getKey() + " ; ");
				Lieu lieu = (Lieu) mapentry.getValue();
				System.out.println("description = " + lieu.description + " ; nb propo = " + lieu.propositions.size());
			 } 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		

	}

}
