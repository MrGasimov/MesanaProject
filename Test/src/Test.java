
import java.io.IOException;

import java.util.zip.CRC32;

public class Test {

	public static void main(String[] args) throws IOException {
		
		  String toBeEncoded = new String("Thequickbrownfoxj") ;
	      System.out.println("lentg of byte "+toBeEncoded.getBytes().length);
		  CRC32 myCRC = new CRC32( ) ;
	      myCRC.update( toBeEncoded.getBytes( ) ) ;
	      System.out.println( "The CRC-32 value is : " + Long.toHexString( myCRC.getValue( ) ) + " !" ) ;
		
	}
}
