package io.kowalski.claptrap.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {

    @GET
    public Map<String, String> test() {
        Map<String, String> test = new HashMap<>();

        test.put("Hello", "World");

        return test;
    }

}
