package tla;

import java.util.List;

public class testAnalyseSyntaxique {


	public static void main(String[] args) {
		testAnalyseLexicale("1-*Chambre*1)*Ranger*[19]*2)*Sortir*[23]");
	}

	/*
	effectue l'analyse lexicale de la chaine entree,
	affiche la liste des tokens reconnus
	 */
	private static void testAnalyseLexicale(String entree) {
		System.out.println("test de l'analyse lexicale sur l'entrée " + entree);
		AnalyseLexicale al = new AnalyseLexicale();
		List<Token> listToken = null;
		try {
			listToken = al.analyse(entree);
			
			for (Token t : listToken) {
				System.out.print(t.toString());
			}
			System.out.println();
			
			System.out.println(listToken.size());

			AnalyseSyntaxique as = new AnalyseSyntaxique();
			
			as.analyse(listToken);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		

	}

}
