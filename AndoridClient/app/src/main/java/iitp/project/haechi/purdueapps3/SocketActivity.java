package iitp.project.haechi.purdueapps3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
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

    private static final String MY_IP = "172.24.1.122";
    private static final int MY_PORT = 12345;

    private static final String TEXT_CONN = "연결시도";
    private static final String TEXT_SUCCESS = "연결성공";
    private static final String TEXT_FAILURE = "연결실패";
    private static final String TEXT_DISCONN = "연결해제";

    Socket mSocket = null;
    DataInputStream inputStream = null;
    DataOutputStream outputStream = null;
    ReadTask rTask = null;
    connTask cTask = null;

    EditText etIpaddress, etPort, etOrder, console;

    private boolean onAir = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        etIpaddress = (EditText) findViewById(R.id.etIpaddress);
        etPort = (EditText) findViewById(R.id.etPort);
        etOrder = (EditText) findViewById(R.id.etOrder);
        console = (EditText) findViewById(R.id.console);

        findViewById(R.id.btn_right).setOnTouchListener(touchListener);
        findViewById(R.id.btn_left).setOnTouchListener(touchListener);
        findViewById(R.id.btn_back).setOnTouchListener(touchListener);
        findViewById(R.id.btn_front).setOnTouchListener(touchListener);
    }

    private void connect(String IP, int PORT) {
        consoleAdd(TEXT_CONN);
        if(mSocket == null){
            cTask = new connTask(IP, PORT);
            cTask.execute();
        } else {
            consoleAdd("이미 연결이 되어있음");
        }
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
        }else{
            onAir = false;
        }
    }


    public void toastM(String msg) {
        Toast.makeText(SocketActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    class connTask extends AsyncTask<Void, String, Boolean> {
        String ipAddress;
        int portNumber;

        public connTask(@NonNull String ipAddress, @NonNull int portNumber) {
            this.ipAddress = ipAddress;
            this.portNumber = portNumber;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
//                Log.d("socketTest", "try socket connect // IP : "+ipAddress +"  port : "+portNumber);
                mSocket = new Socket(ipAddress, portNumber);
                inputStream = new DataInputStream(mSocket.getInputStream());
                outputStream = new DataOutputStream(mSocket.getOutputStream());
//                rTask = new ReadTask();
                onAir = true;
                if(mSocket.isConnected()) publishProgress(TEXT_SUCCESS);
                byte[] buffer = new byte[4096];
                int read = inputStream.read(buffer, 0, 4096);
                while(read != -1){
                    byte[] tempData = new byte[read];
                    System.arraycopy(buffer, 0, tempData, 0, read);
                    String str = new String(tempData);
                    publishProgress(str);
                    read = inputStream.read(buffer, 0, 4096);
                }
            } catch (IOException e) {
                e.printStackTrace();
                publishProgress(TEXT_FAILURE);
                onAir = false;
                return false;
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            consoleAdd(values[0]);
            if (rTask != null) {
                if (rTask.getStatus() != Status.RUNNING && rTask.getStatus() != Status.FINISHED) {
                    rTask.execute();
                    Log.d("test get message", "rTask is not null");
                }
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
        String data = "";

        public ReadTask(){

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String[] msg = new String[2];
            Log.d("test get Message", "readTask is running onAir = " + onAir);
            while (onAir) {
                try {
                    Log.d("test get Message", "input 찾는중");
                    BufferedReader b = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    while((data = b.readLine()) != null){
                        sb.append(data).append("\n");
                    }
                    msg[0] = data;
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
            //에딧텍스트에 msg 출력 하여 채팅 모양 생성
            consoleAdd("낯선이 : " + values[0]);
            Log.d("test get Message", values[0]);
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
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cTask.onProgressUpdate("나 : " + order);
    }

    //콘솔 내용 갱신 메소드
    public void consoleAdd(String str) {
        String s = console.getText().toString();
        s += "\n" + str;
        console.setText(s);
        console.scrollTo(0, console.getLayout().getLineCount());
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
                consoleAdd(TEXT_DISCONN);
                break;
            case R.id.btnSend:
                actionSend(etOrder.getText().toString());
                break;
            case R.id.direct:
                connect(IP_ADDRESS, PORT_);
                break;
        }

    }

    String left = "left=";
    String right = "right=";
    String time = "time=1";
    String ordd = "";
    public void btnMoved(View v){
        switch (v.getId()){
            case R.id.btn_right:
                ordd = left + "1," + right + "0," +time;
                break;
            case R.id.btn_front:
                ordd = left + "1," + right + "1," +time;
                break;
            case R.id.btn_left:
                ordd = left + "0," + right + "1," +time;
                break;
            case R.id.btn_back:
                ordd = left + "-1," + right + "-1," +time;
                break;
        }
        actionSend(ordd);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_right:
                    ordd = left + "1," + right + "0," +time;
                    break;
                case R.id.btn_front:
                    ordd = left + "1," + right + "1," +time;
                    break;
                case R.id.btn_left:
                    ordd = left + "0," + right + "1," +time;
                    break;
                case R.id.btn_back:
                    ordd = left + "-1," + right + "-1," +time;
                    break;
            }
            actionSend(ordd);
        }
    };

    long mTime =  0;
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()){
                case R.id.btn_right:
                    ordd = left + "1," + right + "0," +time;
                    break;
                case R.id.btn_front:
                    ordd = left + "1," + right + "1," +time;
                    break;
                case R.id.btn_left:
                    ordd = left + "0," + right + "1," +time;
                    break;
                case R.id.btn_back:
                    ordd = left + "-1," + right + "-1," +time;
                    break;
            }

            if(System.currentTimeMillis() > mTime + 100){
                actionSend(ordd);
            }

            return false;
        }
    };

}
