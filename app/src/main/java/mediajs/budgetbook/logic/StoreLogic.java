package mediajs.budgetbook.logic;

import android.content.Context;

import java.util.List;

import mediajs.budgetbook.objects.entity.BudgetBookDbHelper;
import mediajs.budgetbook.objects.entity.StoreDAO;
import mediajs.budgetbook.objects.trans.StoreTO;

/**
 * Created by Juergen on 15.06.2015.
 */
public class StoreLogic extends BaseLogic
{
	public StoreLogic( Context context )
	{
		dbHelper = new BudgetBookDbHelper( context );
	}

	public StoreTO getStoreTOByUid( long uid )
	{
		databaseOpen();
			StoreDAO storeDAO = new StoreDAO( database );
			StoreTO storeTOByUid = storeDAO.getStoreTOByUid( uid );
		databaseClose();
		return storeTOByUid;
	}

	public StoreTO getStoreTOByValue( String pValue )
	{
		databaseOpen();
			StoreDAO storeDAO = new StoreDAO( database );
			StoreTO storeTOByUid = storeDAO.getStoreTOByValue( pValue );
		databaseClose();
		return storeTOByUid;
	}

	public List<StoreTO> getAllStoreTOs()
	{
		databaseOpen();
			StoreDAO storeDAO = new StoreDAO( database );
			List<StoreTO> allStoreTOs = storeDAO.getAllStoreTOs();
		databaseClose();
		return allStoreTOs;
	}
}
