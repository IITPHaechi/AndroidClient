package iitp.project.haechi.purdueapps3;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by dnay2 on 2016-10-17.
 */
public class WebSocketActivity extends AppCompatActivity {

    private WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectWebSocket();

    }

    public static class PlaceholderFragment extends Fragment{
        public PlaceholderFragment() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }


    private void connectWebSocket(){
        URI uri = null;
        try{
            uri = new URI("ws://172.24.1.1:8084/");
        }catch (URISyntaxException e){
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri, new Draft_10()) {

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                mWebSocketClient.send("Hello from "+ Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String message) {
                final String str = message;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WebSocketActivity.this, "this message : " + str, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("testapp", "Closed " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.d("testapp", "Error : " + ex.getMessage());
            }
        };

        mWebSocketClient.connect();
    }

    int cnt = 1;
    public void sendMessage(View v){
        mWebSocketClient.send("hi i am jacob " + cnt);
        cnt++;
    }
}
