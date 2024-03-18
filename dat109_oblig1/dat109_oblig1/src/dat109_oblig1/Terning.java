package dat109_oblig1;



public class Terning {
	
	private int verdi;
	
	int trill() {
		this.verdi = (int)(Math.random() *6) + 1;
		return this.verdi;
	}
}
