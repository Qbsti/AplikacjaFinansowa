package com.example.qbook.financeapp.Data;

public enum RecurrenceType {
    OFF(8),
    DAILY(0),
    WEEKLY(1),
    EVERY_TWO_WEEKS(2),
    MONTHLY(3),
    QUARTERLY(4),
    EVERY_FOUR_MONTHS(5),
    EVERY_SIX_MONTHS(6),
    YEARLY(7);

    private int status;

    RecurrenceType(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
