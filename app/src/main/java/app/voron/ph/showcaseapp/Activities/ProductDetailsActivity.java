package app.voron.ph.showcaseapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import app.voron.ph.showcaseapp.Models.ReviewItemDataModel;
import app.voron.ph.showcaseapp.Models.ShowcaseItemDataModel;
import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.System.Consts;
import app.voron.ph.showcaseapp.System.DependencyResolver;
import app.voron.ph.showcaseapp.System.Providers.OnResultListener;
import app.voron.ph.showcaseapp.Utilities.AnimationHelper;
import app.voron.ph.showcaseapp.Utilities.FormatHelper;
import app.voron.ph.showcaseapp.Utilities.ImageHelper;
import app.voron.ph.showcaseapp.Utilities.NotificationsHelper;

public class ProductDetailsActivity extends AppCompatActivity {
    public static final int REVIEWS_LIST_LIMIT = 5;
    //
    private ShowcaseItemDataModel mDataModel = null;
    //
    private Toolbar mToolbar = null;
    private CollapsingToolbarLayout mCollapsingToolbar = null;
    private ImageView mToolbarImage = null;
    private TextView mTextTitle = null;
    private TextView mTextDescription = null;
    private TextView mTextTime = null;
    private TextView mTextCost = null;
    private ImageView mImageFoodCategory = null;
    private ProgressBar mProgressReviews = null;
    private LinearLayout mLayoutReviews = null;
    private LinearLayout mLayoutReviewsGroup = null;
    private FloatingActionButton mButtonOrder = null;
    private FloatingActionButton mButtonRemoveFromCart = null;
    private CoordinatorLayout mLayoutCoordinator = null;
    //
    private Transition.TransitionListener mActivityTransitionListener = new Transition.TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {}
        @Override
        public void onTransitionCancel(Transition transition) {}
        @Override
        public void onTransitionPause(Transition transition) {}
        @Override
        public void onTransitionResume(Transition transition) {}
        @Override
        public void onTransitionEnd(Transition transition) {
            requestCoverImage();
        }
    };
    //
    private void isReviewsProgressVisible(boolean value){
        mProgressReviews.setVisibility(value ? View.VISIBLE : View.GONE);
    }
    //
    private void beginRequestData(){
        //
        mLayoutReviewsGroup.setVisibility(View.GONE);
        //
        DependencyResolver
                .getDataRequestProvider()
                .beginRequestShowcaseItemById(mDataModel.id, new OnResultListener<ShowcaseItemDataModel>() {
                    @Override
                    public void onResult(ShowcaseItemDataModel showcaseItemDataModel, Exception exception) {
                        mDataModel = showcaseItemDataModel;
                        updateViewsByModel();
                    }
                });
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
        isReviewsProgressVisible(false);
        if(exception == null){
            mLayoutReviews.removeAllViews();
            if(reviewItemDataModels.size() > 0){
                mLayoutReviewsGroup.setVisibility(View.VISIBLE);
            }
            for (ReviewItemDataModel item: reviewItemDataModels) {
                View view = getLayoutInflater().inflate(R.layout.layout_review_item, null);
                TextView textName = view.findViewById(R.id.text_name);
                TextView textReview = view.findViewById(R.id.text_review);
                RatingBar rating = view.findViewById(R.id.widget_rating);
                CardView card = view.findViewById(R.id.layout_card_view);
                //
                textName.setText(item.name);
                textReview.setText(item.text);
                rating.setRating(item.rate);
                card.setFocusable(false);
                card.setClickable(false);
                mLayoutReviews.addView(view);
            }
        } else {
            Log.e("onReviewsObtained", exception.getMessage());
        }
    }
    //
    private void setTitleImage(Bitmap bitmap){
        mToolbarImage.setImageBitmap(bitmap);
    }
    //
    private void showCartActivity(){
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }
    //
    private void onShowReviewsAction(View sender){
        Intent intent = new Intent(this, ProductReviewsActivity.class);
        intent.putExtra(Consts.KEY_DATA, mDataModel);
        startActivity(intent);
    }
    //
    private void updateViewsByModel(){
        mTextTitle.setText(mDataModel.title);
        mTextDescription.setText(mDataModel.description);
        mTextTime.setText(mDataModel.timeFormatted);
        mTextCost.setText(
                FormatHelper.formatProductCost(mDataModel.cost));
        mImageFoodCategory.setImageBitmap(
                ImageHelper.getBitmapForFoodId(mDataModel.foodCatecory, this));
        mButtonOrder.setVisibility(mDataModel.itemsInCart > 0 ? View.GONE : View.VISIBLE);
        mButtonRemoveFromCart.setVisibility(mDataModel.itemsInCart > 0 ? View.VISIBLE : View.GONE);
    }
    //
    private void requestCoverImage(){
        if(mDataModel == null || mDataModel.coverImageName == null) return;
        DependencyResolver
                .getDataRequestProvider()
                .beginRequestImage(mDataModel.coverImageName, new OnResultListener<Bitmap>() {
            @Override
            public void onResult(Bitmap bitmap, Exception exception) {
                if(exception == null && bitmap != null) {
                    mToolbarImage.setImageBitmap(bitmap);
                }
            }
        });
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
    private void onProductOrderClick() {
        DependencyResolver
                .getDataRequestProvider()
                .beginPutProductIntoCart(mDataModel.id, new OnResultListener<Integer>() {
                    @Override
                    public void onResult(Integer cartSize, Exception exception) {
                        showOnCartAddSnackbar(cartSize);
                    }
                });
    }
    //
    private void onProductRemoveFromCartClick() {
        DependencyResolver
                .getDataRequestProvider()
                .beginRemoveItemFromCart(mDataModel.id, new OnResultListener<Integer>() {
                    @Override
                    public void onResult(Integer cartSize, Exception exception) {
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        Bitmap previewImage = null;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            mDataModel = (ShowcaseItemDataModel)bundle.getSerializable(Consts.KEY_DATA);
            previewImage = bundle.getParcelable(Consts.KEY_PREVIEW_BITMAP);
        }
        //
        setContentView(R.layout.activity_product_details);
        mLayoutCoordinator = (CoordinatorLayout) findViewById(R.id.layout_coordinator);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.widget_collapsing_toolbar);
        mToolbarImage = (ImageView) findViewById(R.id.image_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextDescription = (TextView) findViewById(R.id.text_description);
        mTextTime = (TextView) findViewById(R.id.text_time);
        mTextCost = (TextView) findViewById(R.id.text_cost);
        mImageFoodCategory = (ImageView)findViewById(R.id.image_food_category);
        mProgressReviews = (ProgressBar)findViewById(R.id.progress_reviews);
        mLayoutReviews = (LinearLayout)findViewById(R.id.layout_reviews);
        mLayoutReviewsGroup = (LinearLayout)findViewById(R.id.layout_reviews_group);
        mButtonOrder = (FloatingActionButton)findViewById(R.id.button_order);
        mButtonRemoveFromCart = (FloatingActionButton) findViewById(R.id.button_remove_product);
        //
        mButtonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationHelper.animateViewSwapZoomAndAppear(mButtonOrder, mButtonRemoveFromCart);
                onProductOrderClick();
            }
        });
        //
        mButtonRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationHelper.animateViewSwapZoomAndAppear(mButtonRemoveFromCart, mButtonOrder);
                onProductRemoveFromCartClick();
            }
        });
        //
        mLayoutReviewsGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowReviewsAction(view);
            }
        });
        //
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //
        if(previewImage != null) {
            setTitleImage(previewImage);
        }
        //
        Transition sharedElementEnterTransition = getWindow().getSharedElementEnterTransition();
        sharedElementEnterTransition.addListener(mActivityTransitionListener);
        //
        updateViewsByModel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //
        isReviewsProgressVisible(true);
        beginRequestData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DependencyResolver
                .getDataRequestProvider()
                .cancelAllRequests();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
