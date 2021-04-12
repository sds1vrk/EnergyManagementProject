package com.mir.ven;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class HTTPRequest {

	private String subPath;
	private StringBuilder httpResponse = new StringBuilder();

	public HTTPRequest(String subPath) {
		setSubPath(subPath);

		HttpClientBuilder hcb = HttpClientBuilder.create();
		HttpClient client = hcb.build();
		HttpPost post = new HttpPost(new Global().VTN_URL + getSubPath());
		
		try {
			
			
			//전송 메시지 
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("registrationid", "123456789"));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			//POST
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				line = line.replace("    ", "");
				setHttpResponse(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println(new HTTPRequest("EiRegisterParty").getHttpResponse());

	}

	public StringBuilder getHttpResponse() {
		return httpResponse;
	}

	public HTTPRequest setHttpResponse(String httpResponse) {
		this.httpResponse = this.httpResponse.append(httpResponse);
		return this;
	}

	public String getSubPath() {
		return subPath;
	}

	public HTTPRequest setSubPath(String subPath) {
		this.subPath = subPath;
		return this;
	}

}
