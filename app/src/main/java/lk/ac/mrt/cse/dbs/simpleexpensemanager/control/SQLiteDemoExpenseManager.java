package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteTransactionDAO;

public class SQLiteDemoExpenseManager extends ExpenseManager{
    public SQLiteDemoExpenseManager(Context context) throws ExpenseManagerException {
        setup();
    }

    @Override
    public void setup() throws ExpenseManagerException {
        setTransactionsDAO(new SQLiteTransactionDAO());
        setAccountsDAO(new SQLiteAccountDAO());
    }
}
