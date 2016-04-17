package mediajs.budgetbook.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import mediajs.budgetbook.R;
import mediajs.budgetbook.objects.trans.ExpenditureTO;
import mediajs.budgetbook.objects.trans.StoreTO;
import mediajs.budgetbook.utilities.DateUtility;

/**
 * Created by Juergen on 17.01.2016.
 */
public class ExpenditureCardAdapter extends RecyclerView.Adapter<ExpenditureCardAdapter.ExpenditureCardViewHolder>
{
	public static class ExpenditureCardViewHolder extends RecyclerView.ViewHolder
	{
		private CardView expenditureCardView;
		private TextView expenditureDate;
		private TextView expenditureAmount;
		private TextView expenditureStore;

		public ExpenditureCardViewHolder( View itemView )
		{
			super( itemView );
			expenditureCardView = (CardView) itemView.findViewById( R.id.cv_expenditure );
			expenditureAmount = (TextView) itemView.findViewById( R.id.expenditureAmount );
			expenditureDate = (TextView) itemView.findViewById( R.id.expenditureDate );
			expenditureStore = (TextView) itemView.findViewById( R.id.expenditureStore );
		}
	}

	private List<ExpenditureTO> expenditureList;
	private Context context;

	public ExpenditureCardAdapter( List<ExpenditureTO>  pExpenditureList, Context pContext )
	{
		this.expenditureList = pExpenditureList;
		this.context = pContext;
	}

	@Override
	public ExpenditureCardViewHolder onCreateViewHolder( ViewGroup parent, int viewType )
	{
		View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.cardview_expenditure, parent, false );

		ExpenditureCardViewHolder expenditureCardViewHolder = new ExpenditureCardViewHolder( view );
		return expenditureCardViewHolder;
	}

	@Override
	public void onBindViewHolder( ExpenditureCardViewHolder holder, int position )
	{
		Float amount = this.expenditureList.get( position ).getAmount();
		holder.expenditureAmount.setText( NumberFormat.getCurrencyInstance().format( amount ) );

		Date date = this.expenditureList.get( position ).getRecordingDate();
		String dateValue = "";
		if( date != null )
		{
			dateValue = DateUtility.formatDateTime( context, date );
		}
		holder.expenditureDate.setText( dateValue );

		StoreTO storeTO = this.expenditureList.get( position ).getStoreTO();

		String storeValue = storeTO != null ? holder.itemView.getResources().getString( R.string.dict_at, storeTO.getValue() ) : "-";
		holder.expenditureStore.setText( storeValue );
	}

	@Override
	public int getItemCount()
	{
		return this.expenditureList.size();
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
	}

}
