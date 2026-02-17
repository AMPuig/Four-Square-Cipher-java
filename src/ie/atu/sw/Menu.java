package ie.atu.sw;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;


/*
DATA FLOW 

[File / URL]
        ↓
 Read input line by line
        ↓
     String line
        ↓
 prepareText(line)        (TextProcessor)
        ↓
 Normalized text
        ↓
 encrypt / decrypt        (CipherEngine)
        ↓
 Write result to output file
*/



/* 
Menu.java
 ├─ Scanner
 ├─ showOptions()
 ├─ setInputSource()
 ├─ setOutputFile()
 ├─ encrypt()
 ├─ decrypt()
 └─ keepRunning
 */

 

// Menu class: user interaction and menu options
public class Menu {

    private Scanner scanner; 
    private boolean keepRunning = true;



    // --- MENU STATE ---

    private String inputSource;     // File path or URL
    private boolean isURL = false;  // true = URL, false = file

    private String outputFile = "output.txt"; // default output
    private String cipherKeyTR; // TR key
    private String cipherKeyBL; // BL key


    public Menu() {
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (keepRunning) {
            showOptions();
            int choice;
            
            try {
                    choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                    System.out.println("[ERROR] Invalid input. Please enter a number between 1 and 6.");
                    continue;
            }
            handleChoice(choice);
        }
         System.out.println("[INFO] Exiting application...");
    }

    private void showOptions() {
        System.out.println(ConsoleColour.WHITE);
        System.out.println("************************************************************");
        System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
        System.out.println("*                                                          *");
        System.out.println("*       Encrypting Files with a Four Square Cipher         *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        System.out.println("(1) Specify Text File to Encrypt");
        System.out.println("(2) Specify Output File (default: ./out.txt)");
        System.out.println("(3) Enter Cipher Keys");
        System.out.println("(4) Encrypt Text File");
        System.out.println("(5) Decrypt Text File");
        System.out.println("(6) Quit");
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Select Option [1-6]> ");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1 -> specifyInputSource();
            case 2 -> specifyOutputFile();
            case 3 -> enterCipherKey();
            case 4 -> encrypt();
            case 5 -> decrypt();
            case 6 -> keepRunning = false;
            default -> System.out.println("[ERROR] Invalid option.");
        }       
    }

    private void specifyInputSource() {
    System.out.println("Select input source:");
    System.out.println("(1) Text file");
    System.out.println("(2) URL");
    System.out.print("> ");

    String choice = scanner.nextLine();

    if (choice.equals("1")) {
        System.out.print("Enter file path: ");
        inputSource = scanner.nextLine();
        isURL = false;
        System.out.println("[INFO] Input file set to: " + inputSource);

    } else if (choice.equals("2")) {
        System.out.print("Enter URL: ");
        inputSource = scanner.nextLine();
        isURL = true;
        System.out.println("[INFO] Input URL set to: " + inputSource);

    } else {
        System.out.println("[ERROR] Invalid input source selection.");
    }
}

    
    private void specifyOutputFile() {
        System.out.print("Enter output file name (press Enter for default): ");
        String input = scanner.nextLine();

        if (!input.isBlank()) {
        outputFile = input;
    }

    System.out.println("[INFO] Output file set to: " + outputFile);
    }


    private void enterCipherKey() {
        System.out.print("Enter Top-Right cipher key: ");
        cipherKeyTR = scanner.nextLine();

        System.out.print("Enter Bottom-Left cipher key: ");
        cipherKeyBL = scanner.nextLine();

    if (cipherKeyTR.isBlank() || cipherKeyBL.isBlank()) {
        System.out.println("[ERROR] Cipher keys cannot be empty.");
        cipherKeyTR = null;
        cipherKeyBL = null;
    } else {
        System.out.println("[INFO] Cipher keys stored.");
    }
    }



