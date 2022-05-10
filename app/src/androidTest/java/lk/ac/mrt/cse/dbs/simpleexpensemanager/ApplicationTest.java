/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import androidx.test.core.app.ApplicationProvider;

import org.junit.BeforeClass;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.SQLiteDemoExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.SQLiteHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest{

    private static ExpenseManager expenseManager;

    @BeforeClass
    public static void add_account_test() throws ExpenseManagerException {
        Context context = ApplicationProvider.getApplicationContext();
        SQLiteHelper.createContext(context);
        expenseManager = new SQLiteDemoExpenseManager(context);

        expenseManager.addAccount("456789","NSB","test user",15000);
    }

    @Test
    public void testAddAccount(){
        assertTrue(expenseManager.getAccountNumbersList().contains("456789"));
    }

    @Test
    public void testCountTransactions() throws InvalidAccountException {
        int old_count = expenseManager.getTransactionLogs().size();

        expenseManager.getTransactionsDAO()
                .logTransaction(new Date(),"456789", ExpenseType.EXPENSE,2000);
        expenseManager.getAccountsDAO().updateBalance("456789",ExpenseType.EXPENSE,2000);

        assertEquals(expenseManager.getTransactionLogs().size() - old_count , 1);
    }
}