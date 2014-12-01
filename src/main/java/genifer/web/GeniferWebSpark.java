package genifer.web;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package genifer.java;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import spark.Request;
//import spark.Response;
//import spark.Route;
//import spark.Spark;
//import spark.utils.IOUtils;
//
///**
// *
// * @author me
// */
//public class GeniferWebSpark extends Spark {
//
//    final static Genifer genifer = new Genifer();
//    public static Map<String, Object> staticpages = new HashMap();
//
//    public static String getLocalTextFile(File file) throws IOException {
//        int len;
//        char[] chr = new char[4096];
//        final StringBuffer buffer = new StringBuffer();
//        final FileReader reader = new FileReader(file);
//        try {
//            while ((len = reader.read(chr)) > 0) {
//                buffer.append(chr, 0, len);
//            }
//        } finally {
//            reader.close();
//        }
//        return buffer.toString();
//    }
//
//    public static void getLocalBinaryFile(File file, OutputStream os) throws IOException {
//
//    	byte[] buf = new byte[1024];
//        FileInputStream in = new FileInputStream(file);
//
//        OutputStreamWriter out = new OutputStreamWriter(os);
//        int count = 0;
//        while ((count = in.read(buf)) >= 0) {
//            os.write(buf, 0, count);
//        }
//        in.close();
//        out.close();
//
//    }
//
//    public static String getStaticTextFile(String path) throws IOException {
//        if (staticpages.containsKey(path)) {
//            return (String) staticpages.get(path);
//        }
//        String content = getLocalTextFile(new File("./web/" + path));
//        staticpages.put(path, content);
//        return content;
//    }
//
//    public static void getStaticBinaryFile(String path, OutputStream os) throws IOException {
//    	getLocalBinaryFile(new File("./web/" + path), os);
//    }
//
//    public static void main(String[] args) {
//
//        setPort(9090); // Spark will run on port 9090
//
//        Route formula;
//
//        post(formula = new Route("/formula") {
//            @Override
//            public Object handle(Request rqst, Response rspns) {
//                rspns.header("Content-type", "text/html");
//                String c = rqst.queryParams("c").toString();	// formula as JSON
//                String r = genifer.sendFormula(c).toString();
//                return r;		// ignore return value for now
//            }
//        });
//
//        get(formula = new Route("/formula/:id") {
//			@Override
//            public Object handle(Request rqst, Response rspns) {
//                rspns.header("Content-type", "text/html");
//                String id = rqst.params(":id").toString();		// index into KB
//                String r = genifer.getFormula(id).toString();
//                return r;
//            }
//			});
//
//        Route formularize;
//
//        post(formularize = new Route("/formularize") {
//            @Override
//            public Object handle(Request rqst, Response rspns) {
//                rspns.header("Content-type", "text/html");
//				// c = list of links, d = list of words
//                String c = rqst.queryParams("c").toString();
//                String d = rqst.queryParams("d").toString();
//                String r = genifer.formularize(c, d).toString();
//                return r;
//            }
//        });
//        get(formularize);
//
//        Route eval;
//
//        post(eval = new Route("/eval") {
//            @Override
//            public Object handle(Request rqst, Response rspns) {
//                rspns.header("Content-type", "text/html");
//                String c = rqst.queryParams("c").toString();
//                String r = genifer.eval(c).toString();
//                r = r.replaceAll("\n", "<br/>");
//                return r;
//            }
//        });
//        get(eval);
//
//        Route saveDatabase;
//
//        post(new Route("/saveDatabase") {
//            @Override
//            public Object handle(Request rqst, Response rspns) {
//					 // System.out.println("saving database....");
//                rspns.header("Content-type", "text/html");
//					 // String list = rqst.queryParams().toString();
//					 // System.out.println("params are: " + list);
//                String data = rqst.queryParams("data").toString();
//                // System.out.println("data is: " + data.substring(0, 100));
//					 try {
//						  PrintWriter out = new PrintWriter("web/database-out.txt");
//						  out.print(data);
//						  out.close();
//		          }	catch (FileNotFoundException ex) {
//						  Logger.getLogger(GeniferWebSpark.class.getName()).log(Level.SEVERE, null, ex);
//					 }
//					 return "Database saved";
//				}
//			});
//
//
//        get(new Route("/*") {
//
//            @Override
//            public Object handle(Request rqst, Response rspns) {
//
//            	String url = rqst.pathInfo();
//                String page = "index.html";
//                if (!url.equals("/")) {
//                    String xpage = url.substring(url.indexOf("/") + 1);
//                    if (page.length() > 0) {
//                        page = xpage;
//                    }
//                }
//
//                System.out.println(rqst.pathInfo());
//                System.out.println(rqst.url() + " -> " + page);
//
//                try {
//                    if (page.endsWith(".jpg")) {
//                        rspns.header("Content-type", "image/jpg");
//
//                        getStaticBinaryFile(page, rspns.raw().getOutputStream());
//                        return null;
//                    } else if (page.endsWith(".png")) {
//                        rspns.header("Content-type", "image/png");
//                        getStaticBinaryFile(page, rspns.raw().getOutputStream());
//                        return null;
//                    } else if (page.endsWith(".ogg")) {
//                        rspns.header("Content-type", "audio/ogg");
//                        getStaticBinaryFile(page, rspns.raw().getOutputStream());
//                        return null;
//                    } else if (page.endsWith(".gif")) {
//                        rspns.header("Content-type", "image/gif");
//                        getStaticBinaryFile(page, rspns.raw().getOutputStream());
//                        return null;
//					} else if (page.endsWith(".ico")) {
//                        rspns.header("Content-type", "image/x-icon");
//                        getStaticBinaryFile(page, rspns.raw().getOutputStream());
//                        return null;
//                    } else if (page.endsWith(".html") || page.endsWith(".htm")) {
//                    	rspns.header("Content-type", "text/html; charset=utf-8");
//                        return getStaticTextFile(page);
//                    } else {
//                    	return getStaticTextFile(page);
//                    }
//                } catch (IOException ex) {
//                    try {
//                        halt(404, getStaticTextFile("404.html"));
//                    } catch (IOException ex1) {
//                        Logger.getLogger(GeniferWebSpark.class.getName()).log(Level.SEVERE, null, ex1);
//                    }
//                }
//                return null;
//            }
//        });
//
//	}
//}
