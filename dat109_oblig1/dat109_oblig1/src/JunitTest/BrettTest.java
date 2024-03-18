package JunitTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dat109_oblig1.Brett;

class BrettTest {
	
	Brett brett = new Brett();

	@Test
	void testOmRuteBlirOppdatert() {
		//sjekker om effekten på 80 sender deg til 100
		assertTrue(brett.ruteEffekt(79) == 99);
		assertFalse(brett.ruteEffekt(79) == 79);
		
		//sjekker om slange fungerer
		assertTrue(brett.ruteEffekt(94) == 75);
		assertFalse(brett.ruteEffekt(94) == 94);
	}
	
	@Test
	void testGetEffekt() {
		//sjekker at stigen på plass 79 gir +20 som svar
		assertTrue(brett.getEffekt(79) == 20);
		assertFalse(brett.getEffekt(79) == 79);
		
		//sjekker om slangeeffekt på plass 94 gir -19 som svar
		assertTrue(brett.getEffekt(94) == -19);
		assertFalse(brett.getEffekt(94) == -94);
	}

}
