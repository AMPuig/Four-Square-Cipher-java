package ie.atu.sw;

/*
Runner
- Entry point of the application
- Starts the menu system
- No encryption or file handling logic here
*/

/*Runner.java
 └─ main()
     └─ new Menu().start();
     */


public class Runner {

    public static void main(String[] args) {
         
        // START MENU INTERFACE
        Menu menu = new Menu();
        menu.start();
    }
       
}


