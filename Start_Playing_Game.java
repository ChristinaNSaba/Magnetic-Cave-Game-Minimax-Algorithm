import java.util.Scanner;

public class Start_Playing_Game { //this class is to print the menu choices and ask the user to enter the choice he/she wants

	public static void main(String[] args) {

		Magnetic_Cave_Board game = new Magnetic_Cave_Board();
        Scanner scanner = new Scanner(System.in);

        System.out.println("-------------------Magnetic Cave Game--------------------");
        System.out.println("-------------------User Menu Choices---------------------");
        System.out.println();
        System.out.println("1. Manual entry for both ■'s moves and □'s moves");
        System.out.println("2. Manual entry for ■'s moves and automatic moves for □");
        System.out.println("3. Manual entry for □'s moves and automatic moves for ■");
        System.out.print("Enter your choice from 1 to 3: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                game.manualMode(); // calling the manual players method
                break;
            case 2:
                game.manualAndAutoMode(); //calling the manual player1 and automatic player2 method
                break;
            case 3:
                game.AutoAndManualMode();//calling the manual player2 and automatic player1 method
                break;
            default:
                System.out.println("Not Accepted Choice, Exiting the game!!");
                break;
        }


        scanner.close();
	}
}
