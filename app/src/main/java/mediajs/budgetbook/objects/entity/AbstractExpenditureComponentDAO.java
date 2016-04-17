package mediajs.budgetbook.objects.entity;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Juergen on 02.03.2015.
 */
public abstract class AbstractExpenditureComponentDAO
{
	protected static final String TEXT_TYPE    = " TEXT";
	protected static final String INTEGER_TYPE = " INTEGER";
	protected static final String COMMA_SEP    = ",";
	protected SQLiteDatabase database          = null;
}
