package ie.atu.sw;


/*
 * Prepares input text for processing by the Four-Square Cipher: 
 * - Only uppercase letters A–Z
 * - No spaces or non-alphabetic characters
 * - Replaces J --> I
 * - Has an even length
 */




public class TextProcessor {

    public String prepareText(String input) {

        // Convert to uppercase
        input = input.toUpperCase();
        // Replace J with I
        input = input.replace('J', 'I');
        
        // Remove non-alphabetic characters and create a new string
        StringBuilder filtered = new StringBuilder();

        // Keep only A-Z characters
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                filtered.append(c);
            }
        }
        //Even length: if not, append 'X'
        if (filtered.length() % 2 != 0) {
            filtered.append('X');
        }

        // Return the processed text as a string
        return filtered.toString();
    }


    // Debugging method to print bigrams of the processed text
public void printBigrams(String text) {
    
    // Iterate through the text two characters at a time
    for (int i = 0; i < text.length(); i += 2) {
        
        // Get 2 characters and form bigram 
        char first = text.charAt(i);
        char second = text.charAt(i + 1);
        // Print bigram to console
        System.out.print("" + first + second + " ");
    }
    // Move to next line after all bigrams printed
    System.out.println();
}


    


}

