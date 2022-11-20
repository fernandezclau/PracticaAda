package ada.prac01;

public class StudentIdNotValidException extends Exception {
    private String idDoc;
    private IdError code;

    public StudentIdNotValidException(IdError code, String idDoc) {
        super(idDoc);
        this.idDoc = idDoc;
        this.code = code;
    }
//
    public IdError getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return "El ID del estudiante (" + idDoc + ") no es correcto (" + code.getMessage() + ")";
    }
    
}
