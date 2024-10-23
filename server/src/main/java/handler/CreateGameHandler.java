package handler;

import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;


public class CreateGameHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        return new Gson().toJson("{ \"gameID\": 1234 }");
    }
}
