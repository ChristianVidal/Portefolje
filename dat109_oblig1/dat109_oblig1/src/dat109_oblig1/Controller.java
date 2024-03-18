package dat109_oblig1;

import java.util.List;
import java.util.Scanner;

public class Controller {

	private SpillUI spillUI;
	private ControllerLogikk logikk;

	public Controller() {
		this.spillUI = new SpillUI();
		this.logikk = new ControllerLogikk();
	}

	public void start(Brett brett, Terning terning ,List<Spiller> spillere) {
		int spillerTeller = 0;
		Spiller spiller = spillere.get(spillerTeller);

		while (true) {
			spiller = spillere.get(spillerTeller);
			printAtNySpiller(spiller);
			do {
				harFattSeks(spiller);

				int terningVerdi = terning.trill();
				spillUI.terningVerdi(terningVerdi);
				

				if (erFanget(spiller, terningVerdi) || erOverHundre(spiller, terningVerdi)) {
					break;
				}

				int pos = spiller.spillTur(terningVerdi);
				
				printHvisTrippelSeks(spiller);

				printBrettFraUI(spiller);

				oppdaterPosOgPrintHvisEffekt(spiller, brett, pos);
				
				if(logikk.sjekkOgSettVunnet(spiller)) {
					break;
				}

			} while (spiller.getTellerSeks() > 0 && spiller.getTellerSeks() < 3);

			if (spiller.getHarVunnet()) {
				spillUI.vunnet(spiller.getName());
				break;
			}

			spillerTeller++;
			if (spillerTeller >= spillere.size()) {
				spillerTeller = 0;
			}
		}
	}


	private void printAtNySpiller(Spiller spiller) {
		spillUI.nySpillerTur(spiller.getName());
		printBrettFraUI(spiller);
	}

	private void printBrettFraUI(Spiller spiller) {
		spillUI.printBrett(spiller.getPos() + 1, spiller.getName());

	}


	private void harFattSeks(Spiller spiller) {
		if (logikk.harFattSeks(spiller)) {
			spillUI.skrivFikkSeks();
		}
	}
	
	
	private boolean erFanget(Spiller spiller, int terningVerdi) {
		int fangetTall = logikk.erFanget(spiller, terningVerdi);
		if(fangetTall == 0) {
			spillUI.ikkeFanget();
		}else if(fangetTall > 1) {
			spillUI.fangetMelding();
			return true;
		}
		return false;
	}

	private void oppdaterPosOgPrintHvisEffekt(Spiller spiller, Brett brett, int pos) {
		spiller.updatePos(brett.ruteEffekt(pos));
		spillUI.effekt(brett.getEffekt(pos), spiller.getPos() + 1, spiller.getName());
	}

	
	private void printHvisTrippelSeks(Spiller spiller) {
		if (spiller.getTripleSix()) {
			spillUI.trippelSeks();
			spillUI.fangetMelding();
		}
	}
	
	private boolean erOverHundre(Spiller spiller, int terningVerdi) {
		boolean erOverHundre = logikk.erOverHundre(spiller, terningVerdi);
		int spillerpos = spiller.getPos();
		
		if(erOverHundre) {
			spillUI.overHundre(spillerpos + 1);
			return erOverHundre;
		}
		return erOverHundre;
	}

}
