package tla;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class testComplet {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String cheminFichier = "C:\\Users\\Andréa\\Dropbox\\"
				+ "Module_2_Programmation_Web_theorie_informatique\\Theorie_langages_automates\\"
				+ "Projet\\projet_TLA\\src\\tla\\scenario_prof.txt";
		
		ConvertionFichierEnString cf = new ConvertionFichierEnString();
		
		String lieuxPropo = cf.obtenirLieuxPropositions(cheminFichier);
		
		testAnalyses(lieuxPropo);
		
		

	}
	
	/*Effectue les analyses lexicales et syntaxiques sur la chaîne entree.
	Affiche les tokens.
	Affiche la liste de Lieu.	
	*/
	
	private static void testAnalyses(String entree) throws Exception {
		System.out.println("test de l'analyse lexicale sur l'entrÃ©e " + entree);
		System.out.println();
		
		AnalyseLexicale al = new AnalyseLexicale();
		List<Token> listToken = null;
		try {
			listToken = al.analyse(entree);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (Token t : listToken) {
			System.out.print(t.toString());
		}
		System.out.println();
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
	}


}
