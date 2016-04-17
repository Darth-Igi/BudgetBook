package mediajs.budgetbook.objects.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mediajs.budgetbook.objects.trans.AbstractExpenditureComponentTO;
import mediajs.budgetbook.objects.trans.ComponentRelationTO;
import mediajs.budgetbook.objects.trans.ExpenditureTO;
import mediajs.budgetbook.objects.trans.ProductTO;
import mediajs.budgetbook.objects.trans.SourceOfFundsTO;
import mediajs.budgetbook.objects.trans.StoreTO;
import mediajs.budgetbook.objects.type.ExpenditureComponentType;

/**
 * Created by Juergen on 03.03.2015.
 */
public class ComponentRelationDAO
{
	private              SQLiteDatabase database     = null;
	private static final String         INTEGER_TYPE = " INTEGER";
	private static final String         COMMA_SEP    = ",";

	public static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + ComponentRelationEntry.TABLE_NAME + " (" +
					ComponentRelationEntry._ID + " INTEGER PRIMARY KEY," +
					ComponentRelationEntry.COLUMN_EXPENDITURE_UID + INTEGER_TYPE + COMMA_SEP +
					ComponentRelationEntry.COLUMN_COMPONENT_UID + INTEGER_TYPE + COMMA_SEP +
					ComponentRelationEntry.COLUMN_COMPONENT_TYPE + INTEGER_TYPE + COMMA_SEP +
					ComponentRelationEntry.COLUMN_SUGGESTED + INTEGER_TYPE + COMMA_SEP +
					"FOREIGN KEY( " + ComponentRelationEntry.COLUMN_EXPENDITURE_UID + " ) REFERENCES " + ExpenditureDAO.ExpenditureEntry.TABLE_NAME + "( " +
					ExpenditureDAO.ExpenditureEntry._ID + " ) );";

	public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ComponentRelationEntry.TABLE_NAME;

	private String[] allColumns =
			{ComponentRelationEntry._ID, ComponentRelationEntry.COLUMN_EXPENDITURE_UID, ComponentRelationEntry.COLUMN_COMPONENT_UID,
					ComponentRelationEntry.COLUMN_COMPONENT_TYPE, ComponentRelationEntry.COLUMN_SUGGESTED};

	public ComponentRelationDAO( SQLiteDatabase pDatabase )
	{
		database = pDatabase;
	}

	public ComponentRelationTO createComponentRelation( AbstractExpenditureComponentTO abstractExpenditureComponentTO, ExpenditureTO expenditureTO )
	{
		ContentValues values = getContentValues( abstractExpenditureComponentTO, expenditureTO );

		long insertId = database.insert( ComponentRelationEntry.TABLE_NAME, null, values );
		ComponentRelationTO componentRelationTO = getComponentRelationTOByUid( insertId );
		return componentRelationTO;
	}

	public void updateComponentRelation( AbstractExpenditureComponentTO abstractExpenditureComponentTO, ExpenditureTO expenditureTO )
	{
		ContentValues values = getContentValues( abstractExpenditureComponentTO, expenditureTO );
		database.update( ComponentRelationEntry.TABLE_NAME, values, ComponentRelationEntry.COLUMN_COMPONENT_UID + "=" + abstractExpenditureComponentTO
				.getUid() + " AND " + ComponentRelationEntry.COLUMN_EXPENDITURE_UID + "=" + expenditureTO.getUid(), null );
	}

	public void deleteComponentRelation( AbstractExpenditureComponentTO abstractExpenditureComponentTO, ExpenditureTO expenditureTO )
	{
		deleteComponentRelation( abstractExpenditureComponentTO.getUid(), expenditureTO.getUid() );
	}

	public void deleteComponentRelation( long componentUid, long expenditureUid )
	{
		database.delete( ComponentRelationEntry.TABLE_NAME, ComponentRelationEntry.COLUMN_COMPONENT_UID + "=" + componentUid + " AND " +
				ComponentRelationEntry.COLUMN_EXPENDITURE_UID + "=" + expenditureUid, null );
	}

	public List<ComponentRelationTO> getComponentRelationTOsByExpenditureUid( long expenditureUid )
	{
		Cursor cursor = database.query( ComponentRelationEntry.TABLE_NAME,
		                                allColumns, ComponentRelationEntry.COLUMN_EXPENDITURE_UID + " = " + expenditureUid, null,
		                                null, null, null );
		List<ComponentRelationTO> resultList = new LinkedList<ComponentRelationTO>();
		if( cursor != null && cursor.getCount() > 0 && cursor.moveToFirst() )
		{
			do
			{
				resultList.add( cursorToComponentRelationTO( cursor ) );
			}
			while( cursor.moveToNext() );
		}
		cursor.close();
		return resultList;
	}

