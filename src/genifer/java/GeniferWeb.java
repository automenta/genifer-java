/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genifer.java;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 *
 * @author me
 */
public class GeniferWeb extends Spark {
    
    final static Genifer genifer = new Genifer();
    
    public static Map<String, String> staticpages = new HashMap();

    public static String getLocalFile(File file) throws IOException {
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

    public static String getStaticPage(String path) throws IOException {
        if (staticpages.containsKey(path)) {
            return staticpages.get(path);
        }
        String content = getLocalFile(new File("./web/" + path));
        staticpages.put(path, content);
        return content;
    }


    public static void main(String[] args) {
        
        setPort(9090); // Spark will run on port 9090

        get(new Route("/*") {

            @Override
            public Object handle(Request rqst, Response rspns) {
                rspns.header("Content-type", "text/html");
				//rspns.header("Content-type", "image/jpg");

                String url = rqst.pathInfo();
                String page = "index.html";
                if (!url.equals("/")) {                
                    String xpage = url.substring(url.indexOf("/")+1);
                    if (page.length() > 0)
                        page = xpage;
                }

                System.out.println(rqst.pathInfo());
                System.out.println(rqst.url() + " -> " + page);

                try {
                    return getStaticPage(page);
                } catch (IOException ex) {
                    try {
                        halt(404, getStaticPage("404.html"));
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
                String r = genifer.formularize(c).toString();
                r = r.replaceAll("\n", "<br/>");
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

