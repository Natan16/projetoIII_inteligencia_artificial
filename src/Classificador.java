import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;



public class Classificador {
	
	     	
	public static void main(String[] args)  throws IOException {
		
	    DataInputStream inputMovies , inputRatings , inputUsers;
		inputMovies = new DataInputStream(new FileInputStream("data.dat"));
		while (inputMovies.available() > 0) {
		    int x = inputMovies.readInt();
		    System.out.println(x);
		}            
        inputMovies.close();	

	}

	
}
