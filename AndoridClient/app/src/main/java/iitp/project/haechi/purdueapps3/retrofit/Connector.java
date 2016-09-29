package iitp.project.haechi.purdueapps3.retrofit;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by dnay2 on 2016-09-25.
 */
public interface Connector {

    //명령을 받아서 쓰는 것
    @GET("/{order}")
    Call<String> order(
            @Path("order") String order
    );

    @GET
    Call<String> msgByUrl(
            @Url String url
    );

    Retrofit helper = new Retrofit.Builder()
            .baseUrl("http://172.24.1.1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
