package app.voron.ph.showcaseapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import app.voron.ph.showcaseapp.Fragments.CartListFragment;
import app.voron.ph.showcaseapp.Fragments.EmptyCartFragment;
import app.voron.ph.showcaseapp.Models.ShowcaseItemDataModel;
import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.System.Consts;
import app.voron.ph.showcaseapp.System.DependencyResolver;
import app.voron.ph.showcaseapp.System.Providers.OnResultListener;

public class CartActivity
        extends AppCompatActivity
        implements
        SwipeRefreshLayout.OnRefreshListener,
        CartListFragment.OnFragmentInteractionListener{

    private Toolbar mToolbar = null;
    private SwipeRefreshLayout mSwipeToRefresh = null;
    private LinearLayout mLayoutContainer = null;
    //
    private List<ShowcaseItemDataModel> mCartData = new ArrayList<>();
    //
    private void beginRequestData(){
        mSwipeToRefresh.setRefreshing(true);
        DependencyResolver
                .getDataRequestProvider()
                .beginRequestCartItems(new OnResultListener<List<ShowcaseItemDataModel>>() {
                    @Override
                    public void onResult(List<ShowcaseItemDataModel> cartItemDataModels, Exception exception) {
                        if(exception == null){
                            onCartItemsObtained(cartItemDataModels);
                        }
                    }
                });
    }
    //
    private void onCartItemsObtained(List<ShowcaseItemDataModel> cartItemDataModels){
        mSwipeToRefresh.setRefreshing(false);
        mCartData = cartItemDataModels;
        if(cartItemDataModels.size() > 0){
            showCartListFragment();
        } else {
            showEmptyCartFragment();
        }
    }
    //
    private void beginClearCart(){
        mSwipeToRefresh.setRefreshing(true);
        DependencyResolver
                .getDataRequestProvider()
                .beginClearCart(new OnResultListener<Void>() {
                    @Override
                    public void onResult(Void aVoid, Exception exception) {
                        mSwipeToRefresh.setRefreshing(false);
                        beginRequestData();
                    }
                });
    }
    //
    private void showEmptyCartFragment(){
        //TODO: сделать проверку на текущий отображаемый фрагмент
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_container,
                EmptyCartFragment.newInstance());
        transaction.commit();
    }
    //
    private void showCartListFragment(){
        //TODO: сделать проверку на текущий отображаемый фрагмент и вызов notifyDataSetChanged()
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_container,
                CartListFragment.newInstance());
        transaction.commit();
    }
    //
    private void showCartClearAskingDialog(){
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setMessage(R.string.clear_cart_notification)
                .setPositiveButton(R.string.common_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        beginClearCart();
                    }
                })
                .setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //
                    }
                })
                .create();
        dlg.show();
    }
    //
    private void showOrderingActivity(){
        Intent intent = new Intent(this, OrderingActivity.class);
        startActivity(intent);
    }
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);
        mSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.widget_swipe_refresh);
        mLayoutContainer = (LinearLayout) findViewById(R.id.layout_container);
        //
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.cart_activity_title);
        mSwipeToRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        beginRequestData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                showCartClearAskingDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRefresh() {
        beginRequestData();
    }

    @Override
    public List<ShowcaseItemDataModel> getCartData() {
        return mCartData;
    }

    @Override
    public void beginRemoveItemFromCart(ShowcaseItemDataModel item) {
        DependencyResolver
                .getDataRequestProvider()
                .beginRemoveItemFromCart(item.id, new OnResultListener<Integer>() {
                    @Override
                    public void onResult(Integer integer, Exception exception) {
                        if(integer == 0) showEmptyCartFragment();
                    }
                });
    }

    @Override
    public void onCartItemCLick(View sender, ShowcaseItemDataModel item) {
        ImageView preview = sender.findViewById(R.id.image_peview);
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra(Consts.KEY_DATA, item);
        //
        intent.putExtra(Consts.KEY_PREVIEW_BITMAP,
                ((BitmapDrawable)preview.getDrawable()).getBitmap());
        startActivity(intent);
    }

    @Override
    public void onSubmitOrder() {
        showOrderingActivity();
    }
}
