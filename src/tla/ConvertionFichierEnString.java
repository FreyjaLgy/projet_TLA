package tla;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvertionFichierEnString {
	
	/**M�thode permettant de convertir un fichier en cha�ne de caract�res.
	 * 
	 * On cr�e une cha�ne de caract�res et tant que l'on n'est pas arriv� au bout du fichier, on ajoute chaque ligne � cette cha�ne.
	 */
	
	public String obtenirLieuxPropositions(String cheminFichier) {
		String lieuxPropositions = "";
		try {
			FileReader fileReader = new FileReader(cheminFichier);
			BufferedReader reader = new BufferedReader(fileReader);
			
			//Lecture de la premi�re ligne.
			String line = reader.readLine();
			
			//Tant que l'on n'est pas arriv� au bout du fichier, ...
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
