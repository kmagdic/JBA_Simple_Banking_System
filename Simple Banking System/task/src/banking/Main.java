package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static List<Account> accountList = new ArrayList<Account>();

    public static Account findAccount(String cardNumber, String pin) {
        for(Account a : accountList) {
            if(a.getCardNumber().equals(cardNumber) && a.getPin().equals(pin))
                return a;
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean doExit = false;
        Account currAccount = null;
        int menu = -1;

        do {
            if (currAccount == null) {
                System.out.println("\n1. Create an account");
                System.out.println("2. Log into account");
                System.out.println("0. Exit");
                System.out.print(">");

                menu = scanner.nextInt();

                // create an account
                if(menu == 1) {
                    Account newAccount = new Account();
                    accountList.add(newAccount);
                    System.out.println("Your card has been created.");
                    System.out.println("Your card number:\n" + newAccount.getCardNumber());
                    System.out.println("Your card PIN:\n" + newAccount.getPin());
               }
                // log into account
                else if (menu == 2) {
                    System.out.print("Enter your card number:\n>");
                    String cardNumber = scanner.next();

                    System.out.print("Enter your pin:\n>");
                    String pin = scanner.next();

                    currAccount = findAccount(cardNumber, pin);
                    if (currAccount == null) {
                        System.out.println("Wrong card number or PIN");
                    } else  {
                        System.out.println("You have successfully logged in!");
                    }
                }


            }
            // logged in user
            else {
                System.out.println("\n1. Balance");
                System.out.println("2. Log out");
                System.out.println("3. Set balance");
                System.out.println("0. Exit");
                System.out.print(">");

                int menu2 = scanner.nextInt();

                if (menu2 == 1) {
                    System.out.println("Balance: " + currAccount.getBalance());
                } else if (menu2 == 2) {
                    currAccount = null;
                    System.out.println("You have successfully logged out!");
                } else if (menu2 == 3) {
                   try {
                       System.out.print("Enter new balance\n>");
                       int b = scanner.nextInt();
                       currAccount.setBalance(b);
                       System.out.println("New balance is set!");
                   } catch (ArithmeticException e) {
                       System.out.println("Wrong number!");
                   }
                }


            }



        } while(menu != 0);

        System.out.println("Bye!");
    }
}