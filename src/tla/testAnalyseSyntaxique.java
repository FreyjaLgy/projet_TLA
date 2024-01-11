package tla;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class testAnalyseSyntaxique {


	public static void main(String[] args) {
		testAnalyseSyntaxique("1-*Jardin*"
				+ "1)*Prendre la fleur par terre.*Fleur*+1pv*(pv>5)||(random>5)*[1]*"
				+ "2)*Le lutin maléfique apparaît.**-2pv*pv>5*[1]*");
	}

	/*
	Effectue les analyses lexicales et syntaxiques de la chaîne entree.
	Affiche la liste de Lieu.
	 */
	private static void testAnalyseSyntaxique(String entree) {
		System.out.println("test de l'analyse syntaxique sur l'entrée ");
		System.out.println(entree);
		System.out.println();
		AnalyseLexicale al = new AnalyseLexicale();
		List<Token> listToken = null;
		try {
			listToken = al.analyse(entree);
			
			for (Token t : listToken) {
				//System.out.println(t.toString());
			}
			//System.out.println();

			AnalyseSyntaxique as = new AnalyseSyntaxique();
			
			//System.out.println("Nombre de pv = " + as.getPV());
			
			HashMap<Integer, Lieu> listeLieux = as.analyse(listToken);
			
			Iterator iterator = listeLieux.entrySet().iterator();
			
			while (iterator.hasNext()) {
				Map.Entry mapentry = (Map.Entry) iterator.next();
				System.out.print("Num lieu = " + mapentry.getKey() + " ; ");
				Lieu lieu = (Lieu) mapentry.getValue();
				System.out.println("description = " + lieu.description + " ; nb propo = " + lieu.propositions.size());
				
				if (lieu.propositions.size() != 0) {
					for (int i = 0 ; i < lieu.propositions.size() ; i++) {
						Proposition p = lieu.propositions.get(i);
						System.out.println("Num = " + p.numeroLieu + "Text = " + p.texte);
					}
				}
				
			} 
					
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		

	}

}
