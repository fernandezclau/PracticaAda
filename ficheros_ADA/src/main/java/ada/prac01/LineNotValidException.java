package ada.prac01;

public class LineNotValidException extends Exception {
    private String line;
    private IdError code;
    public LineNotValidException(String message){
        super(message);
    }

    public LineNotValidException(Exception cause){
        super(cause);
    }
    public LineNotValidException(String message, Throwable cause){
        super(message, cause);
    }
    public IdError getCode() {

        return code;
    }
    public static String generateMessage(Throwable cause){
        return cause.toString();
    }
    @Override
    public String getMessage() {
        return super.getMessage();
    }


}
