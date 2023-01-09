package banking;

public class Account {

    private String cardNumber;
    private String pin;
    private int balance;
    private User user;

    Account() {
        createNewRandom();
    }

    public void createNewRandom() {

        String accountNumberWithoutCheckDigit = "400000" + String.format("%09d", (long) (Math.random() * 999999999L));

        setCardNumber(accountNumberWithoutCheckDigit + generateCheckDigit(accountNumberWithoutCheckDigit));

        setPin(String.format("%04d", (long) (Math.random() * 9999)) );
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public static boolean checkIfCardNumberAndPinInCorrectFormat(String cardNumber, String pin) {
        if (cardNumber.length() != 16 ||
                (!cardNumber.startsWith("4") && !cardNumber.startsWith("34") && !cardNumber.startsWith("37") && !cardNumber.startsWith("5")) )
            return false;

        try {
            if (Integer.parseInt(pin) < 0 || Integer.parseInt(pin) > 9999)
                return false;
        } catch (ArithmeticException e) {
            return false;
        }

        return true;

    }



    /**
     * Generate check digit using luhn algorithm
     * Source from: https://gist.github.com/stanzheng/5781833
     *
     * @param number
     * @return
     */
    private static int generateCheckDigit(String number) {
        int sum = 0;
        int remainder = (number.length() + 1) % 2;
        for (int i = 0; i < number.length(); i++) {

            // Get the digit at the current position.
            int digit = Integer.parseInt(number.substring(i, (i + 1)));

            if ((i % 2) == remainder) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        // The check digit is the number required to make the sum a multiple of
        // 10.
        int mod = sum % 10;
        int checkDigit = ((mod == 0) ? 0 : 10 - mod);

        return checkDigit;
    }

    @Override
    public String toString() {
        return "Account{" +
                "cardNumber='" + cardNumber + '\'' +
                ", pin='" + pin + '\'' +
                ", balance=" + balance +
                '}';
    }
}