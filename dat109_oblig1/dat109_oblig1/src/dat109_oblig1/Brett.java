package dat109_oblig1;

import java.util.ArrayList;
import java.util.List;

public class Brett {
	private List<Ruter> ruter = new ArrayList();

	public Brett() {
		for (int i = 0; i <= 99; i++) {
			ruter.add(new Ruter());
		}
		leggTilSlangeOgStige();
	}

	public int ruteEffekt(int pos) {

		int effekt = ruter.get(pos).getEffekt();
		pos = pos + effekt;
		return pos;
	}
	
	public int getEffekt(int pos) {
		return ruter.get(pos).getEffekt();
	}
	
	private void leggTilSlangeOgStige() {
		
		//stiger
		ruter.get(1).setEffekt(36);
		ruter.get(3).setEffekt(10);
		ruter.get(7).setEffekt(23);
		ruter.get(21).setEffekt(21);
		ruter.get(27).setEffekt(56);
		ruter.get(35).setEffekt(8);
		ruter.get(50).setEffekt(16);
		ruter.get(70).setEffekt(20);
		ruter.get(79).setEffekt(20);
		
		//slanger
		ruter.get(15).setEffekt(-10);
		ruter.get(46).setEffekt(-20);
		ruter.get(48).setEffekt(-38);
		ruter.get(56).setEffekt(-3);
		ruter.get(61).setEffekt(-44);
		ruter.get(63).setEffekt(-4);
		ruter.get(86).setEffekt(-63);
		ruter.get(92).setEffekt(-20);
		ruter.get(94).setEffekt(-19);
		ruter.get(97).setEffekt(-20);
		
	}

}
