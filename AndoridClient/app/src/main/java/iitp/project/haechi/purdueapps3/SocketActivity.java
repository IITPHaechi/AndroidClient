package iitp.project.haechi.purdueapps3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by dnay2 on 2016-10-04.
 */
public class SocketActivity extends AppCompatActivity {

    private static final String IP_ADDRESS = "172.24.1.1";
    private static final int PORT_ = 8888;

    private static final String CONN = "연결시도";
    private static final String SUCCESS = "연결성공";
    private static final String FAILURE = "연결실패";
    private static final String DISCONN = "연결해제";

    Socket mSocket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    ReadTask rTask;
    connTask cTask;

    EditText etIpaddress, etPort, etOrder, console;

    private boolean onAir = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        Log.d("test1","SocketActivity start");
        etIpaddress = (EditText) findViewById(R.id.etIpaddress);
        etPort = (EditText) findViewById(R.id.etPort);
        etOrder = (EditText) findViewById(R.id.etOrder);
        console = (EditText) findViewById(R.id.console);
    }

    private void connect(String IP, int PORT) {
        cTask = new connTask(IP, PORT);
        cTask.execute();
    }

    private void disconnect() {
        if (mSocket != null) {
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


    public void toastM(String msg) {
        Toast.makeText(SocketActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    class connTask extends AsyncTask<Void, Void, Boolean> {
        String ipAddress;
        int portNumber;

        public connTask(@NonNull String ipAddress, @NonNull int portNumber) {
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
                consoleAdd(FAILURE);
                return false;
            }
            publishProgress();
            return true;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            consoleAdd(SUCCESS);
            if (rTask != null) {
                if (rTask.getStatus() != Status.RUNNING && rTask.getStatus() != Status.FINISHED)
                    rTask.execute();
            } else {
                rTask = new ReadTask();
                rTask.execute();
            }
        }
    }

    public void closeSocket() {
        if (mSocket != null) {
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

    @Override
    protected void onPause() {
        super.onPause();
        closeSocket();
    }

    class ReadTask extends AsyncTask<Void, String, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String[] msg = new String[2];
            while (onAir) {
                try {
                    msg[0] = actionReceive();
                    publishProgress(msg);
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
            String msg = values[0];
            consoleAdd("낯선이 : " + msg);
            Log.d("test", msg);
        }
    }

    //받기
    private String actionReceive() {
        String data = "";
        try{
            BufferedReader b = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while((data = b.readLine()) != null){
                sb.append(data).append("\n");
            }
        }catch (IOException e){
            Log.d("TAG", e.getMessage());
        }
        return data;
    }

    //보내기
    private void actionSend(String order) {
        try {
            outputStream.write(order.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        consoleAdd("나 : " + order);
    }

    //콘솔 내용 갱신 메소드
    public void consoleAdd(String str) {
        String s = console.getText().toString();
        s += "\n" + str;
        console.setText(s);
    }


    public void btnClicked(View v) {
        switch (v.getId()) {
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
                actionSend(etOrder.getText().toString());
                break;
            case R.id.direct:
                connect(IP_ADDRESS, PORT_);
                break;
        }

    }

}
