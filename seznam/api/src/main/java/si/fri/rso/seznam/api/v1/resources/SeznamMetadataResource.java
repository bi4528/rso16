package si.fri.rso.seznam.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.seznam.lib.CocktailDBResponse;
import si.fri.rso.seznam.lib.SeznamMetadata;
import si.fri.rso.seznam.lib.SeznamMetadataRequest;
import si.fri.rso.seznam.services.beans.SeznamMetadataBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;



@Log
@ApplicationScoped
@Path("/seznam")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SeznamMetadataResource {

    private Logger log = Logger.getLogger(SeznamMetadataResource.class.getName());

    @Inject
    private SeznamMetadataBean SeznamMetadataBean;


    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all cocktails.", summary = "Get all cocktails.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of all cocktails.",
                    content = @Content(schema = @Schema(implementation = SeznamMetadata.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getSeznamMetadata() {
        log.info("Trying to get all cocktails.");
        List<SeznamMetadata> SeznamMetadata = SeznamMetadataBean.getSeznamMetadataFilter(uriInfo);
        log.info("Returning all cocktails.");
        return Response.status(Response.Status.OK).entity(SeznamMetadata).build();
    }

    @Operation(description = "Get cocktail list by user.", summary = "Get cocktail list by user.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of cocktails by user",
                    content = @Content(schema = @Schema(implementation = SeznamMetadata.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    @Path("/user/{user}")
    public Response getSeznamMetadataByUser(@Parameter(description = "User name.", required = true)
                                                @PathParam("user") String user) {
        log.info("Trying to get cocktails for user " + user + ".");
        List<SeznamMetadata> SeznamMetadata = SeznamMetadataBean.getSeznamMetadataByUser(user);
        log.info("Returning cocktails for user " + user + ".");
        return Response.status(Response.Status.OK).entity(SeznamMetadata).build();
    }


    @Operation(description = "Get cocktail by db id.", summary = "Get cocktail by db id.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Cocktail metadata",
                    content = @Content(
                            schema = @Schema(implementation = SeznamMetadata.class))
            )})
    @GET
    @Path("/{SeznamMetadataId}")
    public Response getSeznamMetadata(@Parameter(description = "Metadata ID.", required = true)
                                     @PathParam("SeznamMetadataId") Integer SeznamMetadataId) {
        log.info("Trying to get cocktail with id " + SeznamMetadataId + ".");
        SeznamMetadata SeznamMetadata = SeznamMetadataBean.getSeznamMetadata(SeznamMetadataId);

        if (SeznamMetadata == null) {
            log.info("Cocktail with id " + SeznamMetadataId + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        log.info("Returning cocktail with id " + SeznamMetadataId + ".");
        return Response.status(Response.Status.OK).entity(SeznamMetadata).build();
    }

    @Operation(description = "Get a singleton list of a cocktail by id.",
            summary = "Get a singleton list of a cocktail by id.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Singleton list of cocktail by id.",
                    content = @Content(
                            schema = @Schema(implementation = CocktailDBResponse.class))
            )})
    @GET
    @Path("/id/{CocktailDBId}")
    public Response getCocktailDBResponseById(@Parameter(description = "Cocktail id.", required = true)
                                          @PathParam("CocktailDBId") String CocktailDBId) {
        log.info("Trying to get cocktail with id " + CocktailDBId + ".");
        CocktailDBResponse cocktailDBResponse = SeznamMetadataBean.getCocktailDBResponseById(CocktailDBId);

        if (cocktailDBResponse == null) {
            log.info("Cocktail with id " + CocktailDBId + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        log.info("Returning cocktail with id " + CocktailDBId + ".");
        return Response.status(Response.Status.OK).entity(cocktailDBResponse).build();
    }
    @Operation(description = "Add cocktail.", summary = "Add cocktail.")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "cocktail successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createSeznamMetadata(@RequestBody(
            description = "DTO object with image metadata.",
            required = true, content = @Content(
            schema = @Schema(implementation = SeznamMetadataRequest.class))) SeznamMetadataRequest seznamMetadataRequest) {

        log.info("Trying to add cocktail for request " + seznamMetadataRequest.toString() + ".");

        SeznamMetadata seznamMetadata;

        if ((seznamMetadataRequest.getCocktailId() == null || seznamMetadataRequest.getUser() == null)) {
            log.info("Validation error for request " + seznamMetadataRequest.toString() + ".");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            seznamMetadata = SeznamMetadataBean.createSeznamMetadata(seznamMetadataRequest.getCocktailId(), seznamMetadataRequest.getUser());
        }
        log.info("Returning cocktail "+ seznamMetadata.toString() + "for request " + seznamMetadataRequest.toString() + ".");
        return Response.status(Response.Status.CREATED).entity(seznamMetadata).build();

    }


    @Operation(description = "Update cocktail for an image.", summary = "Update cocktail")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Cocktail successfully updated."
            )
    })
    @PUT
    @Path("{SeznamMetadataId}")
    public Response putSeznamMetadata(@Parameter(description = "Metadata ID.", required = true)
                                     @PathParam("SeznamMetadataId") Integer SeznamMetadataId,
                                     @RequestBody(
                                             description = "DTO object with image metadata.",
                                             required = true, content = @Content(
                                             schema = @Schema(implementation = SeznamMetadata.class)))
                                             SeznamMetadata SeznamMetadata){
        log.info("Trying to update cocktail with id " + SeznamMetadataId + ".");
        SeznamMetadata = SeznamMetadataBean.putSeznamMetadata(SeznamMetadataId, SeznamMetadata);

        if (SeznamMetadata == null) {
            log.info("Cocktail with id " + SeznamMetadataId + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        log.info("Returning cocktail with id " + SeznamMetadataId + ".");
        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @Operation(description = "Delete cocktail from a list.", summary = "Delete cocktail")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Cocktail successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{SeznamMetadataId}")
    public Response deleteSeznamMetadata(@Parameter(description = "Metadata ID.", required = true)
                                        @PathParam("SeznamMetadataId") Integer SeznamMetadataId){
        log.info("Trying to delete cocktail with id " + SeznamMetadataId + ".");
        boolean deleted = SeznamMetadataBean.deleteSeznamMetadata(SeznamMetadataId);

        if (deleted) {
            log.info("Cocktail with id " + SeznamMetadataId + " deleted.");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            log.info("Cocktail with id " + SeznamMetadataId + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Operation(description = "Get all users.", summary = "Get all users")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of all users",
                    content = @Content(schema = @Schema(implementation = String.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    @Path("/users")
    public Response getUsers() {
        log.info("Trying to get all users.");
        List<String> users = SeznamMetadataBean.getUsers();
        log.info("Returning all users.");
        return Response.status(Response.Status.OK).entity(users).build();
    }


}
