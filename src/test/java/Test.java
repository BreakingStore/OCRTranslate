import me.niotgg.output.OutputFrame;
import me.niotgg.request.Requester;
import org.json.JSONArray;
import org.json.JSONObject;

public class Test {

    public static void main(String[] args) {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("email", "aaa");
        jsonObject1.put("password", "aaa");
        jsonArray.put(jsonObject1);
        jsonObject.put("accounts", jsonArray);

        System.out.println(jsonObject.toString());
    }

}
