package mediajs.budgetbook.objects.fragment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import mediajs.budgetbook.R;
import mediajs.budgetbook.logic.ExpenditureLogic;
import mediajs.budgetbook.logic.ProductLogic;
import mediajs.budgetbook.logic.SourceOfFundsLogic;
import mediajs.budgetbook.logic.StoreLogic;
import mediajs.budgetbook.objects.trans.ProductTO;
import mediajs.budgetbook.objects.trans.SourceOfFundsTO;
import mediajs.budgetbook.objects.trans.StoreTO;

/**
 * A simple {@link DialogFragment} subclass.
 */
public class AddExpenditureDialogFragment extends DialogFragment
{
	public static final String TAG = "add_expenditure_dialog_fragment";
	private static final int RESULT_OK = -1;

	private Toolbar toolbar;

	private View dialogView;

	private AddExpenditureDialogListener activity;

	public interface AddExpenditureDialogListener
	{
		void loadAndInitExpenditures();
	}

	public AddExpenditureDialogFragment()
	{
		// Required empty public constructor
	}


	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState )
	{
		dialogView = inflater.inflate( R.layout.fragment_add_expenditure_dialog, container, false );
		activity = (AddExpenditureDialogListener)getActivity();
		getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );

		bindAmountEditText( dialogView );

		setupAutocomplete( dialogView );

		toolbar = (Toolbar)dialogView.findViewById( R.id.expenditure_dialog_tool_bar );
		toolbar.setNavigationIcon( R.drawable.ic_close_white_24dp );
		toolbar.setNavigationContentDescription( R.string.dict_close );
		toolbar.setPadding( 0, getStatusBarHeight(), 0, 0 );
		toolbar.setTitle( R.string.text_new_expenditure );

		// Set an OnMenuItemClickListener to handle menu item clicks
		toolbar.setOnMenuItemClickListener( new Toolbar.OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick( MenuItem item )
			{
				saveExpenditure( dialogView );
				return true;
			}
		} );



		toolbar.setNavigationOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				dismiss();
			}
		} );

		toolbar.inflateMenu( R.menu.expenditure_dialog_menu );

		// Inflate the layout for this fragment
		return dialogView;
	}

	@Override
	public Dialog onCreateDialog( Bundle savedInstanceState )
	{
		// The only reason you might override this method when using onCreateView() is
		// to modify any dialog characteristics. For example, the dialog includes a
		// title by default, but your custom layout might not need it. So here you can
		// remove the dialog title, but you must call the superclass to get the Dialog.
		final Dialog dialog = super.onCreateDialog( savedInstanceState );
		dialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );
		dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );

		dialog.setOnKeyListener( new Dialog.OnKeyListener()
		{
			@Override
			public boolean onKey( DialogInterface arg0, int keyCode,
			                      KeyEvent event )
			{
				// TODO Auto-generated method stub
				if( keyCode == KeyEvent.KEYCODE_BACK )
				{
					dismiss();
				}
				return true;
			}
		} );

		return dialog;
	}

	private void bindAmountEditText( final View pView )
	{
		final View view = pView;
		EditText amountTextField = (EditText)view.findViewById( R.id.main_amount );
		amountTextField.addTextChangedListener( new TextWatcher()
		{
			@Override
			public void beforeTextChanged( CharSequence s, int start, int count, int after ){}

			@Override
			public void onTextChanged( CharSequence s, int start, int before, int count )
			{
				//ActionMenuItemView saveExpenditureItem = (ActionMenuItemView) view.findViewById( R.id.save_dialog );
				//saveExpenditureItem.setEnabled( s != null && s.length() != 0 );
			}

			@Override
			public void afterTextChanged( Editable s ){}
		} );
	}

	// A method to find height of the status bar
	private int getStatusBarHeight()
	{
		int result = 0;
		int resourceId = getResources().getIdentifier( "status_bar_height", "dimen", "android" );
		if( resourceId > 0 )
		{
			result = getResources().getDimensionPixelSize( resourceId );
		}
		return result;
	}

	private void saveExpenditure( View view )
	{
		ExpenditureLogic expenditureLogic = new ExpenditureLogic( getActivity() );

		EditText amountEditText = (EditText)view.findViewById( R.id.main_amount );
		String amountString = amountEditText.getText().toString();
		Toast toast;
		if( TextUtils.isEmpty( amountString ) )
		{
			toast = Toast.makeText( getActivity(), getResources().getString( R.string.text_amount_necessary ), Toast.LENGTH_SHORT );
		}
		else
		{
			EditText productEditText = (EditText)view.findViewById( R.id.main_product );
			EditText storeEditText = (EditText)view.findViewById( R.id.main_store );
			EditText sourceOfFundsEditText = (EditText)view.findViewById( R.id.main_sourceOfFunds );

			Float amount = Float.parseFloat( amountString );
			int result = expenditureLogic
					.createNewExpenditure( amount, productEditText.getText().toString(), storeEditText.getText().toString(),
					                       sourceOfFundsEditText.getText().toString() );

			if( RESULT_OK == result )
			{
				toast = Toast.makeText( getActivity(), getResources().getString( R.string.text_successfully_saved ), Toast.LENGTH_SHORT );
			}
			else
			{
				toast = Toast.makeText( getActivity(), "Doof", Toast.LENGTH_SHORT );
			}

			amountEditText.setText( "" );
			productEditText.setText( "" );
			storeEditText.setText( "" );
			sourceOfFundsEditText.setText( "" );
			activity.loadAndInitExpenditures();
			dismiss();
		}
		toast.show();
	}

	public void setupAutocomplete( View pView )
	{
		// Load Lists for autocomplete
		ProductLogic productLogic = new ProductLogic( getActivity() );
		List<String> productList = new LinkedList<>(  );
		for( ProductTO product : productLogic.getAllProductTOs() )
		{
			productList.add( product.getValue() );
		}
		String[] productNames = productList.toArray( new String[productList.size()] );

		StoreLogic storeLogic = new StoreLogic( getActivity() );
		List<String> storeList = new LinkedList<>(  );
		for( StoreTO store : storeLogic.getAllStoreTOs() )
		{
			storeList.add( store.getValue() );
		}
		String[] storeNames = storeList.toArray( new String[storeList.size()] );

		SourceOfFundsLogic sourceOfFundsLogic = new SourceOfFundsLogic( getActivity() );
		List<String> sourceOfFundsList = new LinkedList<>(  );
		for( SourceOfFundsTO sourceOfFunds : sourceOfFundsLogic.getAllSourceOfFundsTOs() )
		{
			sourceOfFundsList.add( sourceOfFunds.getValue() );
		}
		String[] sourceOfFundsNames = sourceOfFundsList.toArray( new String[sourceOfFundsList.size()] );

		// Set up autocomplete
		ArrayAdapter<String> productAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.select_dialog_item, productNames );
		AutoCompleteTextView productTextView = (AutoCompleteTextView)pView.findViewById( R.id.main_product );
		productTextView.setThreshold( 1 );
		productTextView.setAdapter(productAdapter);

		ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.select_dialog_item, storeNames);
		AutoCompleteTextView storeTextView = (AutoCompleteTextView)pView.findViewById( R.id.main_store );
		storeTextView.setThreshold( 1 );
		storeTextView.setAdapter(storeAdapter);

		ArrayAdapter<String> sourceOfFundsAdapter = new ArrayAdapter<String>( getActivity(), android.R.layout.select_dialog_item, sourceOfFundsNames);
		AutoCompleteTextView sourceOfFundsTextView = (AutoCompleteTextView)pView.findViewById( R.id.main_sourceOfFunds);
		sourceOfFundsTextView.setThreshold( 1 );
		sourceOfFundsTextView.setAdapter( sourceOfFundsAdapter );
	}

}
