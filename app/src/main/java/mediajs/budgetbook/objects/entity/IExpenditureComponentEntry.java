package mediajs.budgetbook.objects.entity;

/**
 * Created by Juergen on 02.03.2015.
 */
public interface IExpenditureComponentEntry
{
	/**
	 * The unique ID for a row.
	 * <P>Type: INTEGER (long)</P>
	 */
	public static final String _ID = "_id";

	/**
	 * The count of rows in a directory.
	 * <P>Type: INTEGER</P>
	 */
	public static final String _COUNT = "_count";

	/**
	 * The value
	 * <P>Type: TEXT</P>
	 */
	public static final String COLUMN_VALUE = "c_value";

	/**
	 * Defines, if this component value is for its type the favorite one
	 * <P>Type: INTEGER</P>
	 */
	public static final String COLUMN_SUGGESTED = "c_suggested";

	/**
	 * Defines, if this component value is for its type the favorite one
	 * <P>Type: INTEGER</P>
	 */
	public static final String COLUMN_FAVORITE = "c_favorite";

}
