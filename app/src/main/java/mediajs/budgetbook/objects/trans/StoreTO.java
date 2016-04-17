package mediajs.budgetbook.objects.trans;

/**
 * Created by Juergen on 02.03.2015.
 */
public class StoreTO extends AbstractExpenditureComponentTO
{
	private double latitude;
	private double longitude;

	/**
	 * fully qualified constructor
	 *
	 * @param uid
	 * @param value
	 * @param suggested
	 * @param favorite
	 * @param latitude
	 * @param longitude
	 */
	public StoreTO( final Long uid, final String value, final boolean suggested, final boolean favorite, final double latitude, final double longitude )
	{
		this.uid = uid;
		this.value = value;
		this.suggested = suggested;
		this.favorite = favorite;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * without latitude and longitude
	 *
	 * @param uid
	 * @param value
	 * @param suggested
	 * @param favorite
	 */
	public StoreTO( final Long uid, final String value, final boolean suggested, final boolean favorite )
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
	public StoreTO( final Long uid, final String value )
	{
		this.uid = uid;
		this.value = value;
		this.suggested = false;
		this.favorite = false;
	}

	/**
	 * empty constructor
	 */
	public StoreTO()
	{
		// empty constructor
	}

	public double getLatitude()
	{
		return latitude;
	}

	public void setLatitude( double latitude )
	{
		this.latitude = latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public void setLongitude( double longitude )
	{
		this.longitude = longitude;
	}
}
