package mediajs.budgetbook.logic;

import android.content.Context;
import android.text.TextUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mediajs.budgetbook.objects.entity.BudgetBookDbHelper;
import mediajs.budgetbook.objects.entity.ComponentRelationDAO;
import mediajs.budgetbook.objects.entity.ExpenditureDAO;
import mediajs.budgetbook.objects.entity.ProductDAO;
import mediajs.budgetbook.objects.entity.SourceOfFundsDAO;
import mediajs.budgetbook.objects.entity.StoreDAO;
import mediajs.budgetbook.objects.trans.ComponentRelationTO;
import mediajs.budgetbook.objects.trans.ExpenditureTO;
import mediajs.budgetbook.objects.trans.ProductTO;
import mediajs.budgetbook.objects.trans.SourceOfFundsTO;
import mediajs.budgetbook.objects.trans.StoreTO;
import mediajs.budgetbook.objects.type.ExpenditureComponentType;

/**
 * Created by Juergen on 04.03.2015.
 */
public class ExpenditureLogic extends BaseLogic
{
	private Context context;

	public ExpenditureLogic( Context pContext )
	{
		this.context = pContext;
		dbHelper = new BudgetBookDbHelper( context );
	}

	public ExpenditureTO getExpenditureTOByUid( long uid )
	{
		ExpenditureTO result = null;
		databaseOpen();
		{
			ExpenditureDAO expenditureDAO = new ExpenditureDAO( database );
			result = expenditureDAO.getExpenditureTOByUid( uid );

			List<ComponentRelationTO> componentList = new LinkedList<ComponentRelationTO>();
			ComponentRelationDAO componentRelationDAO = new ComponentRelationDAO( database );
			componentList = componentRelationDAO.getComponentRelationTOsByExpenditureUid( result.getUid() );

			for( ComponentRelationTO component : componentList )
			{
				switch( component.getComponentType() )
				{
					case PRODUCT:
					{
						ProductDAO productDAO = new ProductDAO( database );
						ProductTO product = productDAO.getProductTOByUid( component.getComponentUid() );
						product.setSuggested( component.isSuggested() );
						result.setProductTO( product );
						break;
					}
					case STORE:
					{
						StoreDAO storeDAO = new StoreDAO( database );
						StoreTO store = storeDAO.getStoreTOByUid( component.getComponentUid() );
						store.setSuggested( component.isSuggested() );
						result.setStoreTO( store );
						break;
					}
					case SOURCE_OF_FUNDS:
					{
						SourceOfFundsDAO sourceOfFundsDAO = new SourceOfFundsDAO( database );
						SourceOfFundsTO sourceOfFunds = sourceOfFundsDAO.getSourceOfFundsTOByUid( component.getComponentUid() );
						sourceOfFunds.setSuggested( component.isSuggested() );
						result.setSourceOfFundsTO( sourceOfFunds );
						break;
					}
					default:
					{
						// nothing
					}
				}
			}
		}
		databaseClose();

		return result;
	}

