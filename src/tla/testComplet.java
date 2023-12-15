package tla;

import java.util.List;

public class testComplet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String cheminFichier = "C:\\Users\\Andr�a\\Dropbox\\"
				+ "Module_2_Programmation_Web_theorie_informatique\\Theorie_langages_automates\\"
				+ "Projet\\projet_TLA\\src\\tla\\scenario_prof.txt";
		
		ConvertionFichierEnString cf = new ConvertionFichierEnString();
		
		String lieuxPropo = cf.obtenirLieuxPropositions(cheminFichier);
		
		//System.out.println(lieuxPropo);
		
		testAnalyseLexicale(lieuxPropo);

	}
	
	private static void testAnalyseLexicale(String entree) {
		System.out.println("test de l'analyse lexicale sur l'entrée " + entree);
		AnalyseLexicale al = new AnalyseLexicale();
		List<Token> listToken = null;
		try {
			listToken = al.analyse(entree);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (Token t : listToken) {
			System.out.println(t.toString());
		}
		System.out.println();
	}


}
