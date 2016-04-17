package mediajs.budgetbook.objects.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mediajs.budgetbook.objects.trans.SourceOfFundsTO;

/**
 * Created by Juergen on 02.03.2015.
 */
public class SourceOfFundsDAO extends AbstractExpenditureComponentDAO
{
	public static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + SourceOfFundsEntry.TABLE_NAME + " (" +
					SourceOfFundsEntry._ID + " INTEGER PRIMARY KEY," +
					SourceOfFundsEntry.COLUMN_VALUE + TEXT_TYPE + COMMA_SEP +
					SourceOfFundsEntry.COLUMN_SUGGESTED + INTEGER_TYPE + COMMA_SEP +
					SourceOfFundsEntry.COLUMN_FAVORITE + INTEGER_TYPE +
					" );";

	public static final String   SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + SourceOfFundsEntry.TABLE_NAME;
	private             String[] allColumns         =
			{SourceOfFundsEntry._ID, SourceOfFundsEntry.COLUMN_VALUE, SourceOfFundsEntry.COLUMN_SUGGESTED, SourceOfFundsEntry
					.COLUMN_FAVORITE};

	public SourceOfFundsDAO( SQLiteDatabase pDatabase )
	{
		database = pDatabase;
	}

	public SourceOfFundsTO createSourceOfFunds( SourceOfFundsTO sourceOfFundsTO )
	{
		ContentValues values = getContentValues( sourceOfFundsTO );

		long insertId = database.insert( SourceOfFundsEntry.TABLE_NAME, null,
		                                 values );
		SourceOfFundsTO newSourceOfFundsTO = getSourceOfFundsTOByUid( insertId );
		return newSourceOfFundsTO;
	}

	public SourceOfFundsTO updateSourceOfFunds( SourceOfFundsTO sourceOfFundsTO )
	{
		ContentValues values = getContentValues( sourceOfFundsTO );
		long updateId = database.update( SourceOfFundsEntry.TABLE_NAME, values, SourceOfFundsEntry._ID + "=" + sourceOfFundsTO.getUid(), null );

		SourceOfFundsTO newSourceOfFundsTO = getSourceOfFundsTOByUid( updateId );
		return newSourceOfFundsTO;
	}

	public void deleteSourceOfFunds( SourceOfFundsTO sourceOfFundsTO )
	{
		deleteSourceOfFunds( sourceOfFundsTO.getUid() );
	}

	public void deleteSourceOfFunds( long id )
	{
		database.delete( SourceOfFundsEntry.TABLE_NAME, SourceOfFundsEntry._ID + "=" + id, null );
	}

	public SourceOfFundsTO getSourceOfFundsTOByUid( long uid )
	{
		Cursor cursor = database.query( SourceOfFundsEntry.TABLE_NAME,
		                                allColumns, SourceOfFundsEntry._ID + " = " + uid, null,
		                                null, null, null );
		SourceOfFundsTO result = null;

		if( cursor.moveToFirst() )
		{
			result = cursorToSourceOfFundsTO( cursor );
		}
		cursor.close();
		return result;
	}

	public SourceOfFundsTO getSourceOfFundsTOByValue( String pValue )
	{
		Cursor cursor = database.query( SourceOfFundsEntry.TABLE_NAME,
		                                allColumns, "TRIM(" + SourceOfFundsEntry.COLUMN_VALUE + ") = '" + pValue.trim() + "'", null,
		                                null, null, null );
		SourceOfFundsTO result = null;

		if( cursor.moveToFirst() )
		{
			result = cursorToSourceOfFundsTO( cursor );
		}
		cursor.close();
		return result;
	}

	public Cursor getAllSourceOfFundsTOsAsCursor()
	{
		return database.query( SourceOfFundsEntry.TABLE_NAME, allColumns, null, null, null, null, null );
	}

	public List<SourceOfFundsTO> getAllSourceOfFundsTOs()
	{
		Cursor cursor;
		List<SourceOfFundsTO> resultList = new LinkedList<SourceOfFundsTO>();
		cursor = getAllSourceOfFundsTOsAsCursor();
		if( cursor != null && cursor.getCount() > 0 && cursor.moveToFirst() )
		{
			do
			{
				resultList.add( cursorToSourceOfFundsTO( cursor ) );
			}
			while( cursor.moveToNext() );
		}
		cursor.close();
		return resultList;
	}

	public Map<Long, SourceOfFundsTO> getAllSourceOfFundsTOsAsMap()
	{
		Cursor cursor;
		Map<Long, SourceOfFundsTO> resultMap = new HashMap<Long, SourceOfFundsTO>();
		cursor = getAllSourceOfFundsTOsAsCursor();
		if( cursor != null && cursor.getCount() > 0 && cursor.moveToFirst() )
		{
			do
			{
				SourceOfFundsTO sourceOfFundsTO = cursorToSourceOfFundsTO( cursor );
				resultMap.put( sourceOfFundsTO.getUid(), sourceOfFundsTO );
			}
			while( cursor.moveToNext() );
		}
		cursor.close();
		return resultMap;
	}

	private ContentValues getContentValues( SourceOfFundsTO sourceOfFundsTO )
	{
		ContentValues values = new ContentValues();
		values.put( SourceOfFundsEntry.COLUMN_VALUE, sourceOfFundsTO.getValue() );
		values.put( SourceOfFundsEntry.COLUMN_FAVORITE, sourceOfFundsTO.isFavorite() ? 1 : 0 );
		return values;
	}

	private SourceOfFundsTO cursorToSourceOfFundsTO( Cursor cursor )
	{
		SourceOfFundsTO sourceOfFundsTO = new SourceOfFundsTO();
		sourceOfFundsTO.setUid( cursor.getLong( 0 ) );
		sourceOfFundsTO.setValue( cursor.getString( 1 ) );
		sourceOfFundsTO.setSuggested( cursor.getInt( 2 ) > 0 );
		sourceOfFundsTO.setFavorite( cursor.getInt( 3 ) > 0 );
		return sourceOfFundsTO;
	}

	/* Inner class that defines the table contents */
	public static abstract class SourceOfFundsEntry implements IExpenditureComponentEntry
	{
		public static final String TABLE_NAME = "t_sourceoffunds";
	}
}
