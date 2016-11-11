package iitp.project.haechi.purdueapps3.socket;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by dnay2 on 2016-10-13.
 */
public class SocketClientTask extends AsyncTask<Void, String, Boolean> {

    private static final String TEXT_CONN = "연결시도";
    private static final String TEXT_SUCCESS = "연결성공";
    private static final String TEXT_FAILURE = "연결실패";
    private static final String TEXT_DISCONN = "연결해제";

    private String ipAddress;
    int portNumber;
    private Context context;
    private EditText console = null;

    Socket mSocket;
    DataInputStream dis;
    DataOutputStream dos;

    public SocketClientTask(String ipAddress, int portNumber) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
    }

    public void setConsole(EditText console) {
        this.console = console;
    }

    @Override
    protected void onPreExecute() {
        if(console != null) consoleAdd(TEXT_CONN);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(console != null) consoleAdd(TEXT_DISCONN);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            mSocket = new Socket(ipAddress, portNumber);
            dis = new DataInputStream(mSocket.getInputStream());
            dos = new DataOutputStream(mSocket.getOutputStream());

            if (mSocket.isConnected()) publishProgress(TEXT_SUCCESS);
            byte[] buffer = new byte[4096];
            int read = dis.read(buffer, 0, 4096);
            while (read != -1) {
                byte[] tempData = new byte[read];
                System.arraycopy(buffer, 0, tempData, 0, read);
                String str = new String(tempData);
                publishProgress("낯선이 : " + str);
                read = dis.read(buffer, 0, 4096);
            }
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress(TEXT_FAILURE);
            return false;
        }finally {
            disconnection();
        }

        return true;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if (console != null) {
            consoleAdd(values[0]);
        }
    }

    //콘솔 내용 갱신 메소드
    public void consoleAdd(String str) {
        if(console != null){
            String s = console.getText().toString();
//            s += "\n" + str;
            console.setText(str);
            console.setSelection(console.length());
        }
    }

    //보내기
    public void actionSend(String order, long time) {
        if(mSocket != null){
            try {
                dos.write(order.getBytes());
                dos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("joystick", "발현  :  " + order + "time : " + time % 10000);
            onProgressUpdate("나 : " + order);
        }else{
//            onProgressUpdate("연결이 안되어있음");
        }

    }

    //디스커넥션
    public void disconnection(){
        try{
            if(mSocket != null){
                dos.close();
                dis.close();
                mSocket.close();
                mSocket = null;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        this.onCancelled();
    }

    public void runThread(Thread thread){
        thread.run();
    }
}
