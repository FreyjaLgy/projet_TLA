/*

Projet TLA 2023-24

R√©alis√© par :
- GODIVEAU Andr√©a
- LEGUAY Emerance

*/


package tla;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/*
 * Classe principale.
 *
 * G√®re l'IHM.
 * Affiche les lieux et propositions suivant les d√©cisions du joueur.
 */

public class App implements ActionListener {
	
	//Nombre de points de vie.
    PointsVie pv = new PointsVie();
    
    //Tirage alÈatoire d'un dÈ.
    int valeurDe = 1;

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

    //ATTENTION : PENSER A ADAPTER LE CHEMIN DU FICHIER.
    String cheminFichier = "C:\\Users\\AndrÈa\\Dropbox\\Module_2_Programmation_Web_theorie_informatique\\Theorie_langages_automates\\Projet\\projet_TLA\\src\\tla\\scenario_final.txt";

    public static void main(String[] args) {
        App app = new App();
        SwingUtilities.invokeLater(() -> {
            try {
                app.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void init() throws Exception {
        // Charge le contenu de l'aventure
        lieux = analyseGodiveauLeguay();

        // Pr√©pare l'IHM
        labels = new JLabel[nbLignes];
        btns = new ArrayList<>();

        frame = new JFrame(lieux.get(0).description);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        frame.add(mainPanel);

        for (int i = 0; i < nbLignes; i++) {
        	//Ajout : permet d'afficher le paragraphe ‡ propos du dÈ.
        	String affichageAvantLieu = " ";
        	if (i == (nbLignes - 4)) {
        		affichageAvantLieu = "En parallËle de votre aventure, un dÈ est lancÈ ‡ chaque choix que vous faites.";
        	}
        	if (i == (nbLignes - 3)) {
        		affichageAvantLieu = "En fonction du score qu'il renvoie, certaines propositions peuvent apparaÓtre ou disparaÓtre.";
        	}
        	if (i == (nbLignes - 1)) {
        		affichageAvantLieu = "Valeur du dÈ = " + valeurDe;
        	}
        	//Fin ajout.
        	
            labels[i] = new JLabel(affichageAvantLieu);
            mainPanel.add(labels[i], new GridBagConstraints() {{
                this.gridwidth = GridBagConstraints.REMAINDER;
                this.anchor = GridBagConstraints.WEST;
                this.insets = new Insets(0, 20, 0, 20);
            }});
            labels[i].setMinimumSize(new Dimension(750, 20));
            labels[i].setPreferredSize(new Dimension(750, 20));
        }

        // D√©marre l'aventure au lieu n¬∞ 1
        lieuActuel = obtenirPremierLieu();
        initLieu();

        frame.pack();
        frame.setVisible(true);
    }


    /*
     * Affichage du lieu lieuActuel et cr√©ations des boutons de propositions correspondantes
     * √† ce lieu
     */
    void initLieu() {
        for (JButton btn : btns) {
            mainPanel.remove(btn);
        }
        btns.clear();
        String test = lieuActuel.description;
        affiche(test.split("\\|"));
        frame.pack();
        
        //Modification : permet de gÈrer les conditions.
        for (int i = 0; i < lieuActuel.propositions.size(); i++) {
        	Proposition p = lieuActuel.propositions.get(i);
        	
        	ArrayList<Condition> conditions = p.getConditions();
        	
        	System.out.println(conditions);
        	
        	if (conditions.size() == 0) {
        		creerBouton(i);
        	}
        	else {
        		boolean conditionsValidees = testConditions(conditions);
        		
            	if (conditionsValidees) {
            		creerBouton(i);
            	}        		
        	}
        }
        frame.pack();
        
        //Ajout : permet de lancer le dÈ.
        valeurDe = (int)(1 + (Math.random() * (7 - 1)));
    }
    
    
    public void creerBouton(int i) {
        JButton btn = new JButton("<html><p>" + lieuActuel.propositions.get(i).texte + "</p></html>");
        btn.setActionCommand(String.valueOf(i));
        btn.addActionListener(this);
        mainPanel.add(btn, new GridBagConstraints() {{
        	this.gridwidth = GridBagConstraints.REMAINDER;
        	this.fill = GridBagConstraints.HORIZONTAL;
        	this.insets = new Insets(3, 20, 3, 20);
        }});
        btns.add(btn);
    	
    }
    

    /*
     * G√®re les clics sur les boutons de propostion
     */
    public void actionPerformed(ActionEvent event) {

        // Retrouve l'index de la proposition
        int index = Integer.valueOf(event.getActionCommand());

        // Retrouve la propostion
        Proposition proposition = lieuActuel.propositions.get(index);

        // Recherche le lieu d√©sign√© par la proposition
        Lieu lieu = lieux.get(proposition.numeroLieu);
        if (lieu != null) {

            // Affiche la proposition qui vient d'√™tre choisie par le joueur
            affiche(new String[]{"> " + proposition.texte});
            
            affiche(new String[]{"Valeur du dÈ = " + valeurDe});
            affiche(new String[]{"BonusMalus de la propo = " + String.valueOf(proposition.bonusMalus)});
    		
            int bonusMalus = proposition.bonusMalus;
            pv.setPV(bonusMalus);
            int nbPV = pv.getPV();
            affiche(new String[]{"Points de vie = " + String.valueOf(nbPV)});
            

            // Affichage du nouveau lieu et cr√©ation des boutons des nouvelles propositions
            lieuActuel = lieu;
            initLieu();
        } else {
            // Cas particulier : le lieu est d√©clar√©e dans une proposition mais pas encore d√©crit
            // (lors de l'√©laboration de l'aventure par exemple)
            JOptionPane.showMessageDialog(null, "Lieu n¬∞ " + proposition.numeroLieu + " √† impl√©menter");
        }
    }

    /*
     * G√®re l'affichage dans la zone de texte, avec un effet de d√©filement
     * (comme dans un terminal)
     */
    private void affiche(String[] contenu) {
        int n = contenu.length;
        for (int i = 0; i < nbLignes - (n + 1); i++) {
            labels[i].setText(labels[i + n + 1].getText());
        }
        labels[nbLignes - (n + 1)].setText(" ");
        for (int i = 0; i < n; i++) {
            labels[nbLignes - n + i].setText(contenu[i]);
        }
    }


    /*M√©thode permettant de r√©cup√©rer la cha√Æne de caract√®res correspondant au contenu du fichier.
     */

    private String obtenirChaineCaracteres(String cheminFichier) {
        ConvertionFichierEnString cf = new ConvertionFichierEnString();
        return cf.obtenirLieuxPropositions(cheminFichier);
    }


    /*M√©thode permettant de r√©cup√©rer le premier Lieu de la liste de Lieu afin d'initialiser le jeu.
     */

    private Lieu obtenirPremierLieu() throws Exception {
        Map<Integer, Lieu> listeLieux = analyseGodiveauLeguay();
        Set<Integer> ensembleCles = listeLieux.keySet();
        Integer premiereCle = ensembleCles.iterator().next() + 1; //+1 pour passer le titre
        return listeLieux.get(premiereCle);
    }


    /*
     * M√©thode permettant d'effectuer les analyses lexicales et syntaxiques de la cha√Æne de caract√®res cr√©√©s √† partir du fichier, et d'en extraire
     * la liste des Lieu associ√©s √† leurs Proposition.
     */

    private Map<Integer, Lieu> analyseGodiveauLeguay() throws Exception {
        String lieuxPropo = obtenirChaineCaracteres(cheminFichier);

        AnalyseLexicale al = new AnalyseLexicale();
        List<Token> listToken = al.analyse(lieuxPropo);

        AnalyseSyntaxique as = new AnalyseSyntaxique();
        HashMap<Integer, Lieu> listeLieux = as.analyse(listToken);

        return listeLieux;
    }
    
    
    private boolean testConditions(ArrayList<Condition> conditions) {
    	ArrayList<Boolean> conditionsValidees = new ArrayList<Boolean>();
    	
    	boolean valide = true;
    	
    	for (int i = 0 ; i < conditions.size() ; i++) {
    		Condition c = conditions.get(i);
    		TypeDeToken ident = c.getIdent();
    		TypeDeToken symbole = c.getSymbole();
    		int val = c.getVal();
    		
    		//Si la condition commence par : PV
    		if (ident == TypeDeToken.PV) {
    			boolean testConditionPV = testPV(symbole, val);
    			conditionsValidees.add(testConditionPV);
    		}
    		
    		if (ident == TypeDeToken.Random) {
    			boolean testConditionRandom = testRandom(symbole, val);
    			conditionsValidees.add(testConditionRandom);
    		}
    	}

		boolean conditionGauche = conditionsValidees.get(0);
		
    	for (int i = 0 ; i < conditionsValidees.size() - 1 ; i++) {
    		Condition c = conditions.get(i);
    		TypeDeToken operateur = c.getOperateur();
    		boolean conditionDroite = conditionsValidees.get(i + 1);
    		
    		//Si l'opÈrateur qui sÈpare les deux conditions est un OU, ...
    		if (operateur == TypeDeToken.ouLogique) {
    			if ((conditionGauche == true) || (conditionDroite == true)) {
    				conditionGauche = true;
    			}
    		}

    		
    		//Si l'opÈrateur qui sÈpare les deux conditions est un ET, ...
    		if (operateur == TypeDeToken.etLogique) {
    			if ((conditionGauche == false) || (conditionDroite == false)) {
    				conditionGauche = false;
    			}
    		} 		
    		
    	}
    	
    	valide = conditionGauche;
    	
    	
    	System.out.println("les conditions sont validees : " + valide);
    	
    	
    	return valide;
    }
    
    
    public boolean testPV(TypeDeToken symbole, int val) {
    	//Si la condition commence par : PV <
		if (symbole == TypeDeToken.inferieur) {
			//Si PV > val, autrement dit que la condition PV < n'est pas respectÈe :
			if (pv.getPV() > val) {
				return false;
			}
		}
		
		//Si la condition commence par : PV >
		if (symbole == TypeDeToken.superieur) {
			//Si PV < val, autrement dit que la condition PV > n'est pas respectÈe :
			if (pv.getPV() < val) {
				return false;
			}
		}
		
		return true;
    }
    
    
    public boolean testRandom(TypeDeToken symbole, int val) {
    	//Si la condition commence par : random <
		if (symbole == TypeDeToken.inferieur) {
			//Si random > val, autrement dit que la condition random < n'est pas respectÈe :
			if (valeurDe > val) {
				return false;
			}
		}
		
		//Si la condition commence par : random >
		if (symbole == TypeDeToken.superieur) {
			//Si random < val, autrement dit que la condition random > n'est pas respectÈe :
			if (valeurDe < val) {
				return false;
			}
		}
		
		return true;
    }


}
