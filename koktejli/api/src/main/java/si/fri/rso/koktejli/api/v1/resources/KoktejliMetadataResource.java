package si.fri.rso.koktejli.api.v1.resources;

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
import si.fri.rso.koktejli.lib.CocktailDBResponse;
import si.fri.rso.koktejli.lib.KoktejliMetadata;
import si.fri.rso.koktejli.services.beans.KoktejliMetadataBean;

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
@Path("/koktejli")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KoktejliMetadataResource {

    private Logger log = Logger.getLogger(KoktejliMetadataResource.class.getName());

    @Inject
    private KoktejliMetadataBean KoktejliMetadataBean;


    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all image metadata.", summary = "Get all metadata")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of image metadata",
                    content = @Content(schema = @Schema(implementation = KoktejliMetadata.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getKoktejliMetadata() {

        List<KoktejliMetadata> KoktejliMetadata = KoktejliMetadataBean.getKoktejliMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(KoktejliMetadata).build();
    }


    @Operation(description = "Get metadata for an image.", summary = "Get metadata for an image")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Image metadata",
                    content = @Content(
                            schema = @Schema(implementation = KoktejliMetadata.class))
            )})
    @GET
    @Path("/{KoktejliMetadataId}")
    public Response getKoktejliMetadata(@Parameter(description = "Metadata ID.", required = true)
                                     @PathParam("KoktejliMetadataId") Integer KoktejliMetadataId) {

        KoktejliMetadata KoktejliMetadata = KoktejliMetadataBean.getKoktejliMetadata(KoktejliMetadataId);

        if (KoktejliMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(KoktejliMetadata).build();
    }

    @Operation(description = "Get list of multiple cocktails for a cocktail by name.",
            summary = "Get list of multiple cocktails for a cocktail by name.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of multiple cocktails by name.",
                    content = @Content(
                            schema = @Schema(implementation = CocktailDBResponse.class))
            )})
    @GET
    @Path("/name/{CocktailDBName}")
    public Response getCocktailDBResponseByName(@Parameter(description = "Cocktail name.", required = true)
                                        @PathParam("CocktailDBName") String CocktailDBName) {
        log.info("Trying to get cocktails by name: " + CocktailDBName);
        CocktailDBResponse cocktailDBResponse = KoktejliMetadataBean.getCocktailDBResponseByName(CocktailDBName);

        if (cocktailDBResponse == null) {
            log.info("Cocktail by name: " + CocktailDBName + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        log.info("Cocktail by name: " + CocktailDBName + " found.");
        return Response.status(Response.Status.OK).entity(cocktailDBResponse).build();
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
        log.info("Trying to get cocktail by id: " + CocktailDBId);
        CocktailDBResponse cocktailDBResponse = KoktejliMetadataBean.getCocktailDBResponseById(CocktailDBId);

        if (cocktailDBResponse == null) {
            log.info("Cocktail by id: " + CocktailDBId + " not found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        log.info("Cocktail by id: " + CocktailDBId + " found.");
        return Response.status(Response.Status.OK).entity(cocktailDBResponse).build();
    }
    @Operation(description = "Add image metadata.", summary = "Add metadata")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Metadata successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createKoktejliMetadata(@RequestBody(
            description = "DTO object with image metadata.",
            required = true, content = @Content(
            schema = @Schema(implementation = KoktejliMetadata.class))) KoktejliMetadata KoktejliMetadata) {

        if ((KoktejliMetadata.getTitle() == null || KoktejliMetadata.getDescription() == null || KoktejliMetadata.getUri() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            KoktejliMetadata = KoktejliMetadataBean.createKoktejliMetadata(KoktejliMetadata);
        }

        return Response.status(Response.Status.CONFLICT).entity(KoktejliMetadata).build();

    }


    @Operation(description = "Update metadata for an image.", summary = "Update metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Metadata successfully updated."
            )
    })
    @PUT
    @Path("{KoktejliMetadataId}")
    public Response putKoktejliMetadata(@Parameter(description = "Metadata ID.", required = true)
                                     @PathParam("KoktejliMetadataId") Integer KoktejliMetadataId,
                                     @RequestBody(
                                             description = "DTO object with image metadata.",
                                             required = true, content = @Content(
                                             schema = @Schema(implementation = KoktejliMetadata.class)))
                                             KoktejliMetadata KoktejliMetadata){

        KoktejliMetadata = KoktejliMetadataBean.putKoktejliMetadata(KoktejliMetadataId, KoktejliMetadata);

        if (KoktejliMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @Operation(description = "Delete metadata for an image.", summary = "Delete metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Metadata successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{KoktejliMetadataId}")
    public Response deleteKoktejliMetadata(@Parameter(description = "Metadata ID.", required = true)
                                        @PathParam("KoktejliMetadataId") Integer KoktejliMetadataId){

        boolean deleted = KoktejliMetadataBean.deleteKoktejliMetadata(KoktejliMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }





}
