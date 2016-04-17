package mediajs.budgetbook.objects.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mediajs.budgetbook.objects.trans.ProductTO;

/**
 * Created by Juergen on 02.03.2015.
 */
public class ProductDAO extends AbstractExpenditureComponentDAO
{
	public static final String   SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;
	public static final String   SQL_CREATE_ENTRIES =
			"CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
					ProductEntry._ID + " INTEGER PRIMARY KEY," +
					ProductEntry.COLUMN_VALUE + TEXT_TYPE + COMMA_SEP +
					ProductEntry.COLUMN_SUGGESTED + INTEGER_TYPE + COMMA_SEP +
					ProductEntry.COLUMN_FAVORITE + INTEGER_TYPE +
					" );";
	private             String[] allColumns         =
			{ProductEntry._ID, ProductEntry.COLUMN_VALUE, ProductEntry.COLUMN_SUGGESTED, ProductEntry.COLUMN_FAVORITE};

	public ProductDAO( SQLiteDatabase pDatabase )
	{
		database = pDatabase;
	}

	public ProductTO createProduct( ProductTO productTO )
	{
		ContentValues values = getContentValues( productTO );

		long insertId = database.insert( ProductEntry.TABLE_NAME, null,
		                                 values );
		ProductTO newProductTO = getProductTOByUid( insertId );
		return newProductTO;
	}

	public ProductTO updateProduct( ProductTO productTO )
	{
		ContentValues values = getContentValues( productTO );
		long updateId = database.update( ProductEntry.TABLE_NAME, values, ProductEntry._ID + "=" + productTO.getUid(), null );

		ProductTO newProductTO = getProductTOByUid( updateId );
		return newProductTO;
	}

	public void deleteProduct( ProductTO productTO )
	{
		deleteProduct( productTO.getUid() );
	}

	public void deleteProduct( long id )
	{
		database.delete( ProductEntry.TABLE_NAME, ProductEntry._ID + "=" + id, null );
	}

	public ProductTO getProductTOByUid( long uid )
	{
		Cursor cursor = database.query( ProductEntry.TABLE_NAME,
		                                allColumns, ProductEntry._ID + " = " + uid, null,
		                                null, null, null );
		ProductTO result = null;

		if( cursor.moveToFirst() )
		{
			result = cursorToProductTO( cursor );
		}
		cursor.close();
		return result;
	}

	public ProductTO getProductTOByValue( String pValue )
	{
		Cursor cursor = database.query( ProductEntry.TABLE_NAME,
		                                allColumns, "TRIM(" + ProductEntry.COLUMN_VALUE + ") = '" + pValue.trim() + "'", null,
		                                null, null, null );
		ProductTO result = null;
		if( cursor.moveToFirst() )
		{
			result = cursorToProductTO( cursor );
		}
		cursor.close();
		return result;
	}

	public Cursor getAllProductsAsCursor()
	{
		return database.query( ProductEntry.TABLE_NAME, allColumns, null, null, null, null, null );
	}

	public List<ProductTO> getAllProductTOs()
	{
		Cursor cursor;
		List<ProductTO> resultList = new LinkedList<ProductTO>();
		cursor = getAllProductsAsCursor();
		if( cursor != null && cursor.getCount() > 0 && cursor.moveToFirst() )
		{
			do
			{
				resultList.add( cursorToProductTO( cursor ) );
			}
			while( cursor.moveToNext() );
		}
		cursor.close();
		return resultList;
	}

	public Map<Long, ProductTO> getAllProductTOsAsMap()
	{
		Cursor cursor;
		Map<Long, ProductTO> resultMap = new HashMap<Long, ProductTO>();
		cursor = getAllProductsAsCursor();
		if( cursor != null && cursor.getCount() > 0 && cursor.moveToFirst() )
		{
			do
			{
				ProductTO productTO = cursorToProductTO( cursor );
				resultMap.put( productTO.getUid(), productTO );
			}
			while( cursor.moveToNext() );
		}
		cursor.close();
		return resultMap;
	}

	private ContentValues getContentValues( ProductTO productTO )
	{
		ContentValues values = new ContentValues();
		values.put( ProductEntry.COLUMN_VALUE, productTO.getValue() );
		values.put( ProductEntry.COLUMN_FAVORITE, productTO.isFavorite() ? 1 : 0 );
		return values;
	}

	private ProductTO cursorToProductTO( Cursor cursor )
	{
		ProductTO productTO = new ProductTO();
		productTO.setUid( cursor.getLong( 0 ) );
		productTO.setValue( cursor.getString( 1 ) );
		productTO.setSuggested( cursor.getInt( 2 ) > 0 );
		productTO.setFavorite( cursor.getInt( 3 ) > 0 );
		return productTO;
	}

	/* Inner class that defines the table contents */
	public static abstract class ProductEntry implements IExpenditureComponentEntry
	{
		public static final String TABLE_NAME = "t_product";
	}
}
