package com.akshaykale.httpmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import android.net.http.AndroidHttpClient;
import android.util.Log;

public class HttpManager {
	
	public static String getData(String url) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(url));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("RESPONSE", responseString);
        return responseString;

        /*
		AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
		HttpGet request = new HttpGet(url);
		HttpResponse response;
		Log.d("URL",url);
		try {
			response = client.execute(request);
            String jsonFeed = EntityUtils.toString(response.getEntity());
			Log.d("URL",jsonFeed);
			return jsonFeed;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}finally{
			client.close();
		}
		*/
	}
}
