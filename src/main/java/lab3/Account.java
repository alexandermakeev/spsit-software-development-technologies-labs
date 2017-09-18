package lab3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Account {

    private static final Logger LOGGER = LoggerFactory.getLogger(Account.class);
    private Integer number; //identifier
    private Integer balance;

    Account(Integer number) {
        this.number = number;
    }

    Integer getBalance() {
        return balance;
    }

    void setBalance(Integer balance) {
        this.balance = balance;
    }

    static void transfer(Account src, Account dest, int amount) {
        src.withdraw(amount);
        dest.deposit(amount);
    }

    //take it out
    private void withdraw(int amount) {
        this.balance -= amount;
        if (this.balance < 0) {
            LOGGER.error(String.format("Balance of account #%d is negative: %d", number, balance));
        }
    }

    //take it in
    private void deposit(int amount) {
        this.balance += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return number.equals(account.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

}
