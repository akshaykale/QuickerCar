package com.akshaykale.httpmanager;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.akshaykale.com.akshaykale.quickercar.QuickerCar;
import com.akshaykale.com.akshaykale.quickercar.QuickerCity;

// name, image, price, brand, type, rating, color, engine_cc, mileage, abs_exist, description, link, cities demographic-> city, users

//sort 0-default 1-rating 2-price 3-mileage

public class ProductJSONParser {

	public static ArrayList<QuickerCar> parseFeed( String content, int sort) {

		try {
			JSONArray jsonArray = new JSONArray(content);
			Log.d("FEED","parse "+sort);
			if(sort==0){

			}else if(sort == 1) {
				jsonArray = sortJsonArray(jsonArray, sort);
			}else if(sort == 2 || sort == 3){
				jsonArray = sortJsonArrayPrice(jsonArray, sort);
			}
			ArrayList<QuickerCar> produList = new ArrayList<QuickerCar>();

			for (int i=0; i<jsonArray.length(); i++){

				JSONObject obj = jsonArray.getJSONObject(i);

				QuickerCar product = new QuickerCar();

				product.setName(obj.getString("name"));

				product.setImage(obj.getString("image"));

				float price = Float.parseFloat(obj.getString("price"));
				product.setPrice(price);

				product.setBrand(obj.getString("brand"));

				product.setType(obj.getString("type"));

				float rating = Float.parseFloat(obj.getString("rating"));
				product.setRating(rating);

				product.setColor(obj.getString("color"));

				product.setEngine_cc(obj.getString("engine_cc"));

				product.setMileage(obj.getString("mileage"));

				boolean abs_exist = obj.getString("abs_exist") == "yes"? true : false ;
				product.setAbs_exist(abs_exist);

				product.setDescription(obj.getString("description"));

				product.setLink(obj.getString("link"));

                String citiesData = obj.getString("cities");
                product.setCities(parseCities(citiesData));

				Log.d("%%%%%", "" + product.getDescription());

				produList.add(product);
			}

			return produList;

		} catch (JSONException e) {
			e.printStackTrace();
			Log.d("%%%%%","Errorrrr");
			return null;
		}
	}

    private static ArrayList<QuickerCity> parseCities(String citiesData) {

        try {
            JSONArray jsonArray = new JSONArray(citiesData);

            ArrayList<QuickerCity> cityList = new ArrayList<QuickerCity>();

            for (int i=0; i<jsonArray.length(); i++){

                JSONObject obj = jsonArray.getJSONObject(i);
                QuickerCity city = new QuickerCity();

                city.setCity(obj.getString("city"));
                city.setUsers(Integer.parseInt(obj.getString("users")));

                //Log.d("%%%%%", "" + product.getDescription());

                cityList.add(city);
            }

            return cityList;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("%%%%%","Errorrrr");
            return null;
        }

    }


	public static JSONArray sortJsonArray(JSONArray array, final int sort) throws JSONException{
		List<JSONObject> jsons = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			jsons.add(array.getJSONObject(i));
		}
		Collections.sort(jsons, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject lhs, JSONObject rhs) {
				String lid = null;

				Log.d("FEED","Cmp"+sort);

				try {
					if(sort == 1)
						lid = lhs.getString("rating");
					else if(sort == 2)
						lid = lhs.getString("price");
					else if(sort == 3)
						lid = lhs.getString("mileage");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String rid = null;
				try {
					//rid = rhs.getString("rating");

					if(sort == 1)
						rid = rhs.getString("rating");
					else if(sort == 2)
						rid = rhs.getString("price");
					else if(sort == 3)
						rid = rhs.getString("mileage");

				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Here you could parse string id to integer and then compare.
				return lid.compareTo(rid);
			}
		});
		return new JSONArray(jsons);
	}



	public static JSONArray sortJsonArrayPrice(JSONArray array, final int sort) throws JSONException{
		List<JSONObject> jsons = new ArrayList<JSONObject>();
		for (int i = 0; i < array.length(); i++) {
			jsons.add(array.getJSONObject(i));
		}
		Collections.sort(jsons, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject lhs, JSONObject rhs) {
				Double lid = new Double(1);

				Log.d("FEED","Cmp"+sort);

				try {if(sort == 2)
						lid = Double.parseDouble(lhs.getString("price"));
					else if(sort == 3)
						lid = Double.parseDouble(lhs.getString("mileage").replace("kpl",""));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Double rid = new Double(1);
				try {
					//rid = rhs.getString("rating");
					if(sort == 2)
						rid = Double.parseDouble(rhs.getString("price"));
					else if(sort == 3) {
						rid = Double.parseDouble(rhs.getString("mileage").replace("kpl",""));

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Here you could parse string id to integer and then compare.
				return lid.compareTo(rid);
			}
		});
		return new JSONArray(jsons);
	}
}
