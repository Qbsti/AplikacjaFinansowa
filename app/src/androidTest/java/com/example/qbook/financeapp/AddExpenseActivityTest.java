package com.example.qbook.financeapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.qbook.financeapp.Data.RoomDatabase.AppDatabase;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.ExpenseCategoryDao;
import com.example.qbook.financeapp.Data.RoomDatabase.DAO.ExpenseDao;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.Expense;
import com.example.qbook.financeapp.Data.RoomDatabase.Entities.ExpenseCategory;

import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;


@RunWith(AndroidJUnit4.class)
public class AddExpenseActivityTest {


    public static final String TEST_CATEGORY_NAME = "NewCategory";
    public static final String TEST_CATEGORY_NAME2 = "NewCategory2";
    public static final String TEST_EXPENSE_NAME = "NewExpense";
    public static final float TEST_EXPENSE_VALUE = 100;


    private ExpenseDao mExpenseDao;
    private ExpenseCategoryDao mExpenseCategoryDao;
    private AppDatabase mDatabase;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mExpenseDao = mDatabase.expenseDao();
        mExpenseCategoryDao = mDatabase.expenseCategoryDao();
    }

    @After
    public void closeDb() throws IOException {
        mDatabase.close();
    }

    @Test
    public void shouldSaveNewExpenseCategoryInDatabase() {
        //given
        ExpenseCategory category = new ExpenseCategory(TEST_CATEGORY_NAME);

        //when
        mExpenseCategoryDao.insertAll(category);

        //then
        Assert.assertThat(mExpenseCategoryDao.getExpenseCategoryByName(TEST_CATEGORY_NAME).getName(), is(TEST_CATEGORY_NAME));
    }

    @Test
    public void saveNewExpense() throws Exception {
        //given
        Expense newExpense = new Expense();
        newExpense.setName(TEST_EXPENSE_NAME);
        newExpense.setDate(new Date());
        mExpenseCategoryDao.insertAll(new ExpenseCategory(TEST_CATEGORY_NAME2));
        newExpense.setCategoryName(TEST_CATEGORY_NAME2);
        newExpense.setValue(TEST_EXPENSE_VALUE);
        //when
        mExpenseDao.insertAll(newExpense);
        //then
        Assert.assertThat(mExpenseDao.getAll().stream()
                .filter(expense -> Objects.equals(expense.getName(), TEST_EXPENSE_NAME) &&
                        expense.getValue() == TEST_EXPENSE_VALUE &&
                        expense.getCategoryName().equals(TEST_CATEGORY_NAME2))
                .findFirst()
                .get()
                .getDate(), is(newExpense.getDate()));
    }

    @Test
    public void shouldThrowExceptionWhenCategoryExists() throws Exception {
        //given
        mExpenseCategoryDao.insertAll(new ExpenseCategory(TEST_CATEGORY_NAME));

        //when

        //then
        try{
            mExpenseCategoryDao.insertAll(new ExpenseCategory(TEST_CATEGORY_NAME));
            Assert.fail();
        } catch(Exception e)
        {
            //OK
        }
    }
}