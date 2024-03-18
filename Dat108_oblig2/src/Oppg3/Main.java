package Oppg3;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		final String[] kokker = {"Anne", "Eirik", "Knut"};
		final String [] servitorer = {"Mia", "Per" };
		final int kapasitet = 4;
		
		skrivUtHeader(kokker, servitorer, kapasitet);
		
		HamburgerBrett brett = new HamburgerBrett(kapasitet);
		
		Hamburger hamburger = new Hamburger();
		
		for (String navn : kokker) {
			Kokk nyKokk = new Kokk(hamburger, brett, navn);
			Thread kokkThread = new Thread(nyKokk);
			kokkThread.start();
		}
		 
		for (String navn : servitorer) {
			Servitor nyServitor = new Servitor(brett, navn);
			Thread servitorThread = new Thread(nyServitor);
			servitorThread.start();
		}
		
	}

	private static void skrivUtHeader(String[] kokker, String[] servitorer, int kapasitet) {
		
		System.out.println("I denne simulering har vi");
		System.out.print("	" + kokker.length + " kokker [");
		for (int i = 0; i < kokker.length; i++) {
			if(i == kokker.length - 1) {
				System.out.println(kokker[i] + "]");
			}
			else 
			{
				System.out.print(kokker[i] + ", ");
			}
		}
		
		System.out.print("	" + servitorer.length + " servitorer [");
		
		for (int i = 0; i < servitorer.length; i++) {
			if(i == servitorer.length - 1) {
				System.out.println(servitorer[i] + "]");
			}
			else 
			{
				System.out.print(servitorer[i] + ", ");
			}
		}
		
		System.out.println("Kapasiteten til brettet er " + kapasitet + " hamburgere.");
		System.out.println("Vi starter... \n");
	}
}