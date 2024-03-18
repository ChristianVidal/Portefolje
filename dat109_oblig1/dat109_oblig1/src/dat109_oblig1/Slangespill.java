package dat109_oblig1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Slangespill {
	private Brett brett;
	private List<Spiller> spillere = new ArrayList<>();
	private Terning terning;
	private Controller controller;
	
	public Slangespill() {
		controller = new Controller();
		this.brett = new Brett();
		this.terning = new Terning();
	}

	public int skrivSpillere() {
		String brukerInput = "";
		String tallRegex = "^[2-4]$";
		Scanner scanner = new Scanner(System.in);
		while (!brukerInput.matches(tallRegex)) {

			System.out.print("Skriv inn hvor mange spillere (mellom 2 og 4): ");
			brukerInput = scanner.nextLine();
		}
		int brukerTall = Integer.parseInt(brukerInput);
		return brukerTall;
	}

	
	private String[] getSpillerNames(int antallSpillere) {
		String brukerInput = "No name";
		String[] alleSpillerNavn = new String[antallSpillere];
		int teller = 0;
		Scanner scanner = new Scanner(System.in);
		while (antallSpillere > teller) {
			System.out.print("Skriv inn spillernavn: ");
			brukerInput = scanner.nextLine();
			alleSpillerNavn[teller] = brukerInput;
			teller++;
		}
		scanner.close();
		return alleSpillerNavn;
	}
	
	public void initialiser() {
		int antallSpillere = skrivSpillere();
		String[] spillernavn = getSpillerNames(antallSpillere);
		for(int i = 0; i < antallSpillere; i++) {
			spillere.add(new Spiller(spillernavn[i]));
		}
		controller.start(brett,terning,spillere);
	}
}
