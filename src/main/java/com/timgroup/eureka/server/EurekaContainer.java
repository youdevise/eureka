package com.timgroup.eureka.server;

import org.simpleframework.http.Address;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;

import com.timgroup.eureka.server.responders.StaticResourceResponder;

public final class EurekaContainer implements Container {

    private Container route(Address target) {
        final String[] path = target.getPath().getSegments();
        
        if (path.length == 0) {
            return new StaticResourceResponder("/content/welcome.html");
        }
        
        return new StaticResourceResponder("/content/" + path[0]);
    }

    @Override
    public void handle(Request req, Response resp) {
        route(req.getAddress()).handle(req, resp);
    }
}