	/**
	 * Get all expenditures for a specific time window or the last expenditures until the limit or really all.
	 * Time window strikes limit.
	 *
	 * @param withComponents
	 * @param pFromDate
	 * @param pToDate
	 * @param pLimit
	 * @return
	 */
	public List<ExpenditureTO> getAllExpenditureTOs( boolean withComponents, Date pFromDate, Date pToDate, Integer pLimit )
	{
		databaseOpen();
		ExpenditureDAO expenditureDAO = new ExpenditureDAO( database );
		List<ExpenditureTO> resultList = expenditureDAO.getAllExpenditureTOs( pFromDate, pToDate, pLimit );
		databaseClose();

		if( withComponents )
		{
			databaseOpen();
				ComponentRelationDAO componentRelationDAO = new ComponentRelationDAO( database );
				ProductDAO productDAO = new ProductDAO( database );
				SourceOfFundsDAO sourceOfFundsDAO = new SourceOfFundsDAO( database );
				StoreDAO storeDAO = new StoreDAO( database );

				Map<Long, Map<ExpenditureComponentType, ComponentRelationTO>> componentRelationTOMap = componentRelationDAO.getComponentRelationTOsMap();
				Map<Long, ProductTO> productTOMap = null;
				Map<Long, SourceOfFundsTO> sourceOfFundsTOMap = null;
				Map<Long, StoreTO> storeTOMap = null;
			  if( !componentRelationTOMap.isEmpty() )
			  {
				  productTOMap = productDAO.getAllProductTOsAsMap();
				  sourceOfFundsTOMap = sourceOfFundsDAO.getAllSourceOfFundsTOsAsMap();
				  storeTOMap = storeDAO.getAllStoreTOsAsMap();
			  }
			databaseClose();

			if( !componentRelationTOMap.isEmpty() )
			{
				for( ExpenditureTO expenditureTO : resultList )
				{
					Map<ExpenditureComponentType, ComponentRelationTO> componentRelationTO = componentRelationTOMap.get( expenditureTO.getUid() );
					if( componentRelationTO != null )
					{
						ComponentRelationTO componentRelationTOProduct = componentRelationTO.get( ExpenditureComponentType.PRODUCT );
						if( componentRelationTOProduct != null )
						{
							expenditureTO.setProductTO( productTOMap.get( componentRelationTOProduct.getComponentUid() ) );
						}
						ComponentRelationTO componentRelationTOSourceOfFunds = componentRelationTO.get( ExpenditureComponentType.SOURCE_OF_FUNDS );
						if( componentRelationTOSourceOfFunds != null )
						{
							expenditureTO.setSourceOfFundsTO( sourceOfFundsTOMap.get( componentRelationTOSourceOfFunds.getComponentUid() ) );
						}
						ComponentRelationTO componentRelationTOStore = componentRelationTO.get( ExpenditureComponentType.STORE );
						if( componentRelationTOStore != null )
						{
							expenditureTO.setStoreTO( storeTOMap.get( componentRelationTOStore.getComponentUid() ) );
						}
					}
				}
			}
		}
		return resultList;
	}

	public int createNewExpenditure( Float pAmount, String pProduct, String pStore, String pSourceOfFunds )
	{
		ProductTO productTO = null;
		if( !TextUtils.isEmpty( pProduct ) )
		{
			ProductLogic productLogic = new ProductLogic( context );
			productTO = productLogic.getProductTOByValue( pProduct );
			if( productTO == null )
			{
				productTO = new ProductTO( null, pProduct );
			}
		}

		StoreTO storeTO = null;
		if( !TextUtils.isEmpty( pStore ) )
		{
			StoreLogic storeLogic = new StoreLogic( context );
			storeTO = storeLogic.getStoreTOByValue( pStore );
			if( storeTO == null )
			{
				storeTO = new StoreTO( null, pStore );
			}
		}

		SourceOfFundsTO sourceOfFundsTO = null;
		if( !TextUtils.isEmpty( pSourceOfFunds ) )
		{
			SourceOfFundsLogic sourceOfFundsLogic = new SourceOfFundsLogic( context );
			sourceOfFundsTO = sourceOfFundsLogic.getSourceOfFundsTOByValue( pSourceOfFunds );
			if( sourceOfFundsTO == null )
			{
				sourceOfFundsTO = new SourceOfFundsTO( null, pSourceOfFunds );
			}
		}

		ExpenditureTO result = null;

		ExpenditureTO expenditureTO = new ExpenditureTO( null, pAmount, productTO, new Date(), storeTO, sourceOfFundsTO );
		databaseOpen();
		{
			ComponentRelationDAO componentRelationDAO = new ComponentRelationDAO( database );
			ExpenditureDAO expenditureDAO = new ExpenditureDAO( database );
			result = expenditureDAO.createExpenditure( expenditureTO );

			if( productTO != null )
			{
				if( productTO.getUid() == null )
				{
					ProductDAO productDAO = new ProductDAO( database );
					productTO = productDAO.createProduct( productTO );
				}
				componentRelationDAO.createComponentRelation( productTO, result );
			}

			if( storeTO != null )
			{
				if( storeTO.getUid() == null )
				{
					StoreDAO storeDAO = new StoreDAO( database );
					storeTO = storeDAO.createStore( storeTO );
				}
				componentRelationDAO.createComponentRelation( storeTO, result );
			}

			if( sourceOfFundsTO != null )
			{
				if( sourceOfFundsTO.getUid() == null )
				{
					SourceOfFundsDAO sourceOfFundsDAO = new SourceOfFundsDAO( database );
					sourceOfFundsTO = sourceOfFundsDAO.createSourceOfFunds( sourceOfFundsTO );
				}
				componentRelationDAO.createComponentRelation( sourceOfFundsTO, result );
			}
		}
		databaseClose();
		if( result != null )
		{
			return -1;
		}
		return 0;
	}

}
