/*

Projet TLA 2023-24

Réalisé par :
- NOM Prénom 
- NOM Prénom
- NOM Prénom
- NOM Prénom
- NOM Prénom

*/

package tla;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/*
 * Membres du groupe :
 *  GODIVEAU Andréa
 *  LEGUAY Emerance
 *
 * Classe principale.
 * 
 * Gère l'IHM.
 * Affiche les lieux et propositions suivant les décisions du joueur.
 */

public class App implements ActionListener {

    // Nombre de lignes dans la zone de texte
    final int nbLignes = 20;

    Map<Integer, Lieu> lieux;
    Lieu lieuActuel;

    JFrame frame;
    JPanel mainPanel;

    // Labels composant la zone de texte
    JLabel[] labels;

    // Boutons de proposition
    ArrayList<JButton> btns;

    public static void main(String[] args) {
        App app = new App();
        SwingUtilities.invokeLater(() -> {
			try {
				app.init();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    }

    private void init() throws Exception {
        // Charge le contenu de l'aventure
        lieux = travailGodiveauLeguay();

        // Prépare l'IHM
        labels = new JLabel[nbLignes];
        btns = new ArrayList<>();

        frame = new JFrame(ContenuAventure.titre);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        frame.add(mainPanel);

        for(int i=0;i<nbLignes;i++) {
            labels[i] = new JLabel(" ");
            mainPanel.add(labels[i], new GridBagConstraints() {{
                this.gridwidth = GridBagConstraints.REMAINDER;
                this.anchor = GridBagConstraints.WEST;
                this.insets = new Insets(0,20,0,20);
            }});
            labels[i].setMinimumSize(new Dimension(750, 20));
            labels[i].setPreferredSize(new Dimension(750, 20));
        }

        // Démarre l'aventure au lieu n° 1
        lieuActuel = lieux.get(1);
        initLieu();

        frame.pack();
        frame.setVisible(true);
    }

    private Map<Integer, Lieu> travailGodiveauLeguay() throws Exception {
    	/*String cheminFichier = "C:\\Users\\Andr�a\\Dropbox\\"
				+ "Module_2_Programmation_Web_theorie_informatique\\Theorie_langages_automates\\"
				+ "Projet\\projet_TLA\\src\\tla\\scenario_prof.txt";
		
		ConvertionFichierEnString cf = new ConvertionFichierEnString();
		
		String lieuxPropo = cf.obtenirLieuxPropositions(cheminFichier);*/
    	
    	String lieuxPropo = "22-*Chambre*1)*Ranger*[1]*2)*Sortir*[2]*2-*TestLieux*3)*Propo*[5]";
		
    	AnalyseLexicale al = new AnalyseLexicale();
    	List<Token> listToken = al.analyse(lieuxPropo);
    	
    	AnalyseSyntaxique as = new AnalyseSyntaxique();
    	HashMap<Integer, Lieu> listeLieux = as.analyse(listToken);
    	
    	return listeLieux;
	}

	/*
     * Affichage du lieu lieuActuel et créations des boutons de propositions correspondantes
     * à ce lieu
     */
    void initLieu() {
        for(JButton btn: btns) {
            mainPanel.remove(btn);
        }
        btns.clear();
        affiche(lieuActuel.description.split("\n"));
        frame.pack();
        for(int i=0; i<lieuActuel.propositions.size(); i++) {
            JButton btn = new JButton("<html><p>" + lieuActuel.propositions.get(i).texte + "</p></html>");
            btn.setActionCommand(String.valueOf(i));
            btn.addActionListener(this);
            mainPanel.add(btn, new GridBagConstraints() {{
                this.gridwidth = GridBagConstraints.REMAINDER;
                this.fill = GridBagConstraints.HORIZONTAL;
                this.insets = new Insets(3,20,3,20);
            }});
            btns.add(btn);
        }
        frame.pack();
    }

    /*
     * Gère les clics sur les boutons de propostion
     */
    public void actionPerformed(ActionEvent event) {

        // Retrouve l'index de la proposition
        int index = Integer.valueOf(event.getActionCommand());

        // Retrouve la propostion
        Proposition proposition = lieuActuel.propositions.get(index);

        // Recherche le lieu désigné par la proposition
        Lieu lieu = lieux.get(proposition.numeroLieu);
        if (lieu != null) {

            // Affiche la proposition qui vient d'être choisie par le joueur
            affiche(new String[]{"> " + proposition.texte});

            // Affichage du nouveau lieu et création des boutons des nouvelles propositions
            lieuActuel = lieu;
            initLieu();
        } else {
            // Cas particulier : le lieu est déclarée dans une proposition mais pas encore décrit
            // (lors de l'élaboration de l'aventure par exemple)
            JOptionPane.showMessageDialog(null,"Lieu n° " + proposition.numeroLieu + " à implémenter"); 
        }
    }

    /*
     * Gère l'affichage dans la zone de texte, avec un effet de défilement
     * (comme dans un terminal)
     */
    private void affiche(String[] contenu) {
        int n = contenu.length;
        for (int i = 0; i < nbLignes-(n+1); i++) {
            labels[i].setText(labels[i + n + 1].getText());
        }
        labels[nbLignes-(n+1)].setText(" ");
        for(int i = 0; i<n; i++) {
            labels[nbLignes-n+i].setText(contenu[i]);
        }
    }

}
