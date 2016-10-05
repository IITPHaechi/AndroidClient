package iitp.project.haechi.purdueapps3;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by dnay2 on 2016-10-04.
 */
public class SocketAction extends AppCompatActivity {

    private static final String IP_ADDRESS = "172.24.1.1";
    private static final int PORT_ = 8888;

    boolean onAir = false;

    Socket socket;

    Handler handler = new Handler();

    DataInputStream dis;
    DataOutputStream dos;

    private static final String CONN = "연결시도";
    private static final String SUCCESS = "연결성공";
    private static final String FAILURE = "연결실패";
    private static final String DISCONN = "연결해제";

    EditText etIpaddress, etPort, etOrder, console;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        etIpaddress = (EditText) findViewById(R.id.etIpaddress);
        etPort = (EditText) findViewById(R.id.etPort);
        etOrder = (EditText) findViewById(R.id.etOrder);
        console = (EditText) findViewById(R.id.console);

    }

    public void addConsole(String str){
        String s = console.getText().toString();
        s += "\n" + str;
        console.setText(s);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disconnect();
    }

    public void connect(String IP, int PORT){
        try{
            socket = new Socket(IP, PORT);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            ReadThread trd = new ReadThread();
            onAir = true;
            trd.start();
            addConsole(SUCCESS);
        }catch(Exception e){
        }
    }

    class ReadThread extends Thread{
        String msg = "";
        public void run(){
            while(onAir){
                try{
                    BufferedReader b = new BufferedReader(new InputStreamReader(dis));
                    while((msg = b.readLine()) != null && msg.length() > 0){
                        msg += "\n";
                        msg += msg;
                    }
                    handler.post(new Runnable(){
                        public void run(){
                            addConsole("낯선이 : " +msg);
                        }
                    });
                }catch(Exception e){
                }
            }
        }
    }



    private void disconnect(){
        try{
            onAir = false;
            dis.close();
            dos.close();
            socket.close();
        }catch(Exception e){

        }
    }

    public void sendAction(String order){
        try {
            dos.writeUTF(order);
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        addConsole("나 : " + order);
    }

    public void btnClicked(View v){
        switch (v.getId()){
            case R.id.btnConn:
                connect(
                        etIpaddress.getText().toString(),
                        Integer.parseInt(etPort.getText().toString())
                );
                break;
            case R.id.btnDisconn:
                disconnect();
                break;
            case R.id.btnSend:
                sendAction(etOrder.getText().toString());
                break;
            case R.id.direct:
                connect(IP_ADDRESS, PORT_);
                break;
        }

    }
}
