package mediajs.budgetbook.objects.trans;

/**
 * Created by Juergen on 02.03.2015.
 */
public abstract class AbstractExpenditureComponentTO
{
	public boolean isSuggested()
	{
		return suggested;
	}

	public void setSuggested( boolean suggested )
	{
		this.suggested = suggested;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue( String value )
	{
		this.value = value;
	}

	public Long getUid()
	{
		return uid;
	}

	public void setUid( long uid )
	{
		this.uid = uid;
	}

	public boolean isFavorite()
	{
		return favorite;
	}

	public void setFavorite( boolean favorite )
	{
		this.favorite = favorite;
	}

	protected Long uid;
	protected String value;
	protected boolean suggested = false;
	protected boolean favorite  = false;
}
