package tla;

import java.util.List;

public class testComplet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String cheminFichier = "C:\\Users\\Andréa\\Dropbox\\"
				+ "Module_2_Programmation_Web_theorie_informatique\\Theorie_langages_automates\\"
				+ "Projet\\projet_TLA\\src\\tla\\scenario_prof.txt";
		
		ConvertionFichierEnString cf = new ConvertionFichierEnString();
		
		String lieuxPropo = cf.obtenirLieuxPropositions(cheminFichier);
		
		System.out.println(lieuxPropo);
		
		testAnalyses(lieuxPropo);

	}
	
	private static void testAnalyses(String entree) {
		System.out.println("test de l'analyse lexicale sur l'entrÃ©e " + entree);
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
