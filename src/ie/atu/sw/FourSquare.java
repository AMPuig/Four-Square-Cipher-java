package ie.atu.sw;


/*
 * This class is responsible for building and storing
 * the four 5x5 matrices used in the Four-Square Cipher
 * No encryption or decryption*/

public class FourSquare {
	
    
	// The four matrices
	private char[][] TL; //Top-left (plaintext)
	private char[][] TR; //Top-Right  (ciphertext)
	private char[][] BL; //Bottom Left (ciphertext)
	private char[][] BR; //Bottom-Right (plaintext)

	
	// Base alphabet (no J)
	private static final char[] ALPHABET =
	        "ABCDEFGHIKLMNOPQRSTUVWXYZ".toCharArray();

	//Constructor: builds the 4 matrices needed
	public FourSquare(String keyTR, String keyBL) {
		
		// Build the two plaintext matrices
		TL = buildPlainMatrix();
		BR = buildPlainMatrix(); 
		
		// Build the two ciphertext matrices
		char[] keyArrayTR = buildKeyArray(keyTR);
		char[] keyArrayBL = buildKeyArray(keyBL);

		TR = buildCipherMatrix(keyArrayTR);
		BL = buildCipherMatrix(keyArrayBL);

	
	} 
	
	private char[][] buildPlainMatrix() {
	    // Create an empty 5x5 matrix
		char[][] matrix = new char[5][5];
	    int index = 0;
	    
	    /*Loop through each row of the matrix and
	    assign the next alphabet character to the matrix */
	    for (int row = 0; row < 5; row++) {
	        for (int col = 0; col < 5; col++) {
	            matrix[row][col] = ALPHABET[index++];
	        }
	    }
	    //return plaintext matrix 
	    return matrix;
	}
	
	/*Process the keyword and generates a 25 char array 
	(No duplicate letter and add remaining letters of alphabet)*/
	
	private char[] buildKeyArray(String keyword) {
	    
		// Temporary array holding key characteres
		char[] temp = new char[25];
	    
		// Counter iteration characteres
		int count = 0; 
	    
	    //Convert to uppercase and replace 'J' with 'I'
	    keyword = keyword.toUpperCase().replace('J', 'I');
	    
	    //1-Add keyword characters 
	    for (int i = 0; i < keyword.length(); i++) {
	        
	    	//Get every character of keyword
	    	char c = keyword.charAt(i);

	    	//Add if it doesn't exist
	        if (!contains(temp, count, c)) {
	            temp[count++] = c;
	        }
	    }

	    //2- Add remaining characters of alphabet
	    for (int i = 0; i < ALPHABET.length; i++) {
	        
	    	//Get every alphabet character 
	    	char c = ALPHABET[i];
	    	
	    	//Add if it doesn't exist
	        if (!contains(temp, count, c)) {
	            temp[count++] = c;
	        }
	    }

	    //Return 25 char key array
	    return temp;
	}

	
	//Check if a char exits in an array (No Collections allowed)
	private boolean contains(char[] arr, int length, char c) {
		
		// Loop through the part of the array that already contains characters
		for (int i = 0; i < length; i++) {
	        
			//character found
			if (arr[i] == c) {
	            return true;
	        }
	    }
		
		//Not Found
	    return false;
	}

	
	
	
	//Build 25 character ciphertext array
	private char[][] buildCipherMatrix(char[] keyArray) {
	    
		//Create empty 5x5 matrix 
		char[][] matrix = new char[5][5];
	    
		// Counter iteration characters
		int index = 0; 

		//Loop through each row 
	    for (int row = 0; row < 5; row++) {
	    	// Loop through each row 
	        for (int col = 0; col < 5; col++) {
	        	//Assign next key char 
	            matrix[row][col] = keyArray[index++];
	        }
	    }
	    
	    // Return ciphertext matrix
	    return matrix;
	}



	
	//Getters for the four matrices
	public char[][] getTL() {
		return TL;
	}
	
	public char[][] getTR() {
		return TR;
	}
	
	public char[][] getBL() {
		return BL;
	}
	
	public char[][] getBR() {
		return BR;
	}


}
