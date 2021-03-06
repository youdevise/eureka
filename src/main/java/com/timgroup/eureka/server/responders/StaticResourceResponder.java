package com.timgroup.eureka.server.responders;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Hashtable;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.Status;
import org.simpleframework.http.core.Container;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

public final class StaticResourceResponder implements Container {

    private static final Map<String, String> MIME_TYPES = new Hashtable<String, String>() {{
        put("html", "text/html; charset=utf-8");
        put("js",   "text/javascript; charset=utf-8");
        put("css",  "text/css; charset=utf-8");
        put("json", "application/json; charset=utf-8");
        put("ico",  "image/x-icon");
        put("jpg",  "image/jpeg");
        put("png",  "image/png");
        put("gif",  "image/gif");
        put("ogg",  "audio/ogg");
        put("mp3",  "audio/mpeg");
        put("wav",  "audio/wav");
    }};

    private final String name;
    private final String mimeType;

    public StaticResourceResponder(String name) {
        this.name = name;
        final String extension = name.substring(name.lastIndexOf('.') + 1);
        mimeType = MIME_TYPES.get(extension);
    }

    @Override
    public void handle(Request req, Response resp) {
        System.out.println("Handling request..");

        resp.setValue("Content-Type", mimeType);
        resp.setValue("Server", "Eureka/1.0 (Simple 5.1.6)");
        resp.setDate("Date", System.currentTimeMillis());
//        resp.setDate("Last-Modified", lastModified);
//        resp.setDate("Expires", expires);
//        resp.setContentLength(contentLength);
        resp.setStatus(Status.OK);

        InputStream input = null;
        OutputStream output = null;
        try {
            input = getClass().getResourceAsStream(name);
            output = resp.getOutputStream();
            ByteStreams.copy(input, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                Closeables.close(input, true);
                Closeables.close(output, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

