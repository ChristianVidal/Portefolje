package Oppg3;

import java.util.concurrent.*;

public class Servitor implements Runnable{
	
	private Oppg3.HamburgerBrett brett;
	private String navn;
	
	public Servitor(Oppg3.HamburgerBrett brett, String navn) {
		this.brett = brett;
		this.navn = navn;
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
			
			if(!brett.isEmpty()) {
				String fjernetHamburger = brett.removeBrett();
				System.out.println(navn + " (servitor) tar av hamburger " + fjernetHamburger + ". Brett: " + brett.brettKo);
			}
		}
	}
}
