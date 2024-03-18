package Oppg3;

public class Hamburger {
	private String leftBun = "(";
	private String rightBun = ")";
	
	public String lagHamburger(int hamburgerNummer) {
		String klar = leftBun + hamburgerNummer + rightBun;
		return klar;
	}
}