    private void encrypt() {

    // --- VALIDATION ---
    if (inputSource == null) {
        System.out.println("[ERROR] No input source specified.");
        return;
    }

    if (cipherKeyTR == null || cipherKeyBL == null) {
        System.out.println("[ERROR] No cipher key specified.");
        return;
    }

    System.out.println("[INFO] Starting encryption...");

    try {
        // Create necessary objects
        
        FourSquare fourSquare = new FourSquare(cipherKeyTR, cipherKeyBL);
        CipherEngine cipherEngine = new CipherEngine(fourSquare);
        TextProcessor textProcessor = new TextProcessor();

        BufferedReader reader;
        
        // --- INPUT: FILE or URL ---
        if (isURL) {
            reader = new BufferedReader(
                        new InputStreamReader(
                            new java.net.URL(inputSource).openStream()
                        )
                     );
        } else {
            reader = new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(inputSource)
                        )
                     );
        }

        // --- OUTPUT ---
        FileWriter writer = new FileWriter(outputFile);

        String line;

        // --- PROCESS LINE BY LINE ---
        
        //Progress counter
        int linesProcessed = 0;

        while ((line = reader.readLine()) != null) {

            String preparedText = textProcessor.prepareText(line);
            String encryptedText = cipherEngine.encrypt(preparedText);

            writer.write(encryptedText + System.lineSeparator());
            
            linesProcessed++;

            // Show progress every 10 lines
            if (linesProcessed % 10 == 0) {
                System.out.println("[INFO] Processed " + linesProcessed + " lines...");
            }
        }

        // Final progress report
        System.out.println("[INFO] Total lines processed: " + linesProcessed);


        // --- CLOSE RESOURCES ---
        reader.close();
        writer.flush();
        writer.close();

        System.out.println("[INFO] Encryption completed successfully.");
        System.out.println("[INFO] Output written to: " + outputFile);

    } catch (Exception e) {
        System.out.println("[ERROR] Encryption failed.");
        System.out.println(e.getMessage());
    }
}

    private void decrypt() {

    // --- VALIDATION ---
    if (inputSource == null) {
        System.out.println("[ERROR] No input source specified.");
        return;
    }

    if (cipherKeyTR == null || cipherKeyBL == null) {
        System.out.println("[ERROR] No cipher key specified.");
        return;
    }

    System.out.println("[INFO] Starting decryption...");

    try {
        // --- CREATE CIPHER OBJECTS ---
        FourSquare fourSquare = new FourSquare(cipherKeyTR, cipherKeyBL);
        CipherEngine cipherEngine = new CipherEngine(fourSquare);
        

        BufferedReader reader;

        // --- INPUT: FILE or URL ---
        if (isURL) {
            reader = new BufferedReader(
                        new InputStreamReader(
                            new java.net.URL(inputSource).openStream()
                        )
                     );
        } else {
            reader = new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(inputSource)
                        )
                     );
        }

        // --- OUTPUT ---
        FileWriter writer = new FileWriter(outputFile);

        String line;

        //Progress counter
        int linesProcessed = 0;

        // --- PROCESS LINE BY LINE ---
        while ((line = reader.readLine()) != null) {

            String decryptedText = cipherEngine.decrypt(line);

            writer.write(decryptedText + System.lineSeparator());
        
            linesProcessed++;
            // Show progress every 10 lines
            if (linesProcessed % 10 == 0) {
                System.out.println("[INFO] Processed " + linesProcessed + " lines...");
            }

        }

        // Final progress report
        System.out.println("[INFO] Total lines processed: " + linesProcessed);

        // --- CLOSE RESOURCES ---
        reader.close();
        writer.flush();
        writer.close();

        System.out.println("[INFO] Decryption completed successfully.");
        System.out.println("[INFO] Output written to: " + outputFile);

    } catch (Exception e) {
        System.out.println("[ERROR] Decryption failed.");
        System.out.println(e.getMessage());
    }
}



    
}
