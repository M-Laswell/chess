package handler;

import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;

public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        return new Gson().toJson("{ \"username\":\"\", \"authToken\":\"\" }");
    }
}
