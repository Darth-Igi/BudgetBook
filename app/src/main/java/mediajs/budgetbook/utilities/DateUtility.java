package mediajs.budgetbook.utilities;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Juergen on 27.01.2016.
 */
public class DateUtility
{
	private static SimpleDateFormat iso8601Format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );

	public static String convertDateToString( Date pDate )
	{
		String finalDate = "";

		if( pDate != null )
		{
			TimeZone timeZone = TimeZone.getDefault();
			iso8601Format.setTimeZone( timeZone );

			finalDate = iso8601Format.format( pDate );
		}

		return finalDate;
	}

	public static String formatDateTime( Context pContext, Date pDate )
	{
		String finalDateTime = "";

		if( pDate != null )
		{
			long when = pDate.getTime();
			int flags = 0;
			flags |= DateUtils.FORMAT_SHOW_TIME;
			flags |= DateUtils.FORMAT_SHOW_DATE;
			flags |= DateUtils.FORMAT_ABBREV_MONTH;
			flags |= DateUtils.FORMAT_SHOW_YEAR;

			finalDateTime = DateUtils.formatDateTime( pContext, when, flags );
		}

		return finalDateTime;
	}


	public static Date convertStringToIsoDate( String pTimeToFormat )
	{
		Date date = null;
		if( pTimeToFormat != null )
		{
			try
			{
				date = iso8601Format.parse( pTimeToFormat );
			}
			catch( ParseException e )
			{
				date = null;
			}
		}
		return date;
	}
}
