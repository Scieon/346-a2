package common;

public class FullStackException extends Exception{

	
	private static final long serialVersionUID = -7448960627229882034L;
	
	private String errorMessage;

	public FullStackException(){
		super("The stack is full!");
	}
	
	public FullStackException(String msg){
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
