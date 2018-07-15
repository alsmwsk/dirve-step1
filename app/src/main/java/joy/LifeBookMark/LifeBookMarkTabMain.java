package joy.LifeBookMark;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import com.joy.db.DbCommand;
import com.joy.db.DbInterface;
import com.joy.db.DbRecordset;
import joy.LifeBookMark.Daeri.MainActivity;
import joy.LifeBookMark.Event.EventActivity;
import joy.LifeBookMark.More.MoreActivity;
import joy.LifeBookMark.Store.StoreActivity;
import joy.common.CommonUtil;
import joy.common.JoyLocationUtil;
import joy.common.JoyNUtil;

public class LifeBookMarkTabMain extends TabActivity {
    private final String TAG = "LifeBookMarkTabMain";
    private CommonUtil commonUtil = null;
    private TabHost tabhost;

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {
        private LoginAsyncTask() {
        }

        protected Void doInBackground(Void... params) {
            Log.d("LifeBookMarkTabMain", "LoginAsyncTask start...");
            Cursor cursor = LifeBookMarkTabMain.this.commonUtil.getDbAdapter().queryGetCursor("SELECT push_yn FROM tb_usr_user WHERE phone_number='" + LifeBookMarkTabMain.this.commonUtil.getMyTelNumber() + "';");
            String push_yn = "N";
            if (cursor != null && cursor.getCount() > 0) {
                push_yn = JoyNUtil.nullCheck(cursor.getString(0), "N");
            }
            String serial = ((TelephonyManager) LifeBookMarkTabMain.this.getSystemService("phone")).getDeviceId();
            DbCommand command = new DbCommand(LifeBookMarkTabMain.this.commonUtil.getDbDatabase(), "sp_mobile01_insertSessionLog");
            command.addParameter((int) DbInterface.STRING_TYPE, 1, LifeBookMarkTabMain.this.commonUtil.getMyTelNumber().replaceAll("-", ""));
            command.addParameter((int) DbInterface.STRING_TYPE, 1, LifeBookMarkTabMain.this.commonUtil.getEnterprise_id());
            command.addParameter((int) DbInterface.STRING_TYPE, 1, "A");
            command.addParameter((int) DbInterface.STRING_TYPE, 1, LifeBookMarkTabMain.this.commonUtil.getAppType());
            command.addParameter((int) DbInterface.STRING_TYPE, 1, LifeBookMarkTabMain.this.commonUtil.getProgram_version());
            command.addParameter((int) DbInterface.STRING_TYPE, 1, serial);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, push_yn);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, "");
            new DbRecordset(LifeBookMarkTabMain.this.commonUtil.getDbDatabase()).execute(command);
            if (cursor != null) {
                cursor.close();
            }
            return null;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.main);
        this.commonUtil = (CommonUtil) getApplicationContext();
        if (SettingStepViewActivity.stepActivity != null) {
            new GcmController(this, this.commonUtil).RegisterInBackground();
            SettingStepViewActivity.stepActivity.finish();
        }
        new LoginAsyncTask().execute(new Void[0]);
        this.tabhost = getTabHost();
        this.tabhost.addTab(this.tabhost.newTabSpec("daeri").setIndicator(null, getResources().getDrawable(R.drawable.tabicon_daeri)).setContent(new Intent(this, MainActivity.class)));
        this.tabhost.addTab(this.tabhost.newTabSpec("store").setIndicator(null, getResources().getDrawable(R.drawable.tabicon_store)).setContent(new Intent(this, StoreActivity.class)));
        this.tabhost.addTab(this.tabhost.newTabSpec("event").setIndicator(null, getResources().getDrawable(R.drawable.tabicon_event)).setContent(new Intent(this, EventActivity.class)));
        this.tabhost.addTab(this.tabhost.newTabSpec("more").setIndicator(null, getResources().getDrawable(R.drawable.tabicon_more)).setContent(new Intent(this, MoreActivity.class)));
        this.tabhost.setCurrentTab(0);
        this.tabhost.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String tabSpec) {
                ((InputMethodManager) LifeBookMarkTabMain.this.getSystemService("input_method")).hideSoftInputFromWindow(LifeBookMarkTabMain.this.getCurrentFocus().getWindowToken(), 0);
            }
        });
        for (int i = 0; i < 4; i++) {
            this.tabhost.getTabWidget().getChildAt(i).setBackgroundColor(0);
            this.tabhost.getTabWidget().getChildAt(i).getLayoutParams().height = this.tabhost.getLayoutParams().height;
        }
        this.commonUtil.setTabHost(this.tabhost);
        this.commonUtil.setLocationUtil(JoyLocationUtil.getInstance(this));
    }

    protected void onDestroy() {
        Log.d("LifeBookMarkTabMain", "onDestroy");
        if (LifeBookMarkMain.lifeBookMarkMain != null) {
            LifeBookMarkMain.lifeBookMarkMain.finish();
        }
        if (this.commonUtil.getLocationUtil() != null) {
            this.commonUtil.getLocationUtil().stop();
        }
        this.commonUtil.setLocationUtil(null);
        if (this.commonUtil.getDbDatabase() != null) {
            this.commonUtil.getDbDatabase().close();
        }
        this.commonUtil.setDbDatabase(null);
        super.onDestroy();
    }
}
