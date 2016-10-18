//package iitp.project.haechi.purdueapps3;
//
//
//import iitp.project.haechi.purdueapps3.joystick.dummy;
//import retrofit2.Call;
//import retrofit2.http.GET;
//import retrofit2.http.Query;
//
///**
// * Created by cholmink on 16. 9. 25..
// */
//public interface NetworkService {
//    @GET("/on")
//    Call<dummy> turnonLight();
//
//    @GET("/off")
//    Call<dummy> turnoffLight();
//
//    @GET("/command/")
//    Call<dummy> moveRobot(
//            @Query("left") int isLeft,
//            @Query("duration") int duration,
//            @Query("move") int stopORmove
//    );
//
//}
