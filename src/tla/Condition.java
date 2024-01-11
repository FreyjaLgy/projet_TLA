package tla;

public class Condition {
	
	private TypeDeToken ident;
	private TypeDeToken symbole;
	private int val;
	private TypeDeToken operateur = null;
	
	
	public Condition(TypeDeToken ident, TypeDeToken symbole, int val, TypeDeToken operateur) {
		this.ident = ident;
		this.symbole = symbole;
		this.val = val;
		this.operateur = operateur;
	}


	public TypeDeToken getIdent() {
		return ident;
	}


	public TypeDeToken getSymbole() {
		return symbole;
	}


	public int getVal() {
		return val;
	}


	public TypeDeToken getOperateur() {
		return operateur;
	}




}
