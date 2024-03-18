package Oppg3;

import java.util.concurrent.*;	

public class Kokk implements Runnable{
		
	private String navn;
	private Oppg3.HamburgerBrett brett;
	private Oppg3.Hamburger hamburger;
	private static int hamburgerNummer;
	
	public Kokk (Oppg3.Hamburger hamburger,Oppg3.HamburgerBrett brett, String navn) {
		this.brett = brett;
		this.navn = navn;
		this.hamburger = hamburger;
	}
	
	@Override
	public void run() {
		while(true) {
			double randomNum = ThreadLocalRandom.current().nextDouble(2.0, 6.0);
			int randomNumInt = (int) (randomNum * 1000);
			try {
				Thread.sleep(randomNumInt);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			synchronized (this) {
				hamburgerNummer++;
			}
			
			if(!brett.isFull()) {
				String klarHamburger = hamburger.lagHamburger(hamburgerNummer);
				brett.addBrett(klarHamburger);
				System.out.println(navn + " (kokk) legger paa hamburger " + klarHamburger + ". Brett : " + brett.brettKo);
			}
		}
	}
}
