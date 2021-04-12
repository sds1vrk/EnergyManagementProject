package com.mir.ven;

//import org.apache.ht
//
//import org.apache.commons.httpclient.*;
//import org.apache.commons.httpclient.methods.*;
//import org.apache.commons.httpclient.params.HttpMethodParams;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HttpClientTutorial {

	private static String url = "http://www.apache.org/";

	public static void main(String[] args) {
		HttpClientBuilder hcb = HttpClientBuilder.create();
		HttpClient client = hcb.build();
		HttpPost post = new HttpPost("http://166.104.28.51:8080/OpenADR2/Simple/2.0b/EiRegisterParty");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("registrationid", "123456789"));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder aa = new StringBuilder();
			String line = "";
			while ((line = rd.readLine()) != null) {
				line = line.replace("    ", "");
				aa.append(line);
				System.out.println(line);
			}
			System.out.println("@@");
			System.out.println(aa);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}