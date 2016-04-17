package mediajs.budgetbook.objects.type;

/**
 * Created by Juergen on 03.03.2015.
 */
public enum ExpenditureComponentType
{
	PRODUCT         ( 0, "product" ),
	STORE           ( 1, "store" ),
	SOURCE_OF_FUNDS ( 2, "source_of_funds" );

	private final Long id;
	private final String key;

	private ExpenditureComponentType( final long id, final String key )
	{
		this.id = id;
		this.key = key;
	}

	public static ExpenditureComponentType getById( final long id )
	{
		for( ExpenditureComponentType e : values() )
		{
			if( e.id.equals(id) )
			{
				return e;
			}
		}
		return null;
	}

	public long getId()
	{
		return id;
	}

	public String getKey()
	{
		return key;
	}
}
