package mediajs.budgetbook.objects.trans;

/**
 * Created by Juergen on 02.03.2015.
 */
public class SourceOfFundsTO extends AbstractExpenditureComponentTO
{
	/**
	 * fully qualified constructor
	 *
	 * @param uid
	 * @param value
	 * @param suggested
	 * @param favorite
	 */
	public SourceOfFundsTO( final Long uid, final String value, final boolean suggested, final boolean favorite )
	{
		this.uid = uid;
		this.value = value;
		this.suggested = suggested;
		this.favorite = favorite;
	}

	/**
	 * constructor with default "false" boolean
	 *
	 * @param uid
	 * @param value
	 */
	public SourceOfFundsTO( final Long uid, final String value )
	{
		this.uid = uid;
		this.value = value;
		this.suggested = false;
		this.favorite = false;
	}

	/**
	 * empty constructor
	 */
	public SourceOfFundsTO()
	{
		// empty constructor
	}
}
