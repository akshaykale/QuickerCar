package com.akshaykale.quickercarhub;

import com.akshaykale.com.akshaykale.quickercar.QuickerCar;
import com.akshaykale.httpmanager.ProductJSONParser;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.gsm.SmsManager;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DialerFilter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import org.eazegraph.lib.communication.IOnItemFocusChangedListener;

import java.util.ArrayList;

public class DetailsActivity extends Activity {

	TextView tv_name,tv_desc,tv_type,tv_price,tv_mileage,tv_rating,tv_engine,tv_brand,tv_color,tv_abs;
	ImageView iv_image;
	ImageView iv_share,iv_download,iv_sendsms;
	EditText et_ph;
	String mbno=null;

	String jsonFeed,index;
    int sort;

    private PieChart mPieChart;

	private String name;//
	private String image;//
	private float price;//
	private String brand;//
	private String type;//
	private float rating;//
	private String color;//
	private String engine_cc;//
	private String mileage;//
	private boolean abs_exist;//
	private String description;//
	private String link;

	ArrayList<QuickerCar> carList;
	QuickerCar selectedCar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity);

		carList = new ArrayList<QuickerCar>();
        mPieChart = (PieChart) findViewById(R.id.piechart);

		try {
			jsonFeed = getIntent().getExtras().getString(MainActivity.DETAILS_INTENT);
			index = getIntent().getExtras().getString(MainActivity.DETAILS_INDEX);
            String s = getIntent().getExtras().getString(MainActivity.DETAILS_SORT);
            sort = Integer.parseInt(s);

          //  Toast.makeText(getApplicationContext(),index+"  "+jsonFeed,Toast.LENGTH_SHORT).show();

			carList = ProductJSONParser.parseFeed(jsonFeed,sort);

            selectedCar = carList.get(Integer.parseInt(index));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		init();
		
		try {
			setValues();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		pannelClickListeners();

       pie();
	}

    private void pie() {

        int a = selectedCar.getCities().get(0).getUsers()/(selectedCar.getCities().get(0).getUsers()+selectedCar.getCities().get(1).getUsers()+selectedCar.getCities().get(2).getUsers());
        a = a*100;

        mPieChart.addPieSlice(new PieModel(selectedCar.getCities().get(0).getCity(),
                selectedCar.getCities().get(0).getUsers(), Color.parseColor("#FE6DA8")));
        int b = selectedCar.getCities().get(1).getUsers()/(selectedCar.getCities().get(0).getUsers()+selectedCar.getCities().get(1).getUsers()+selectedCar.getCities().get(2).getUsers());
        b = b*100;
        mPieChart.addPieSlice(new PieModel(selectedCar.getCities().get(1).getCity(),
                selectedCar.getCities().get(1).getUsers(), Color.parseColor("#56B7F1")));
        int c = selectedCar.getCities().get(2).getUsers()/(selectedCar.getCities().get(0).getUsers()+selectedCar.getCities().get(1).getUsers()+selectedCar.getCities().get(2).getUsers());
        c = c*100;
        mPieChart.addPieSlice(new PieModel(selectedCar.getCities().get(2).getCity(),
                selectedCar.getCities().get(2).getUsers(), Color.parseColor("#CDA67F")));


        mPieChart.addPieSlice(new PieModel(selectedCar.getCities().get(3).getCity(),
                selectedCar.getCities().get(3).getUsers(), Color.parseColor("#FED70E")));//mPieChart.addPieSlice(new PieModel("Eating", 9, Color.parseColor("#FED70E")));

        mPieChart.setOnItemFocusChangedListener(new IOnItemFocusChangedListener() {
            @Override
            public void onItemFocusChanged(int _Position) {
                Toast.makeText(getApplicationContext(),selectedCar.getCities().get(0).getCity(),Toast.LENGTH_LONG).show();
//              Toa
            }
        });
    }


    private void pannelClickListeners() {
		iv_download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(selectedCar.getLink()));
				startActivity(intent);
			}
		});
		
		iv_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
	            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            shareIntent.setType("text/plain");
	            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, Check out this Amazing "+selectedCar.getName()+" Car.\n"
						+selectedCar.getDescription()+"\nlink: "+selectedCar.getLink());
	            startActivity(shareIntent);
			}
		});
		
		iv_sendsms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog d = new Dialog(DetailsActivity.this);
				d.setContentView(R.layout.smslink);
				d.setCancelable(true);
				d.setTitle("Car Share");
				
				ImageView iv_c = (ImageView) d.findViewById(R.id.iv_contacts);
				et_ph = (EditText) d.findViewById(R.id.editText1);
				Button bt_send = (Button) d.findViewById(R.id.bt_sms_send);
				
				iv_c.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
			              pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
			              int position=0;
						startActivityForResult(pickContactIntent, position);
						//et_ph.setText(""+mbno);
					}
				});
				
				bt_send.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(et_ph.getText().toString().equals("")){
							Toast.makeText(getApplicationContext(), "Enter No.", Toast.LENGTH_SHORT).show();
						}else{
							SmsManager smsManager = SmsManager.getDefault();
							smsManager.sendTextMessage(mbno, null, ""+name+"\nlink :  "+selectedCar.getLink(), null, null);
							d.dismiss();
						}
						
					}
				});
				d.show();
			}
		});
	}

	private void setValues() {


		tv_name.setText(selectedCar.getName());
		tv_desc.setText(selectedCar.getDescription());
		tv_type.setText(selectedCar.getType());
		tv_price.setText(selectedCar.getPrice()+"");
		tv_mileage.setText(selectedCar.getMileage());
		tv_rating.setText(""+selectedCar.getRating());
		tv_engine.setText(selectedCar.getName());
		tv_brand.setText(selectedCar.getBrand());
		tv_color.setText(selectedCar.getColor());tv_color.setBackgroundColor(Color.parseColor(selectedCar.getColor()));
		tv_abs.setText(selectedCar.isAbs_exist() == true ? "yes" : "No");
		Picasso.with(getApplicationContext())
				.load(selectedCar.getImage())//.resize(290,290)
				//.centerCrop()
				.into(iv_image);

	}

    private void GraphCar(){



    }

	private void init() {
		tv_name = (TextView) findViewById(R.id.tv_details_name_brand);
				tv_desc = (TextView) findViewById(R.id.tv_details_desc);
		tv_type = (TextView) findViewById(R.id.tv_details_type);
				tv_price = (TextView) findViewById(R.id.tv_details_price);
		tv_mileage = (TextView) findViewById(R.id.tv_details_milage);
				tv_rating = (TextView) findViewById(R.id.tv_row_rate);
		tv_engine = (TextView) findViewById(R.id.tv_details_engine);
				tv_brand = (TextView) findViewById(R.id.tv_details_brand);
		tv_color  = (TextView) findViewById(R.id.tv_details_color);
				tv_abs = (TextView) findViewById(R.id.tv_details_abs);;
		iv_image = (ImageView) findViewById(R.id.iv_mainimg);

		iv_download = (ImageView) findViewById(R.id.iv_pannel_download);
		iv_share = (ImageView) findViewById(R.id.iv_pannel_share);
		iv_sendsms = (ImageView) findViewById(R.id.iv_pannel_sms);

       // mPieChart = (PieChart) findViewById(R.id.piechart);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ( resultCode == RESULT_OK) {
	        if (requestCode == 0) {
	            {
		            Uri contactUri = data.getData();
		            String[] projection = {Phone.NUMBER};
		            Cursor cursor = getContentResolver()
		                    .query(contactUri, projection, null, null, null);
		            cursor.moveToFirst();
		            int column = cursor.getColumnIndex(Phone.NUMBER);
		            String number = cursor.getString(column);
		            Toast.makeText(getApplicationContext(), number, Toast.LENGTH_LONG).show();
		            mbno = number;
		            et_ph.setText(""+number);
	            }
	            
	          }
		}
	
	}


}
