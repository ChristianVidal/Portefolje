package dat109_oblig1;

public class Spiller {

	private String name;
	private int tellerSeks;
	private Brikke brikke;
	private boolean tripleSix;
	private boolean harVunnet;

	public Spiller(String name) {
		this.brikke = new Brikke();
		this.name = name;
		this.tripleSix = false;
		this.harVunnet = false;
	}

	public int spillTur(int terning) {
		int sumPos = brikke.getPosisjon() + terning;
		boolean erFanget = erFanget(terning);
		boolean harTreSeksere = harTreSeksere(terning);
		boolean erOverHundre = erOverHundre(sumPos);

		if(erFanget || harTreSeksere || erOverHundre) {
			return getPos();
		}
		
		updatePos(sumPos);
		return sumPos;
	}

	private boolean harTreSeksere(int terning) {
		boolean fattTreSeks = false;
		if (terning != 6) {
			resetTeller();
		} else {
			tellerSeks++;
			if (tellerSeks == 3) {
				brikke.setPosisjon(0);
				tripleSix = true;
				resetTeller();
				fattTreSeks = true;
			}
		}
		return fattTreSeks;
	}
	
	private boolean erFanget(int terning) {
		boolean erFanget = false;
		if (tripleSix == true) {
			if (terning != 6) {
				erFanget = true;;
			} else {
				tripleSix = false;
			}
		}
		return erFanget;
	}

	public boolean erOverHundre(int sumPos) {
		if (brikke.getPosisjon() >= 93) {
			if (sumPos > 99) {
				return true;
			}
		}
		return false;
	}

	private void resetTeller() {
		this.tellerSeks = 0;
	}

	public int getTellerSeks() {
		return tellerSeks;
	}

	public String getName() {
		return this.name;
	}

	public void updatePos(int pos) {
		brikke.setPosisjon(pos);
	}

	public int getPos() {
		return brikke.getPosisjon();
	}

	public boolean getTripleSix() {
		return this.tripleSix;
	}

	public boolean getHarVunnet() {
		return this.harVunnet;
	}

	public void setHarVunnet() {
		this.harVunnet = true;
	}
}
