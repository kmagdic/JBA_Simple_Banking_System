package banking;

class Account {
    private String cardNumber;
    private String pin;
    private int balance;


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

    public Account() {
        cardNumber = "400000" + String.format("%010d", (long) (Math.random() * 9999999999L)) ;
        pin = String.format("%04d", (long) (Math.random() * 9999));
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