	/**
	 * @return a Map of ExpenditureUids and a Map of ExpenditureComponentTypes and ComponentRelationTOs
	 */
	public Map<Long, Map<ExpenditureComponentType, ComponentRelationTO>> getComponentRelationTOsMap()
	{
		ComponentRelationTO tempComponentRelationTO = null;
		Cursor cursor = database.query( ComponentRelationEntry.TABLE_NAME, allColumns, null, null, null, null, null );
		Map<Long, Map<ExpenditureComponentType, ComponentRelationTO>> resultMap = new HashMap<>();
		if( cursor != null && cursor.getCount() > 0 && cursor.moveToFirst() )
		{
			do
			{
				tempComponentRelationTO = cursorToComponentRelationTO( cursor );

				ComponentRelationTO componentRelationTO = new ComponentRelationTO();
				componentRelationTO.setUid( tempComponentRelationTO.getUid() );
				componentRelationTO.setComponentType( tempComponentRelationTO.getComponentType() );
				componentRelationTO.setComponentUid( tempComponentRelationTO.getComponentUid() );
				componentRelationTO.setExpenditureUid( tempComponentRelationTO.getExpenditureUid() );
				componentRelationTO.setSuggested( tempComponentRelationTO.isSuggested() );

				if( resultMap.containsKey( componentRelationTO.getExpenditureUid() ) )
				{
					Map<ExpenditureComponentType, ComponentRelationTO> innerMap = resultMap.get( componentRelationTO.getExpenditureUid() );
					innerMap.put( componentRelationTO.getComponentType(), componentRelationTO );
				}
				else
				{
					HashMap<ExpenditureComponentType, ComponentRelationTO> innerMap = new HashMap<>();
					innerMap.put( componentRelationTO.getComponentType(), componentRelationTO );
					resultMap.put( componentRelationTO.getExpenditureUid(), innerMap );
				}
			}
			while( cursor.moveToNext() );
		}
		cursor.close();
		return resultMap;
	}

	public Cursor getAllComponentRelationTOsAsCursor()
	{
		return database.query( ComponentRelationEntry.TABLE_NAME, allColumns, null, null, null, null, null );
	}

	public long getExpenditureUidByComponentUidAndComponentType( long componentUid, ExpenditureComponentType type )
	{
		long result = -1l;

		Cursor cursor = database.query( ComponentRelationEntry.TABLE_NAME,
		                                allColumns, ComponentRelationEntry.COLUMN_COMPONENT_UID + " = " + componentUid + " AND " +
				                                ComponentRelationEntry.COLUMN_COMPONENT_TYPE + "=" + type.getId(), null,
		                                null, null, null );

		if( cursor.moveToFirst() )
		{
			result = cursor.getLong( 1 );
		}
		cursor.close();
		return result;
	}

	public ComponentRelationTO getComponentRelationTOByUid( long pUid )
	{
		Cursor cursor = database.query( ComponentRelationEntry.TABLE_NAME, allColumns, ComponentRelationEntry._ID + " = " + pUid, null, null, null,
		                                null );
		ComponentRelationTO componentRelationTO = null;

		if( cursor.moveToFirst() )
		{
			componentRelationTO = cursorToComponentRelationTO( cursor );
		}
		cursor.close();
		return componentRelationTO;
	}

	private ContentValues getContentValues( AbstractExpenditureComponentTO abstractExpenditureComponentTO, ExpenditureTO expenditureTO )
	{
		ContentValues values = new ContentValues();
		values.put( ComponentRelationEntry.COLUMN_EXPENDITURE_UID, expenditureTO.getUid() );
		values.put( ComponentRelationEntry.COLUMN_COMPONENT_UID, abstractExpenditureComponentTO.getUid() );
		values.put( ComponentRelationEntry.COLUMN_SUGGESTED, abstractExpenditureComponentTO.isSuggested() ? 1 : 0 );

		ExpenditureComponentType componentType = ExpenditureComponentType.STORE;
		if( ProductTO.class.isInstance( abstractExpenditureComponentTO ) )
		{
			componentType = ExpenditureComponentType.PRODUCT;
		}
		if( StoreTO.class.isInstance( abstractExpenditureComponentTO ) )
		{
			componentType = ExpenditureComponentType.STORE;
		}
		if( SourceOfFundsTO.class.isInstance( abstractExpenditureComponentTO ) )
		{
			componentType = ExpenditureComponentType.SOURCE_OF_FUNDS;
		}
		values.put( ComponentRelationEntry.COLUMN_COMPONENT_TYPE, componentType.getId() );

		return values;
	}

	private ComponentRelationTO cursorToComponentRelationTO( Cursor cursor )
	{
		ComponentRelationTO componentRelationTO = new ComponentRelationTO();
		componentRelationTO.setUid( cursor.getLong( 0 ) );
		componentRelationTO.setExpenditureUid( cursor.getLong( 1 ) );
		componentRelationTO.setComponentUid( cursor.getLong( 2 ) );
		componentRelationTO.setComponentType( ExpenditureComponentType.getById( cursor.getLong( 3 ) ) );
		componentRelationTO.setSuggested( cursor.getLong( 4 ) > 0 );
		return componentRelationTO;
	}

	/* Inner class that defines the table contents */
	public static abstract class ComponentRelationEntry implements BaseColumns
	{
		public static final String TABLE_NAME             = "t_componenterelation";
		/**
		 * UID of the Expenditure
		 * <P>Type: INTEGER</P>
		 */
		public static final String COLUMN_EXPENDITURE_UID = "c_expenditureuid";

		/**
		 * UID of the Component
		 * <P>Type: INTEGER</P>
		 */
		public static final String COLUMN_COMPONENT_UID = "c_componentuid";

		/**
		 * Component-Type
		 * <P>Type: INTEGER</P>
		 */
		public static final String COLUMN_COMPONENT_TYPE = "c_componenttype";

		/**
		 * Defines, if this component value is suggested
		 * <P>Type: INTEGER</P>
		 */
		public static final String COLUMN_SUGGESTED = "c_suggested";
	}
}
