package xyz.bradjohnson.jukebox.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.bradjohnson.jukebox.entity.Jukebox;
import xyz.bradjohnson.jukebox.service.JukeboxService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.regex.Pattern;

/**
 * API class for jukebox microservice
 */
@Path("/")
@Api(value = "Jukebox Api", tags = {"Jukebox-Filter"})
@Produces(MediaType.APPLICATION_JSON)
public class JukeboxResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(JukeboxResource.class);

    private final JukeboxService jukeboxService;

    @Inject
    public JukeboxResource(JukeboxService jukeboxService) {
        this.jukeboxService = jukeboxService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("jukebox")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Jukebox[].class),
            @ApiResponse(code = 400, message = "Invalid SettingId format", response = String.class),
            @ApiResponse(code = 404, message = "ID not found", response = String.class),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @ApiParam(value = "settingId", required = true)
    @ApiOperation(value = "Retrieve a jukebox based on the compatibility with a component setting")
    public Response getJukebox(
            @NotNull
            @QueryParam("settingId")
                    String settingId,
            @QueryParam("model")
                    String model,
            @QueryParam("offset")
            @DefaultValue("0")
                    Long offset,
            @QueryParam("limit")
            @DefaultValue("20")
                    Long limit) {
        Pattern guidPattern = Pattern.compile("[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}");

        if (!guidPattern.matcher(settingId).find()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(Json.pretty("SettingId must conform to UUID pattern"))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        try {
            return Response
                    .ok(jukeboxService.getJukeboxes(settingId, model, offset, limit).toArray(), MediaType.APPLICATION_JSON)
                    .build();
        } catch (NotFoundException ex) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(Json.pretty(ex.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
