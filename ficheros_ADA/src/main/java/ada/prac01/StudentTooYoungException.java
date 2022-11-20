package ada.prac01;

import java.time.LocalDate;

public class StudentTooYoungException extends Exception{
    private LocalDate dob;

    public StudentTooYoungException(LocalDate dob) {
        this.dob = dob;
    }


    @Override//
    public String getMessage() {

        return "Estudiante demasiado joven (Fecha de nacimiento: " + dob + ")";
    }
}
