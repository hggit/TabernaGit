package club.cyberlabs.taberna;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.wallet.Cart;

import java.util.ArrayList;
import java.util.List;

public class CatalogueActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout mDrawerLayout;

    int getTab=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        Intent mIntent = getIntent();
        getTab = mIntent.getIntExtra("ans", 0);




        setupViewPager(viewPager);
        viewPager.setCurrentItem(getTab);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                int id = menuItem.getItemId();
                switch(id){
                    case R.id.Gadgets:
                        Intent intent = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent.putExtra("ans",0);
                        startActivity(intent);
                        return true;
                    case R.id.Appliances:
                        Intent intent1 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent1.putExtra("ans",1);
                        startActivity(intent1);
                        return true;
                    case R.id.Books:
                        Intent intent2 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent2.putExtra("ans",2);
                        startActivity(intent2);
                        return true;
                    case R.id.Toys:
                        Intent intent3 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent3.putExtra("ans",3);
                        startActivity(intent3);
                        return true;
                    case R.id.Office:
                        Intent intent4 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent4.putExtra("ans",4);
                        startActivity(intent4);
                        return true;
                    case R.id.Daily_use:
                        Intent intent5 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent5.putExtra("ans",5);
                        startActivity(intent5);
                        return true;
                    case R.id.Set_Budget:
                        Intent intent6 = new Intent(getApplicationContext(),CatalogueActivity.class);
                        intent6.putExtra("ans",6);
                        startActivity(intent6);
                        return true;
                    case R.id.My_cart:
                        Intent intent7 = new Intent(getApplicationContext(),CartActivity.class);

                        startActivity(intent7);
                        return true;
                    case R.id.Logout:
                        Intent intent8 = new Intent(getApplicationContext(),LoginActivity.class);

                        startActivity(intent8);
                        return true;

                }
                return true;
            }
        });





    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "Gadgets");
        adapter.addFrag(new TwoFragment(), "Appliances");
        adapter.addFrag(new ThreeFragment(), "Books");
        adapter.addFrag(new FourFragment(), "Toys");
        adapter.addFrag(new FiveFragment(), "Office");
        adapter.addFrag(new SevenFragment(),"Pharmaceuticals");
        adapter.addFrag(new SixFragment(), "Daily Use");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                //Here u can get the value "query" which is entered in the search box.
                Intent i=new Intent(getApplicationContext(),SearchActivity.class);
                i.putExtra("search-key",query);

                startActivity(i);
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;


            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);


    }


}

