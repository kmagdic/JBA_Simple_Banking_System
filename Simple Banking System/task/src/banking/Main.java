package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    static AccountDao accountDao = null;
    static UserDao userDao = null;
    static Scanner scanner = new Scanner(System.in);
    static User currUser = null;
    static Connection conn = null;

    public static void main(String[] args) {
        makeDBConnection(args[1]);
        userDao = new UserDao(conn);
        accountDao = new AccountDao(conn);

        mainMenu();

        System.out.println("Bye!");
    }

    private static void mainMenu() {
        int userMenu = -1;
        User currUser = null;

        do {
            System.out.println("\n1. Create an user");
            System.out.println("2. Log into user");
            System.out.println("3. List all users");
            System.out.println("0. Exit");
            System.out.print(">");

            userMenu = scanner.nextInt();
            switch (userMenu) {
                case 1:
                    User newUser = new User();
                    System.out.print("Enter first name:\n>");
                    newUser.setFirstName(scanner.next());

                    System.out.print("Enter last name:\n>");
                    newUser.setLastName(scanner.next());

                    System.out.print("Enter email:\n>");
                    newUser.setEmail(scanner.next());

                    System.out.print("Enter password:\n>");
                    newUser.setPassword(scanner.next());
                    userDao.addUser(newUser);
                    break;
                case 2:
                    System.out.print("Enter email:\n>");
                    String email = scanner.next();

                    System.out.print("Enter password:\n>");
                    String password = scanner.next();

                    currUser = userDao.findUser(email);
                    if(!currUser.getPassword().equals(password))
                        currUser = null;

                    if(currUser == null) {
                        System.out.println("User is not found.");
                    } else {
                        System.out.println("Successfully logged in. ");
                        loggedInUserMenu(currUser);
                    }
                    break;
                case 3:
                    List<User> users = userDao.findAllUsers();
                    for (User u: users) {
                        System.out.println(u);

                    }
                    break;

            }
        } while (userMenu != 0);
    }

    private static void loggedInUserMenu(User currUser) {
        Account currAccount = null;

        int menu = -1;
        do {
            // print user menu

            System.out.println("\n1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("3. List all accounts");
            System.out.println("0. Exit");
            System.out.print(">");

            menu = scanner.nextInt();

            switch (menu) {
                // create an account
                case 1:
                    Account newAccount = new Account();
                    newAccount.setUser(currUser);

                    if (!Account.checkIfCardNumberAndPinInCorrectFormat(newAccount.getCardNumber(), newAccount.getPin())) {
                        System.out.println("Wrong card number or PIN!");
                    } else if (accountDao.findAccount(newAccount.getCardNumber()) != null) {
                        System.out.println("Genereated card number already exists, please try again!");
                    } else {
                        System.out.println("Your card has been created.");
                        System.out.println("Your card number:\n" + newAccount.getCardNumber());
                        System.out.println("Your card PIN:\n" + newAccount.getPin());

                        accountDao.addAccount(newAccount);
                    }
                    break;
                // log into account
                case 2:
                    System.out.print("Enter your card number:\n>");
                    String cardNumber = scanner.next();

                    System.out.print("Enter your pin:\n>");
                    String pin = scanner.next();

                    currAccount = accountDao.findAccount(cardNumber);
                    if(currAccount != null && !currAccount.getPin().equals(pin))
                        currAccount = null;

                    if (currAccount == null) {
                        System.out.println("Wrong card number or PIN");
                    } else {
                        System.out.println("You have successfully logged in!");
                        loggedInAccountMenu(currAccount);
                    }
                    break;
                // list all accounts
                case 3:
                    System.out.println("All accounts:");
                    for(Account a : accountDao.findAllAccounts(currUser)) {
                        System.out.println(a);
                    }

                }

        } while(menu != 0);
    }

    private static void loggedInAccountMenu(Account currAccount) {
        int menu = -1;
        do {
            System.out.println("\n1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit\n");

            menu = scanner.nextInt();

            switch (menu) {
                case 1: // balance
                    System.out.println("Balance: " + currAccount.getBalance());
                    break;
                case 2: // add income
                    addIncome(currAccount);
                    break;
                case 3: // do transfer
                    doTransfer(currAccount);
                    break;
                case 4:
                    accountDao.deleteAccount(currAccount);
                    System.out.println("You have successfully deleted account!");
                    break;
                case 5:
                    System.out.println("You have successfully logged out!");
                    break;
                case 0:
                    System.out.println("Bye!");
                    System.exit(0);
                    break;
            }
        } while (menu != 5);

    }

    private static void makeDBConnection(String fileName) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static void addIncome(Account currAccount) {
        try {
            System.out.println("Enter income:");
            int income = scanner.nextInt();
            currAccount.setBalance(currAccount.getBalance() + income);
            accountDao.updateAccount(currAccount);
            System.out.println("Income was added!");
        } catch (ArithmeticException e) {
            System.out.println("Wrong number!");
        }
    }

    private static boolean doTransfer(Account currAccount) {
        System.out.println("Transfer\nEnter card number:");
        String accountToTransferStr = scanner.next();

        if(Account.checkIfCardNumberAndPinInCorrectFormat(accountToTransferStr, "1111") == false) {
            System.out.println("Probably you made a mistake in the card number or it doesn't exist.\nPlease try again!\n");
            return false;
        }

        Account accountToTransfer = accountDao.findAccount(accountToTransferStr);
        if(accountToTransfer == null) {
            System.out.println("Such a card does not exist. Probably you made mistake.");
            return false;
        }

        System.out.println("Enter how much money you want to transfer:");
        int amount = scanner.nextInt();
        if(amount > currAccount.getBalance()) {
            System.out.println("Not enough money!");
            return false;
        }

        accountToTransfer.setBalance(accountToTransfer.getBalance() + amount);
        currAccount.setBalance(currAccount.getBalance() - amount);
        accountDao.updateAccount(currAccount);
        accountDao.updateAccount(accountToTransfer);
        System.out.println("Success!");
        return true;
    }


}