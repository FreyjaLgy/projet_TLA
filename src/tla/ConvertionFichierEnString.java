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
			// Cr�ation d'un fileReader pour lire le fichier
			FileReader fileReader = new FileReader(cheminFichier);
			
			// Cr�ation d'un bufferedReader qui utilise le fileReader
			BufferedReader reader = new BufferedReader(fileReader);
			
			// une fonction � essayer pouvant g�n�rer une erreur
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
