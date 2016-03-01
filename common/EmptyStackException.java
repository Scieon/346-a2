package common;

public class EmptyStackException extends Exception {
	

	private static final long serialVersionUID = 7975917652121690810L;
	private String errorMessage;
	
	public EmptyStackException(){
		super("The stack is full!");
	}
	
	public EmptyStackException(String msg){
		super();
		errorMessage = msg;
	}

	public String getErrorMessage(){
		return errorMessage;
	}


	public void setErrorMessage(String message){
		errorMessage = message;

	}


}
