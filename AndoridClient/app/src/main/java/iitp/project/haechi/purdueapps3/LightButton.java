package iitp.project.haechi.purdueapps3;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import iitp.project.haechi.purdueapps3.retrofit.Connector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dnay2 on 2016-09-25.
 */
public class LightButton extends AppCompatActivity {

    //WIfi 연결상태 확인
    WifiConfiguration wfConfig;
    WifiManager wifiManager;

    //UI
    EditText urlEt;
    TextView resultText;

    //데이터 통신
    Connector connector = null;
    Call<String> call;

    //UI로직
    View.OnClickListener bHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(connector == null) return;
            switch (view.getId()) {
                case R.id.on:
                    call = connector.order("on");
                    findViewById(R.id.on).setBackgroundColor(Color.RED);
                    findViewById(R.id.off).setBackgroundColor(Color.LTGRAY);
                    break;
                case R.id.off:
                    call = connector.order("off");
                    findViewById(R.id.on).setBackgroundColor(Color.LTGRAY);
                    findViewById(R.id.off).setBackgroundColor(Color.RED);
                    break;
                case R.id.urlBtn:
                    if (urlEt.getText() != null || !urlEt.getText().equals(""))
                        call = connector.msgByUrl(urlEt.getText().toString());
                    else Toast.makeText(LightButton.this, "입력이 안되어있슴", Toast.LENGTH_SHORT).show();
                    break;
            }

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    resultText.setText(response.body().toString());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    resultText.setText(t.toString());
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_button);
        wfConfig = new WifiConfiguration();

        //WifiConfiguraion 사용
        wfConfig.SSID = "NETWORK ID";
        wfConfig.BSSID = "MAC addr";
        wfConfig.hiddenSSID = true;
        wfConfig.wepKeys = new String[] {"1234567890"};
        wfConfig.wepTxKeyIndex = 0;

        //디바이스의 WIfi네트워크 객체 호출
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        wifiManager.addNetwork(wfConfig);

        //데이터 통신 인스턴스
        connector = Connector.helper.create(Connector.class);

        //UI 사용
        urlEt = (EditText) findViewById(R.id.editText);
        resultText = (TextView) findViewById(R.id.checkString);

        //UI로직 사용
        findViewById(R.id.on).setOnClickListener(bHandler);
        findViewById(R.id.off).setOnClickListener(bHandler);
        findViewById(R.id.urlBtn).setOnClickListener(bHandler);

    }
}
