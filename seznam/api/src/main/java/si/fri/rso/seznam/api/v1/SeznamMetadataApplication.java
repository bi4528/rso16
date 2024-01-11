package si.fri.rso.seznam.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "User cocktail list API", version = "v1",
        contact = @Contact(email = "bi4528@student.uni-lj.si"),
        license = @License(name = "dev"), description = "API for managing User cocktail list."),
        servers = @Server(url = "http://34.89.140.13:8080/"))
@ApplicationPath("/v1")
public class SeznamMetadataApplication extends Application {

}
