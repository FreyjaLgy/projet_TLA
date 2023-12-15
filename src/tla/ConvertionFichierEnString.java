package tla;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvertionFichierEnString {
	
	public String obtenirLieuxPropositions(String cheminFichier) {
		String lieuxPropositions = "";
		try {
			// Création d'un fileReader pour lire le fichier
			FileReader fileReader = new FileReader(cheminFichier);
			
			// Création d'un bufferedReader qui utilise le fileReader
			BufferedReader reader = new BufferedReader(fileReader);
			
			// une fonction à essayer pouvant générer une erreur
			String line = reader.readLine();
			
			while (line != null) {
				lieuxPropositions = lieuxPropositions + line;
				//System.out.println(lieuxPropositions);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lieuxPropositions;
	}
}
