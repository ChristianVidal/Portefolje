package dat109_oblig1;

public class ControllerLogikk {
	
	public boolean harFattSeks(Spiller spiller) {
		if (spiller.getTellerSeks() > 0) {
			return true;
		}
		return false;
	}
	
	public int erFanget(Spiller spiller, int terningVerdi) {
		if (spiller.getTripleSix()) {
			if (terningVerdi != 6) {
				return 1;
			}
			return 0;
		}
		return -1;
	}
	
	public boolean erOverHundre(Spiller spiller, int terningVerdi) {
		int spillerpos = spiller.getPos();
		int total = spillerpos + terningVerdi;
		boolean erOverHundre = spiller.erOverHundre(total);
		
		if(erOverHundre) {
			return erOverHundre;
		}
		return erOverHundre;
	}
	
	public boolean sjekkOgSettVunnet(Spiller spiller) {
		if(spiller.getPos() == 99) {
			spiller.setHarVunnet();
		}
		return spiller.getHarVunnet();
	}
}
