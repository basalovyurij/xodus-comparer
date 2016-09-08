package org.xoduscomparer.web;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class UtilsResource {

    private static final String API_CONTEXT = "/api/v1";

    //private final TodoService todoService;

    public UtilsResource() {
        //this.todoService = todoService;
        setupEndpoints();
    }

    private void setupEndpoints() {
        post(API_CONTEXT + "/todos", "application/json", (request, response) -> {
            //todoService.createNewTodo(request.body());
            response.status(201);
            return response;
        }, new JsonTransformer());
/*
        get(API_CONTEXT + "/todos/:id", "application/json", (request, response)

                -> todoService.find(request.params(":id")), new JsonTransformer());

        get(API_CONTEXT + "/todos", "application/json", (request, response)

                -> todoService.findAll(), new JsonTransformer());

        put(API_CONTEXT + "/todos/:id", "application/json", (request, response)

                -> todoService.update(request.params(":id"), request.body()), new JsonTransformer());*/
    }

}