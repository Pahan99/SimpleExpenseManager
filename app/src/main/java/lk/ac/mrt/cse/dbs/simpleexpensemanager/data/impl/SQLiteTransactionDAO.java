package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SQLiteTransactionDAO implements TransactionDAO {

    private SQLiteHelper sqLiteHelper;

    public SQLiteTransactionDAO() {
        sqLiteHelper = SQLiteHelper.getInstance();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date",new SimpleDateFormat("dd-MM-yyyy").format(date));
        values.put("account_no",accountNo);
        values.put("expense_type",expenseType.toString());
        values.put("amount",amount);

        long id = database.insert("transactions",null,values);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM transactions",null);

        return getTransactionList(cursor);
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM transactions LIMIT "+limit,null);
        return getTransactionList(cursor);
    }

    private List<Transaction> getTransactionList(Cursor cursor){

        List<Transaction> transactionList = new LinkedList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String account_no = cursor.getString(cursor.getColumnIndex("account_no"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String expense_type = cursor.getString(cursor.getColumnIndex("expense_type"));
            Double amount = cursor.getDouble(cursor.getColumnIndex("amount"));

            ExpenseType expenseType = expense_type.equals(ExpenseType.EXPENSE.toString()) ? ExpenseType.EXPENSE : ExpenseType.INCOME;

            Transaction transaction = null;
            try {
                transaction = new Transaction(new SimpleDateFormat("dd-MM-yyyy").parse(date),account_no,expenseType,amount);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transactionList.add(transaction);
        }

        return transactionList;
    }
}
