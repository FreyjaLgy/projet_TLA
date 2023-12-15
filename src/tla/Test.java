package tla;

import java.util.ArrayList;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		testAnalyseLexicale("1");
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
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		for (Token t : listToken) {
			System.out.print(t.toString());
		}
		System.out.println();
	}

}
