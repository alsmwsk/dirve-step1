package joy.LifeBookMark.Store;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.facebook.appevents.AppEventsConstants;
import com.joy.db.DbCommand;
import com.joy.db.DbInterface;
import com.joy.db.DbRecordset;
import com.kakao.network.ServerProtocol;
import java.util.HashMap;

import joy.LifeBookMark.LifeBookMarkMain;
import joy.common.CommonUtil;
import joy.common.JoyLocationUtil;
import joy.common.JoyNInterface;
import joy.common.JoyNUtil;
import joy.common.MyWebViewClient;

public class SettingStepViewActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
    private static final String TAG = "SettingStepViewActivity";
    public static SettingStepViewActivity stepActivity = null;
    private boolean bRegionCheck = true;
    private String[] cardArray = new String[]{"없음", "KB국민카드", "롯데카드", "삼성카드", "신한카드", "씨티카드", "외환카드", "하나SK카드", "현대카드", "비씨카드", "농협NH카드"};
    private CommonUtil commonUtil = null;
    private InputMethodManager imm;
    private JoyLocationUtil locationUtil = null;
    private String[] ownerArray = new String[]{"개인", "법인"};
    private String[] regionArray = null;
    private String[] regionIndex = null;
    private String serial = "";
    private CheckBox step1AllChk = null;
    private ImageButton step1CloseBtn = null;
    private Dialog step1Dialog = null;
    private String step1EnterpriseId = "EA0001";
    private String step1EnterpriseNm = "조이앤드라이브";
    private String step1EnterpriseTel = "1600-4321";
    private Button step1InfoBtn = null;
    private CheckBox step1InfoChk = null;
    private ScrollView step1Layout = null;
    private Button step1LocationBtn = null;
    private CheckBox step1LocationChk = null;
    private HashMap<String, String> step1Map = new HashMap();
    private Button step1NextBtn = null;
    private Spinner step1ReginSpinner = null;
    private String step1RegionId = null;
    private String step1RegionNm = null;
    private CheckBox step1ServiceChk = null;
    private Button step1SeviceBtn = null;
    private String step1Tel = "";
    private EditText step1TelEdit = null;
    private Button step1TrialogueBtn = null;
    private CheckBox step1TrialogueChk = null;
    private CheckBox step2CardCheck = null;
    private RelativeLayout step2CardInfoLayout = null;
    private String step2CardNum1 = "";
    private EditText step2CardNum1Edit = null;
    private String step2CardNum2 = "";
    private EditText step2CardNum2Edit = null;
    private String step2CardNum3 = "";
    private EditText step2CardNum3Edit = null;
    private String step2CardNum4 = "";
    private EditText step2CardNum4Edit = null;
    private String step2CardOwnerGubun = "";
    private Spinner step2CardOwnerGubunSpinner = null;
    private String step2CardOwnerNm = "";
    private EditText step2CardOwnerNmEdit = null;
    private String step2CardType = "";
    private Spinner step2CardTypeSpinner = null;
    private String step2CardVidMonth = "";
    private EditText step2CardVidMonthEdit = null;
    private String step2CardVidYear = "";
    private EditText step2CardVidYearEdit = null;
    private String step2Email = "";
    private EditText step2EmailEdit = null;
    private String step2EmailYn = "N";
    private ScrollView step2Layout = null;
    private Button step2NextBtn = null;
    private Button step2PrveBtn = null;
    private Button step3CompBtn = null;
    private ScrollView step3Layout = null;
    private Button step3PrveBtn = null;
    private String step3Recmmend = "";
    private EditText step3RecommendEdit = null;
    private ViewFlipper stepFlipper = null;
    private RelativeLayout tabBarInfoLayout1 = null;
    private RelativeLayout tabBarInfoLayout2 = null;
    private RelativeLayout tabBarInfoLayout3 = null;
    private TextView tabBarTitleText = null;

    private class CompletionTask extends AsyncTask<Void, Void, String> {
        private CompletionTask() {
        }

        protected String doInBackground(Void... params) {
            DbCommand command = new DbCommand(SettingStepViewActivity.this.commonUtil.getDbDatabase(), "sp_mobile01_insertLogin_new4");
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step1EnterpriseId);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step1Tel);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.commonUtil.getProgram_version());
            command.addParameter((int) DbInterface.STRING_TYPE, 1, "A");
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.serial);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, JoyNUtil.K2E(SettingStepViewActivity.this.step2CardType));
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step2CardNum1);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step2CardNum2);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step2CardNum3);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step2CardNum4);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step2CardVidYear);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step2CardVidMonth);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step3Recmmend);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step2EmailYn);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, JoyNUtil.K2E(SettingStepViewActivity.this.step2Email));
            command.addParameter((int) DbInterface.STRING_TYPE, 1, SettingStepViewActivity.this.step2CardOwnerGubun);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, JoyNUtil.K2E(SettingStepViewActivity.this.step2CardOwnerNm));
            DbRecordset pRs = new DbRecordset(command);
            String msg = "";
            if (!pRs.execute(command)) {
                return "";
            }
            long return_out = pRs.getFieldLongValue("RETURN_OUT");
            String recommend_out = pRs.getFieldStringValue("RECOMMEND_OUT");
            if (return_out != 0) {
                return "";
            }
            if (recommend_out.equals("00")) {
                msg = "저장 완료되었습니다.";
            } else if (recommend_out.equals("01")) {
                msg = "추천인 전화번호가 잘못 입력되었습니다. 다시 입력해 주십시오.\n ※[더보기]-[설정]-[추천인HP] 입력";
                SettingStepViewActivity.this.step3Recmmend = "";
            } else if (recommend_out.equals("02")) {
                msg = "추천인 포인트 적립 시 오류가 발생하였습니다. 다시 입력해 주십시오.\n ※[더보기]-[설정]-[추천인HP] 입력";
                SettingStepViewActivity.this.step3Recmmend = "";
            } else if (recommend_out.equals("03")) {
                msg = SettingStepViewActivity.this.commonUtil.getMyTelNumber() + "고객님은 신규고객이 아니므로 추천인 포인트가 적립되지 않습니다.";
            } else if (recommend_out.equals("04")) {
                msg = "동일인의 중복 추천은 불가능합니다.";
            } else if (recommend_out.equals("05")) {
                msg = "당월의 추천 포인트 지급횟수가 초과하였습니다.";
            } else if (recommend_out.equals("99")) {
                return "";
            } else {
                msg = "추천인 포인트 적립 시 오류가 발생하였습니다. 다시 입력해 주십시오.\n ※[더보기]-[설정]-[추천인HP] 입력";
                SettingStepViewActivity.this.step3Recmmend = "";
            }
            String dbSql = ((((((((((((((((((((("UPDATE tb_usr_user" + "    SET REGION           = '" + SettingStepViewActivity.this.step1RegionId + "',") + "        REGION_NM        = '" + SettingStepViewActivity.this.step1RegionNm + "',") + "        phone_number     = '" + SettingStepViewActivity.this.step1Tel + "',") + "        ENTERPRISE_ID    = '" + SettingStepViewActivity.this.step1EnterpriseId + "',") + "        ENTERPRISE_NM    = '" + SettingStepViewActivity.this.step1EnterpriseNm + "',") + "        ENTERPRISE_TEL   = '" + SettingStepViewActivity.this.step1EnterpriseTel + "',") + "        PUSH_YN          = 'Y',") + "        LOCAL_YN         = 'Y',") + "        card_type        = '" + SettingStepViewActivity.this.step2CardType + "',") + "        card_num1        = '" + SettingStepViewActivity.this.step2CardNum1 + "',") + "        card_num2        = '" + SettingStepViewActivity.this.step2CardNum2 + "',") + "        card_num3        = '" + SettingStepViewActivity.this.step2CardNum3 + "',") + "        card_num4        = '" + SettingStepViewActivity.this.step2CardNum4 + "',") + "        card_year        = '" + SettingStepViewActivity.this.step2CardVidYear + "',") + "        card_month       = '" + SettingStepViewActivity.this.step2CardVidMonth + "',") + "        card_owner_gubun = '" + SettingStepViewActivity.this.step2CardOwnerGubun + "',") + "        card_owner_nm    = '" + SettingStepViewActivity.this.step2CardOwnerNm + "',") + "        email_yn         = '" + SettingStepViewActivity.this.step2EmailYn + "',") + "        email_address    = '" + SettingStepViewActivity.this.step2Email + "',") + "        recommend_tel    = '" + SettingStepViewActivity.this.step3Recmmend + "',") + "        app_dt           = '" + JoyNUtil.getToday("yyyy-MM-dd HH:mm:ss") + "',") + "        update_dt        = '" + JoyNUtil.getToday("yyyy-MM-dd HH:mm:ss") + "'";
            SettingStepViewActivity.this.commonSetting();
            if (SettingStepViewActivity.this.commonUtil.getDbAdapter().queryExecute(dbSql)) {
            }
            return msg;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("")) {
                Toast.makeText(SettingStepViewActivity.this, "설정 시 오류가 발생했습니다. 다시 저장해주세요.", 1).show();
                return;
            }
            Dialog dialog = new Dialog(SettingStepViewActivity.this);
            dialog.requestWindowFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            View view = ((LayoutInflater) SettingStepViewActivity.this.getSystemService("layout_inflater")).inflate(R.layout.custom_alert, null);
            ((RelativeLayout) view.findViewById(R.id.dialog_titlelayout)).setVisibility(8);
            ((TextView) view.findViewById(R.id.dialog_contenttext)).setText(result);
            ((LinearLayout) view.findViewById(R.id.dialog_twobtnlayout)).setVisibility(8);
            ((LinearLayout) view.findViewById(R.id.dialog_onebtnlayout)).setVisibility(0);
            ((Button) view.findViewById(R.id.dialog_closebtn)).setOnClickListener(new SettingStepViewActivity$CompletionTask$1(this, dialog));
            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().width = SettingStepViewActivity.this.commonUtil.getDialogWidth();
            dialog.show();
        }
    }

    public class asyncTask extends AsyncTask<Void, Integer, String[]> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String[] doInBackground(Void... params) {
            if (SettingStepViewActivity.this.locationUtil == null) {
                return null;
            }
            try {
                if (SettingStepViewActivity.this.locationUtil.getLastLocation() == null) {
                    return null;
                }
                return new String[]{JoyNUtil.getAddress(SettingStepViewActivity.this, SettingStepViewActivity.this.locationUtil.getLastLocation().getLatitude(), SettingStepViewActivity.this.locationUtil.getLastLocation().getLongitude())};
            } catch (Exception e) {
                return null;
            }
        }

        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            if (result != null) {
                String[] addr = result[0].split(ServerProtocol.AUTHORIZATION_HEADER_DELIMITER);
                addr[0] = addr[0].replace("특별시", "").replace("광역시", "").replace("특별자치도", "").replace("특별자치시", "").replace("경기도", "경기").replace("강원도", "강원").replace("전라북도", "전북").replace("전라남도", "전남").replace("경상북도", "경북").replace("경상남도", "경남").replace("충청북도", "충북").replace("충청남도", "충남");
                if (addr[1].length() > 2) {
                    addr[1] = addr[1].substring(0, addr[1].length() - 1);
                }
                boolean bChk = false;
                int i = 2;
                while (i > 0) {
                    for (int j = 0; j < SettingStepViewActivity.this.regionArray.length; j++) {
                        if (SettingStepViewActivity.this.regionArray[j].indexOf(addr[i - 1]) > -1) {
                            if (SettingStepViewActivity.this.step1Map.containsKey(SettingStepViewActivity.this.regionIndex[j])) {
                                SettingStepViewActivity.this.step1RegionId = SettingStepViewActivity.this.regionIndex[j];
                                SettingStepViewActivity.this.step1RegionNm = SettingStepViewActivity.this.regionArray[j];
                                String[] param = ((String) SettingStepViewActivity.this.step1Map.get(SettingStepViewActivity.this.regionIndex[j])).split(JoyNInterface.EXPR);
                                SettingStepViewActivity.this.step1EnterpriseId = param[0];
                                SettingStepViewActivity.this.step1EnterpriseNm = param[1];
                                SettingStepViewActivity.this.step1EnterpriseTel = param[2];
                                SettingStepViewActivity.this.bRegionCheck = true;
                                SettingStepViewActivity.this.step1ReginSpinner.setSelection(j);
                            }
                            bChk = true;
                            if (bChk) {
                                i--;
                            } else {
                                return;
                            }
                        }
                    }
                    if (bChk) {
                        i--;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.stepview);
        stepActivity = this;
        this.commonUtil = (CommonUtil) getApplicationContext();
        this.locationUtil = this.commonUtil.getLocationUtil(this);
        this.tabBarInfoLayout1 = (RelativeLayout) findViewById(R.id.tabbar_infolayout1);
        this.tabBarInfoLayout2 = (RelativeLayout) findViewById(R.id.tabbar_infolayout2);
        this.tabBarInfoLayout3 = (RelativeLayout) findViewById(R.id.tabbar_infolayout3);
        this.tabBarTitleText = (TextView) findViewById(R.id.tabbar_titletext);
        this.stepFlipper = (ViewFlipper) findViewById(R.id.stepflipper);
        this.step1Layout = (ScrollView) View.inflate(this, R.layout.step1view, null);
        this.step2Layout = (ScrollView) View.inflate(this, R.layout.step2view, null);
        this.step3Layout = (ScrollView) View.inflate(this, R.layout.step3view, null);
        this.stepFlipper.addView(this.step1Layout);
        this.stepFlipper.addView(this.step2Layout);
        this.stepFlipper.addView(this.step3Layout);
        step1Init();
        step2Init();
        step3Init();
        this.imm = (InputMethodManager) getSystemService("input_method");
        if (!this.commonUtil.getAppType().equals(JoyNInterface.LGT)) {
            this.step1ServiceChk.setChecked(true);
            this.step1LocationChk.setChecked(true);
            this.step1InfoChk.setChecked(true);
            this.step1TrialogueChk.setChecked(true);
        }
    }

    private void step1Init() {
        int i;
        this.step1ReginSpinner = (Spinner) this.step1Layout.findViewById(R.id.step1_regionspinner);
        this.step1Map.clear();
        Cursor regionCursor = this.commonUtil.getDbAdapter().queryGetCursor((("SELECT REGION_ID, REGION_NM" + "  FROM TB_REGION") + " WHERE REGION_ORDER <> '99'") + " ORDER BY REGION_ORDER ASC;");
        this.regionIndex = new String[regionCursor.getCount()];
        this.regionArray = new String[regionCursor.getCount()];
        if (regionCursor != null) {
            for (i = 0; i < regionCursor.getCount(); i++) {
                this.regionIndex[i] = regionCursor.getString(0);
                this.regionArray[i] = regionCursor.getString(1);
                regionCursor.moveToNext();
            }
        }
        if (regionCursor != null) {
            regionCursor.close();
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter(this, R.layout.customerspinner, this.regionArray);
        adapter1.setDropDownViewResource(17367049);
        this.step1ReginSpinner.setPrompt(getResources().getString(R.string.text_region));
        this.step1ReginSpinner.setAdapter(adapter1);
        this.step1ReginSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int idx = position;
                SettingStepViewActivity.this.step1RegionId = SettingStepViewActivity.this.regionIndex[idx];
                SettingStepViewActivity.this.step1RegionNm = SettingStepViewActivity.this.regionArray[idx];
                if (SettingStepViewActivity.this.step1Map.containsKey(SettingStepViewActivity.this.regionIndex[idx])) {
                    String[] param = ((String) SettingStepViewActivity.this.step1Map.get(SettingStepViewActivity.this.regionIndex[idx])).split(JoyNInterface.EXPR);
                    SettingStepViewActivity.this.step1EnterpriseId = param[0];
                    SettingStepViewActivity.this.step1EnterpriseNm = param[1];
                    SettingStepViewActivity.this.step1EnterpriseTel = param[2];
                    SettingStepViewActivity.this.bRegionCheck = true;
                    return;
                }
                SettingStepViewActivity.this.bRegionCheck = false;
                SettingStepViewActivity.this.step1RegionNm = SettingStepViewActivity.this.step1RegionNm.replace("-업체선정중입니다", "");
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        Cursor cursor = this.commonUtil.getDbAdapter().queryGetCursor(((("SELECT ENTERPRISE_ID, ENTERPRISE_NM, ENTERPRISE_TEL, ENTERPRISE_RANK" + "  FROM TB_ENT_ENTERPRISE") + " WHERE ENTERPRISE_TYPE = '1'") + "   AND ENTERPRISE_YN   = 'Y'") + " ORDER BY ENTERPRISE_RANK ASC;");
        String str;
        String myNum;
        if (cursor == null || cursor.getCount() <= 0) {
            if (cursor != null) {
                cursor.close();
            }
            for (i = 0; i < this.regionArray.length; i++) {
                str = this.regionArray[i];
                if (!this.step1Map.containsKey(this.regionIndex[i])) {
                    this.regionArray[i] = str + "-업체선정중입니다";
                }
            }
            new asyncTask().execute(new Void[0]);
            this.step1TelEdit = (EditText) this.step1Layout.findViewById(R.id.step1_teledit);
            TelephonyManager mgr = (TelephonyManager) getSystemService("phone");
            myNum = mgr.getLine1Number();
            this.serial = mgr.getDeviceId();
            if (myNum != null || myNum.length() <= 3) {
                myNum = "";
            } else if (myNum.substring(0, 1).equals("+") && myNum.substring(0, 3).equals("+82")) {
                myNum = AppEventsConstants.EVENT_PARAM_VALUE_NO + myNum.substring(3, myNum.length());
            }
            this.step1TelEdit.setText(myNum);
            this.step1NextBtn = (Button) this.step1Layout.findViewById(R.id.step1_nextbtn);
            this.step1NextBtn.setOnClickListener(this);
            this.step1SeviceBtn = (Button) this.step1Layout.findViewById(R.id.step1_servicebtn);
            this.step1SeviceBtn.setOnClickListener(this);
            this.step1LocationBtn = (Button) this.step1Layout.findViewById(R.id.step1_locationbtn);
            this.step1LocationBtn.setOnClickListener(this);
            this.step1InfoBtn = (Button) this.step1Layout.findViewById(R.id.step1_infobtn);
            this.step1InfoBtn.setOnClickListener(this);
            this.step1TrialogueBtn = (Button) this.step1Layout.findViewById(R.id.step1_trialoguebtn);
            this.step1TrialogueBtn.setOnClickListener(this);
            this.step1AllChk = (CheckBox) this.step1Layout.findViewById(R.id.step1_allprovisionchk);
            this.step1AllChk.setOnClickListener(this);
            this.step1ServiceChk = (CheckBox) this.step1Layout.findViewById(R.id.step1_servicechk);
            this.step1ServiceChk.setOnCheckedChangeListener(this);
            this.step1LocationChk = (CheckBox) this.step1Layout.findViewById(R.id.step1_locationchk);
            this.step1LocationChk.setOnCheckedChangeListener(this);
            this.step1InfoChk = (CheckBox) this.step1Layout.findViewById(R.id.step1_infochk);
            this.step1InfoChk.setOnCheckedChangeListener(this);
            this.step1TrialogueChk = (CheckBox) this.step1Layout.findViewById(R.id.step1_trialoguechk);
            this.step1TrialogueChk.setOnCheckedChangeListener(this);
            this.step1Dialog = new Dialog(this);
            this.step1Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.step1Dialog.setCancelable(true);
            this.step1Dialog.requestWindowFeature(1);
            this.step1Dialog.setContentView(R.layout.customprovisiondialog);
            this.step1CloseBtn = (ImageButton) this.step1Dialog.findViewById(R.id.provision_close);
            this.step1CloseBtn.setOnClickListener(this);
        }
        do {
            String key = "";
            String value = "";
            for (int j = 0; j < 4; j++) {
                value = value + cursor.getString(j) + JoyNInterface.EXPR;
            }
            this.step1Map.put(cursor.getString(3), value);
        } while (cursor.moveToNext());
        if (cursor != null) {
            cursor.close();
        }
        for (i = 0; i < this.regionArray.length; i++) {
            str = this.regionArray[i];
            if (!this.step1Map.containsKey(this.regionIndex[i])) {
                this.regionArray[i] = str + "-업체선정중입니다";
            }
        }
        new asyncTask().execute(new Void[0]);
        this.step1TelEdit = (EditText) this.step1Layout.findViewById(R.id.step1_teledit);
        TelephonyManager mgr2 = (TelephonyManager) getSystemService("phone");
        myNum = mgr2.getLine1Number();
        this.serial = mgr2.getDeviceId();
        if (myNum != null) {
        }
        myNum = "";
        this.step1TelEdit.setText(myNum);
        this.step1NextBtn = (Button) this.step1Layout.findViewById(R.id.step1_nextbtn);
        this.step1NextBtn.setOnClickListener(this);
        this.step1SeviceBtn = (Button) this.step1Layout.findViewById(R.id.step1_servicebtn);
        this.step1SeviceBtn.setOnClickListener(this);
        this.step1LocationBtn = (Button) this.step1Layout.findViewById(R.id.step1_locationbtn);
        this.step1LocationBtn.setOnClickListener(this);
        this.step1InfoBtn = (Button) this.step1Layout.findViewById(R.id.step1_infobtn);
        this.step1InfoBtn.setOnClickListener(this);
        this.step1TrialogueBtn = (Button) this.step1Layout.findViewById(R.id.step1_trialoguebtn);
        this.step1TrialogueBtn.setOnClickListener(this);
        this.step1AllChk = (CheckBox) this.step1Layout.findViewById(R.id.step1_allprovisionchk);
        this.step1AllChk.setOnClickListener(this);
        this.step1ServiceChk = (CheckBox) this.step1Layout.findViewById(R.id.step1_servicechk);
        this.step1ServiceChk.setOnCheckedChangeListener(this);
        this.step1LocationChk = (CheckBox) this.step1Layout.findViewById(R.id.step1_locationchk);
        this.step1LocationChk.setOnCheckedChangeListener(this);
        this.step1InfoChk = (CheckBox) this.step1Layout.findViewById(R.id.step1_infochk);
        this.step1InfoChk.setOnCheckedChangeListener(this);
        this.step1TrialogueChk = (CheckBox) this.step1Layout.findViewById(R.id.step1_trialoguechk);
        this.step1TrialogueChk.setOnCheckedChangeListener(this);
        this.step1Dialog = new Dialog(this);
        this.step1Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.step1Dialog.setCancelable(true);
        this.step1Dialog.requestWindowFeature(1);
        this.step1Dialog.setContentView(R.layout.customprovisiondialog);
        this.step1CloseBtn = (ImageButton) this.step1Dialog.findViewById(R.id.provision_close);
        this.step1CloseBtn.setOnClickListener(this);
    }

    private void step2Init() {
        this.step2CardCheck = (CheckBox) this.step2Layout.findViewById(R.id.step2_cardcheck);
        this.step2CardCheck.setOnCheckedChangeListener(this);
        this.step2CardInfoLayout = (RelativeLayout) this.step2Layout.findViewById(R.id.step2_cardinfolayout);
        this.step2CardTypeSpinner = (Spinner) this.step2Layout.findViewById(R.id.step2_typespinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.customerspinner, this.cardArray);
        adapter.setDropDownViewResource(17367049);
        this.step2CardTypeSpinner.setPrompt(getResources().getString(R.string.text_cardtype));
        this.step2CardTypeSpinner.setAdapter(adapter);
        this.step2CardTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    SettingStepViewActivity.this.step2CardType = "";
                    SettingStepViewActivity.this.step2CardNum1Edit.setText("");
                    SettingStepViewActivity.this.step2CardNum2Edit.setText("");
                    SettingStepViewActivity.this.step2CardNum3Edit.setText("");
                    SettingStepViewActivity.this.step2CardNum4Edit.setText("");
                    SettingStepViewActivity.this.step2CardVidYearEdit.setText("");
                    SettingStepViewActivity.this.step2CardVidMonthEdit.setText("");
                    SettingStepViewActivity.this.step2EmailEdit.setText("");
                    SettingStepViewActivity.this.step2CardOwnerNmEdit.setText("");
                    SettingStepViewActivity.this.step2CardOwnerGubunSpinner.setSelection(0);
                    SettingStepViewActivity.this.step2CardOwnerGubun = "01";
                    return;
                }
                SettingStepViewActivity.this.step2CardType = SettingStepViewActivity.this.cardArray[position];
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.step2CardOwnerGubunSpinner = (Spinner) this.step2Layout.findViewById(R.id.step2_ownergubunspinner);
        ArrayAdapter<String> ownerAdapter = new ArrayAdapter(this, R.layout.customerspinner, this.ownerArray);
        ownerAdapter.setDropDownViewResource(17367049);
        this.step2CardOwnerGubunSpinner.setPrompt(getResources().getString(R.string.text_cardgubun));
        this.step2CardOwnerGubunSpinner.setAdapter(ownerAdapter);
        this.step2CardOwnerGubunSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    SettingStepViewActivity.this.step2CardOwnerGubun = "01";
                } else {
                    SettingStepViewActivity.this.step2CardOwnerGubun = "02";
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.step2CardNum1Edit = (EditText) this.step2Layout.findViewById(R.id.step2_num1edit);
        this.step2CardNum2Edit = (EditText) this.step2Layout.findViewById(R.id.step2_num2edit);
        this.step2CardNum3Edit = (EditText) this.step2Layout.findViewById(R.id.step2_num3edit);
        this.step2CardNum4Edit = (EditText) this.step2Layout.findViewById(R.id.step2_num4edit);
        this.step2CardVidMonthEdit = (EditText) this.step2Layout.findViewById(R.id.step2_monthedit);
        this.step2CardVidYearEdit = (EditText) this.step2Layout.findViewById(R.id.step2_yearedit);
        this.step2CardOwnerNmEdit = (EditText) this.step2Layout.findViewById(R.id.step2_ownernmedit);
        this.step2EmailEdit = (EditText) this.step2Layout.findViewById(R.id.step2_emailedit);
        this.step2NextBtn = (Button) this.step2Layout.findViewById(R.id.step2_nextbtn);
        this.step2NextBtn.setOnClickListener(this);
        this.step2PrveBtn = (Button) this.step2Layout.findViewById(R.id.step2_prvebtn);
        this.step2PrveBtn.setOnClickListener(this);
    }

    private void step3Init() {
        this.step3RecommendEdit = (EditText) this.step3Layout.findViewById(R.id.step3_recommededit);
        ((TextView) this.step3Layout.findViewById(R.id.step3_etc)).setText(this.commonUtil.getInitRecommend());
        this.step3CompBtn = (Button) this.step3Layout.findViewById(R.id.step3_compbtn);
        this.step3CompBtn.setOnClickListener(this);
        this.step3PrveBtn = (Button) this.step3Layout.findViewById(R.id.step3_prvebtn);
        this.step3PrveBtn.setOnClickListener(this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        keyboardHide();
        return super.onTouchEvent(event);
    }

    public void onClick(View v) {
        keyboardHide();
        switch (v.getId()) {
            case R.id.provision_close:
                this.step1Dialog.cancel();
                break;
            case R.id.step1_allprovisionchk:
                this.step1ServiceChk.setChecked(this.step1AllChk.isChecked());
                this.step1LocationChk.setChecked(this.step1AllChk.isChecked());
                this.step1InfoChk.setChecked(this.step1AllChk.isChecked());
                this.step1TrialogueChk.setChecked(this.step1AllChk.isChecked());
                break;
            case R.id.step1_servicebtn:
                provisionDialog("01");
                break;
            case R.id.step1_locationbtn:
                provisionDialog("02");
                break;
            case R.id.step1_infobtn:
                provisionDialog("03");
                break;
            case R.id.step1_trialoguebtn:
                provisionDialog("04");
                break;
            case R.id.step1_nextbtn:
                this.step1Tel = String.valueOf(this.step1TelEdit.getText()).replaceAll(ServerProtocol.AUTHORIZATION_HEADER_DELIMITER, "");
                if (this.bRegionCheck) {
                    if (!this.step1Tel.equals("")) {
                        if (JoyNUtil.getTelNumCheck(this.step1Tel)) {
                            if (this.step1ServiceChk.isChecked()) {
                                if (this.step1InfoChk.isChecked()) {
                                    if (this.step1TrialogueChk.isChecked()) {
                                        if (this.step1ServiceChk.isChecked()) {
                                            MoveNextView();
                                            break;
                                        } else {
                                            Toast.makeText(this, "개인정보 제3자 동의하셔야 합니다.", 0).show();
                                            return;
                                        }
                                    }
                                    Toast.makeText(this, "개인정보 이용방침에 동의하셔야 합니다.", 0).show();
                                    return;
                                }
                                Toast.makeText(this, "위치정보 이용약관에 동의하셔야 합니다.", 0).show();
                                return;
                            }
                            Toast.makeText(this, "서비스 이용약관에 동의하셔야 합니다.", 0).show();
                            return;
                        }
                        Toast.makeText(this, "대리운전 호출 및 포인트 적립/사용에 필요한 전화번호를 반드시 입력해주세요.", 0).show();
                        return;
                    }
                    Toast.makeText(this, "대리운전 호출 및 포인트 적립/사용에 필요한 전화번호를 반드시 입력해주세요.", 0).show();
                    return;
                }
                Toast.makeText(this, this.step1RegionNm + " 지역에 해당하는 업체 선정중 입니다. 이용에 불편을 드려 죄소합니다.", 0).show();
                return;
            case R.id.step2_prvebtn:
                MovePreviousView();
                break;
            case R.id.step2_nextbtn:
                this.step2CardNum1 = String.valueOf(this.step2CardNum1Edit.getText());
                this.step2CardNum2 = String.valueOf(this.step2CardNum2Edit.getText());
                this.step2CardNum3 = String.valueOf(this.step2CardNum3Edit.getText());
                this.step2CardNum4 = String.valueOf(this.step2CardNum4Edit.getText());
                this.step2CardVidMonth = String.valueOf(this.step2CardVidMonthEdit.getText());
                this.step2CardVidYear = String.valueOf(this.step2CardVidYearEdit.getText());
                this.step2CardOwnerNm = String.valueOf(this.step2CardOwnerNmEdit.getText());
                if (!this.step2CardType.equals("")) {
                    if (this.step2CardNum1.equals("") || this.step2CardNum2.equals("") || this.step2CardNum3.equals("") || this.step2CardNum4.equals("")) {
                        Toast.makeText(this, "카드 번호 정보를 정확히 입력하셔야 합니다.", 0).show();
                        return;
                    } else if (this.step2CardVidMonth.equals("") || this.step2CardVidYear.equals("")) {
                        Toast.makeText(this, "카드 유효기간을 정확히 입력하셔야 합니다.", 0).show();
                        return;
                    } else if (this.step2CardOwnerNm.equals("")) {
                        Toast.makeText(this, "카드 소유주명을 정확히 입력하셔야 합니다.", 0).show();
                        return;
                    }
                }
                this.step2Email = String.valueOf(this.step2EmailEdit.getText());
                if (this.step2Email.equals("")) {
                    this.step2EmailYn = "N";
                } else if (JoyNUtil.isCheckEmailCorrect(this.step2Email)) {
                    this.step2EmailYn = "Y";
                } else {
                    Toast.makeText(this, "이메일주소를 정확히 입력해주세요.", 0).show();
                    return;
                }
                MoveNextView();
                break;
            case R.id.step3_prvebtn:
                MovePreviousView();
                break;
            case R.id.step3_compbtn:
                this.step3Recmmend = String.valueOf(this.step3RecommendEdit.getText());
                if (this.step3Recmmend.length() > 0) {
                    if (this.step1Tel.equals(this.step3Recmmend.replaceAll("-", ""))) {
                        Toast.makeText(this, "추천인 HP는 본인 전화번호를 입력하실 수 없습니다.", 0).show();
                        return;
                    } else if (this.step3Recmmend.length() < 9 || this.step3Recmmend.length() > 11) {
                        Toast.makeText(this, "추천인 전화번호를 정확히 입력해주세요.", 0).show();
                        return;
                    }
                }
                new CompletionTask().execute(new Void[0]);
                break;
        }
        titleChange();
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        keyboardHide();
        switch (buttonView.getId()) {
            case R.id.step1_servicechk:
                if (!isChecked) {
                    this.step1AllChk.setChecked(isChecked);
                    return;
                } else if (this.step1LocationChk.isChecked() && this.step1InfoChk.isChecked() && this.step1TrialogueChk.isChecked()) {
                    this.step1AllChk.setChecked(isChecked);
                    return;
                } else {
                    return;
                }
            case R.id.step1_locationchk:
                if (!isChecked) {
                    this.step1AllChk.setChecked(isChecked);
                    return;
                } else if (this.step1ServiceChk.isChecked() && this.step1InfoChk.isChecked() && this.step1TrialogueChk.isChecked()) {
                    this.step1AllChk.setChecked(isChecked);
                    return;
                } else {
                    return;
                }
            case R.id.step1_infochk:
                if (!isChecked) {
                    this.step1AllChk.setChecked(isChecked);
                    return;
                } else if (this.step1ServiceChk.isChecked() && this.step1LocationChk.isChecked() && this.step1TrialogueChk.isChecked()) {
                    this.step1AllChk.setChecked(isChecked);
                    return;
                } else {
                    return;
                }
            case R.id.step1_trialoguechk:
                if (!isChecked) {
                    this.step1AllChk.setChecked(isChecked);
                    return;
                } else if (this.step1ServiceChk.isChecked() && this.step1LocationChk.isChecked() && this.step1InfoChk.isChecked()) {
                    this.step1AllChk.setChecked(isChecked);
                    return;
                } else {
                    return;
                }
            case R.id.step2_cardcheck:
                if (isChecked) {
                    this.step2CardInfoLayout.setVisibility(0);
                    return;
                }
                this.step2CardInfoLayout.setVisibility(8);
                cardInfoInit();
                return;
            default:
                return;
        }
    }

    private void cardInfoInit() {
        this.step2CardCheck.setChecked(false);
        this.step2CardTypeSpinner.setSelection(0);
        this.step2CardType = "";
        this.step2CardNum1Edit.setText("");
        this.step2CardNum2Edit.setText("");
        this.step2CardNum3Edit.setText("");
        this.step2CardNum4Edit.setText("");
        this.step2CardVidYearEdit.setText("");
        this.step2CardVidMonthEdit.setText("");
        this.step2EmailEdit.setText("");
        this.step2EmailYn = "N";
        this.step2CardOwnerNmEdit.setText("");
        this.step2CardOwnerGubunSpinner.setSelection(0);
        this.step2CardOwnerGubun = "01";
    }

    public void onBackPressed() {
        MovePreviousView();
        titleChange();
    }

    private void MoveNextView() {
        this.stepFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        this.stepFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        this.stepFlipper.showNext();
    }

    private void MovePreviousView() {
        if (this.stepFlipper.getDisplayedChild() == 0) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(1);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            View view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.custom_alert, null);
            ((TextView) view.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.endTitle));
            ((TextView) view.findViewById(R.id.dialog_contenttext)).setText(getResources().getString(R.string.endMessage));
            ((LinearLayout) view.findViewById(R.id.dialog_twobtnlayout)).setVisibility(0);
            ((LinearLayout) view.findViewById(R.id.dialog_onebtnlayout)).setVisibility(8);
            Button actionBtn = (Button) view.findViewById(R.id.dialog_actionbtn);
            actionBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (LifeBookMarkMain.lifeBookMarkMain != null) {
                        LifeBookMarkMain.lifeBookMarkMain.finish();
                    }
                    if (SettingStepViewActivity.this.commonUtil.getDbDatabase() != null) {
                        SettingStepViewActivity.this.commonUtil.getDbDatabase().close();
                    }
                    SettingStepViewActivity.this.commonUtil.setDbDatabase(null);
                    dialog.cancel();
                    SettingStepViewActivity.this.finish();
                }
            });
            actionBtn.setText(getResources().getString(R.string.end));
            Button closeBtn = (Button) view.findViewById(R.id.dialog_cancelbtn);
            closeBtn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            closeBtn.setText(getResources().getString(R.string.text_close));
            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().width = (int) (((double) ((WindowManager) getSystemService("window")).getDefaultDisplay().getWidth()) * 0.9d);
            dialog.show();
            return;
        }
        switch (this.stepFlipper.getDisplayedChild()) {
            case 1:
                cardInfoInit();
                break;
            case 2:
                this.step3RecommendEdit.setText("");
                break;
        }
        this.stepFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
        this.stepFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
        this.stepFlipper.showPrevious();
    }

    private void commonSetting() {
        this.commonUtil.setRegion(this.step1RegionId);
        this.commonUtil.setRegion_nm(this.step1RegionNm);
        this.commonUtil.setEnterprise_id(this.step1EnterpriseId);
        this.commonUtil.setEnterprise_nm(this.step1EnterpriseNm);
        this.commonUtil.setEnterprise_tel(this.step1EnterpriseTel);
        this.commonUtil.setLocalType("Y");
        this.commonUtil.setCard_type(this.step2CardType);
        this.commonUtil.setCard_num1(this.step2CardNum1);
        this.commonUtil.setCard_num2(this.step2CardNum2);
        this.commonUtil.setCard_num3(this.step2CardNum3);
        this.commonUtil.setCard_num4(this.step2CardNum4);
        this.commonUtil.setCard_year(this.step2CardVidYear);
        this.commonUtil.setCard_month(this.step2CardVidMonth);
        this.commonUtil.setEmail_yn(this.step2EmailYn);
        this.commonUtil.setEmail_address(this.step2Email);
        this.commonUtil.setCard_owner_gubun(this.step2CardOwnerGubun);
        this.commonUtil.setCard_owner_nm(this.step2CardOwnerNm);
        this.commonUtil.setMyTelNumber(this.step1Tel);
    }

    private void provisionDialog(String gubun) {
        if (this.step1Dialog == null) {
            this.step1Dialog = new Dialog(this);
        }
        WebView provisionWebView = (WebView) this.step1Dialog.findViewById(R.id.provision_content);
        provisionWebView.setWebViewClient(new MyWebViewClient(this, (ImageView) this.step1Dialog.findViewById(R.id.webview_error)));
        provisionWebView.setHorizontalScrollbarOverlay(false);
        provisionWebView.setHorizontalScrollBarEnabled(false);
        provisionWebView.setVerticalScrollbarOverlay(false);
        provisionWebView.setVerticalScrollBarEnabled(false);
        provisionWebView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public String toString() {
                return "injectedObject";
            }
        }, "injectedObject");
        provisionWebView.getSettings().setJavaScriptEnabled(true);
        provisionWebView.getSettings().setSupportZoom(false);
        provisionWebView.getSettings().setCacheMode(2);
        provisionWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
        provisionWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        provisionWebView.loadUrl("http://www.joyndrive.co.kr:9900/contents/provision/mobileProvision.htm?gubun=" + gubun);
        this.step1Dialog.show();
    }

    private void titleChange() {
        switch (this.stepFlipper.getDisplayedChild()) {
            case 0:
                this.tabBarInfoLayout1.setBackgroundResource(R.drawable.bg_steptitle_select);
                this.tabBarInfoLayout2.setBackgroundResource(R.drawable.bg_steptitle_noselect);
                this.tabBarInfoLayout3.setBackgroundResource(R.drawable.bg_steptitle_noselect);
                this.tabBarTitleText.setText(R.string.text_infotitle1);
                return;
            case 1:
                this.tabBarInfoLayout1.setBackgroundResource(R.drawable.bg_steptitle_noselect);
                this.tabBarInfoLayout2.setBackgroundResource(R.drawable.bg_steptitle_select);
                this.tabBarInfoLayout3.setBackgroundResource(R.drawable.bg_steptitle_noselect);
                this.tabBarTitleText.setText(R.string.text_infotitle2);
                return;
            case 2:
                this.tabBarInfoLayout1.setBackgroundResource(R.drawable.bg_steptitle_noselect);
                this.tabBarInfoLayout2.setBackgroundResource(R.drawable.bg_steptitle_noselect);
                this.tabBarInfoLayout3.setBackgroundResource(R.drawable.bg_steptitle_select);
                this.tabBarTitleText.setText(R.string.text_infotitle3);
                return;
            default:
                return;
        }
    }

    private void keyboardHide() {
        this.imm.hideSoftInputFromWindow(this.step2CardNum1Edit.getWindowToken(), 0);
        this.imm.hideSoftInputFromWindow(this.step2CardNum2Edit.getWindowToken(), 0);
        this.imm.hideSoftInputFromWindow(this.step2CardNum3Edit.getWindowToken(), 0);
        this.imm.hideSoftInputFromWindow(this.step2CardNum4Edit.getWindowToken(), 0);
        this.imm.hideSoftInputFromWindow(this.step2CardVidMonthEdit.getWindowToken(), 0);
        this.imm.hideSoftInputFromWindow(this.step2CardVidYearEdit.getWindowToken(), 0);
        this.imm.hideSoftInputFromWindow(this.step2CardOwnerNmEdit.getWindowToken(), 0);
        this.imm.hideSoftInputFromWindow(this.step2EmailEdit.getWindowToken(), 0);
        this.imm.hideSoftInputFromWindow(this.step3RecommendEdit.getWindowToken(), 0);
    }

    protected void onDestroy() {
        if (LifeBookMarkMain.lifeBookMarkMain != null) {
            LifeBookMarkMain.lifeBookMarkMain.finish();
        }
        super.onDestroy();
    }
}
