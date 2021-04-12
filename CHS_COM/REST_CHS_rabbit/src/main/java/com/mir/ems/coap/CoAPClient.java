package com.mir.ems.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class CoAPClient extends Thread{

	/*
	 * public static void main(String[] args) { CoapClient client = new
	 * CoapClient(); CoapResponse response;
	 * 
	 * String path = "/mir/smarthome"; String uri = "coap://127.0.0.1:5683" +
	 * path + "?text=everybody"; //query option value: everybody
	 * 
	 * client.setURI(uri); response = client.get();
	 * 
	 * if(response != null){ System.out.println("code: " + response.getCode());
	 * System.out.println("options: " + response.getOptions());
	 * System.out.println("payload: " +
	 * Utils.toHexString(response.getPayload())); System.out.println("text: " +
	 * response.getResponseText()); System.out.println("advanced: " +
	 * Utils.prettyPrint(response)); }
	 * 
	 * uri = "coap://127.0.0.1:5683" + path;
	 * 
	 * client.setURI(uri);
	 * 
	 * System.out.println("Post: " + client.post("everybody",
	 * MediaTypeRegistry.TEXT_PLAIN).getResponseText()); }
	 */
	CoapResponse response;

	String addr, path;
	byte[] text;
	
	@SuppressWarnings("unused")
	public CoAPClient(String addr, String path, byte[] text) {
		
		this.addr = addr;
		this.path = path;
		this.text = text;
//		
//		CoapClient client = new CoapClient();
////		CoapResponse response;
//		String uri = "coap://" + addr + path;
//		// String uri = "coap://166.104.143.224:5683" + path;
//		// String uri = "coap://127.0.0.1:5683" + path + "?text=" + text;
//		// //query option value: everybody
//
//		client.setURI(uri);
//		/*
//		 * response = client.get();
//		 * 
//		 * if(response != null){ System.out.println("code: " +
//		 * response.getCode()); System.out.println("options: " +
//		 * response.getOptions()); System.out.println("payload: " +
//		 * Utils.toHexString(response.getPayload()));
//		 * System.out.println("text: " + response.getResponseText());
//		 */
//		// System.out.println("advanced: " + Utils.prettyPrint(response));
//		// }
//		if (path.contains("dimming") || path.contains("control")) {
//			client.setURI(uri);
//			response = client.put(text, MediaTypeRegistry.TEXT_PLAIN);
//			System.out.println("PUT MESSAGE: " + text);
//			System.out.println("Path: " + path);
//			// System.out.println(response.isSuccess());
//			/*
//			 * System.out.println("POST"); client.post(text,
//			 * MediaTypeRegistry.TEXT_PLAIN);
//			 */
//		} else {
//			String c_msg = new String(text);
//			client.setURI(uri);
//			response = client.put(c_msg, MediaTypeRegistry.TEXT_PLAIN);
//			System.out.println("PUT MESSAGE: " + c_msg);
//			System.out.println("Path: " + path);
//		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		CoapClient client = new CoapClient();
//		CoapResponse response;
		String uri = "coap://" + addr + path;
		// String uri = "coap://166.104.143.224:5683" + path;
		// String uri = "coap://127.0.0.1:5683" + path + "?text=" + text;
		// //query option value: everybody

		client.setURI(uri);
		/*
		 * response = client.get();
		 * 
		 * if(response != null){ System.out.println("code: " +
		 * response.getCode()); System.out.println("options: " +
		 * response.getOptions()); System.out.println("payload: " +
		 * Utils.toHexString(response.getPayload()));
		 * System.out.println("text: " + response.getResponseText());
		 */
		// System.out.println("advanced: " + Utils.prettyPrint(response));
		// }
		if (path.contains("dimming") || path.contains("control")) {
			client.setURI(uri);
			response = client.put(text, MediaTypeRegistry.TEXT_PLAIN);
			System.out.println("PUT MESSAGE: " + text);
			System.out.println("Path: " + path);
			// System.out.println(response.isSuccess());
			/*
			 * System.out.println("POST"); client.post(text,
			 * MediaTypeRegistry.TEXT_PLAIN);
			 * 
			 */
		}
		super.run();
	}
	public CoAPClient(String addr, String path, String text) {
		CoapClient client = new CoapClient();
//		CoapResponse response;
		String uri = "coap:/" + addr + path;
		// String uri = "coap://166.104.143.224:5683" + path;
		// String uri = "coap://127.0.0.1:5683" + path + "?text=" + text;
		// //query option value: everybody

		client.setURI(uri);
		response = client.put(text, MediaTypeRegistry.TEXT_PLAIN);
	}

}
