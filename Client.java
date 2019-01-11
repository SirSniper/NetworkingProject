import java.net.*;
import java.util.Scanner;

public class Client {

    private static void getMenu(){
        System.out.println("1. ");
        System.out.println("2. ");
        System.out.println("3. ");
        System.out.println("4. ");
        System.out.println("5. ");
        System.out.println("6. ");
        System.out.println("7. ");

    }

    private static int getMenuSelection(){
        Scanner userSelectionReader = new Scanner(System.in);
        boolean valid = false;
        String selection;
        int selectedItem = 0;
        while(!valid){
            getMenu();
            selection = userSelectionReader.nextLine();
            try{
                selectedItem = Integer.parseInt(selection);
            } catch (Exception e){
                System.out.println("That wasn't a valid selection, please only enter an integer between 1 and 7");
                continue;
            }
            if(selectedItem < 1 || selectedItem > 7){
                System.out.println("That wasn't a valid selection, please only enter an integer between 1 and 7");
                continue;
            }else{
                valid = true;
            }
        }
        return selectedItem;
    }

    public static void main(String args[]){
        System.out.println(getMenuSelection());
    }

    public void emulate(String[] commands){
        return ;
    }

}