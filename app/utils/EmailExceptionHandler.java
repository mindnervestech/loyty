package utils;

public class EmailExceptionHandler {

	public static void handleException(Exception e){
		//System.out.println("Comes HERE");
		//System.out.println(e.getMessage());
		e.printStackTrace();
	}
}
