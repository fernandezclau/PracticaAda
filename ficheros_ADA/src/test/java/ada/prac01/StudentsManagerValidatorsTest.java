package ada.prac01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class StudentsManagerValidatorsTest {
	
	@Test
	void validateDocOkTest() {
		try {
			StudentsManager.validateDoc("12345678Z");
			assertTrue(true);
		} catch (StudentIdNotValidException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	void validateDocBadLetterTest() {
		try {
			StudentsManager.validateDoc("12345678A");
			fail();
		} catch (StudentIdNotValidException e) {
			assertEquals("El ID del estudiante (12345678A) no es correcto (La letra del ID no es correcta)", e.getMessage());
			switch(e.getCode()) {
			case BAD_LETTER:
				try {
					String nuevoDoc = StudentsManager.fixId(e.getCode(), "12345678A");
					assertEquals(nuevoDoc, "12345678Z");
					break;
				} catch (StudentIdNotFixableException e1) {
					fail(e1.getMessage());
				}
			default:
				fail(e.getMessage());
			}
		}
	}

	@Test
	void validateDocTooShortTest() {
		try {
			StudentsManager.validateDoc("12345V");
			fail();
		} catch (StudentIdNotValidException e) {
			assertEquals("El ID del estudiante (12345V) no es correcto (El ID es demasiado corto)", e.getMessage());
			switch(e.getCode()) {
			case TOO_SHORT:
				try {
					String nuevoDoc = StudentsManager.fixId(e.getCode(), "12345V");
					assertEquals(nuevoDoc, "00012345V");
					break;
				} catch (StudentIdNotFixableException e1) {
					fail(e1.getMessage());
				}
			default:
				fail(e.getMessage());
			}
		}
	}

	@Test
	void validateDocTooLongTest() {
		try {
			StudentsManager.validateDoc("123456789B");
			fail();
		} catch (StudentIdNotValidException e) {
			assertEquals("El ID del estudiante (123456789B) no es correcto (El ID es demasiado largo)", e.getMessage());
			switch(e.getCode()) {
			case TOO_LONG:
				try {
					StudentsManager.fixId(e.getCode(), "123456789B");
					fail();
				} catch (StudentIdNotFixableException e1) {
					assertEquals(IdError.TOO_LONG, e1.getCode());
					assertEquals("El ID del estudiante (123456789B) no es correcto (El ID es demasiado largo)", e1.getMessage());
				}
				break;
			default:
				fail(e.getMessage());
			}
		}
	}

	@Test
	void validateDocBadFormatTest() {
		try {
			StudentsManager.validateDoc("abecedari");
			fail();
		} catch (StudentIdNotValidException e) {
			assertEquals("El ID del estudiante (abecedari) no es correcto (El formato del ID no es correcto)", e.getMessage());
			switch(e.getCode()) {
			case BAD_FORMAT:
				try {
					StudentsManager.fixId(e.getCode(), "abecedari");
					fail();
				} catch (StudentIdNotFixableException e1) {
					assertEquals(IdError.BAD_FORMAT, e1.getCode());
					assertEquals("El ID del estudiante (abecedari) no es correcto (El formato del ID no es correcto)", e1.getMessage());
				}
				break;
			default:
				fail(e.getMessage());
			}
		}
	}

	@Test
	void validateDocBadFormat2Test() {
		try {
			StudentsManager.validateDoc("12345-V");
			fail();
		} catch (StudentIdNotValidException e) {
			assertEquals("El ID del estudiante (12345-V) no es correcto (El formato del ID no es correcto)", e.getMessage());
			switch(e.getCode()) {
			case BAD_FORMAT:
				try {
					StudentsManager.fixId(e.getCode(), "12345-V");
					fail();
				} catch (StudentIdNotFixableException e1) {
					assertEquals(IdError.BAD_FORMAT, e1.getCode());
					assertEquals("El ID del estudiante (12345-V) no es correcto (El formato del ID no es correcto)", e1.getMessage());
				}
				break;
			default:
				fail(e.getMessage());
			}
		}
	}
	
	@Test
	void validateAgeAdult19Test() {
		LocalDate dob = LocalDate.now().minusYears(19);
		boolean isAdult = StudentsManager.isAdult(dob);
		assertTrue(isAdult);
		try {
			StudentsManager.validateAge(dob);
			assertTrue(true);
		} catch (StudentTooYoungException e) {
			fail(e);
		}
	}
	
	@Test
	void validateAgeAdult18Test() {
		LocalDate dob = LocalDate.now().minusYears(18);
		boolean isAdult = StudentsManager.isAdult(dob);
		assertTrue(isAdult);
		try {
			StudentsManager.validateAge(dob);
			assertTrue(true);
		} catch (StudentTooYoungException e) {
			fail(e);
		}
	}
	
	@Test
	void validateAgeChildNear18Test() {
		LocalDate dob = LocalDate.now().minusYears(18).plusDays(1);
		boolean isAdult = StudentsManager.isAdult(dob);
		assertFalse(isAdult);
		try {
			StudentsManager.validateAge(dob);
			fail();
		} catch (StudentTooYoungException e) {
			assertEquals("Estudiante demasiado joven (Fecha de nacimiento: " + dob + ")", e.getMessage());
		}
	}
	
	@Test
	void validateAgeChild5Test() {
		LocalDate dob = LocalDate.now().minusYears(5);
		boolean isAdult = StudentsManager.isAdult(dob);
		assertFalse(isAdult);
		try {
			StudentsManager.validateAge(dob);
			fail();
		} catch (StudentTooYoungException e) {
			assertEquals("Estudiante demasiado joven (Fecha de nacimiento: " + dob + ")", e.getMessage());
		}
	}
}
