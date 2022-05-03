package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper sqLiteHelperInstance;

    public static SQLiteHelper getInstance(){

        return sqLiteHelperInstance;
    }

    private SQLiteHelper(@Nullable Context context) {
        super(context, "190408R", null, 1, null);
    }

    public static void createContext(Context context){
        if (sqLiteHelperInstance == null){
            sqLiteHelperInstance = new SQLiteHelper(context);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS accounts(" +
                "account_no VARCHAR(255) PRIMARY KEY NOT NULL," +
                "bank_name VARCHAR(255) NOT NULL," +
                "account_holder VARCHAR(255) NOT NULL," +
                "balance DOUBLE)");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS transactions(" +
                "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date DATE NOT NULL," +
                "account_no VARCHAR(255) NOT NULL," +
                "expense_type VARCHAR(10) NOT NULL," +
                "amount DOUBLE NOT NULL," +
                "FOREIGN KEY (account_no) REFERENCES accounts(account_no))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS acounts");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(sqLiteDatabase);
    }
}
