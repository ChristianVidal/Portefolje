package Oppg2;

import java.util.concurrent.*;

public class Servitor implements Runnable{
	
	private HamburgerBrett brett;
	private String navn;
	
	public Servitor(HamburgerBrett brett, String navn) {
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
			
			synchronized (brett) {
				if(brett.isEmpty()) {
					System.out.println(navn + " (servitor) onsker aa ta av hamburger, men brettet er tomt. Venter!");
					try {
						brett.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					String fjernetHamburger = brett.getBrett().get(0);
					brett.removeBrett();
					System.out.println(navn + " (servitor) tar av hamburger " + fjernetHamburger + ". Brett: " + brett.getBrett());
					brett.notify();
				}
			}
		}
	}

}
