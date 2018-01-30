package com.example.qbook.financeapp.Data.RoomDatabase;

import android.arch.persistence.room.TypeConverter;

import com.example.qbook.financeapp.Data.RecurrenceType;

public class EnumConverter {
    @TypeConverter
    public static RecurrenceType toType(int status) {
        switch (status) {
            case 8:
                return RecurrenceType.OFF;
            case 0:
                return RecurrenceType.DAILY;
            case 1:
                return RecurrenceType.WEEKLY;
            case 2:
                return RecurrenceType.EVERY_TWO_WEEKS;
            case 3:
                return RecurrenceType.MONTHLY;
            case 4:
                return RecurrenceType.QUARTERLY;
            case 5:
                return RecurrenceType.EVERY_FOUR_MONTHS;
            case 6:
                return RecurrenceType.EVERY_SIX_MONTHS;
            case 7:
                return RecurrenceType.YEARLY;
            default:
                return RecurrenceType.OFF;
        }
    }

    @TypeConverter
    public static int toInt(RecurrenceType recurrenceType) {
        return recurrenceType.getStatus();
    }
}
