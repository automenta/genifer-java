/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genifer.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


@Path("/genifer")
@Api("/genifer")
public class GeniferWeb  {
    
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
//						  Logger.getLogger(GeniferWeb.class.getName()).log(Level.SEVERE, null, ex);
//					 }
//					 return "Database saved";
//				}
//			});

    @POST
    @ApiOperation("Save Database")
    @Path("/saveDatabase")
    public Response get() {
        return Response.ok(new SamplePojo("Federico", 1234)).build();
    }

    
    
//        get(formula = new Route("/formula/:id") {
//			@Override
//            public Object handle(Request rqst, Response rspns) {
//                rspns.header("Content-type", "text/html");
//                String id = rqst.params(":id").toString();		// index into KB
//                String r = genifer.getFormula(id).toString();
//                return r;
//            }
//			});    

    @GET
    @ApiOperation("Get Formula by ID")
    @Path("/formula/{id}")
    public Response getWithPathParam(@PathParam("id") String formulaID) {
        return Response.ok(new SamplePojo("Hello " + formulaID, 333)).build();
    }

    @GET
    @ApiOperation("Sample endpoint with query param")
    @Path("/hello-with-query-param")
    public Response getWithQueryParam(@QueryParam("name") String name) {
        return Response.ok(new SamplePojo("Hello " + name, 444)).build();
    }
    
    
    
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

//
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
//
//
//
}
