package iitp.project.haechi.purdueapps3.socket;

import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by dnay2 on 2016-10-04.
 */
public class SocketClient {



    SocketClientListener listener;

    Socket mSocket;
    DataInputStream inputStream;
    DataOutputStream outputStream;

    private static final String CONN = "연결시도";
    private static final String SUCCESS = "연결성공";
    private static final String FAILURE = "연결실패";
    private static final String DISCONN = "연결해제";

    EditText console;

    ReadTask rTask;

    boolean onAir = true;

    public SocketClient() {
        //singletone
    }

    public interface SocketClientListener {
        void onConsoleChange();
    }

    public void connectSocket(String ipAddress, int portNumber) {
        new connTask(ipAddress, portNumber).execute();
    }

    class connTask extends AsyncTask<Void, Void, Boolean> {
        String ipAddress;
        int portNumber;


        public connTask(String ipAddress, int portNumber) {
            this.ipAddress = ipAddress;
            this.portNumber = portNumber;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                mSocket = new Socket(ipAddress, portNumber);
                inputStream = new DataInputStream(mSocket.getInputStream());
                outputStream = new DataOutputStream(mSocket.getOutputStream());
                outputStream.flush();
                rTask = new ReadTask();
                onAir = true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            if (rTask != null) rTask.execute();
            if (b) consoleAdd(SUCCESS);
            else consoleAdd(FAILURE);
        }
    }

    public void closeSocket() {
        if(mSocket != null){
            try {
                onAir = false;
                outputStream.close();
                inputStream.close();
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocket = null;
        }
    }

    class ReadThread extends Thread{

        String msg = "";
        Message iHandler;

        @Override
        public void run() {
            super.run();

            while (onAir) {
                try {
                    BufferedReader b = new BufferedReader(new InputStreamReader(inputStream));
                    msg = b.readLine();
                } catch (Exception e) {
                    Log.d("test", e.getMessage());
                    e.printStackTrace();
                }
            }

        }
    }
    class ReadTask extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String[] msg = new String[2];
            while (onAir) {
                try {
                    BufferedReader b = new BufferedReader(new InputStreamReader(inputStream));
                    msg[0] = b.readLine();
//                    onProgressUpdate(msg);
                } catch (Exception e) {
                    Log.d("test", e.getMessage());
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //에딧텍스트에 msg  출력 하여 채팅 모양 생성
            String str = values[0];
            consoleAdd("낯선이 : " + str);
            Log.d("test",str);
        }
    }

    public void sendAction(String order) {

            try {
            outputStream.write(order.getBytes());
        } catch (Exception e){
            e.printStackTrace();
        }

        consoleAdd("나 : " + order);

    }

    public boolean isSuccess() {
        return mSocket != null;
    }

    public void consoleAdd(String str) {
        String s = console.getText().toString();
        s += "\n" + str;
        console.setText(s);
    }

    public void setConsole(EditText console) {
        this.console = console;
    }

    public void setListener(SocketClientListener listener) {
        this.listener = listener;
    }

}
