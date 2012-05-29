/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genifer.java;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.utils.IOUtils;

/**
 *
 * @author me
 */
public class GeniferWeb extends Spark {

    final static Genifer genifer = new Genifer();
    public static Map<String, Object> staticpages = new HashMap();

    public static String getLocalTextFile(File file) throws IOException {
        int len;
        char[] chr = new char[4096];
        final StringBuffer buffer = new StringBuffer();
        final FileReader reader = new FileReader(file);
        try {
            while ((len = reader.read(chr)) > 0) {
                buffer.append(chr, 0, len);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }

    public static void getLocalBinaryFile(File file, OutputStream os) throws IOException {
    	
    	byte[] buf = new byte[1024];
        FileInputStream in = new FileInputStream(file);

        OutputStreamWriter out = new OutputStreamWriter(os);
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            os.write(buf, 0, count);
        }
        in.close();
        out.close();

    }

    public static String getStaticTextFile(String path) throws IOException {
        if (staticpages.containsKey(path)) {
            return (String) staticpages.get(path);
        }
        String content = getLocalTextFile(new File("./web/" + path));
        staticpages.put(path, content);
        return content;
    }

    public static void getStaticBinaryFile(String path, OutputStream os) throws IOException {
    	getLocalBinaryFile(new File("./web/" + path), os);
    }

    public static void main(String[] args) {

        setPort(9090); // Spark will run on port 9090
       

        get(new Route("/*") {

            @Override
            public Object handle(Request rqst, Response rspns) {
                rspns.header("Content-type", "text/html");


                String url = rqst.pathInfo();
                String page = "index.html";
                if (!url.equals("/")) {
                    String xpage = url.substring(url.indexOf("/") + 1);
                    if (page.length() > 0) {
                        page = xpage;
                    }
                }

                System.out.println(rqst.pathInfo());
                System.out.println(rqst.url() + " -> " + page);

                try {
                    if (page.endsWith(".jpg")) {
                        rspns.header("Content-type", "image/jpg");

                        getStaticBinaryFile(page, rspns.raw().getOutputStream());
                        return null;
                    } else if (page.endsWith(".png")) {
                        rspns.header("Content-type", "image/png");
                        getStaticBinaryFile(page, rspns.raw().getOutputStream());
                        return null;
                    } else if (page.endsWith(".css")) {
                        rspns.header("Content-type", "text/css");
                        getStaticBinaryFile(page, rspns.raw().getOutputStream());
                        return null;
                    } else if (page.endsWith(".js")) {
                        rspns.header("Content-type", "text/javascript");
                        getStaticBinaryFile(page, rspns.raw().getOutputStream());
                        return null;
					} else if (page.endsWith(".ico")) {
                        rspns.header("Content-type", "image/x-icon");
                        getStaticBinaryFile(page, rspns.raw().getOutputStream());
                        return null;
                    } else {
                        return getStaticTextFile(page);
                    }
                } catch (IOException ex) {
                    try {
                        halt(404, getStaticTextFile("404.html"));
                    } catch (IOException ex1) {
                        Logger.getLogger(GeniferWeb.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
                return null;
            }
        });
        
        Route formularize;
        post(formularize = new Route("/formularize") {

            @Override
            public Object handle(Request rqst, Response rspns) {
                rspns.header("Content-type", "text/html");
                String c = rqst.queryParams("c").toString();
                String d = rqst.queryParams("d").toString();
                String r = genifer.formularize(c, d).toString();
                // r = r.replaceAll("\n", "<br/>");
                return r;
            }
        });
        get(formularize);
        
        Route eval;
        post(eval = new Route("/eval") {

            @Override
            public Object handle(Request rqst, Response rspns) {
                rspns.header("Content-type", "text/html");
                String c = rqst.queryParams("c").toString();
                String r = genifer.eval(c).toString();
                r = r.replaceAll("\n", "<br/>");
                return r;
            }
        });
        get(eval);

    }
}
