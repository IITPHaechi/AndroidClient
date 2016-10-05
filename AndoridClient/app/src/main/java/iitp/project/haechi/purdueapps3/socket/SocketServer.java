package iitp.project.haechi.purdueapps3.socket;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by dnay2 on 2016-10-04.
 */
public class SocketServer {
    ArrayList<ServerCom> list = new ArrayList<>();
    ServerCom serverCom;

    boolean onAir = false;

    public SocketServer(){

    }

    class ServerTask extends AsyncTask<Void, Void, String>{
        SocketServer server;
        int port;

        public ServerTask() {
        }

        public ServerTask(SocketServer server, int port) {
            this.server = server;
            this.port = port;
        }

        @Override
        protected String doInBackground(Void... voids) {
            ServerSocket ss = null;
            Socket s = null;
            try {
                ss = new ServerSocket(port);
            }catch (IOException e){
                e.printStackTrace();
            }
            Log.d("test", "서버생성됨");
            while(onAir){
                try {
                    s = ss.accept();
                    Log.d("test", "새로운 접속자" + s);
                    serverCom = new ServerCom(s, server);
                    list.add(serverCom);
                    serverCom.execute();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    public void startServer(){
            new ServerTask().execute();

    }

    public void sendMsg(String msg){
        for(ServerCom s : list){
            s.senMsg(msg);
        }
    }

}
