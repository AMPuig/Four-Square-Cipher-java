package ie.atu.sw;

/*
CipherEngine
├── findPosition(char[][] matrix, char letter)
├── encryptBigram(char first, char second)
├── encrypt(String text)
*/

/*
DATA FLOW (Text Preparation)

[File / URL / Console]
        ↓
     Raw text (String)
        ↓
 prepareText(text)        (TextProcessor)
        ↓
 Normalized text
        ↓
    Encrypt / Decrypt        (CipherEngine)
*/


public class CipherEngine {

// References to the four cipher matrices
private char[][] TL; // Top-Left (plaintext)
private char[][] TR; // Top-Right (ciphertext)
private char[][] BL; // Bottom-Left (ciphertext)
private char[][] BR; // Bottom-Right (plaintext)

// Constructor: receives matrices from FourSquare
public CipherEngine(FourSquare fs) {
        this.TL = fs.getTL();
        this.TR = fs.getTR();
        this.BL = fs.getBL();
        this.BR = fs.getBR();
    }

// Find the row and column of a letter in a given matrix
private int[] findPosition(char[][] matrix, char letter) {
    for (int row = 0; row < matrix.length; row++) {
        for (int col = 0; col < matrix[row].length; col++) {
            if (matrix[row][col] == letter) {
                return new int[]{row, col};
            }
        }
    }
    return null;
}

// Encrypt a single bigram (2 letters)
private String encryptBigram(char first, char second) {

    int[] posFirst  = findPosition(TL, first);
    int[] posSecond = findPosition(BR, second);

    char cipher1 = TR[posFirst[0]][posSecond[1]];
    char cipher2 = BL[posSecond[0]][posFirst[1]];

    return "" + cipher1 + cipher2;
}


// Encrypt the entire text
public String encrypt(String text) {

        StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            char first = text.charAt(i);
            char second = text.charAt(i + 1);

            encrypted.append(encryptBigram(first, second));
        }

        return encrypted.toString();
    }


// Decrypt the entire ciphertext
public String decrypt(String ciphertext) {

    StringBuilder decrypted = new StringBuilder();

    for (int i = 0; i < ciphertext.length(); i += 2) {
        char first = ciphertext.charAt(i);
        char second = ciphertext.charAt(i + 1);

        int[] posFirst  = findPosition(TR, first);
        int[] posSecond = findPosition(BL, second);

        char plain1 = TL[posFirst[0]][posSecond[1]];
        char plain2 = BR[posSecond[0]][posFirst[1]];

        decrypted.append(plain1).append(plain2);
    }

    return decrypted.toString();
}



}