package com.pdfwebapi;

import static spark.Spark.*;

import com.itextpdf.text.DocumentException;
import javafx.util.Pair;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import com.itextpdf.text.pdf.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException, DocumentException {
        port(1337);
        staticFileLocation("public");

        get("/hello", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("name", "Robin!");

            // The hello.ftl file is located in directory:
            // src/test/resources/spark/template/freemarker
            return new ModelAndView(attributes, "views/hello.ftl");
        }, new FreeMarkerEngine());

        get("/", (request, response) -> {
            response.redirect("index.html");
            return null;
        });

        get("generate/:file", (request, response) -> {
            OutputStream outputStream = response.raw().getOutputStream();
            String file = request.params("file");
            PdfReader reader = null;
            PdfStamper stamper = null;

            //init
            try {
                reader = new PdfReader("templates/" + file);
            } catch (IOException e) {
                return "Could not load file: " + file + "\n" +
                       "Did you supply template filename like this: 'http://url/generate/name-of-template.pdf'?";
            }
            try {
                stamper = new PdfStamper(reader, outputStream);
            } catch (DocumentException e) {
                return "Something went wrong on the server.";
            }

            //insert data
            AcroFields fields = stamper.getAcroFields();
            for(String property : request.queryParams()){
                if (fields.getFields().containsKey(property)) {
                    fields.setField(property, request.queryParams(property));
                }else{
                    return "Error: Property '" + property +
                            "' could not be found in pdf template. Did you spell correctly?";
                }
            }

            //clean up
            stamper.setFormFlattening(true);
            stamper.close();
            reader.close();

            outputStream.flush();
            outputStream.close();
            response.type("application/pdf");
            return null;
        });
    }
}
