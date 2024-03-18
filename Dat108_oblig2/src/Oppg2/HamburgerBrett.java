package Oppg2;

import java.util.ArrayList;

public class HamburgerBrett {
	
	private int kapasitet;
	private ArrayList<String> brett;
	
	public HamburgerBrett(int kapasitet) {
		this.kapasitet = kapasitet;
		this.brett = new ArrayList<>();
	}
	
	public void addBrett(String hamburger) {
		brett.add(hamburger);
	}
	
	public void removeBrett() {
		brett.remove(0);
	}
	
	public Boolean isFull() {
		if(brett.size() == kapasitet) {
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Boolean isEmpty() {
		if(brett.size() == 0) {
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	public ArrayList<String> getBrett() {
		return brett;
	}
}
