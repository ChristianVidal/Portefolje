package Oppg3;

import java.util.concurrent.*;

public class HamburgerBrett {
	
	private int kapasitet;
	BlockingQueue<String> brettKo;
	
	public HamburgerBrett(int kapasitet) {
		this.kapasitet = kapasitet;
		this.brettKo = new ArrayBlockingQueue<>(kapasitet);
	}
	
	public void addBrett(String hamburger) {
		synchronized (this) {
			try {
				brettKo.put(hamburger);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String removeBrett() {
		synchronized (this) {
			String fjern = "";
			try {
				fjern = brettKo.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return fjern;
		}
	}
	
	public Boolean isFull() {
		if(brettKo.size() == kapasitet) {
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Boolean isEmpty() {
		if(brettKo.size() == 0) {
			return true;
		}
		else 
		{
			return false;
		}
	}
	
}
