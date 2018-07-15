package joy.LifeBookMark.Daeri;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher.ViewFactory;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.appevents.AppEventsConstants;
import com.joy.db.DbCommand;
import com.joy.db.DbInterface;
import com.joy.db.DbRecordset;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import joy.LifeBookMark.Event.EventDetailActivity;
import joy.LifeBookMark.R;
import joy.LifeBookMark.Store.StoreCouponActivity;
import joy.common.CommonUtil;
import joy.common.JoyCallUtil;
import joy.common.JoyLocationUtil;
import joy.common.JoyNInterface;
import joy.common.JoyNUtil;
import joy.common.NavigationActivity;
import joy.common.Security;

public class DaeriMainActivity extends NavigationActivity implements OnClickListener {
    private final String TAG = "DaeriMainActivity";
    private String address = null;
    private Animation animDailogDown = null;
    private Animation animDailogUp = null;
    private boolean bEventPopup = false;
    private String beforRegion = "";
    private String beforTel = "";
    public CallbackManager callbackManager;
    private Button cardCallBtn = null;
    private Button cashCallBtn = null;
    private int cnt = 0;
    private CommonUtil commonUtil = null;
    private RelativeLayout eventContentLayout = null;
    private Handler eventHandler = null;
    private RelativeLayout eventLayout = null;
    private List<HashMap<String, String>> eventParamList = null;
    private ImageButton eventPopupCloseBtn = null;
    private RelativeLayout eventPopupLayout = null;
    private TextSwitcher eventTitleText = null;
    private ImageButton eventpopupbtn = null;
    private ImageButton helpBtn = null;
    private HashMap<String, Bitmap> imageMap = null;
    private KakaoLink kakaoLink = null;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private ImageButton locationRefreshBtn = null;
    private JoyLocationUtil locationUtil = null;
    private ImageButton logisticeBtn = null;
    private int mTouchPosX = 0;
    private ImageButton mapBtn = null;
    private ImageButton multiCallBtn = null;
    private Dialog noticDialog = null;
    private ImageButton okCashBtn = null;
    private Handler pointHandler = null;
    private ImageButton pointHisBtn = null;
    private ImageButton pointStroeBtn = null;
    private DbAsyncTask pointTask = null;
    private TextView pointText = null;
    private TextSwitcher popupEventTitle = null;
    private ImageButton recommendBtn = null;
    private Dialog recommendDialog = null;
    private TextView regionText = null;
    private String serial = null;
    private Handler storyLinkHandler = null;
    private ImageButton subPageNextBtn = null;
    private ImageButton subPagePrevBtn = null;
    private ViewFlipper subViewFlipper = null;

