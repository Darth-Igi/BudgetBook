package mediajs.budgetbook.objects.trans;

import mediajs.budgetbook.objects.type.ExpenditureComponentType;

/**
 * Created by Juergen on 04.03.2015.
 */
public class ComponentRelationTO
{
	private Long                     uid;
	private Long                     expenditureUid;
	private Long                     componentUid;
	private ExpenditureComponentType componentType;
	private boolean                  suggested;

	public Long getUid()
	{
		return uid;
	}

	public void setUid( Long uid )
	{
		this.uid = uid;
	}

	public Long getExpenditureUid()
	{
		return expenditureUid;
	}

	public void setExpenditureUid( Long expenditureUid )
	{
		this.expenditureUid = expenditureUid;
	}

	public Long getComponentUid()
	{
		return componentUid;
	}

	public void setComponentUid( Long componentUid )
	{
		this.componentUid = componentUid;
	}

	public ExpenditureComponentType getComponentType()
	{
		return componentType;
	}

	public void setComponentType( ExpenditureComponentType componentType )
	{
		this.componentType = componentType;
	}

	public boolean isSuggested()
	{
		return suggested;
	}

	public void setSuggested( boolean suggested )
	{
		this.suggested = suggested;
	}
}
