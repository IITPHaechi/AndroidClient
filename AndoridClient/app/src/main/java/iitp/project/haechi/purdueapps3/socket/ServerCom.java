package iitp.project.haechi.purdueapps3.socket;

import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by dnay2 on 2016-10-04.
 */
public class ServerCom extends AsyncTask<Void, Void, Void> {

    Socket s;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    SocketServer  server;

    boolean onAir = false;

    public ServerCom() {
    }

    public ServerCom(Socket s, SocketServer server) {
        this.s = s;
        this.server = server;
        try{
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    void senMsg(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String str = "";
        try {
            while (onAir){
                str = dis.readUTF();
                server.sendMsg(str);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
