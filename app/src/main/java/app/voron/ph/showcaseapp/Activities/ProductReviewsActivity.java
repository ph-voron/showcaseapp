package app.voron.ph.showcaseapp.Activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import app.voron.ph.showcaseapp.Adapters.ProductReviewsAdapter;
import app.voron.ph.showcaseapp.Models.ReviewItemDataModel;
import app.voron.ph.showcaseapp.Models.ShowcaseItemDataModel;
import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.System.Consts;
import app.voron.ph.showcaseapp.System.DependencyResolver;
import app.voron.ph.showcaseapp.System.Providers.OnResultListener;

public class ProductReviewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final int REVIEWS_LIST_LIMIT = -1;
    //
    private ShowcaseItemDataModel mDataModel = null;
    //
    private Toolbar mToolbar = null;
    private RecyclerView mReviewsList = null;
    private SwipeRefreshLayout mSwipeToRefresh = null;
    private TextView mTextRating = null;
    private TextView mTextReviewsCount = null;
    private RatingBar mWidgetRating = null;
    //
    private ProductReviewsAdapter mReviewsAdapter = null;
    //
    private void beginRequestData(){
        mSwipeToRefresh.setRefreshing(true);
        DependencyResolver
                .getDataRequestProvider()
                .beginRequestReviews(mDataModel.id, REVIEWS_LIST_LIMIT, new OnResultListener<List<ReviewItemDataModel>>() {
                    @Override
                    public void onResult(List<ReviewItemDataModel> reviewItemDataModels, Exception exception) {
                        onReviewsObtained(reviewItemDataModels, exception);
                    }
                });
    }
    //
    private void onReviewsObtained(List<ReviewItemDataModel> reviewItemDataModels, Exception exception){
        mSwipeToRefresh.setRefreshing(false);
        if(exception == null){
            mReviewsAdapter.setData(reviewItemDataModels);
        } else {
            Log.e("onReviewsObtained", exception.getMessage());
        }
    }
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_reviews);
        //
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            mDataModel = (ShowcaseItemDataModel) bundle.getSerializable(Consts.KEY_DATA);
        }
        //
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);
        mReviewsList = (RecyclerView) findViewById(R.id.list_reviews);
        mSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.widget_swipe_refresh);
        mTextRating = (TextView) findViewById(R.id.text_rating);
        mTextReviewsCount = (TextView) findViewById(R.id.text_reviews_count);
        mWidgetRating = (RatingBar) findViewById(R.id.widget_rating);
        //
        mReviewsAdapter = new ProductReviewsAdapter();
        mReviewsList.setLayoutManager(new LinearLayoutManager(this));
        mReviewsList.setAdapter(mReviewsAdapter);
        mSwipeToRefresh.setOnRefreshListener(this);
        //
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(mDataModel.title);
        //
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ROOT);
        formatSymbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("0.0", formatSymbols);
        mTextRating.setText(decimalFormat.format(mDataModel.rating));
        mTextReviewsCount.setText(String.valueOf(mDataModel.reviewsCount));
        mWidgetRating.setRating(mDataModel.rating);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        DependencyResolver
                .getDataRequestProvider()
                .cancelAllRequests();
    }

    @Override
    public void onRefresh() {
        beginRequestData();
    }
}
