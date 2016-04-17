package mediajs.budgetbook.objects.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import mediajs.budgetbook.objects.trans.ExpenditureTO;
import mediajs.budgetbook.utilities.DateUtility;

/**
 * Created by Juergen on 02.03.2015.
 */
public class ExpenditureDAO
{
	private              SQLiteDatabase database  = null;
	private static final String         REAL_TYPE = " REAL";
	private static final String         COMMA_SEP = ",";

	public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ExpenditureEntry.TABLE_NAME;
	public static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + ExpenditureEntry.TABLE_NAME + " (" +
					ExpenditureEntry._ID + " INTEGER PRIMARY KEY," +
					ExpenditureEntry.COLUMN_AMOUNT + REAL_TYPE + COMMA_SEP +
					ExpenditureEntry.COLUMN_RECORDING_DATE + " TEXT NOT NULL);";

	private String[] allColumns = {ExpenditureEntry._ID, ExpenditureEntry.COLUMN_AMOUNT, ExpenditureEntry.COLUMN_RECORDING_DATE};

	public ExpenditureDAO( SQLiteDatabase pDatabase )
	{
		database = pDatabase;
	}

	public ExpenditureTO createExpenditure( ExpenditureTO expenditureTO )
	{
		ContentValues values = getContentValues( expenditureTO );

		long insertId = database.insert( ExpenditureEntry.TABLE_NAME, null, values );
		ExpenditureTO newExpenditureTO = getExpenditureTOByUid( insertId );
		return newExpenditureTO;
	}

	public ExpenditureTO updateExpenditure( ExpenditureTO expenditureTO )
	{
		ContentValues values = getContentValues( expenditureTO );
		long updateId = database.update( ExpenditureEntry.TABLE_NAME, values, ExpenditureEntry._ID + "=" + expenditureTO.getUid(), null );

		ExpenditureTO newExpenditureTO = getExpenditureTOByUid( updateId );
		return newExpenditureTO;
	}

	public void deleteExpenditure( ExpenditureTO expenditureTO )
	{
		deleteExpenditure( expenditureTO.getUid() );
	}

	public void deleteExpenditure( long id )
	{
		database.delete( ExpenditureEntry.TABLE_NAME, ExpenditureEntry._ID + "=?", new String[]{String.valueOf( id )} );
	}

	public ExpenditureTO getExpenditureTOByUid( long uid )
	{
		Cursor cursor = database.query( ExpenditureEntry.TABLE_NAME,
		                                allColumns, ExpenditureEntry._ID + " =?", new String[]{String.valueOf( uid )},
		                                null, null, null );
		ExpenditureTO result = null;

		if( cursor.moveToFirst() )
		{
			result = cursorToExpenditureTO( cursor );
		}
		cursor.close();
		return result;
	}

	private Cursor getAllExpenditureTOsAsCursorForTimeWindow( Date fromTate, Date toDate )
	{
		return database.query( ExpenditureEntry.TABLE_NAME, allColumns, ExpenditureEntry.COLUMN_RECORDING_DATE + " BETWEEN ? AND ?", new String[]{
				DateUtility.convertDateToString( fromTate ), DateUtility.convertDateToString( toDate )}, null, null,
		                       ExpenditureEntry.COLUMN_RECORDING_DATE + " DESC" );
	}

	private Cursor getExpenditureTOsAsCursorForLimit( int pLimit )
	{
		return database.query( ExpenditureEntry.TABLE_NAME, allColumns, ExpenditureEntry.COLUMN_RECORDING_DATE, null, null, null, ExpenditureEntry
				.COLUMN_RECORDING_DATE + " DESC", Integer.toString( pLimit ) );
	}

	private Cursor getAllExpenditureTOsAsCursor()
	{
		return database.query( ExpenditureEntry.TABLE_NAME, allColumns, ExpenditureEntry.COLUMN_RECORDING_DATE, null, null, null, null );
	}

	/**
	 * Get all expenditures for a specific time window or the last expenditures until the limit or really all.
	 * Time window strikes limit.
	 *
	 * @param pFromDate
	 * @param pToDate
	 * @param pLimit
	 * @return
	 */
	public List<ExpenditureTO> getAllExpenditureTOs( Date pFromDate, Date pToDate, Integer pLimit )
	{
		Cursor cursor = null;
		List<ExpenditureTO> resultList = new LinkedList<ExpenditureTO>();
		if( pFromDate != null && pToDate != null )
		{
			cursor = getAllExpenditureTOsAsCursorForTimeWindow( new Date( pFromDate.getTime() ), new Date( pToDate.getTime() ) );
		}
		else if( pLimit != null )
		{
			cursor = getExpenditureTOsAsCursorForLimit( pLimit );
		}
		else
		{
			cursor = getAllExpenditureTOsAsCursor();
		}

		if( cursor != null && cursor.getCount() > 0 && cursor.moveToFirst() )
		{
			do
			{
				resultList.add( cursorToExpenditureTO( cursor ) );
			}
			while( cursor.moveToNext() );
		}
		cursor.close();
		return resultList;
	}

	private ContentValues getContentValues( ExpenditureTO expenditureTO )
	{
		ContentValues values = new ContentValues();
		values.put( ExpenditureEntry.COLUMN_AMOUNT, expenditureTO.getAmount().doubleValue() );
		values.put( ExpenditureEntry.COLUMN_RECORDING_DATE, DateUtility.convertDateToString( expenditureTO.getRecordingDate() ) );
		return values;
	}

	private ExpenditureTO cursorToExpenditureTO( Cursor cursor )
	{
		ExpenditureTO expenditureTO = new ExpenditureTO();
		expenditureTO.setUid( cursor.getLong( 0 ) );
		expenditureTO.setAmount( cursor.getFloat( 1 ) );
		expenditureTO.setRecordingDate( DateUtility.convertStringToIsoDate( cursor.getString( 2 ) ) );
		return expenditureTO;
	}

	/* Inner class that defines the table contents */
	public static abstract class ExpenditureEntry implements BaseColumns
	{
		public static final String TABLE_NAME = "t_expenditure";

		/**
		 * Amount
		 * <P>Type: REAL</P>
		 */
		public static final String COLUMN_AMOUNT = "c_amount";

		/**
		 * Amount
		 * <P>Type: INTEGER</P>
		 */
		public static final String COLUMN_RECORDING_DATE = "c_recordingdate";
	}
}
