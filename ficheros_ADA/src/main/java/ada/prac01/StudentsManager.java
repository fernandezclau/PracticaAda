package ada.prac01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.*;

public class StudentsManager {

    private static final String INPUT_FILE = "src/main/resources/students.txt";
    private static final String OUTPUT_FILE = "src/main/resources/records.csv";
	private static final int ADULT_AGE = 18;
	private static final int ID_SIZE = 9;


	public static void main(String[] args) {
		ArrayList<Student> students = new ArrayList<Student>();
		String inputFile;
		String outputFile = OUTPUT_FILE;
		switch (args.length){
			case 2:
				outputFile = args[1];
			case 1:
				inputFile = args[0];
				break;
			default:
				inputFile = INPUT_FILE;
		}
		System.out.println("Leyendo datos de fichero " + inputFile);
		try(Scanner s = new Scanner(new File(inputFile))){
			Student st;
			while(s.hasNextLine()){
				String line = s.nextLine();
				try {
					st =processLine(line);
					students.add(st);
					System.out.println("Línea tratada correctamente: " + st.toString());
				} catch (StudentTooYoungException e) {
					System.out.println("Estudiante descartado: " +e.getMessage());
				} catch (LineNotValidException e) {
					System.out.println("Linea " + line + " descartada. ("  +e.getCause().getMessage() +")");
					System.out.println("Estudiante descartado: " + e.getMessage());
				}
			}

			//ordena la lista de objetos por dni
			Collections.sort(students, new Comparator<Student>() {
				@Override
				public int compare(Student o1, Student o2) {
					return o1.getIdDoc().compareTo(o2.getIdDoc());
				}
			});

			System.out.println("Procedemos al volcado de datos en el fichero " + outputFile);
			try(FileWriter fw = new FileWriter(outputFile)){

				for(Student auxStudent: students) {
					fw.write(auxStudent.csvFormat());
				}
			}catch(IOException e){
				System.out.println("No hemos podido ejecutar el programa ya que no hemos encontrado"
						+ "el fichero de entrada esperado:" + outputFile);
			}

		}catch(FileNotFoundException e){
			System.err.println("No podemos ejecutar el programa porque no se encuentra el fichero de entrada esperado: " + inputFile);
		}
		System.out.println("Procedimiento terminado");
    }

	protected static Student processLine(String line) throws StudentTooYoungException, LineNotValidException {
		String [] data;
		data = line.split("\t");
		try {
			String name = data[0];
			String surname = data[1];
			String dob1 = data[2];
			LocalDate dob;
			try{
				dob = LocalDate.parse(dob1);
			}catch (DateTimeParseException e){
				e.getMessage();
				throw new LineNotValidException(e);
			}
			String idDoc = data[3];
			validateAge(dob);
			try{
				validateDoc(idDoc);
			} catch (StudentIdNotValidException e) {
				try {
					idDoc = fixId(e.getCode(), idDoc);
					System.out.println("Corregido el id del estudiante " + name + " " + surname + ". Nuevo id: " + idDoc);
				} catch (StudentIdNotFixableException ex) {
					throw new LineNotValidException(ex);
				}
			}
			Student st = new Student(name, surname, idDoc, dob);

			return st;
		} catch (ArrayIndexOutOfBoundsException e){
			e.getMessage();
			throw new LineNotValidException(e);
		}
	}
	
    protected static void validateDoc(String doc) throws StudentIdNotValidException {

		if((!onlyNumbers(doc)) || (!Character.isLetter(doc.charAt(doc.length() - 1)))){
			throw new StudentIdNotValidException(IdError.BAD_FORMAT, doc);
		}else if(doc.length() < 9){
			throw new StudentIdNotValidException(IdError.TOO_SHORT, doc);
		}else if (doc.length() > 9){
			throw new StudentIdNotValidException(IdError.TOO_LONG, doc);
		}else if(idLetter(doc) != doc.charAt(doc.length()-1)){
			throw new StudentIdNotValidException(IdError.BAD_LETTER, doc);
		}
    }
    protected static void validateAge(LocalDate dob) throws StudentTooYoungException {
		if(!isAdult(dob)) {
			throw new StudentTooYoungException(dob);
		}
    }

    protected static char idLetter(String doc) throws StudentIdNotValidException{
		String characters= "TRWAGMYFPDXBNJZSQVHLCKE";
    	int DNI = Integer.parseInt(doc.substring(0,8));
		char letter;
		int module = DNI % 23;
		letter = characters.charAt(module);
    	return letter;
    }
    
    protected static String fixId(IdError code, String doc) throws StudentIdNotFixableException {
    	switch(code) {
    	case TOO_SHORT:
			while(doc.length() < ID_SIZE) {
				doc = "0" + doc;
			}
			break;
    	case BAD_LETTER:
    		int DNI = Integer.parseInt(doc.substring(0,8));
			try {
				doc = String.valueOf(DNI) + idLetter(doc);
			} catch (StudentIdNotValidException e) {
				//No se va a producir
				e.printStackTrace();
			}
			break;
    	case BAD_FORMAT:
			throw new StudentIdNotFixableException(IdError.BAD_FORMAT, doc);
    	case TOO_LONG:
			throw new StudentIdNotFixableException(IdError.TOO_LONG, doc);
    	}
    	return doc;
    }
    
	protected static boolean isAdult(LocalDate dob) {
		LocalDate currentDate = LocalDate.now();
		int age = 0;
		if((dob != null)&& (currentDate != null)) {
			age = Period.between(dob, currentDate).getYears();
		}
		if(age >= ADULT_AGE) {
			return true;
		}else {
			return false;
		}
	}
	protected static boolean onlyNumbers(String doc){
		String num;
		String misNumeros = "0123456789";
		if(doc.length() > 1){
			for(int i = 0; i < doc.length()-1; i++){
				num = doc.substring(i, i+1);
				if(!misNumeros.contains(num)) {
					return false;
				}
			}
		}else{
			return false;
		}
		return true;
	}

}
