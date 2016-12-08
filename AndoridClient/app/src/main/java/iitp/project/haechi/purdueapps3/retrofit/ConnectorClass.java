package iitp.project.haechi.purdueapps3.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dnay2 on 2016-09-25.
 */
public class ConnectorClass {

    private Connector connector;
    private Call<String> call;
    private Response<String> result;

    public ConnectorClass() {
        connector = Connector.helper.create(Connector.class);
    }

    public Response<String> Operation(String url){

        call = connector.order(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                result = response;
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        return result;
    }

}
