package handler;

import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        System.out.println("Made It");
        return new Gson().toJson("{ 'username':'', 'password':'', 'email':'' }");
    }
}
