package mediajs.budgetbook.objects.entity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Juergen on 02.03.2015.
 */
public class BudgetBookDbHelper extends SQLiteOpenHelper
{
	// If you change the database schema, you must increment the database version.
	public static final int    DATABASE_VERSION = 1;
	public static final String DATABASE_NAME    = "BudgetBook.db";

	public BudgetBookDbHelper( Context context )
	{
		super( context, DATABASE_NAME, null, DATABASE_VERSION );
	}

	public void onCreate( SQLiteDatabase db )
	{
		db.execSQL( StoreDAO.SQL_CREATE_ENTRIES );
		db.execSQL( ProductDAO.SQL_CREATE_ENTRIES );
		db.execSQL( SourceOfFundsDAO.SQL_CREATE_ENTRIES );
		db.execSQL( ComponentRelationDAO.SQL_CREATE_ENTRIES );
		db.execSQL( ExpenditureDAO.SQL_CREATE_ENTRIES );
	}

	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
	{
		// This database is only a cache for online data, so its upgrade policy is
		// to simply to discard the data and start over
		db.execSQL( StoreDAO.SQL_DELETE_ENTRIES );
		db.execSQL( ProductDAO.SQL_DELETE_ENTRIES );
		db.execSQL( SourceOfFundsDAO.SQL_DELETE_ENTRIES );
		db.execSQL( ComponentRelationDAO.SQL_DELETE_ENTRIES );
		db.execSQL( ExpenditureDAO.SQL_DELETE_ENTRIES );
		onCreate( db );
	}

	public void onDowngrade( SQLiteDatabase db, int oldVersion, int newVersion )
	{
		onUpgrade( db, oldVersion, newVersion );
	}

}
