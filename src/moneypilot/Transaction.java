package moneypilot;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public final class Transaction {
    public enum Type { INCOME, EXPENSE }
    public static final List<String> CATEGORIES = List.of("Food", "Transport", "Housing", "Shopping", "Entertainment", "Health", "Education", "Salary", "Other");
    private final UUID id;
    private final LocalDate date;
    private final Type type;
    private final String category, note;
    private final BigDecimal amount;
    public Transaction(UUID id, LocalDate date, Type type, String category, BigDecimal amount, String note) {
        this.id = Objects.requireNonNull(id, "ID is required");
        this.date = Objects.requireNonNull(date, "Date is required");
        this.type = Objects.requireNonNull(type, "Type is required");
        if (!CATEGORIES.contains(category)) throw new IllegalArgumentException("Choose a valid category");
        this.category = category;
        this.amount = money(amount);
        if (this.amount.signum() <= 0 || this.amount.compareTo(new BigDecimal("999999999999.99")) > 0)
            throw new IllegalArgumentException("Amount must be between 0.01 and 999999999999.99");
        this.note = Objects.requireNonNull(note).strip();
        if (this.note.length() > 180 || this.note.contains("\n") || this.note.contains("\r"))
            throw new IllegalArgumentException("Note must be one line, up to 180 characters");
    }
    public static BigDecimal money(BigDecimal value) {
        if (value == null) throw new IllegalArgumentException("Enter an amount");
        try { return value.setScale(2, java.math.RoundingMode.UNNECESSARY); }
        catch (ArithmeticException e) { throw new IllegalArgumentException("Use at most two decimal places"); }
    }
    public UUID id() { return id; }
    public LocalDate date() { return date; }
    public Type type() { return type; }
    public String category() { return category; }
    public String note() { return note; }
    public BigDecimal amount() { return amount; }

}
