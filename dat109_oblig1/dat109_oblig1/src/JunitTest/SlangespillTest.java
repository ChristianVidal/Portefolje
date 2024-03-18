package JunitTest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import dat109_oblig1.Slangespill;

class SlangespillTest {

	@Test
	void skrivAntallSpillereTest() {
		Slangespill setup = new Slangespill();

		// Skriver ugyldig inn først så en 3
		String input = "streng\n20\n3\n";

		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);

		int resultat = setup.skrivSpillere();

		// dette fungerer uavhengig av hva du skriver i programmet
		
		assertEquals(3, resultat);
	}
}
