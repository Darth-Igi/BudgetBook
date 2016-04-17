package mediajs.budgetbook.logic;

import android.content.Context;

import java.util.List;

import mediajs.budgetbook.objects.entity.BudgetBookDbHelper;
import mediajs.budgetbook.objects.entity.SourceOfFundsDAO;
import mediajs.budgetbook.objects.trans.SourceOfFundsTO;

/**
 * Created by Juergen on 15.06.2015.
 */
public class SourceOfFundsLogic extends BaseLogic
{
	public SourceOfFundsLogic( Context context )
	{
		dbHelper = new BudgetBookDbHelper( context );
	}

	public SourceOfFundsTO getSourceOfFundsTOByUid( long uid )
	{
		databaseOpen();
			SourceOfFundsDAO sourceOfFundsDAO = new SourceOfFundsDAO( database );
			SourceOfFundsTO sourceOfFundsTOByUid = sourceOfFundsDAO.getSourceOfFundsTOByUid( uid );
		databaseClose();
		return sourceOfFundsTOByUid;
	}

	public SourceOfFundsTO getSourceOfFundsTOByValue( String pValue )
	{
		databaseOpen();
			SourceOfFundsDAO sourceOfFundsDAO = new SourceOfFundsDAO( database );
			SourceOfFundsTO sourceOfFundsTOByUid = sourceOfFundsDAO.getSourceOfFundsTOByValue( pValue );
		databaseClose();
		return sourceOfFundsTOByUid;
	}

	public List<SourceOfFundsTO> getAllSourceOfFundsTOs()
	{
		databaseOpen();
			SourceOfFundsDAO sourceOfFundsDAO = new SourceOfFundsDAO( database );
			List<SourceOfFundsTO> allSourceOfFundsTOs = sourceOfFundsDAO.getAllSourceOfFundsTOs();
		databaseClose();
		return allSourceOfFundsTOs;
	}
}
