package mediajs.budgetbook.logic;

import android.database.sqlite.SQLiteDatabase;

import mediajs.budgetbook.objects.entity.BudgetBookDbHelper;

/**
 * Created by Juergen on 25.06.2015.
 */
public class BaseLogic
{
	protected SQLiteDatabase database = null;
	protected BudgetBookDbHelper dbHelper;

	protected void databaseOpen()
	{
		if( database == null )
		{
			database = dbHelper.getWritableDatabase();
		}
	}

	protected void databaseClose()
	{
		if( database != null )
		{
			database.close();
			database = null;
		}
	}
}
