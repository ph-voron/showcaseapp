package app.voron.ph.showcaseapp.Activities;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.voron.ph.showcaseapp.Adapters.ShowcaseFragmentPagerAdapter;
import app.voron.ph.showcaseapp.Fragments.ShowcaseCategoryFragment;
import app.voron.ph.showcaseapp.Fragments.ShowcaseFragment;
import app.voron.ph.showcaseapp.Models.ShowcaseFilteringOptionsModel;
import app.voron.ph.showcaseapp.Models.ShowcaseItemDataModel;
import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.System.Consts;
import app.voron.ph.showcaseapp.System.DependencyResolver;
import app.voron.ph.showcaseapp.System.Providers.OnResultListener;
import app.voron.ph.showcaseapp.Utilities.NotificationsHelper;
import app.voron.ph.showcaseapp.Views.SearchViewExt;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ShowcaseFragment.OnFragmentInteractionListener,
        ShowcaseCategoryFragment.OnFragmentInteractionListener{
    //
    private String mSearchQueryText = "";
    //
    private ImageView mAvatarImageView = null;
    private NavigationView mNavigationView = null;
    private Toolbar mToolbar = null;
    private ViewPager mViewPager = null;
    private TabLayout mTabLayout = null;
    private CoordinatorLayout mLayoutCoordinator = null;
    //
    //
    private void prepareViewPagerData(ViewPager viewPager) {
        ShowcaseFragmentPagerAdapter adapter = new ShowcaseFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(
                ShowcaseCategoryFragment.newInstance(null),
                getResources().getString(R.string.common_all));
        for (Pair<String, String> item : Consts.SHOWCASE_CATEGORY_TAGS) {
            adapter.addFragment(
                    ShowcaseCategoryFragment.newInstance(item.first),
                    item.second);
        }
        viewPager.setAdapter(adapter);
    }
    //
    private ShowcaseFilteringOptionsModel getFilteringOptions(String searchText, String categoryTag){
        ShowcaseFilteringOptionsModel result = new ShowcaseFilteringOptionsModel();
        result.searchText = searchText;
        result.categoryTag = categoryTag;
        return result;
    }
    //
    private ShowcaseFilteringOptionsModel getFilteringOptions(String categoryTag){
        return getFilteringOptions(mSearchQueryText, categoryTag);
    }
    //
    private void showOnCartAddSnackbar(int cartSize) {
        NotificationsHelper.showOnCartAddSnackbar(this, mLayoutCoordinator, cartSize,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showCartActivity();
                    }
                });
    }
    //
    private void showCartActivity(){
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }
    //
    private void showAboutActivity(){
        Intent intent = new Intent(this, AboutAppActivity.class);
        startActivity(intent);
    }
    //
    private ShowcaseCategoryFragment getCurrentTabFragment(){
        ShowcaseFragmentPagerAdapter adapter = (ShowcaseFragmentPagerAdapter)mViewPager.getAdapter();
        return (ShowcaseCategoryFragment)adapter.getItem(mViewPager.getCurrentItem());
    }
    //
    private void refreshCurrentFragment(){
        ShowcaseCategoryFragment frag = getCurrentTabFragment();
        if(frag != null) frag.onRefresh();
    }
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationView = (NavigationView)findViewById(R.id.navView);
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);
        mViewPager = (ViewPager) findViewById(R.id.widget_viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.widget_tabs);
        mLayoutCoordinator = (CoordinatorLayout) findViewById(R.id.layout_coordinator);
        //
        View headerLayout = mNavigationView.getHeaderView(0);
        mAvatarImageView = headerLayout.findViewById(R.id.avatar);
        setSupportActionBar(mToolbar);
        prepareViewPagerData(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);
        //
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //
        mNavigationView.setNavigationItemSelectedListener(this);
        //
        getSupportActionBar().setTitle(R.string.main_activity_title);
        //showFragment(ShowcaseFragment.newInstance(), false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(mViewPager.getCurrentItem() > 0){
                mViewPager.setCurrentItem(0);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //
        MenuItem search = menu.findItem(R.id.search);
        SearchViewExt searchView = (SearchViewExt) search.getActionView();
        searchView.setOnCollapsedListener(new Runnable() {
            @Override
            public void run() {
                mSearchQueryText = "";
                refreshCurrentFragment();
            }
        });
        //
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!mSearchQueryText.toLowerCase().equals(query.trim().toLowerCase())){
                    mSearchQueryText = query.trim();
                    refreshCurrentFragment();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.i("peq", "onQueryTextChange > " + newText);
                return false;
            }

        });
        //
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cart:
                showCartActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*
        if (id == R.id.nav_camera) {
            //
            Fragment fragment = LoginFragment.newInstance("","");
            //

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainContainerLayout, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            //
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */

        switch (item.getItemId()){
            case R.id.nav_cart:
                showCartActivity();
                break;
            case R.id.nav_about:
                showAboutActivity();
                break;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onShowcaseItemClick(View sender, ShowcaseItemDataModel item) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(Consts.KEY_DATA, item);
        TextView title = sender.findViewById(R.id.text_title);
        TextView description = sender.findViewById(R.id.text_description);
        ImageView preview = sender.findViewById(R.id.image_peview);
        //
        intent.putExtra(Consts.KEY_PREVIEW_BITMAP,
                ((BitmapDrawable)preview.getDrawable()).getBitmap());
        //
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,
                        Pair.create((View) preview, "preview"),
                        Pair.create((View) title, "title"),
                        Pair.create((View) description, "description"));
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onShowcaseItemOrderClick(View sender, ShowcaseItemDataModel item) {
        DependencyResolver
                .getDataRequestProvider()
                .beginPutProductIntoCart(item.id, new OnResultListener<Integer>() {
                    @Override
                    public void onResult(Integer cartSize, Exception exception) {
                        showOnCartAddSnackbar(cartSize);
                    }
                });
    }

    @Override
    public void onShowcaseItemRemoveFromCartClick(View sender, ShowcaseItemDataModel item) {
        DependencyResolver
                .getDataRequestProvider()
                .beginRemoveItemFromCart(item.id, new OnResultListener<Integer>() {
                    @Override
                    public void onResult(Integer integer, Exception exception) {
                    }
                });
    }

    @Override
    public void onShowcaseCategoryFragmentRequestsItems(String categoryTag,
                                                        OnResultListener<List<ShowcaseItemDataModel>> listener) {
        DependencyResolver
                .getDataRequestProvider()
                .beginRequestShowcaseItems(getFilteringOptions(categoryTag), listener);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this,text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int resId) {
        String text =  getResources().getString(resId);
        showToast(text);
    }
}
