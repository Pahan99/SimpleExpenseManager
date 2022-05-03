package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class SQLiteAccountDAO implements AccountDAO {

    private SQLiteHelper sqLiteHelper;

    public SQLiteAccountDAO() {
        sqLiteHelper = SQLiteHelper.getInstance();
    }

    @Override
    public List<String> getAccountNumbersList() {
        // Get the list of existing account numbers
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT account_no FROM accounts",null);

        List<String> account_no_list = new LinkedList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String account_no = cursor.getString(cursor.getColumnIndex("account_no"));
            account_no_list.add(account_no);
        }

        return account_no_list;
    }

    @Override
    public List<Account> getAccountsList() {
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM accounts",null);

        List<Account> account_list = new LinkedList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String account_no = cursor.getString(cursor.getColumnIndex("account_no"));
            String bank_name = cursor.getString(cursor.getColumnIndex("bank_name"));
            String account_holder = cursor.getString(cursor.getColumnIndex("account_holder"));
            Double balance = cursor.getDouble(cursor.getColumnIndex("balance"));

            Account account = new Account(account_no,bank_name,account_holder,balance);
            account_list.add(account);
        }

        return account_list;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase database = sqLiteHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM accounts WHERE account_no="+accountNo,null);
        cursor.moveToFirst();
        String account_no = cursor.getString(cursor.getColumnIndex("account_no"));
        String bank_name = cursor.getString(cursor.getColumnIndex("bank_name"));
        String account_holder = cursor.getString(cursor.getColumnIndex("account_holder"));
        Double balance = cursor.getDouble(cursor.getColumnIndex("balance"));

        Account account = new Account(account_no,bank_name,account_holder,balance);
        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("account_no",account.getAccountNo());
        values.put("bank_name",account.getBankName());
        values.put("account_holder",account.getAccountHolderName());
        values.put("balance",account.getBalance());

        long id = database.insert("accounts",null,values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
        String [] whereArgs = {accountNo};

        int result = database.delete("accounts","account_no=?",whereArgs);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();
//        String [] whereArgs = {accountNo,expenseType.toString(),Double.toString(amount)};
        Account account = getAccount(accountNo);
        double balance = account.getBalance();
        if (expenseType == ExpenseType.EXPENSE) amount*=(-1);
        balance += amount;

        ContentValues values = new ContentValues();
        values.put("balance",balance);

        String [] whereArgs = {accountNo};
        database.update("accounts",values,"account_no=?",whereArgs);

    }
}
