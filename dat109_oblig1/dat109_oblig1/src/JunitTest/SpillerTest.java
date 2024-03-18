package JunitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import dat109_oblig1.Spiller;

class SpillerTest {
	
	List<Spiller> spillere = new ArrayList();

	@Test
	void testSpillTur() {
		spillere.add(new Spiller("Per"));
		spillere.add(new Spiller("Pål"));
		spillere.add(new Spiller("Askeladden"));
		
		Spiller per = spillere.get(0);
		Spiller paal = spillere.get(1);
		Spiller aske = spillere.get(2);
		
		assertTrue(per.getName() == "Per");
		assertTrue(paal.getName() == "Pål");
		assertFalse(aske.getName() == "Aske");
		assertTrue(aske.getName() == "Askeladden");
		
		per.updatePos(7);
		paal.updatePos(5);
		aske.updatePos(10);
		
		assertTrue(per.getPos() == 7);
		assertTrue(paal.getPos() == 5);
		assertFalse(aske.getPos() == 8);
		assertTrue(aske.getPos() == 10);
		
		assertFalse(per.getHarVunnet());
		assertFalse(paal.getHarVunnet());
		assertFalse(aske.getHarVunnet());
		
		aske.setHarVunnet();
		
		assertTrue(aske.getHarVunnet());
	}

}
