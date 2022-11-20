package ada.prac01;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;


class StudentsManagerMainTest {

	@Test
	void mainArgsTest() {
		PrintStream originalOut = System.out;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bos));

		String[] args = {"src/test/resources/test.in", "src/test/resources/test.csv"};
		StudentsManager.main(args);
		String salida = bos.toString();
		assertTrue(salida.contains("Leyendo datos de fichero src/test/resources/test.in"));
		assertTrue(salida.contains("Línea tratada correctamente: Student [firstName=Pepa, lastName=Perez, dob=2000-03-02, idDoc=12345678Z]"));
		assertTrue(salida.contains("Corregido el id del estudiante Pepe Perez. Nuevo id: 23456789D"));
		assertTrue(salida.contains("Línea tratada correctamente: Student [firstName=Pepe, lastName=Perez, dob=2000-03-02, idDoc=23456789D]"));
		assertTrue(salida.contains("Corregido el id del estudiante Pepi Perez. Nuevo id: 00012345V"));
		assertTrue(salida.contains("Línea tratada correctamente: Student [firstName=Pepi, lastName=Perez, dob=2000-03-02, idDoc=00012345V]"));
		assertTrue(salida.contains("Linea Pepo	Perez	2000-03-02	123456789B	# id largo descartada. (El ID del estudiante (123456789B) no es correcto (El ID es demasiado largo))"));
		assertTrue(salida.contains("Estudiante descartado: ada.prac01.StudentIdNotFixableException: El ID del estudiante (123456789B) no es correcto (El ID es demasiado largo)"));
		assertTrue(salida.contains("Estudiante descartado: Estudiante demasiado joven (Fecha de nacimiento: 2010-03-02)"));
		assertTrue(salida.contains("Línea tratada correctamente: Student [firstName=María del Mar, lastName=González del Prado, dob=2004-09-30, idDoc=01223344C]"));
		assertTrue(salida.contains("Procedemos al volcado de datos en el fichero src/test/resources/test.csv"));
		assertTrue(salida.contains("Procedimiento terminado"));
		// assertion
		File csv = new File("src/test/resources/test.csv");
		assertTrue(csv.exists());
		assertTrue(csv.isFile());
		try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
			List<String> lines = new ArrayList<>();
			String line;
			while((line = br.readLine()) != null) {
				lines.add(line);
			}
			assertEquals(4, lines.size());
			assertEquals("00012345V,Perez,Pepi", lines.get(0));
			assertEquals("01223344C,González del Prado,María del Mar", lines.get(1));
			assertEquals("12345678Z,Perez,Pepa", lines.get(2));
			assertEquals("23456789D,Perez,Pepe", lines.get(3));
		} catch (IOException e) {
			fail(e);
		}

		csv.delete();
		// undo the binding in System
		System.setOut(originalOut);
	}

	@Test
	void mainNoInFileTest() {
		PrintStream originalErr = System.err;
		ByteArrayOutputStream bosErr = new ByteArrayOutputStream();
		System.setErr(new PrintStream(bosErr));

		PrintStream originalOut = System.out;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bos));

		String[] args = {"test.no"};
		StudentsManager.main(args);
		String salida = bos.toString();
		assertTrue(salida.contains("Leyendo datos de fichero test.no"));
		assertTrue(salida.contains("Procedimiento terminado"));

		String error = bosErr.toString();
		assertTrue(error.contains("No podemos ejecutar el programa porque no se encuentra el fichero de entrada esperado: test.no"));
		
		// undo the binding in System
		System.setOut(originalOut);
		System.setErr(originalErr);
	}
	
	@Test
	void mainNoArgsTest() {
		PrintStream originalOut = System.out;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bos));

		String[] args = {};
		StudentsManager.main(args);
		String salida = bos.toString();
		assertTrue(salida.contains("Leyendo datos de fichero src/main/resources/students.txt"));
		assertTrue(salida.contains("Procedemos al volcado de datos en el fichero src/main/resources/records.csv"));
		assertTrue(salida.contains("Procedimiento terminado"));

		// undo the binding in System
		System.setOut(originalOut);
	}
	
	
	@Test
	void mainNoArgs1Test() {
		PrintStream originalOut = System.out;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(bos));

		String[] args = {"src/test/resources/test.in"};
		StudentsManager.main(args);
		String salida = bos.toString();
		assertTrue(salida.contains("Leyendo datos de fichero src/test/resources/test.in"));
		assertTrue(salida.contains("Procedemos al volcado de datos en el fichero src/main/resources/records.csv"));
		assertTrue(salida.contains("Procedimiento terminado"));

		// undo the binding in System
		System.setOut(originalOut);
	}
}
