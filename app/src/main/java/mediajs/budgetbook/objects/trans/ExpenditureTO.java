package mediajs.budgetbook.objects.trans;

import java.util.Date;

/**
 * Created by Juergen on 02.03.2015.
 */
public class ExpenditureTO
{
	private Long            uid;
	private ProductTO       productTO;
	private SourceOfFundsTO sourceOfFundsTO;
	private StoreTO         storeTO;
	private Float           amount;
	private Date            recordingDate;

	public ExpenditureTO( Long uid, Float amount, ProductTO productTO, Date recordingDate, StoreTO storeTO, SourceOfFundsTO sourceOfFundsTO )
	{
		this.uid = uid;
		this.amount = amount;
		this.productTO = productTO;
		this.recordingDate = recordingDate;
		this.storeTO = storeTO;
		this.sourceOfFundsTO = sourceOfFundsTO;
	}

	public ExpenditureTO()
	{
		// empty
	}

	public Long getUid()
	{
		return uid;
	}

	public void setUid( Long uid )
	{
		this.uid = uid;
	}

	public Float getAmount()
	{
		return amount;
	}

	public void setAmount( Float amount )
	{
		this.amount = amount;
	}

	public Date getRecordingDate()
	{
		return recordingDate;
	}

	public void setRecordingDate( Date recordingDate )
	{
		this.recordingDate = recordingDate;
	}

	public ProductTO getProductTO()
	{
		return productTO;
	}

	public void setProductTO( ProductTO productTO )
	{
		this.productTO = productTO;
	}

	public SourceOfFundsTO getSourceOfFundsTO()
	{
		return sourceOfFundsTO;
	}

	public void setSourceOfFundsTO( SourceOfFundsTO sourceOfFundsTO )
	{
		this.sourceOfFundsTO = sourceOfFundsTO;
	}

	public StoreTO getStoreTO()
	{
		return storeTO;
	}

	public void setStoreTO( StoreTO storeTO )
	{
		this.storeTO = storeTO;
	}
}
