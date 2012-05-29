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
        final static byte[] buf = new byte[1024];
        final static char[] chr = new char[4096];

    public static String getLocalTextFile(File file) throws IOException {
        int len;
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
//        if (staticpages.containsKey(path)) {
//            return (String)staticpages.get(path);
//        }
        getLocalBinaryFile(new File("./web/" + path), os);
        //staticpages.put(path, content);
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
