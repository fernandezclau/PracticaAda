package ada.prac01;

public enum IdError {
	TOO_SHORT("El ID es demasiado corto"), 
	TOO_LONG("El ID es demasiado largo"), 
	BAD_FORMAT("El formato del ID no es correcto"), 
	BAD_LETTER("La letra del ID no es correcta");
	
	private String message;
	//hola

	IdError(String message){
		this.message = message;
	}
	public String getMessage(){
		return message;
	}
}
