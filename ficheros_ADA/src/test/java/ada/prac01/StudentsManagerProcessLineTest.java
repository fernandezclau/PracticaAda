package ada.prac01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class StudentsManagerProcessLineTest {

	@Test
	void errorEmptyLineTest() {
		try {
			StudentsManager.processLine("");
			fail();
		} catch (StudentTooYoungException e) {
			fail(e);
		} catch (LineNotValidException e) {
			assertEquals("class java.lang.ArrayIndexOutOfBoundsException", e.getCause().getClass().toString());
			assertEquals("java.lang.ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1", e.getMessage());
		}
	}

	@Test
	void errorOneElementLineTest() {
		try {
			StudentsManager.processLine("a");
			fail();
		} catch (StudentTooYoungException e) {
			fail(e);
		} catch (LineNotValidException e) {
			assertEquals("class java.lang.ArrayIndexOutOfBoundsException", e.getCause().getClass().toString());
			assertEquals("java.lang.ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1", e.getMessage());
		}
	}

	@Test
	void errorTwoElementsLineTest() {
		try {
			StudentsManager.processLine("a\tb");
			fail();
		} catch (StudentTooYoungException e) {
			fail(e);
		} catch (LineNotValidException e) {
			assertEquals("class java.lang.ArrayIndexOutOfBoundsException", e.getCause().getClass().toString());
			assertEquals("java.lang.ArrayIndexOutOfBoundsException: Index 2 out of bounds for length 2", e.getMessage());
		}
	}

	@Test
	void errorThreeElementsLineTest() {
		try {
			StudentsManager.processLine("a\tb\tc");
			fail();
		} catch (StudentTooYoungException e) {
			fail(e);
		} catch (LineNotValidException e) {
			assertEquals("class java.time.format.DateTimeParseException", e.getCause().getClass().toString());
			assertEquals("java.time.format.DateTimeParseException: Text 'c' could not be parsed at index 0", e.getMessage());
		}
	}
	
	@Test
	void errorThreeElementsLineBadDateTest() {
		try {
			StudentsManager.processLine("a\tb\t2021/04/05");
			fail();
		} catch (StudentTooYoungException e) {
			fail(e);
		} catch (LineNotValidException e) {
			assertEquals("class java.time.format.DateTimeParseException", e.getCause().getClass().toString());
			assertEquals("java.time.format.DateTimeParseException: Text '2021/04/05' could not be parsed at index 4", e.getMessage());
		}
	}

	@Test
	void errorThreeElementsLineDateTest() {
		try {
			StudentsManager.processLine("a\tb\t2021-04-05");
			fail();
		} catch (StudentTooYoungException e) {
			fail(e);
		} catch (LineNotValidException e) {
			assertEquals("class java.lang.ArrayIndexOutOfBoundsException", e.getCause().getClass().toString());
			assertEquals("java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 3", e.getMessage());
		}
	}
	
	@Test
	void errorFourElementsLineYoungTest() {
		try {
			StudentsManager.processLine("a\tb\t2021-04-05\td");
			fail();
		} catch (StudentTooYoungException e) {
			assertEquals("Estudiante demasiado joven (Fecha de nacimiento: 2021-04-05)", e.getMessage());
		} catch (LineNotValidException e) {
			fail(e);
		}
	}
	
	@Test
	void errorFourElementsLineAdultTest() {
		try {
			StudentsManager.processLine("a\tb\t2001-04-05\td");
			fail();
		} catch (StudentTooYoungException e) {
			fail(e);
		} catch (LineNotValidException e) {
			assertEquals("class ada.prac01.StudentIdNotFixableException", e.getCause().getClass().toString());
			assertEquals("ada.prac01.StudentIdNotFixableException: El ID del estudiante (d) no es correcto (El formato del ID no es correcto)", e.getMessage());
		}
	}
	
	@Test
	void linePepaTest() {
		try {
			Student s = StudentsManager.processLine("Pepa\tPerez\t2000-03-02\t12345678Z");
			assertEquals("Pepa", s.getFirstName());
			assertEquals("Perez", s.getLastName());
			assertEquals(LocalDate.of(2000, 03, 02), s.getDob());
			assertEquals("12345678Z", s.getIdDoc());
		} catch (StudentTooYoungException | LineNotValidException e) {
			fail(e);
		}
	}

	@Test
	void linePepeTest() {
		try {
			Student s = StudentsManager.processLine("Pepe\tPerez\t2000-03-02\t23456789A");
			assertEquals("Pepe", s.getFirstName());
			assertEquals("Perez", s.getLastName());
			assertEquals(LocalDate.of(2000, 03, 02), s.getDob());
			assertEquals("23456789D", s.getIdDoc());
		} catch (StudentTooYoungException | LineNotValidException e) {
			fail(e);
		}
	}

	@Test
	void linePepiTest() {
		try {
			Student s = StudentsManager.processLine("Pepi\tPerez\t2000-03-02\t12345V");
			assertEquals("Pepi", s.getFirstName());
			assertEquals("Perez", s.getLastName());
			assertEquals(LocalDate.of(2000, 03, 02), s.getDob());
			assertEquals("00012345V", s.getIdDoc());
		} catch (StudentTooYoungException | LineNotValidException e) {
			fail(e);
		}
	}

	@Test
	void linePepoTest() {
		try {
			StudentsManager.processLine("Pepo\tPerez\t2000-03-02\t123456789B");
			fail();
		} catch (StudentTooYoungException e) {
			fail(e);
		} catch (LineNotValidException e) {
			assertEquals("class ada.prac01.StudentIdNotFixableException", e.getCause().getClass().toString());
			assertEquals("ada.prac01.StudentIdNotFixableException: El ID del estudiante (123456789B) no es correcto (El ID es demasiado largo)", e.getMessage());
		}
	}

	@Test
	void linePepuTest() {
		try {
			StudentsManager.processLine("Pepu\tPerez\t2010-03-02\t11223344B");
			fail();
		} catch (StudentTooYoungException e) {
			assertEquals("Estudiante demasiado joven (Fecha de nacimiento: 2010-03-02)", e.getMessage());
		} catch (LineNotValidException e) {
			fail(e);
		}
	}

	@Test
	void lineEspaciosTest() {
		try {
			Student s = StudentsManager.processLine("María del Mar\tGonzález del Prado\t2004-09-30\t01223344C");
			assertEquals("María del Mar", s.getFirstName());
			assertEquals("González del Prado", s.getLastName());
			assertEquals(LocalDate.of(2004, 9, 30), s.getDob());
			assertEquals("01223344C", s.getIdDoc());
		} catch (StudentTooYoungException | LineNotValidException e) {
			fail(e);
		}
	}
}
