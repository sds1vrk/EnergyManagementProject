//package RestTopology2.java;
//
//import java.awt.BorderLayout;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Set;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.stream.Collectors;
//
//import javax.swing.JPanel;
//import org.graphstream.graph.Graph;
//import org.graphstream.graph.Node;
//import org.graphstream.graph.implementations.SingleGraph;
//import org.graphstream.ui.swingViewer.ViewPanel;
//import org.graphstream.ui.view.Viewer;
//import org.json.JSONException;
//
//import com.mir.ems.globalVar.global;
//
//import com.mir.rest.clent.*;
//
//@SuppressWarnings("serial")
//public class RestTopology extends JPanel {
//   public static int flag = 0;
//   
//   
//   
//   
//   Graph graph = new SingleGraph("Tutorial 1");
//   public final String emsEdge = "REST_EMS";
//
//   public RestTopology() throws Exception {
//      /*
//       * Graph Layout Setting
//       */
//      java.net.URL emsUrl = EmaTopology.class.getResource("/IMAGE/dddd.png");
//
//      System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
//      Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
//      // viewer.enableAutoLayout();
//      viewer.disableAutoLayout();
//
//      ViewPanel topologyPanel = (ViewPanel) viewer.addDefaultView(false);
//      topologyPanel.setSize(1467, 700);
//      add(topologyPanel);
//
//      setBounds(14, 60, 1467, 700);
//      setLayout(new BorderLayout(0, 0));
//      setVisible(true);
//
//      Node a = graph.addNode(emsEdge);
//
//      a.addAttribute("ui.label", a.getId());
//
//      int sum = 0;
//
//      for (int i = 0; i < 20; i++) {
//
//         sum += (i * 20);
//      }
//
//      a.setAttribute("x", (sum / 40));
//      a.setAttribute("y", 10);
//
//      a.addAttribute("ui.style",
//            "text-alignment: above; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//                  + emsUrl + "');");
//
//      createTopology();
//
//   }
//
//   public void createTopology() throws Exception {
//      java.net.URL gatewayUrl = EmaTopology.class.getResource("/IMAGE/GATEWAYICONS.png");
//      java.net.URL ledUrl = EmaTopology.class.getResource("/IMAGE/GATEWAYICONS.png");
//      java.net.URL deviceURL=EmaTopology.class.getResource("/IMAGE/LED.png");
//      
//
//
//      
//      TimerTask chartUpdaterTask = new TimerTask() {
//
//         Node emaGroup = null;
//         Node deviceGroup = null;
//         
//         Node restGroup=null;
//
//         @Override
//         public void run() {
//        	 
//        	 
//        	 
//             EchoApplication ea=new EchoApplication();
//             EchoApplication_test ea2=new EchoApplication_test();
//             
//             ea.run();
//             ea2.run();
//
//            ArrayList<String> emaString = new ArrayList<String>();
////            emaString.addAll(c)
//            
//            
//            
//            
//            emaString.addAll(global.emaProtocolCoAP.keySet());
//            // emaString.addAll(global.openADRHashMap.keySet());
//
//            String[] emaList = new String[emaString.size()];
//
//            for (int i = 0; i < emaString.size(); i++) {
//               emaList[i] = emaString.get(i).toString();
//            }
//
//            Arrays.sort(emaList);
//
//            ArrayList<String> devString = new ArrayList<String>();
//            // devString.addAll(global.devHashMap.keySet());
//            devString.addAll(global.emaProtocolCoAP_Device.keySet());
//
//            String[] devList = new String[devString.size()];
//
//            for (int i = 0; i < devString.size(); i++) {
//               devList[i] = devString.get(i).toString();
//            }
//
//            Arrays.sort(devList);
//
//            String nodeSet_ADD = graph.getNodeSet().toString();
//            nodeSet_ADD = nodeSet_ADD.replace("[", "");
//            nodeSet_ADD = nodeSet_ADD.replace("]", "");
//            nodeSet_ADD = nodeSet_ADD.replace(" ", "");
//
//            String[] nodeSet_ADD_ARR = nodeSet_ADD.split(",");
//            Set<String> strSet = Arrays.stream(nodeSet_ADD_ARR).collect(Collectors.toSet());
//
//            
//   
//            for (int i = 0; i < emaList.length; i++) {
//               String key = emaList[i];
//               
//               if (!strSet.contains(key.toString())) {
//
//                  try {
//                     emaGroup = graph.addNode(key);
//                     emaGroup.setAttribute("x", (global.cnt * 10));
//                     emaGroup.addAttribute("ui.label", emaGroup.getId());
//
//                     emaGroup.setAttribute("y", 0);
//                     emaGroup.addAttribute("ui.style",
//                           "text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//                                 + gatewayUrl + "');");
//
//                     graph.addEdge(emsEdge + key, emsEdge, key);
//                     global.cnt += 1;
//                     
//                  } catch (Exception e) {
//                  }
//               }
//            }
//
//            String nodeDevSet_ADD = graph.getNodeSet().toString();
//            nodeDevSet_ADD = nodeDevSet_ADD.replace("[", "");
//            nodeDevSet_ADD = nodeDevSet_ADD.replace("]", "");
//            nodeDevSet_ADD = nodeDevSet_ADD.replace(" ", "");
//
//            String[] nodeDevSet_ADD_ARR = nodeDevSet_ADD.split(",");
//            Set<String> strDevSet = Arrays.stream(nodeDevSet_ADD_ARR).collect(Collectors.toSet());
//
//            int devCounting = devList.length;
//
////            if (devList.length > 0) {
////               if (devList[0] == 1) {
////                  devCounting = 1;
////               } else {
////                  devCounting = 5;
////
////               }
////            }
////            //
////            // if (devCounting > 5)
////            // devCounting = 5;
//
//            for (int k = 0; k < devCounting; k++) {
//
//               // System.out.println("여기");
//
//               String dev_key = devList[k] + "";
//               if (!strDevSet.contains(dev_key.toString())) {
//
//                  global.devCnt+= 1;
//
//                  try {
//                     deviceGroup = graph.addNode(dev_key);
//                     deviceGroup.setAttribute("x", (global.devCnt * 10) - 5);
//                     deviceGroup.addAttribute("ui.label", deviceGroup.getId());
//                     deviceGroup.setAttribute("y", -20);
//                     deviceGroup.addAttribute("ui.style",
//                           "text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//                                 + ledUrl + "');");
//
//                     graph.addEdge("DEVICE" + dev_key,
//                           global.emaProtocolCoAP_Device.get(dev_key).getEmaID().toString(), dev_key);
//                     
//                     
//                     System.err.println("이게 의미하는 바는?"+global.emaProtocolCoAP_Device.get(dev_key).getEmaID().toString());
//                  } catch (Exception e) {
//                  }
//               }
//
//               if (global.emaProtocolCoAP_Device.get(dev_key).getState().matches("ON|on|On|oN")) {
//                  // System.out.println("온");
//                  graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style", "fill-color: black;");
//
//               } else if (global.emaProtocolCoAP_Device.get(dev_key).getState().matches("OFF|off|Off|oFF")) {
//                  // System.out.println("오프");
//                  graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style", "fill-color: red;");
//
//               }
//
//            }
//            
//            
//            //rest start
//            try {
//               
//
//      
//               
////               ea.EchoApplication();
////               ea.EchoApplication();
//               
////               System.out.println("여기는"+REST_Structure.getData()+"length"+REST_Structure.getData().length());
//               
//               
//               String test=REST_Structure.getData();
//               
//               
//               
//               
//               if(REST_Structure.getData()!=null &&REST_Structure.getData().length()>3) {
//                  String deviceParse = REST_Structure.getData();
//                  deviceParse=deviceParse.replace("[", "");
//                  deviceParse=deviceParse.replace("]", "");
//                  deviceParse = deviceParse.replace(" ", "");
//                  
//                  deviceParse = deviceParse.replace(" ", "");
//                  
//                  String[] device_ADD_ARR = deviceParse.split(",");
//                  
////                  System.out.println("Parsing 후"+device_ADD_ARR[0]+device_ADD_ARR[1]+device_ADD_ARR[2]);
//                  
//                  
//                  Set<String> strRestSet = Arrays.stream(device_ADD_ARR).collect(Collectors.toSet());
//
//                  int restCounting = device_ADD_ARR.length;
//                  
//                  
//                  System.out.println("strRestSet?!"+strRestSet+"counting"+restCounting);
//                  
//                  for (int k = 0; k < restCounting; k++) {
//
//                     // System.out.println("여기");
//
//                     String dev_key = device_ADD_ARR[k]+"";
//                     
//                     System.out.println("dev_key:"+dev_key);
//                     System.out.println("strRestSet:"+strRestSet);
//                     
//                     if (strRestSet.contains(dev_key.toString())) {
//                        
//                        System.out.println("dev_key?!"+dev_key.toString());
//                        
//                        System.out.println("들어옴?!");
//                        
//                        //꼭필요?>
//                        global.deviceCnt+= 1;
//                        
//
//                        try {
//                           restGroup = graph.addNode(dev_key);
//                           restGroup.setAttribute("x", (global.deviceCnt * 10) - 5);
//                           restGroup.addAttribute("ui.label", restGroup.getId());
//                           restGroup.setAttribute("y", -20);
//                           restGroup.addAttribute("ui.style",
//                                 "text-alignment: under; size: 45px, 45px; shape: rounded-box; size-mode: fit; fill-mode: image-scaled; fill-image: url('"
//                                       + deviceURL + "');");
//                           
//                           //여기를 어떻게 수정해야될까?!
////                           graph.addEdge("DEVICE" + dev_key,
////                                 global.emaProtocolCoAP_Device.get(dev_key).getEmaID().toString(), dev_key);
//                           
//                           graph.addEdge("REST" + dev_key,
//                                 "CLIENT_EMA1", dev_key);
//
//                        } catch (Exception e) {
//                        }
//                     }
//                     
//                     //여기도 수정해야됨
////                     if (global.emaProtocolCoAP_Device.get(dev_key).getState().matches("ON|on|On|oN")) {
////                        // System.out.println("온");
////                        graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style", "fill-color: black;");
////
////                     } else if (global.emaProtocolCoAP_Device.get(dev_key).getState().matches("OFF|off|Off|oFF")) {
////                        // System.out.println("오프");
////                        graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style", "fill-color: red;");
////
////                     }
//                     
////                     
////                     if (global.emaProtocolCoAP_Device.get(dev_key).getState().matches("ON|on|On|oN")) {
////                        // System.out.println("온");
////                        graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style", "fill-color: black;");
////
////                     } else if (global.emaProtocolCoAP_Device.get(dev_key).getState().matches("OFF|off|Off|oFF")) {
////                        // System.out.println("오프");
////                        graph.getEdge("DEVICE" + dev_key).addAttribute("ui.style", "fill-color: red;");
////
////                     }
//
//                  }
//                  
//                  
//               }
//               
//            
//                     
//   
//               
//               
//            } catch (Exception e1) {
//               // TODO Auto-generated catch block
//               e1.printStackTrace();
//            }
//            
//   
//            
//
//            
//            
//
//            
//            
//
//         }
//
//      };
//      Timer timer = new Timer();
//      timer.scheduleAtFixedRate(chartUpdaterTask, 10000, 3000);
//   }
//
//}