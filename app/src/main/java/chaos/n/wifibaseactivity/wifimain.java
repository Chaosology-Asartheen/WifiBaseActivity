package chaos.n.wifibaseactivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;



public class wifimain extends FragmentActivity implements WifiBase.WifiBaseListener {

    private static final int TIMEOUT = 10;
    private WifiBase mWifiBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        mWifiBase = new WifiBase(this);
    }

    @Override
    public String getWifiSSID() {
        return "net3455";
    }

    @Override
    public String getWifiPass() {
        return "123456789";
    }

    @Override
    public int getSecondsTimeout() {
        return TIMEOUT;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

}

//
//public class wifimain extends WifiBaseActivity {
//
//    private static final int TIMEOUT = 5;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wifi);
//    }
//
//    @Override
//    protected int getSecondsTimeout() {
//        return TIMEOUT;
//    }
//
//    @Override
//    protected String getWifiPass() {
//        return "123456789";
//    }
//
//    @Override
//    protected String getWifiSSID() {
//        return "net3455";
//    }
//
//}