    public class DbAsyncTask extends AsyncTask<String, Integer, String[]> {
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String[] doInBackground(String... params) {
            String[] result = null;
            if (params != null) {
                switch (Integer.valueOf(params[0]).intValue()) {
                    case R.id.daeri_point:
                        result = new String[]{params[0], JoyNUtil.mile(DaeriMainActivity.this.commonUtil, DaeriMainActivity.this.serial)};
                        break;
                    case R.id.daeri_addr:
                        Location location = DaeriMainActivity.this.locationUtil.getLastLocation();
                        String addr = DaeriMainActivity.this.getResources().getString(R.string.text_adderr);
                        if (location != null) {
                            try {
                                addr = JoyNUtil.getAddress(DaeriMainActivity.this.getParent(), location.getLatitude(), location.getLongitude());
                            } catch (Exception e) {
                            }
                        }
                        result = new String[]{params[0], addr};
                        break;
                }
                Log.d("DaeriMainActivity", result[0] + ":" + result[1]);
            }
            return result;
        }

        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            if (result != null) {
                switch (Integer.valueOf(result[0]).intValue()) {
                    case R.id.daeri_point:
                        DaeriMainActivity.this.pointText.setText(JoyNUtil.money(result[1]));
                        DaeriMainActivity.this.pointTask = null;
                        return;
                    case R.id.daeri_addr:
                        DaeriMainActivity.this.address = result[1];
                        TextView addrText = (TextView) DaeriMainActivity.this.findViewById(R.id.daeri_addr);
                        addrText.setText(DaeriMainActivity.this.address);
                        addrText.setSelected(true);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private class DoImageLoad extends AsyncTask<String, Integer, Void> {
        String fileName;

        private DoImageLoad() {
        }

        protected Void doInBackground(String... strData) {
            this.fileName = strData[0];
            if (!this.fileName.equals("") && ((Bitmap) DaeriMainActivity.this.imageMap.get(this.fileName)) == null) {
                DaeriMainActivity.this.imageMap.put(this.fileName, JoyNUtil.getImageFile(DaeriMainActivity.this.getParent(), this.fileName));
                DaeriMainActivity.this.commonUtil.setImageMap(DaeriMainActivity.this.imageMap);
            }
            return null;
        }
    }

    private class versionCheckTask extends AsyncTask<Void, Void, Void> {
        private versionCheckTask() {
        }

        protected Void doInBackground(Void... params) {
            try {
                DbCommand command = new DbCommand(DaeriMainActivity.this.commonUtil.getDbDatabase(), "sp_mobile02_getMobileVerCheck");
                command.addParameter((int) DbInterface.STRING_TYPE, 1, DaeriMainActivity.this.commonUtil.getAppType());
                command.addParameter((int) DbInterface.STRING_TYPE, 1, DaeriMainActivity.this.commonUtil.getProgram_version());
                DbRecordset pRs = new DbRecordset(DaeriMainActivity.this.commonUtil.getDbDatabase());
                if (!pRs.execute(command)) {
                    DaeriMainActivity.this.commonUtil.setAppUpdateType(DaeriMainActivity.this.commonUtil.getProgram_version());
                } else if (pRs.getRowCount() > 0) {
                    DaeriMainActivity.this.commonUtil.setAppUpdateType(pRs.getFieldStringValue("MOBILE_VERSION"));
                } else {
                    DaeriMainActivity.this.commonUtil.setAppUpdateType(DaeriMainActivity.this.commonUtil.getProgram_version());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            String fileVer = JoyNUtil.getFileData(DaeriMainActivity.this.getParent(), JoyNInterface.FILE_UPDATE_VER);
            int nProgramVer = Integer.valueOf(DaeriMainActivity.this.commonUtil.getProgram_version().replace(".", "")).intValue();
            int nUpdataVer = Integer.valueOf(DaeriMainActivity.this.commonUtil.getAppUpdateType().replace(".", "")).intValue();
            int nFileVer = 0;
            if (!fileVer.equals("")) {
                nFileVer = Integer.valueOf(fileVer.replace(".", "")).intValue();
            }
            if (nUpdataVer > nProgramVer) {
                if (nFileVer != nUpdataVer) {
                    JoyNUtil.setFileData(DaeriMainActivity.this.getParent(), JoyNInterface.FILE_UPDATE_VER, DaeriMainActivity.this.commonUtil.getAppUpdateType());
                    DaeriMainActivity.this.versionCheck(AppEventsConstants.EVENT_PARAM_VALUE_YES);
                } else if (Integer.valueOf(JoyNUtil.getFileData(DaeriMainActivity.this.getParent(), JoyNInterface.FILE_UPDATE_CNT)).intValue() < 2) {
                    int hour = Calendar.getInstance().get(11);
                    if (hour < 18 && hour > 9) {
                        DaeriMainActivity.this.versionCheck(JoyNInterface.TAXI);
                    }
                }
            } else if (nFileVer != nProgramVer) {
                JoyNUtil.setFileData(DaeriMainActivity.this.getParent(), JoyNInterface.FILE_UPDATE_VER, DaeriMainActivity.this.commonUtil.getProgram_version());
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.daerimain);
        this.commonUtil = (CommonUtil) getApplicationContext();
        this.callbackManager = Factory.create();
        this.beforTel = this.commonUtil.getMyTelNumber();
        this.beforRegion = this.commonUtil.getRegion();
        try {
            this.kakaoLink = KakaoLink.getKakaoLink(getParent());
            this.kakaoTalkLinkMessageBuilder = this.kakaoLink.createKakaoTalkLinkMessageBuilder();
            this.commonUtil.setKakaoLink(this.kakaoLink);
            this.commonUtil.setKakaoTalkLinkMessageBuilder(this.kakaoTalkLinkMessageBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.regionText = (TextView) findViewById(R.id.daeri_region);
        this.pointText = (TextView) findViewById(R.id.daeri_point);
        this.helpBtn = (ImageButton) findViewById(R.id.daeri_helpbtn);
        this.helpBtn.setOnClickListener(this);
        this.pointHisBtn = (ImageButton) findViewById(R.id.daeri_pointhisbtn);
        this.pointHisBtn.setOnClickListener(this);
        this.cashCallBtn = (Button) findViewById(R.id.daeri_cashcallbtn);
        this.cashCallBtn.setOnClickListener(this);
        this.cardCallBtn = (Button) findViewById(R.id.daeri_cardcallbtn);
        this.cardCallBtn.setOnClickListener(this);
        this.serial = ((TelephonyManager) getSystemService("phone")).getDeviceId();
        this.locationUtil = this.commonUtil.getLocationUtil(getParent());
        this.mapBtn = (ImageButton) findViewById(R.id.daeri_mapbtn);
        this.mapBtn.setOnClickListener(this);
        new DbAsyncTask().execute(new String[]{String.valueOf(R.id.daeri_addr)});
        this.pointHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (DaeriMainActivity.this.pointTask == null) {
                    DaeriMainActivity.this.pointTask = new DbAsyncTask();
                    DaeriMainActivity.this.pointTask.execute(new String[]{String.valueOf(R.id.daeri_point)});
                }
            }
        };
        this.commonUtil.setPointHandler(this.pointHandler);
        this.pointHandler.sendEmptyMessage(0);
        regionSetting();
        notice();
        eventInit();
        recommendInit();
        subPageInit();
    }

    private void recommendInit() {
        this.recommendDialog = new Dialog(getParent());
        this.recommendDialog.requestWindowFeature(1);
        this.recommendDialog.getWindow().clearFlags(2);
        this.recommendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.recommendDialog.setCanceledOnTouchOutside(false);
        this.recommendDialog.setContentView(R.layout.recommenddialog);
        ((TextView) this.recommendDialog.findViewById(R.id.dialog_content)).setText(this.commonUtil.getPopupRecommend());
        ((ImageButton) this.recommendDialog.findViewById(R.id.dialog_recommendbandbtn)).setOnClickListener(this);
        ((ImageButton) this.recommendDialog.findViewById(R.id.dialog_recommendkakaobtn)).setOnClickListener(this);
        ((ImageButton) this.recommendDialog.findViewById(R.id.dialog_recommendkakaostorybtn)).setOnClickListener(this);
        ((ImageButton) this.recommendDialog.findViewById(R.id.dialog_recommendfacebookbtn)).setOnClickListener(this);
        ((ImageButton) this.recommendDialog.findViewById(R.id.dialog_recommendtwitterbtn)).setOnClickListener(this);
        ((Button) this.recommendDialog.findViewById(R.id.dialog_closebtn)).setOnClickListener(this);
        this.storyLinkHandler = new Handler() {
            public void handleMessage(Message msg) {
            }
        };
        this.recommendDialog.getWindow().getAttributes().width = this.commonUtil.getDialogWidth();
        this.commonUtil.setStoryLinkHandler(this.storyLinkHandler);
    }

    private void notice() {
        Cursor cursor = this.commonUtil.getDbAdapter().queryGetCursor(((((("SELECT notice_seq, notice_title, notice_content, notice_check, notice_gubun, notice_image, notice_action_yn, notice_action_url" + "   FROM tb_notice") + "  WHERE enterprise_id in ('" + this.commonUtil.getEnterprise_id() + "', '')") + "    AND notice_useyn  = 'Y'") + "    AND notice_mainyn = 'Y'") + "    AND notice_check  = 'N'") + "  ORDER BY notice_seq desc");
        if (cursor != null) {
            Log.d("DaeriMainActivity", "cursor.getCount():" + cursor.getCount());
            if (cursor.getCount() > 0) {
                String seq = cursor.getString(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String gubun = cursor.getString(4);
                String image = cursor.getString(5);
                final String actionYn = cursor.getString(6);
                final String actionUrl = cursor.getString(7);
                if (cursor.getString(3).equals("N")) {
                    this.noticDialog = new Dialog(getParent());
                    this.noticDialog.requestWindowFeature(1);
                    this.noticDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    View view;
                    final String str;
                    if (gubun.equals("I")) {
                        view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.custom_imgalert, null);
                        final CheckBox chk = (CheckBox) view.findViewById(R.id.img_check);
                        str = seq;
                        ((ImageButton) view.findViewById(R.id.img_closebtn)).setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                if (!str.equals("") && chk.isChecked()) {
                                    if (!DaeriMainActivity.this.commonUtil.getDbAdapter().queryExecute(("UPDATE tb_notice" + "    SET notice_check = 'Y'") + "  WHERE notice_seq   = '" + str + "'")) {
                                        Log.d("DaeriMainActivity", "notice check false....");
                                    }
                                }
                                new versionCheckTask().execute(new Void[0]);
                                DaeriMainActivity.this.noticDialog.cancel();
                            }
                        });
                        Bitmap bm = (Bitmap) this.commonUtil.getImageMap().get(image);
                        ImageView imageView = (ImageView) view.findViewById(R.id.img_imageview);
                        imageView.setImageBitmap(Bitmap.createScaledBitmap(bm, this.commonUtil.getImgDialogWidth(), (this.commonUtil.getImgDialogWidth() * bm.getHeight()) / bm.getWidth(), true));
                        str = title;
                        imageView.setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                if (actionYn.equals("I")) {
                                    DaeriMainActivity.this.noticDialog.cancel();
                                    new versionCheckTask().execute(new Void[0]);
                                    Intent intent = new Intent(DaeriMainActivity.this, NoticeDetailActivity.class);
                                    String url = actionUrl + "?enterprise=" + DaeriMainActivity.this.commonUtil.getEnterprise_id() + "&security=Y&phone=" + Security.Encode(DaeriMainActivity.this.commonUtil.getMyTelNumber().replaceAll("-", ""));
                                    intent.putExtra("title", str);
                                    intent.putExtra("url", url);
                                    intent.setFlags(603979776);
                                    DaeriMainActivity.this.startActivity(intent);
                                } else if (actionYn.equals("O")) {
                                    DaeriMainActivity.this.noticDialog.cancel();
                                    new versionCheckTask().execute(new Void[0]);
                                    DaeriMainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(actionUrl)));
                                } else {
                                    new versionCheckTask().execute(new Void[0]);
                                }
                            }
                        });
                        this.noticDialog.setContentView(view);
                        this.noticDialog.getWindow().getAttributes().width = this.commonUtil.getImgDialogWidth();
                    } else {
                        view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.custom_dialog, null);
                        ((TextView) view.findViewById(R.id.dialog_title)).setText(title);
                        RelativeLayout dialogBtnLayout = (RelativeLayout) view.findViewById(R.id.dialog_btnlayout);
                        LayoutParams btnParams = new LayoutParams(-1, -2);
                        btnParams.addRule(12);
                        dialogBtnLayout.setLayoutParams(btnParams);
                        ((LinearLayout) view.findViewById(R.id.dialog_twobtnlayout)).setVisibility(8);
                        ((LinearLayout) view.findViewById(R.id.dialog_onebtnlayout)).setVisibility(0);
                        ((TextView) view.findViewById(R.id.dialog_contenttext)).setText(content);
                        str = seq;
                        ((Button) view.findViewById(R.id.dialog_closebtn)).setOnClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                if (!str.equals("")) {
                                    if (!DaeriMainActivity.this.commonUtil.getDbAdapter().queryExecute(("UPDATE tb_notice" + "    SET notice_check = 'Y'") + "  WHERE notice_seq   = '" + str + "'")) {
                                        Log.d("DaeriMainActivity", "notice check false....");
                                    }
                                }
                                new versionCheckTask().execute(new Void[0]);
                                DaeriMainActivity.this.noticDialog.cancel();
                            }
                        });
                        this.noticDialog.setContentView(view);
                        this.noticDialog.setCancelable(false);
                        this.noticDialog.getWindow().getAttributes().width = this.commonUtil.getDialogWidth();
                        this.noticDialog.getWindow().getAttributes().height = this.commonUtil.getDialogHeight();
                    }
                    this.noticDialog.show();
                } else {
                    new versionCheckTask().execute(new Void[0]);
                }
            } else {
                new versionCheckTask().execute(new Void[0]);
            }
            cursor.close();
        }
    }

    private void eventInit() {
        this.eventHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (DaeriMainActivity.this.eventParamList.size() != 0) {
                    HashMap<String, String> param = (HashMap) DaeriMainActivity.this.eventParamList.get(DaeriMainActivity.this.cnt % DaeriMainActivity.this.eventParamList.size());
                    DaeriMainActivity.this.eventTitleText.setText((CharSequence) param.get("event_title"));
                    DaeriMainActivity.this.popupEventTitle.setText((CharSequence) param.get("event_title"));
                    DaeriMainActivity.this.eventSetting(param);
                    DaeriMainActivity.this.cnt = DaeriMainActivity.this.cnt + 1;
                    DaeriMainActivity.this.eventHandler.sendEmptyMessageDelayed(0, 7000);
                }
            }
        };
        this.eventContentLayout = (RelativeLayout) findViewById(R.id.popup_eventcontentlayout);
        this.eventContentLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (DaeriMainActivity.this.eventParamList.size() != 0) {
                    HashMap<String, String> param = (HashMap) DaeriMainActivity.this.eventParamList.get((DaeriMainActivity.this.cnt - 1) % DaeriMainActivity.this.eventParamList.size());
                    if (((String) param.get("event_action_yn")).equals("Y")) {
                        DaeriMainActivity.this.alertLink((String) param.get("event_title"), (String) param.get("event_action_url"), (String) param.get("event_tag"));
                    }
                }
            }
        });
        this.eventTitleText = (TextSwitcher) findViewById(R.id.daeri_eventtitle);
        this.eventTitleText.setFactory(new ViewFactory() {
            public View makeView() {
                TextView myText = new TextView(DaeriMainActivity.this.getParent());
                myText.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 19));
                myText.setTextAppearance(DaeriMainActivity.this.getParent(), R.style.text17sp_left);
                myText.setEllipsize(TruncateAt.MARQUEE);
                myText.setSingleLine();
                myText.setFocusable(true);
                myText.setMarqueeRepeatLimit(-1);
                myText.setHorizontallyScrolling(true);
                myText.setFocusableInTouchMode(true);
                return myText;
            }
        });
        this.popupEventTitle = (TextSwitcher) findViewById(R.id.popup_eventtitle);
        this.popupEventTitle.setFactory(new ViewFactory() {
            public View makeView() {
                TextView myText = new TextView(DaeriMainActivity.this.getParent());
                myText.setLayoutParams(new FrameLayout.LayoutParams(-1, -2, 19));
                myText.setTextAppearance(DaeriMainActivity.this.getParent(), R.style.text17sp_left);
                myText.setEllipsize(TruncateAt.MARQUEE);
                myText.setSingleLine();
                myText.setFocusable(true);
                myText.setMarqueeRepeatLimit(-1);
                myText.setHorizontallyScrolling(true);
                myText.setFocusableInTouchMode(true);
                return myText;
            }
        });
        this.eventParamList = new ArrayList();
        event();
        this.eventLayout = (RelativeLayout) findViewById(R.id.eventlayout);
        this.eventPopupLayout = (RelativeLayout) findViewById(R.id.eventpopuplayout);
        this.eventPopupLayout.setVisibility(0);
        this.eventPopupLayout.setVisibility(8);
        this.eventpopupbtn = (ImageButton) findViewById(R.id.daeri_eventpopupbtn);
        this.eventpopupbtn.setOnClickListener(this);
        this.eventPopupCloseBtn = (ImageButton) findViewById(R.id.popup_closebtn);
        this.eventPopupCloseBtn.setOnClickListener(this);
        this.animDailogUp = AnimationUtils.loadAnimation(getParent(), R.anim.slide_up_dialog);
        this.animDailogDown = AnimationUtils.loadAnimation(getParent(), R.anim.slide_out_down);
    }

    private void event() {
        this.imageMap = this.commonUtil.getImageMap();
        Cursor cursor = this.commonUtil.getDbAdapter().queryGetCursor(((((((("SELECT a.event_seq, a.event_title, a.event_start, a.event_end, a.event_content," + "        a.event_image, a.event_url, a.event_action_yn, a.event_action_url, a.event_gubun,") + "        a.event_tag, a.winner_yn, a.winner_dt") + "   FROM tb_event a") + "  WHERE datetime('now') between a.event_start and a.event_end") + "    AND a.event_use = 'Y'") + "    AND a.event_mainshow = 'Y'") + "    AND (a.enterprise_id in ('" + this.commonUtil.getEnterprise_id() + "', '')  OR all_show != 'N')") + "  ORDER BY a.event_seq desc");
        if (cursor == null || cursor.getCount() <= 0) {
            if (cursor != null) {
                cursor.close();
            }
            this.eventHandler.sendEmptyMessage(0);
        }
        do {
            HashMap<String, String> param = new HashMap();
            param.put("event_seq", cursor.getString(0));
            param.put("event_title", cursor.getString(1));
            param.put("event_start", cursor.getString(2).substring(0, 10));
            param.put("event_end", cursor.getString(3).substring(0, 10));
            param.put("event_content", cursor.getString(4));
            param.put("event_image", cursor.getString(5));
            param.put("event_url", cursor.getString(6));
            param.put("event_action_yn", cursor.getString(7));
            param.put("event_action_url", cursor.getString(8));
            param.put("event_gubun", JoyNUtil.nullCheck(cursor.getString(9), ""));
            param.put("event_tag", JoyNUtil.nullCheck(cursor.getString(10), ""));
            param.put("winner_yn", cursor.getString(11));
            param.put("winner_dt", cursor.getString(12));
            this.eventParamList.add(param);
            if (((String) param.get("event_gubun")).equals("I") && !((String) param.get("event_image")).equals("")) {
                new DoImageLoad().execute(new String[]{cursor.getString(5)});
            }
        } while (cursor.moveToNext());
        if (cursor != null) {
            cursor.close();
        }
        this.eventHandler.sendEmptyMessage(0);
    }

    private void eventSetting(HashMap<String, String> param) {
        String event_content = (String) param.get("event_content");
        String event_image = (String) param.get("event_image");
        String event_url = (String) param.get("event_url");
        String event_gubun = (String) param.get("event_gubun");
        RelativeLayout textLayout = (RelativeLayout) findViewById(R.id.eventrow_text);
        RelativeLayout imageLayout = (RelativeLayout) findViewById(R.id.eventrow_image);
        RelativeLayout webLayout = (RelativeLayout) findViewById(R.id.eventrow_web);
        if (event_gubun.equals("T")) {
            textLayout.setVisibility(0);
            imageLayout.setVisibility(8);
            webLayout.setVisibility(8);
            ((TextView) findViewById(R.id.eventrow_text_content)).setText(event_content);
        } else if (event_gubun.equals("I") && !event_image.equals("")) {
            textLayout.setVisibility(8);
            imageLayout.setVisibility(0);
            webLayout.setVisibility(8);
            ImageView imageview = (ImageView) findViewById(R.id.eventrow_img_imageview);
            int imgViewWidth = (int) (((double) imageview.getWidth()) * 0.8d);
            Bitmap bm = (Bitmap) this.imageMap.get(event_image);
            if (bm != null) {
                int iWidth = bm.getWidth();
                int iHeight = bm.getHeight();
                if (iWidth < imgViewWidth) {
                    bm = Bitmap.createScaledBitmap(bm, imgViewWidth, (iHeight * imgViewWidth) / iWidth, true);
                }
                imageview.setImageBitmap(bm);
            }
        } else if (event_gubun.equals("U")) {
            textLayout.setVisibility(8);
            imageLayout.setVisibility(8);
            webLayout.setVisibility(0);
        }
    }

    private void alertLink(String title, String url, String tag) {
        try {
            if (!JoyNUtil.isCheckNetworkState(getParent(), this.commonUtil.getAppType().equals(JoyNInterface.KT))) {
                return;
            }
            if (tag.equals("02")) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
                return;
            }
            Intent intent = new Intent(getParent(), EventDetailActivity.class);
            url = url + "?enterprise=" + this.commonUtil.getEnterprise_id() + "&security=Y&phone=" + Security.Encode(this.commonUtil.getMyTelNumber().replaceAll("-", ""));
            Log.d("DaeriMainActivity", "action_url:" + url);
            intent.putExtra("title", title);
            intent.putExtra("url", url);
            intent.setFlags(603979776);
            goNextHistory("EventDetailActivity", intent);
        } catch (Exception e) {
            Log.d("DaeriMainActivity", "e:" + e);
        }
    }

    private void subPageInit() {
        this.subViewFlipper = (ViewFlipper) findViewById(R.id.sub_viewflipper);
        this.subViewFlipper.setDisplayedChild(0);
        this.subPagePrevBtn = (ImageButton) findViewById(R.id.sub_prevbtn);
        this.subPagePrevBtn.setOnClickListener(this);
        this.subPageNextBtn = (ImageButton) findViewById(R.id.sub_nextbtn);
        this.subPageNextBtn.setOnClickListener(this);
        this.pointStroeBtn = (ImageButton) findViewById(R.id.daeri_pointstroebtn);
        this.pointStroeBtn.setOnClickListener(this);
        this.multiCallBtn = (ImageButton) findViewById(R.id.daeri_multicallbtn);
        this.multiCallBtn.setOnClickListener(this);
        this.logisticeBtn = (ImageButton) findViewById(R.id.daeri_logisticebtn);
        this.logisticeBtn.setOnClickListener(this);
        this.recommendBtn = (ImageButton) findViewById(R.id.daeri_recommendbtn);
        this.recommendBtn.setOnClickListener(this);
        this.locationRefreshBtn = (ImageButton) findViewById(R.id.daeri_locationrefreshbtn);
        this.locationRefreshBtn.setOnClickListener(this);
        this.okCashBtn = (ImageButton) findViewById(R.id.daeri_okcashbtn);
        this.okCashBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        UnsupportedEncodingException e;
        if (!this.bEventPopup || v.getId() == R.id.popup_closebtn) {
            String enterprise_tel = "";
            String lan = "";
            String lon = "";
            String call_type = "";
            String msg = "";
            String[] params = new String[10];
            Intent intent;
            switch (v.getId()) {
                case R.id.dialog_closebtn:
                    break;
                case R.id.daeri_helpbtn:
                    intent = new Intent(this, HelpViewActivity.class);
                    intent.setFlags(603979776);
                    startActivity(intent);
                    return;
                case R.id.daeri_pointhisbtn:
                    intent = new Intent(getParent(), PointViewActivity.class);
                    intent.setFlags(603979776);
                    goNextHistory("PointViewActivity", intent);
                    return;
                case R.id.daeri_locationrefreshbtn:
                case R.id.daeri_mapbtn:
                    intent = new Intent(getParent(), LocalViewMapActivity.class);
                    intent.setFlags(603979776);
                    goNextHistory("LocalViewMapActivity", intent);
                    return;
                case R.id.daeri_cashcallbtn:
                    enterprise_tel = this.commonUtil.getEnterprise_tel();
                    lan = "0.0";
                    lon = "0.0";
                    call_type = AppEventsConstants.EVENT_PARAM_VALUE_YES;
                    msg = "";
                    if (this.locationUtil.getLastLocation() != null) {
                        lan = String.valueOf(this.locationUtil.getLastLocation().getLatitude());
                        lon = String.valueOf(this.locationUtil.getLastLocation().getLongitude());
                    }
                    params[0] = "적립된 포인트는 현금처럼 사용 할 수 있습니다.";
                    params[1] = enterprise_tel + " 연결을 하시겠습니까?";
                    params[2] = this.commonUtil.getEnterprise_id();
                    params[3] = AppEventsConstants.EVENT_PARAM_VALUE_YES;
                    params[4] = enterprise_tel;
                    params[5] = lan;
                    params[6] = lon;
                    params[7] = this.address;
                    params[8] = call_type;
                    params[9] = this.commonUtil.getProgram_version();
                    new JoyCallUtil(this.commonUtil, getParent()).CallConnect(params);
                    return;
                case R.id.daeri_cardcallbtn:
                    enterprise_tel = this.commonUtil.getEnterprise_tel();
                    lan = "0.0";
                    lon = "0.0";
                    call_type = JoyNInterface.TAXI;
                    msg = "";
                    String strmsg = "";
                    if (this.locationUtil.getLastLocation() != null) {
                        lan = String.valueOf(this.locationUtil.getLastLocation().getLatitude());
                        lon = String.valueOf(this.locationUtil.getLastLocation().getLongitude());
                        msg = "적립된 포인트는 현금처럼 사용 할 수 있습니다.";
                    } else {
                        msg = "현위치가 조회되지 않아 상담이 원활하지 않을수 있습니다.";
                    }
                    if (!this.commonUtil.getEnterprise_id().equals("EA0001")) {
                        msg = "카드결제를 사용하실 수 없는 지역입니다.";
                        strmsg = "현금졀제로 전화연결하시겠습니까?";
                    } else if (this.commonUtil.getCard_type().length() > 0) {
                        strmsg = "입력하신 카드정보입니다.\n카드사 : " + this.commonUtil.getCard_type() + "\n카드번호 : " + this.commonUtil.getCard_num1() + "-" + this.commonUtil.getCard_num2() + "-****-****\n유효기간 : **월 **년\n" + enterprise_tel + "  연결을 하시겠습니까?";
                    } else {
                        strmsg = "*카드 결제를 위해 상담원에게 카드번호와 유효기간을 알려주시기 바랍니다.\n*설정화면에서 카드정보를 입력하시면 좀더 원활한 상담을 진행하실 수 있습니다.\n" + enterprise_tel + "  연결을 하시겠습니까?";
                    }
                    params[0] = msg;
                    params[1] = strmsg;
                    params[2] = this.commonUtil.getEnterprise_id();
                    params[3] = AppEventsConstants.EVENT_PARAM_VALUE_YES;
                    params[4] = enterprise_tel;
                    params[5] = lan;
                    params[6] = lon;
                    params[7] = this.address;
                    params[8] = call_type;
                    params[9] = this.commonUtil.getProgram_version();
                    new JoyCallUtil(this.commonUtil, getParent()).CallConnect(params);
                    return;
                case R.id.sub_prevbtn:
                    this.subPagePrevBtn.setVisibility(8);
                    this.subPageNextBtn.setVisibility(0);
                    this.subViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
                    this.subViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
                    this.subViewFlipper.showPrevious();
                    return;
                case R.id.sub_nextbtn:
                    this.subPagePrevBtn.setVisibility(0);
                    this.subPageNextBtn.setVisibility(8);
                    this.subViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
                    this.subViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
                    this.subViewFlipper.showNext();
                    return;
                case R.id.daeri_okcashbtn:
                    Log.d("DaeriMainActivity", "DaeriMainActivity R.id.daeri_okcashbtn");
                    intent = new Intent(getParent(), OkCashBagActivity.class);
                    intent.setFlags(603979776);
                    goNextHistory("OkCashBagActivity", intent);
                    return;
                case R.id.daeri_pointstroebtn:
                    intent = new Intent(getParent(), StoreCouponActivity.class);
                    intent.setFlags(603979776);
                    goNextHistory("StoreCouponActivity", intent);
                    return;
                case R.id.daeri_multicallbtn:
                    intent = new Intent(getParent(), DaeriMultiCallActivity.class);
                    intent.setFlags(603979776);
                    goNextHistory("DaeriMultiCallActivity", intent);
                    return;
                case R.id.daeri_recommendbtn:
                    this.recommendDialog.show();
                    return;
                case R.id.daeri_logisticebtn:
                    intent = new Intent(getParent(), LogisticeListActivity.class);
                    intent.setFlags(603979776);
                    goNextHistory("LogisticeListActivity", intent);
                    return;
                case R.id.daeri_eventpopupbtn:
                    this.bEventPopup = true;
                    this.eventPopupLayout.startAnimation(this.animDailogUp);
                    this.eventPopupLayout.setVisibility(0);
                    this.eventLayout.setVisibility(4);
                    return;
                case R.id.popup_closebtn:
                    this.bEventPopup = false;
                    this.eventPopupLayout.startAnimation(this.animDailogDown);
                    this.eventPopupLayout.setVisibility(8);
                    this.eventLayout.setVisibility(0);
                    break;
                case R.id.dialog_recommendkakaobtn:
                    this.recommendDialog.cancel();
                    JoyNUtil.kakaoLink(getParent());
                    return;
                case R.id.dialog_recommendkakaostorybtn:
                    final Dialog dialog = new Dialog(getParent());
                    dialog.requestWindowFeature(1);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    View view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.custom_alert, null);
                    ((RelativeLayout) view.findViewById(R.id.dialog_titlelayout)).setVisibility(8);
                    ((TextView) view.findViewById(R.id.dialog_contenttext)).setText("카카오스토리로 조이앤대리운전을 추천하겠습니까?");
                    ((LinearLayout) view.findViewById(R.id.dialog_twobtnlayout)).setVisibility(0);
                    Button actionBtn = (Button) view.findViewById(R.id.dialog_actionbtn);
                    actionBtn.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            DaeriMainActivity.this.recommendDialog.cancel();
                            dialog.cancel();
                            if (DaeriMainActivity.this.commonUtil.getStory_yn().equals("Y")) {
                                DaeriMainActivity.this.storyLinkHandler.sendEmptyMessage(0);
                            }
                        }
                    });
                    actionBtn.setText("추천");
                    Button cancelBtn = (Button) view.findViewById(R.id.dialog_cancelbtn);
                    cancelBtn.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    cancelBtn.setText("취소");
                    ((LinearLayout) view.findViewById(R.id.dialog_onebtnlayout)).setVisibility(8);
                    dialog.setContentView(view);
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().width = this.commonUtil.getDialogWidth();
                    dialog.show();
                    return;
                case R.id.dialog_recommendfacebookbtn:
                    this.recommendDialog.cancel();
                    JoyNUtil.facebookLink(getParent(), this.callbackManager);
                    return;
                case R.id.dialog_recommendtwitterbtn:
                    this.recommendDialog.cancel();
                    try {
                        Intent intent2 = new Intent(getParent(), EventDetailActivity.class);
                        try {
                            intent2.setFlags(603979776);
                            intent2.putExtra("title", "트위터 공유");
                            intent2.putExtra("url", "https://twitter.com/intent/tweet?text=" + URLEncoder.encode(this.commonUtil.getTwitterMsg(), "UTF-8"));
                            goNextHistory("EventDetailActivity", intent2);
                            intent = intent2;
                            return;
                        } catch (UnsupportedEncodingException e2) {
                            e = e2;
                            intent = intent2;
                            e.printStackTrace();
                            return;
                        }
                    } catch (UnsupportedEncodingException e3) {
                        e = e3;
                        e.printStackTrace();
                        return;
                    }
                case R.id.dialog_recommendbandbtn:
                    this.recommendDialog.cancel();
                    JoyNUtil.bandLink(getParent());
                    return;
                default:
                    return;
            }
            this.recommendDialog.cancel();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void onResume() {
        super.onResume();
        this.pointHandler.sendEmptyMessage(0);
        if (!this.beforTel.equals(this.commonUtil.getMyTelNumber()) || !this.beforRegion.equals(this.commonUtil.getRegion())) {
            this.beforTel = this.commonUtil.getMyTelNumber();
            this.beforRegion = this.commonUtil.getRegion();
            regionSetting();
        }
    }

    private void regionSetting() {
        String region = this.commonUtil.getRegion_nm();
        int end = region.indexOf("(");
        if (end > 0) {
            region = region.substring(0, end);
        }
        this.regionText.setText(region);
    }

    private void versionCheck(String cnt) {
        JoyNUtil.setFileData(getParent(), JoyNInterface.FILE_UPDATE_CNT, cnt);
        final Dialog dialog = new Dialog(getParent());
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View view = ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.custom_alert, null);
        ((RelativeLayout) view.findViewById(R.id.dialog_titlelayout)).setVisibility(8);
        ((TextView) view.findViewById(R.id.dialog_contenttext)).setText("최신버전으로 업데이트 하시겠습니까?");
        ((LinearLayout) view.findViewById(R.id.dialog_onebtnlayout)).setVisibility(8);
        ((LinearLayout) view.findViewById(R.id.dialog_twobtnlayout)).setVisibility(0);
        Button updateBtn = (Button) view.findViewById(R.id.dialog_actionbtn);
        updateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
                JoyNUtil.app_update(DaeriMainActivity.this.getParent(), DaeriMainActivity.this.commonUtil.getAppType());
            }
        });
        updateBtn.setText(getResources().getString(R.string.text_updatetext));
        Button lastBtn = (Button) view.findViewById(R.id.dialog_cancelbtn);
        lastBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        lastBtn.setText(getResources().getString(R.string.text_lasttext));
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().width = this.commonUtil.getDialogWidth();
        dialog.show();
    }
}
