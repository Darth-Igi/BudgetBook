package mediajs.budgetbook.logic;

import android.content.Context;

import java.util.List;

import mediajs.budgetbook.objects.entity.BudgetBookDbHelper;
import mediajs.budgetbook.objects.entity.ProductDAO;
import mediajs.budgetbook.objects.trans.ProductTO;

/**
 * Created by Juergen on 15.06.2015.
 */
public class ProductLogic extends BaseLogic
{
	public ProductLogic( Context context )
	{
		dbHelper = new BudgetBookDbHelper( context );
	}

	public ProductTO getProductTOByUid( long uid )
	{
		databaseOpen();
			ProductDAO productDAO = new ProductDAO( database );
			ProductTO productTOByUid = productDAO.getProductTOByUid( uid );
		databaseClose();
		return productTOByUid;
	}

	public ProductTO getProductTOByValue( String pValue )
	{
		databaseOpen();
			ProductDAO productDAO = new ProductDAO( database );
			ProductTO productTOByUid = productDAO.getProductTOByValue( pValue );
		databaseClose();
		return productTOByUid;
	}

	public List<ProductTO> getAllProductTOs()
	{
		databaseOpen();
			ProductDAO productDAO = new ProductDAO( database );
			List<ProductTO> allProductTOs = productDAO.getAllProductTOs();
		databaseClose();
		return allProductTOs;
	}

}
