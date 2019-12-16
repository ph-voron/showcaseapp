package app.voron.ph.showcaseapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import app.voron.ph.showcaseapp.Models.OrderingCompletedModel;
import app.voron.ph.showcaseapp.Models.OrderingDataModel;
import app.voron.ph.showcaseapp.R;
import app.voron.ph.showcaseapp.System.DependencyResolver;
import app.voron.ph.showcaseapp.System.Providers.OnResultListener;
import app.voron.ph.showcaseapp.Utilities.NotificationsHelper;

public class OrderingActivity extends AppCompatActivity {
    //
    private Toolbar mToolbar = null;
    private TextInputEditText mEditName = null;
    private TextInputEditText mEditPhone = null;
    private TextInputEditText mEditStreet = null;
    private TextInputEditText mEditHouse = null;
    private TextInputEditText mEditBuilding = null;
    private TextInputEditText mEditApartments = null;
    private TextInputEditText mEditComments = null;
    private Button mButtonSubmit = null;
    //
    private OrderingDataModel getFormModel(){
        return new OrderingDataModel(
                mEditName.getText().toString(),
                mEditPhone.getText().toString(),
                mEditStreet.getText().toString(),
                mEditHouse.getText().toString(),
                mEditBuilding.getText().toString(),
                mEditApartments.getText().toString(),
                mEditComments.getText().toString());
    }
    //
    private boolean isFormDataValid(){
        if(mEditName.getText().length() < 3){
            mEditName.setError(getResources().getString(R.string.error_field_requires_3_symbols));
            return false;
        }
        if(mEditPhone.getText().length() < 6){
            mEditPhone.setError(getResources().getString(R.string.error_field_requires_6_symbols));
            return false;
        }
        if(mEditStreet.getText().length() < 3){
            mEditStreet.setError(getResources().getString(R.string.error_field_requires_3_symbols));
            return false;
        }
        if(mEditHouse.getText().length() == 0){
            mEditHouse.setError(getResources().getString(R.string.error_field_required));
            return false;
        }
        return true;
    }
    //
    private void beginRequestData(){
        DependencyResolver
                .getDataRequestProvider()
                .beginRequestLastOrdering(new OnResultListener<OrderingDataModel>() {
                    @Override
                    public void onResult(OrderingDataModel orderingDataModel, Exception exception) {
                        if(exception == null){
                            mEditName.setText(orderingDataModel.name);
                            mEditPhone.setText(orderingDataModel.phoneNumber);
                            mEditStreet.setText(orderingDataModel.street);
                            mEditHouse.setText(orderingDataModel.house);
                            mEditBuilding.setText(orderingDataModel.building);
                            mEditApartments.setText(orderingDataModel.apartments);
                            mEditComments.setText(orderingDataModel.comments);
                        }
                    }
                });
    }
    //
    private void beginSubmitOrder() {
        if (isFormDataValid()) {
            DependencyResolver
                    .getDataRequestProvider()
                    .beginSubmitOrdering(getFormModel(), new OnResultListener<OrderingCompletedModel>() {
                        @Override
                        public void onResult(OrderingCompletedModel data, Exception exception) {
                            if(exception == null) {
                                onOrderingCompleted(data);
                            } else {
                                String message = String.format(
                                        OrderingActivity.this.getResources()
                                                .getString(R.string.error_submit_order),
                                        exception.getMessage());
                                NotificationsHelper.showToast(
                                        OrderingActivity.this, message);
                            }
                        }
                    });
        }
    }
    //
    private void onOrderingCompleted(OrderingCompletedModel model){
        Intent intent = new Intent(this, AboutAppActivity.class);
        startActivity(intent);
    }
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering);
        //
        mToolbar = (Toolbar) findViewById(R.id.widget_toolbar);
        mEditName = (TextInputEditText) findViewById(R.id.edit_name);
        mEditPhone = (TextInputEditText) findViewById(R.id.edit_phone);
        mEditStreet = (TextInputEditText) findViewById(R.id.edit_street);
        mEditHouse = (TextInputEditText) findViewById(R.id.edit_house);
        mEditBuilding = (TextInputEditText) findViewById(R.id.edit_building);
        mEditApartments = (TextInputEditText) findViewById(R.id.edit_apartments);
        mEditComments = (TextInputEditText) findViewById(R.id.edit_comments);
        mButtonSubmit = (Button) findViewById(R.id.button_submit);
        //
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginSubmitOrder();
            }
        });
        //
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(R.string.ordering_activity_title);
        //
        beginRequestData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
