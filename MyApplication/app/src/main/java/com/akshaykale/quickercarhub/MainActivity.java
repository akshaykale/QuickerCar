package com.akshaykale.quickercarhub;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akshaykale.com.akshaykale.quickercar.QuickerCar;
import com.akshaykale.com.akshaykale.quickercar.QuickerCity;
import com.akshaykale.httpmanager.HttpManager;
import com.akshaykale.httpmanager.ProductJSONParser;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    public static final String DETAILS_INDEX = "d_index";
    //Intent constants
    public static final String DETAILS_INTENT = "d_intent";
    public static final String DETAILS_SORT = "d_sort";

    String jsonFeed = null;
    int sortingMethod = 0;//default

    RotateLoading rotateLoading;
    public static final String CAR_DATA_URL = "http://quikr.0x10.info/api/cars?type=json&query=list_cars";

    private ArrayList<QuickerCar> m_carList;

    Dialog loadingDialog;
    Context m_mainContext;

    ListView lv_carList;

    ImageView iv_person,iv_info,iv_sort,iv_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        MenuListner();
        LoadingDialog();
        requestData(CAR_DATA_URL);




        ListViewListener();
    }

    private void MenuListner() {

        iv_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(MainActivity.this);
                d.setContentView(R.layout.sort_lay);
                d.setCancelable(true);

                TextView tv_price,tv_m,tv_rat;
                tv_m = (TextView) d.findViewById(R.id.sort_mileage);
                tv_rat = (TextView) d.findViewById(R.id.sort_rating);
                tv_price = (TextView) d.findViewById(R.id.sort_price);

                tv_m.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        rotateLoading.start();
                        sortingMethod = 3;
                        d.dismiss();
                        requestData(CAR_DATA_URL);
                    }
                });

                tv_rat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        rotateLoading.start();
                        sortingMethod = 1;
                        d.dismiss();
                        requestData(CAR_DATA_URL);
                    }
                });

                tv_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        rotateLoading.start();
                        sortingMethod = 2;
                        d.dismiss();
                        requestData(CAR_DATA_URL);
                    }
                });

                d.show();
            }
        });

        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new Dialog(MainActivity.this);
                d.setContentView(R.layout.filter_lay);
                d.setCancelable(true);

                d.show();
            }
        });

        iv_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"LinkedIn Profile",Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://in.linkedin.com/in/kaleakshay"));
                startActivity(intent);
            }
        });

        iv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"GitHub Repo",Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/akshaykale/QuickerCar"));
                startActivity(intent);
            }
        });

    }

    private void LoadingDialog() {
       loadingDialog = new Dialog(MainActivity.this);
       loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       loadingDialog.setCancelable(false);
       loadingDialog.setContentView(R.layout.loadingdialog);

       rotateLoading = (RotateLoading) loadingDialog.findViewById(R.id.rotateloading);
       rotateLoading.start();
       loadingDialog.show();
    }

    private void init(){
        m_mainContext = getApplicationContext();
        lv_carList = (ListView) findViewById(R.id.lv_carListView);

        iv_person = (ImageView) findViewById(R.id.menu_about);
        iv_info = (ImageView) findViewById(R.id.menu_about_);
        iv_sort = (ImageView) findViewById(R.id.menu_sort);
        iv_filter = (ImageView) findViewById(R.id.menu_filter);
    }

    public void requestData(String uri){
        if(isOnline()){
            MyTask task = new MyTask();
            task.execute(uri);
        }else{
            Toast.makeText(getApplicationContext(), "OFFLINE", Toast.LENGTH_SHORT).show();
        }
    }

    private void ListViewListener() {
        lv_carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                QuickerCar pro = m_carList.get(position);
                Intent detailsIntent = new Intent(MainActivity.this,DetailsActivity.class);
                detailsIntent.putExtra(DETAILS_INDEX,"" + position);
                detailsIntent.putExtra(DETAILS_INTENT, jsonFeed);
                detailsIntent.putExtra(DETAILS_SORT,""+sortingMethod);

                try {
                    startActivity(detailsIntent);
                } catch (Exception e) {
                    Log.d("!!!!!!", "er");e.printStackTrace();
                }
            }
        });
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }


    /////background work
    ////HTTP requests
    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            jsonFeed = result;
            try {
                Log.d("FEED",""+sortingMethod);
                m_carList = ProductJSONParser.parseFeed(result,sortingMethod);

                lv_carList.setAdapter(new MyAdapter(m_mainContext));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //tv.setText(result);

            rotateLoading.stop();
            loadingDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    class MyAdapter extends BaseAdapter {

        Context context;

        public MyAdapter(Context c) {
            this.context = c;
        }

        @Override
        public int getCount() {
            return m_carList.size();
        }

        @Override
        public Object getItem(int position) {
            return m_carList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.list_row_land, parent,false);

            TextView tv_row_title = (TextView) row.findViewById(R.id.tv_row_title);
            TextView tv_row_price = (TextView) row.findViewById(R.id.tv_row_price);
            TextView tv_row_star = (TextView) row.findViewById(R.id.tv_row_stars);
            TextView tv_row_milage = (TextView) row.findViewById(R.id.tv_row_milage);
            ImageView iv_row_image = (ImageView) row.findViewById(R.id.iv_row_image);
            if( !m_carList.isEmpty()){
                String title = m_carList.get(position).getName()+" ("+m_carList.get(position).getBrand()+")";
                tv_row_title.setText(title);
                tv_row_price.setText("$ "+m_carList.get(position).getPrice());
                tv_row_milage.setText(m_carList.get(position).getMileage());
                tv_row_star.setText("" + m_carList.get(position).getRating());

                String img_url = m_carList.get(position).getImage();
                Log.d("@@@", "" + img_url);
                Picasso.with(context)
                        .load(img_url).resize(290,290)
                        //.centerCrop()
                        .into(iv_row_image);
            }
            return row;
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       // if (id == R.id.action_settings) {
       //     return true;
       // }

        return super.onOptionsItemSelected(item);
    }
}
