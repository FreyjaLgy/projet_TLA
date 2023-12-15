package tla;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvertionFichierEnString {
	
	/**Méthode permettant de convertir un fichier en chaîne de caractères.
	 * 
	 * On crée une chaîne de caractères et tant que l'on n'est pas arrivé au bout du fichier, on ajoute chaque ligne à cette chaîne.
	 */
	
	public String obtenirLieuxPropositions(String cheminFichier) {
		String lieuxPropositions = "";
		try {
			FileReader fileReader = new FileReader(cheminFichier);
			BufferedReader reader = new BufferedReader(fileReader);
			
			//Lecture de la première ligne.
			String line = reader.readLine();
			
			//Tant que l'on n'est pas arrivé au bout du fichier, ...
			while (line != null) {
				lieuxPropositions = lieuxPropositions + line;
				
				//Lecture de la ligne suivante
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lieuxPropositions;
	}
}
