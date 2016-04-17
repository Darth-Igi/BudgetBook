package mediajs.budgetbook.activities;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import mediajs.budgetbook.R;
import mediajs.budgetbook.adapters.DrawerAdapter;

/**
 * Created by Juergen on 17.01.2016.
 */
public abstract class AbstractBudgetBookActivity extends AppCompatActivity
{

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private Toolbar      toolbar;                              // Declaring the Toolbar Object

	String TITLES[] = {"Home", "Store"};
	int    ICONS[]  = {R.drawable.ic_home_black_48dp, R.drawable.ic_store_black_48dp};


	RecyclerView               drawerRecyclerView;        // Declaring RecyclerView
	RecyclerView.Adapter       drawerAdapter;            // Declaring Adapter For Recycler View
	RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
	DrawerLayout               drawerLayout;              // Declaring DrawerLayout

	ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar drawerLayout Toggle

	protected void createDrawerAndBar()
	{
		getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
		mTitle = getTitle();

		/* Assigning the toolbar object ot the view
    and setting the Action bar to our toolbar
     */
		toolbar = (Toolbar)findViewById( R.id.expenditure_dialog_tool_bar );
		setSupportActionBar( toolbar );

		toolbar.setPadding( 0, getStatusBarHeight(), 0, 0 );

		drawerRecyclerView = (RecyclerView)findViewById( R.id.rv_DrawerEntries ); // Assigning the RecyclerView Object to the xml View

		drawerRecyclerView.setHasFixedSize( true );                            // Letting the system know that the list objects are of fixed size

		drawerAdapter = new DrawerAdapter( TITLES, ICONS );       // Creating the Adapter of DrawerAdapter class(which we are going to see in a bit)
		// And passing the titles,icons,header view name, header view email,
		// and header view profile picture

		drawerRecyclerView.setAdapter( drawerAdapter );                              // Setting the adapter to RecyclerView

		mLayoutManager = new LinearLayoutManager( this );                 // Creating a layout Manager

		drawerRecyclerView.setLayoutManager( mLayoutManager );                 // Setting the layout Manager


		drawerLayout = (DrawerLayout)findViewById( R.id.DrawerLayout );        // drawerLayout object Assigned to the view
		mDrawerToggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer )
		{

			@Override
			public void onDrawerOpened( View drawerView )
			{
				super.onDrawerOpened( drawerView );
				// code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
				// open I am not going to put anything here)
			}

			@Override
			public void onDrawerClosed( View drawerView )
			{
				super.onDrawerClosed( drawerView );
				// Code here will execute once drawer is closed
			}


		}; // drawerLayout Toggle Object Made
		drawerLayout.setDrawerListener( mDrawerToggle ); // drawerLayout Listener set to the drawerLayout toggle
		mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

		// create our manager instance after the content view is set
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled( true );
		// enable navigation bar tint
		tintManager.setNavigationBarTintEnabled( true );
		// set the transparent color of the status bar, 30% darker
		tintManager.setTintColor( Color.parseColor( "#30000000" ) );
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

	public void restoreActionBar()
	{
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_STANDARD );
		actionBar.setDisplayShowTitleEnabled( true );
		actionBar.setTitle( mTitle );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate( R.menu.main, menu );
		return true;
	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if( id == R.id.action_settings )
		{
			return true;
		}

		return super.onOptionsItemSelected( item );
	}
}
