
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws IOException {
		String command = "restart"; 
		ProcessBuilder builder = new ProcessBuilder( "cmd.exe", "/c", "cd \"C:/Users/Gasimov/Desktop\" &&devcon.exe "+command+" *VID_05E3*");
		 builder.redirectErrorStream(true);
		 Process p = builder.start();
		 BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            System.out.println(line);
	        }
		 
	}
	
	
	
	
	
	
}
