/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genifer.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import genifer.Genifer;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;


@Path("/genifer")
@Api("/genifer")
public class GeniferWeb  {
    
    private final Genifer genifer;

    public GeniferWeb(Genifer genifer) {
        this.genifer = genifer;
    }
    
    
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
    public Response saveDatabase() {
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

        return Response.ok(new SamplePojo("Federico", 1234)).build();
    }

    

    @GET
    @ApiOperation("Get Formula by ID")
    @Path("/formula/{id}")
    public Response getFormula(@PathParam("id") String formulaID) {        
        String r = genifer.getFormula(formulaID).toString();
        return Response.ok(r).build();
    }
    

    @POST
    @ApiOperation("Add Formula")
    @Path("/formula")
    public Response addFormula(@FormParam("c") String formulaContent) {
        String r = genifer.sendFormula(formulaContent).toString();
        return Response.ok(r).build();
    }    
    
    @POST
    @ApiOperation("Formularize")
    @Path("/formularize")
    public Response formularize(@FormParam("c") String listOfLinks, @FormParam("d") String listOfWords) {
        String r = genifer.formularize(listOfLinks, listOfWords).toString();
        return Response.ok(r).build();
    }        

    @POST
    @ApiOperation("Evaluate")
    @Path("/eval")
    public Response eval(@FormParam("c") String input) {
        String r;
        if (input!=null) {
            r = genifer.eval(input).toString();
            r = r.replaceAll("\n", "<br/>"); //do this on client-side, to save some transferred bytes
        }
        else {
            r = "null";
        }
        return Response.ok(r).build();
    }        
    
    @GET
    @ApiOperation("Sample endpoint with query param")
    @Path("/hello-with-query-param")
    public Response getWithQueryParam(@QueryParam("name") String name) {
        return Response.ok(new SamplePojo("Hello " + name, 444)).build();
    }
    
}
