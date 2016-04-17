package mediajs.budgetbook.objects.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mediajs.budgetbook.objects.trans.StoreTO;

/**
 * Created by Juergen on 02.03.2015.
 */
public class StoreDAO extends AbstractExpenditureComponentDAO
{
	private static final String REAL_TYPE = " REAL";

	public static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + StoreEntry.TABLE_NAME + " (" +
					StoreEntry._ID + " INTEGER PRIMARY KEY," +
					StoreEntry.COLUMN_VALUE + TEXT_TYPE + COMMA_SEP +
					StoreEntry.COLUMN_LATITUDE + REAL_TYPE + COMMA_SEP +
					StoreEntry.COLUMN_LONGITUDE + REAL_TYPE + COMMA_SEP +
					StoreEntry.COLUMN_SUGGESTED + INTEGER_TYPE + COMMA_SEP +
					StoreEntry.COLUMN_FAVORITE + INTEGER_TYPE +
					" );";

	public static final String   SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + StoreEntry.TABLE_NAME;
	private             String[] allColumns         =
			{StoreEntry._ID, StoreEntry.COLUMN_VALUE, StoreEntry.COLUMN_SUGGESTED, StoreEntry.COLUMN_FAVORITE, StoreEntry
					.COLUMN_LATITUDE, StoreEntry.COLUMN_LONGITUDE};

	public StoreDAO( SQLiteDatabase pDatabase )
	{
		database = pDatabase;
	}

	public StoreTO createStore( StoreTO StoreTO )
	{
		ContentValues values = getContentValues( StoreTO );

		long insertId = database.insert( StoreEntry.TABLE_NAME, null, values );
		StoreTO newStoreTO = getStoreTOByUid( insertId );
		return newStoreTO;
	}

	public StoreTO updateStore( StoreTO StoreTO )
	{
		ContentValues values = getContentValues( StoreTO );
		long updateId = database.update( StoreEntry.TABLE_NAME, values, StoreEntry._ID + "=" + StoreTO.getUid(), null );

		StoreTO newStoreTO = getStoreTOByUid( updateId );
		return newStoreTO;
	}

	public void deleteStore( StoreTO StoreTO )
	{
		deleteStore( StoreTO.getUid() );
	}

	public void deleteStore( long id )
	{
		database.delete( StoreEntry.TABLE_NAME, StoreEntry._ID + "=" + id, null );
	}

	public StoreTO getStoreTOByUid( long uid )
	{
		Cursor cursor = database.query( StoreEntry.TABLE_NAME,
		                                allColumns, StoreEntry._ID + " = " + uid, null,
		                                null, null, null );
		StoreTO result = null;

		if( cursor.moveToFirst() )
		{
			result = cursorToStoreTO( cursor );
		}
		cursor.close();
		return result;
	}

	public StoreTO getStoreTOByValue( String pValue )
	{
		Cursor cursor = database.query( StoreEntry.TABLE_NAME,
		                                allColumns, "TRIM(" + StoreEntry.COLUMN_VALUE + ") = '" + pValue.trim() + "'", null,
		                                null, null, null );
		StoreTO result = null;

		if( cursor.moveToFirst() )
		{
			result = cursorToStoreTO( cursor );
		}
		cursor.close();
		return result;
	}

	public Cursor getAllStoreTOsAsCursor()
	{
		return database.query( StoreEntry.TABLE_NAME, allColumns, null, null, null, null, null );
	}

	public List<StoreTO> getAllStoreTOs()
	{
		Cursor cursor;
		List<StoreTO> resultList = new LinkedList<StoreTO>();
		cursor = getAllStoreTOsAsCursor();
		if( cursor != null && cursor.getCount() > 0 && cursor.moveToFirst() )
		{
			do
			{
				resultList.add( cursorToStoreTO( cursor ) );
			}
			while( cursor.moveToNext() );
		}
		cursor.close();
		return resultList;
	}

	public Map<Long, StoreTO> getAllStoreTOsAsMap()
	{
		Cursor cursor;
		Map<Long, StoreTO> resultMap = new HashMap<Long, StoreTO>();
		cursor = getAllStoreTOsAsCursor();
		if( cursor != null && cursor.getCount() > 0 && cursor.moveToFirst() )
		{
			do
			{
				StoreTO storeTO = cursorToStoreTO( cursor );
				resultMap.put( storeTO.getUid(), storeTO );
			}
			while( cursor.moveToNext() );
		}
		cursor.close();
		return resultMap;
	}

	private ContentValues getContentValues( StoreTO StoreTO )
	{
		ContentValues values = new ContentValues();
		values.put( StoreEntry.COLUMN_VALUE, StoreTO.getValue() );
		values.put( StoreEntry.COLUMN_FAVORITE, StoreTO.isFavorite() ? 1 : 0 );
		values.put( StoreEntry.COLUMN_LATITUDE, StoreTO.getLatitude() );
		values.put( StoreEntry.COLUMN_LONGITUDE, StoreTO.getLongitude() );
		return values;
	}

	private StoreTO cursorToStoreTO( Cursor cursor )
	{
		StoreTO StoreTO = new StoreTO();
		StoreTO.setUid( cursor.getLong( 0 ) );
		StoreTO.setValue( cursor.getString( 1 ) );
		StoreTO.setSuggested( cursor.getInt( 2 ) > 0 );
		StoreTO.setFavorite( cursor.getInt( 3 ) > 0 );
		StoreTO.setLatitude( cursor.getDouble( 4 ) );
		StoreTO.setLongitude( cursor.getDouble( 5 ) );
		return StoreTO;
	}

	/* Inner class that defines the table contents */
	public static abstract class StoreEntry implements IExpenditureComponentEntry
	{
		public static final String TABLE_NAME = "t_store";

		/**
		 * The Latitude
		 * <P>Type: REAL</P>
		 */
		public static final String COLUMN_LATITUDE = "c_latitude";

		/**
		 * The Longitude
		 * <P>Type: REAL</P>
		 */
		public static final String COLUMN_LONGITUDE = "c_longitude";
	}
}
