//package com.mir.vtn.controller;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Iterator;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import com.mir.ems.globalVar.global;
//import com.mir.vtn.global.Global;
//import com.mir.vtn.profile.EventDetail;
//
//@Controller
//public class FrontController_Original {
//
//	enum EventDetails {
//		venID, dtstart_str, duration, market_context_id, priority, response_required_type_id, vtn_comment, test_event, signal_name_id, signal_type_id, payload_value
//	}
//
//	/*
//	 * 
//	 * =========================================================================
//	 * 
//	 * Restful Front Pages Methods
//	 * 
//	 * =========================================================================
//	 * 
//	 */
//
//	@GetMapping("/")
//	public String welcome() {
//		return "home";
//	}
//
//	@GetMapping("/index")
//	public String index() {
//		return "index";
//	}
//
//	@GetMapping("/event")
//	public String event() {
//		System.out.println("이벤트!");
//		return "event";
//	}
//
//	// @GetMapping("/test")
//	// public void test(HttpServletRequest request, HttpServletResponse response)
//	// throws IOException {
//	//
//	// String ipADDR = request.getRemoteAddr();
//	// System.out.println(ipADDR);
//	// //
//	// // String aa = "hyunjin";
//	//// response.setStatus(200);
//	//// OutputStream os = response.getOutputStream();
//	//// System.out.println(response.);
//	// new PushHandler(ipADDR, response).start();
//	//
//	// //
//	// // response.setStatus(200);
//	// // OutputStream os = response.getOutputStream();
//	// // os.write(aa.getBytes());
//	// // os.flush();
//	// // os.close();
//	//
//	// }
//
//	@PostMapping("/events")
//	public ResponseEntity<Object> createStudent(@RequestBody byte[] payload) throws UnsupportedEncodingException, JSONException {
//
//		EventDetail eventDetail = new EventDetail();
//
//		String requestBody = new String(payload, "UTF-8");
//		
//		
//		System.out.println("requestBODY:@@"+requestBody);
//		
//		
//		
//		
//		
//		
//		if(requestBody.contains("&")) {
//			String[] parseRequestBody = requestBody.split("&");
//
//			for (int i = 2; i < parseRequestBody.length - 1; i++) {
//
//				if (parseRequestBody[i].contains("%5D")) {
//
//					// System.err.println("Filter" +
//					// parseRequestBody[i].split("%5D=")[0].split("%5B")[1]);
//					//
//					// System.err.println("Payload" + parseRequestBody[i].split("%5D=")[1]);
//
//					saveEventDetail(eventDetail, parseRequestBody[i].split("%5D=")[0].split("%5B")[1],
//							parseRequestBody[i].split("%5D=")[1]);
//				}
//
//			}
//		}
//		else {
//			JSONObject json=new JSONObject(requestBody);
//			
//			String venID=json.getString("EMAID");
//			
//			String value=Integer.toString(json.getInt("value"));
//			
//			
//			eventDetail.setVenID(venID);
//			eventDetail.setDtStart("2018-08-07");
//			eventDetail.setEvent_flag(false);
//			eventDetail.setDuration(1);
//			eventDetail.setEvent_flag(false);
//			eventDetail.setMarket_context_id("http://MarketContext1");
//			eventDetail.setPriority(0);
//			eventDetail.setResponse_required_type_id("always");
//			eventDetail.setVtn_comment("1");
//			eventDetail.setTest_event(false);
//			eventDetail.setSignal_name_id("simple");
//			eventDetail.setSignal_type_id("delta");
//			eventDetail.setPayload_value(Double.parseDouble(value));
//			
//			
//			
//		
//		}
//
//
//		
//		
//		
//		
//		
//
//		
//		//ADDED
//		if (eventDetail.getVenID().matches("PULL|pull|Pull")) {
//			changeRegisteredVenSeqNum(eventDetail);
//		}
//
//		else if(eventDetail.getVenID().matches("PUSH|push|Push")) {
//			
//			Iterator<String> it = Global.getRegisterVEN().keySet().iterator();
//
//			while (it.hasNext()) {
//
//				String key = it.next();
//				Global.eventInfo.put(Global.getRegisterVEN().get(key).getStrVenID(), eventDetail);
//				Global.registerVEN.get(key).setThreshold(eventDetail.getPayload_value());
//				Global.initiater.eventOccur(eventDetail);
//
//			}
//			
//			
//		}
//		else {
//			Global.eventInfo.put(eventDetail.getVenID(), eventDetail);
//
//			String registeredVenID = getKeyFromValue(eventDetail.getVenID());
//
//			boolean pullModel = Global.registerVEN.get(registeredVenID).isPullModel();
//
//			System.out.println(registeredVenID);
//			System.out.println("pull Model" + pullModel);
//			
//
//
//			if (pullModel) {
//				
//				
//				if(global.autoDR) {
//					while(global.autoDRCNT<global.autoDRTOTAL) {
//						changeRegisteredVenSeqNum(registeredVenID);
//						Global.registerVEN.get(registeredVenID).setThreshold(eventDetail.getPayload_value());
//					}
//					global.autoDRCNT=0;
//				}
//				else {
//					changeRegisteredVenSeqNum(registeredVenID);
//					Global.registerVEN.get(registeredVenID).setThreshold(eventDetail.getPayload_value());
//				}
//			} else if (!pullModel) {
//
//				if(global.autoDR) {
//					
//					while(global.autoDRCNT<global.autoDRTOTAL) {
//						try {
//							Thread.sleep(1000);
//							Global.initiater.eventOccur(eventDetail);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						
//					}
//					global.autoDRCNT=0;
//				}
//				
//				Global.initiater.eventOccur(eventDetail);
//
//			}
//
//		}
//
//		return new ResponseEntity<Object>("event", HttpStatus.OK);
//
//	}
//
//	/*
//	 * 
//	 * =========================================================================
//	 * 
//	 * Functions Methods
//	 * 
//	 * =========================================================================
//	 * 
//	 */
//
//	
//	public void changeRegisteredVenSeqNum(EventDetail eventDetail) {
//
//		Iterator<String> it = Global.getRegisterVEN().keySet().iterator();
//
//		while (it.hasNext()) {
//
//			String key = it.next();
//			Global.getRegisterVEN().get(key).setSeqNum(2);
//			Global.eventInfo.put(Global.getRegisterVEN().get(key).getStrVenID(), eventDetail);
//			Global.registerVEN.get(key).setThreshold(eventDetail.getPayload_value());
//
//		}
//
//	}
//
//	public void changeRegisteredVenSeqNum(String registeredVenID) {
//
//		Global.getRegisterVEN().get(registeredVenID).setSeqNum(2);
//		//
//		// System.err.println(Global.getRegisterVEN().get(registeredVenID).getStrVenID());
//		// System.err.println(Global.getRegisterVEN().get(registeredVenID).getSeqNum());
//
//	}
//
//	public String getKeyFromValue(Object strVenID) {
//
//		for (Object key : Global.getRegisterVEN().keySet()) {
//
//			if (Global.getRegisterVEN().get(key).getStrVenID().equals(strVenID)) {
//
//				return key.toString();
//			}
//		}
//
//		return null;
//	}
//
//	public void saveEventDetail(EventDetail eventDetail, String id, String value) {
//
//		if (value.contains("+"))
//			value = value.replace("+", " ");
//		if (value.contains("%3A"))
//			value = value.replace("%3A", ":");
//		if (value.contains("%2F"))
//			value = value.replace("%2F", "/");
//
//		EventDetails eventDetails = EventDetails.valueOf(id);
//
//		switch (eventDetails) {
//
//		case venID:
//			eventDetail.setVenID(value);
//			break;
//		case dtstart_str:
//			eventDetail.setDtStart(value);
//			break;
//		case duration:
//			eventDetail.setDuration(Integer.parseInt(value));
//			break;
//		case market_context_id:
//			eventDetail.setMarket_context_id(value);
//			break;
//		case priority:
//			eventDetail.setPriority(Integer.parseInt(value));
//			break;
//		case response_required_type_id:
//			eventDetail.setResponse_required_type_id(value);
//			break;
//		case vtn_comment:
//			eventDetail.setVtn_comment(value);
//			break;
//		case test_event:
//			eventDetail.setTest_event(Boolean.parseBoolean(value));
//			break;
//		case signal_name_id:
//			eventDetail.setSignal_name_id(value);
//			break;
//		case signal_type_id:
//			eventDetail.setSignal_type_id(value);
//			break;
//		case payload_value:
//			eventDetail.setPayload_value(Double.parseDouble(value));
//			break;
//		}
//	}
//
//}
