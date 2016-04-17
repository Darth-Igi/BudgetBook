package mediajs.budgetbook.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import mediajs.budgetbook.R;
import mediajs.budgetbook.adapters.ExpenditureCardAdapter;
import mediajs.budgetbook.logic.ExpenditureLogic;
import mediajs.budgetbook.objects.fragment.AddExpenditureDialogFragment;
import mediajs.budgetbook.objects.trans.ExpenditureTO;


public class MainActivity extends AbstractBudgetBookActivity implements AddExpenditureDialogFragment.AddExpenditureDialogListener
{
	private int MAX_EXPENDITURE_COUNT = 10;
	private boolean mIsLargeLayout;
	private RecyclerView expenditureRecycler;
	private RecyclerView.LayoutManager layoutManager;
	private List<ExpenditureTO> expenditureList;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		mIsLargeLayout = getResources().getBoolean( R.bool.large_layout );

		createDrawerAndBar();

		expenditureRecycler = (RecyclerView)findViewById( R.id.rv_ExpenditureCards );
		layoutManager = new LinearLayoutManager( this );
		expenditureRecycler.setLayoutManager( layoutManager );
		expenditureRecycler.setHasFixedSize( true );

		loadAndInitExpenditures();

		bindAddExpenditureDialogButton();

		/*
		calculateExpenditureSumInMonth();
		*/
	}

	@Override
	public void onResume()
	{
		super.onResume();

		loadAndInitExpenditures();
		bindAddExpenditureDialogButton();

		/*
		calculateExpenditureSumInMonth();
		*/
	}

	@Override
	public void loadAndInitExpenditures()
	{
		ExpenditureLogic expenditureLogic = new ExpenditureLogic( this );
		expenditureList = expenditureLogic.getAllExpenditureTOs( true, null, null, MAX_EXPENDITURE_COUNT );
		ExpenditureCardAdapter expenditureCardAdapter = new ExpenditureCardAdapter( expenditureList, this );
		expenditureRecycler.setAdapter( expenditureCardAdapter );
	}

	private void bindAddExpenditureDialogButton()
	{

		FloatingActionButton openAddExpenditureDialogButton = (FloatingActionButton)findViewById( R.id.fab_openAddExpenditureDialog );
		final Context context = this;
		openAddExpenditureDialogButton.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick( View v )
					{
						FragmentManager fragmentManager = getFragmentManager();
						Fragment previousFragment = fragmentManager.findFragmentByTag( AddExpenditureDialogFragment.TAG );
						FragmentTransaction transaction = fragmentManager.beginTransaction();

						if( previousFragment != null )
						{
							transaction.remove( previousFragment );
						}
						transaction.setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN );
						transaction.addToBackStack( null );

						AddExpenditureDialogFragment addExpenditureDialogFragment = new AddExpenditureDialogFragment();
						addExpenditureDialogFragment.show( fragmentManager, AddExpenditureDialogFragment.TAG );
						/*
						if( mIsLargeLayout )
						{
							// The device is using a large layout, so show the fragment as a dialog
						}
						else
						{
							// To make it fullscreen, use the 'content' root view as the container
							// for the fragment, which is always the root view for the activity
							transaction.add( android.R.id.content, addExpenditureDialogFragment ).commit();
						}
						*/
					}
				}
		);
	}

	/*
	private void calculateExpenditureSumInMonth()
	{
		ExpenditureLogic expenditureLogic = new ExpenditureLogic( getBaseContext() );
		Calendar calendar = Calendar.getInstance();   // this takes current date
		calendar.set( Calendar.DAY_OF_MONTH, 1 );
		Date fromDate = calendar.getTime();
		int toDay = calendar.getActualMaximum( Calendar.DAY_OF_MONTH );
		calendar.set( Calendar.DAY_OF_MONTH, toDay );
		Date toDate = calendar.getTime();

		System.out.println( fromDate );
		System.out.println( toDate );

		List<ExpenditureTO> expenditureTOs = expenditureLogic.getAllExpenditureTOs( false, fromDate, toDate );
		Float totalAmount = 0f;

		for( ExpenditureTO expenditureTO : expenditureTOs )
		{
			totalAmount = totalAmount + expenditureTO.getAmount();
		}

		((TextView)findViewById( R.id.last_expenditure_sum_amount )).setText( totalAmount.toString() );
	}
*/

}
