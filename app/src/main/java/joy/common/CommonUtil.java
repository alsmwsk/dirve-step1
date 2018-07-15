package joy.common;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.widget.TabHost;
//import com.joy.db.DbDatabase;
//import com.kakao.kakaolink.KakaoLink;
//import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtil extends MultiDexApplication implements JoyNInterface {
    private String address = "";
    private String appType = "";
    private String appUpdateType = "N";
    private String bandMsg = "";
    private String beforRegion = "";
    private String beforTel = "";
    private String bestProvider = "";
    private int bigTextSize = 20;
    private String card_month = "";
    private String card_num1 = "";
    private String card_num2 = "";
    private String card_num3 = "";
    private String card_num4 = "";
    private String card_owner_gubun = "01";
    private String card_owner_nm = "";
    private String card_type = "";
    private String card_year = "";
    private Handler changeListHandler = null;
    private Handler couponListHandler = null;
    private JoyDbAdapter dbAdapter = null;
    //private DbDatabase dbDatabase = null;
    private String db_version = "";
    private int dialogHeight = 0;
    private int dialogWidth = 0;
    private String email_address = "";
    private String email_yn = "";
    private String enterprise_id = "EA0001";
    private String enterprise_nm = "";
    private String enterprise_tel = "";
    private String event_title = "";
    private String facebookMsg = "";
    private float font = 1.0f;
    private HashMap<String, Bitmap> imageMap = null;
    private int imgDialogHeight = 0;
    private int imgDialogWidth = 0;
    private String initRecommend = "";
    private String ipType = "";
    private boolean isDbConnect = false;
    private boolean isSideMenu = false;
    private JoyNUtil joyNUtil = null;
    //private KakaoLink kakaoLink;
    private String kakaoMsg = "";
    //private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private String latitude = "";
    private int listTextSize = 18;
    private String localType = "Y";
    private JoyLocationUtil locationUtil = null;
    private String longitude = "";
    private String mailAddress = "joyndrive@gmail.com";
    private Handler mainHandler = null;
    private int mediumTextSize = 18;
    private String myTelNumber = "";
    private String newMember = "";
    private String order_cancel = "N";
    private Map<Integer, List<String>> pageMap = null;
    private int pageView_cnt = 0;
    private Handler pointHandler = null;
    private String popupRecommend = "";
    private String program_version = "";
    private Handler qnaListHandler = null;
    private String region = "";
    private String region_nm = "";
    private float scale = 1.0f;
    private float scaleHeight = 1.0f;
    private float scaleWidth = 1.0f;
    private String settingYN = "N";
    private Handler settingviewHandler = null;
    private String showUpdate = "N";
    private String sidePageMove = "";
    private int smallTextSize = 15;
    private Handler storyLinkHandler = null;
    private String storyMsg = "";
    private String story_yn = "N";
    private TabHost tabHost = null;
    private int titleHeight = 70;
    private int titleTextSize = 20;
    private String twitterMsg = "";
    private String type = "";

    public void dbOpen() {
        this.dbAdapter.open();
    }

    public void dbClose() {
        this.dbAdapter.close();
    }

//    public void open() {
//        if (this.ipType.equals("M")) {
//            this.dbDatabase = new DbDatabase(JoyNInterface.SUB_IP, 6408);
//            this.ipType = "S";
//        } else {
//            this.dbDatabase = new DbDatabase(JoyNInterface.MAIN_IP, 6408);
//            this.ipType = "M";
//        }
//        try {
//            if (this.dbDatabase.open()) {
//                this.isDbConnect = true;
//                return;
//            }
//            if (this.ipType.equals("M")) {
//                this.dbDatabase = new DbDatabase(JoyNInterface.SUB_IP, 6408);
//                this.ipType = "S";
//            } else {
//                this.dbDatabase = new DbDatabase(JoyNInterface.MAIN_IP, 6408);
//                this.ipType = "M";
//            }
//            if (this.dbDatabase.open()) {
//                this.isDbConnect = true;
//            } else {
//                this.isDbConnect = false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            this.isDbConnect = false;
//        }
//    }
//
//    public void close() {
//        this.dbDatabase.close();
//        this.dbAdapter.close();
//    }

    private void set() {
        Cursor cursor = JoyNUtil.dbSearchDate(this, this.myTelNumber);
        if (cursor != null && cursor.getCount() > 0) {
            this.enterprise_id = cursor.getString(0);
            this.enterprise_nm = cursor.getString(1);
            this.enterprise_tel = cursor.getString(2);
            this.region = cursor.getString(3);
            this.region_nm = cursor.getString(4);
            this.myTelNumber = cursor.getString(5);
        }
    }

    public JoyNUtil getJoyNUtil() {
        return this.joyNUtil;
    }

    public void setJoyNUtil(JoyNUtil joyNUtil) {
        this.joyNUtil = joyNUtil;
    }

    public JoyDbAdapter getDbAdapter() {
        return this.dbAdapter;
    }

    public void setDbAdapter(JoyDbAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
    }

//    public DbDatabase getDbDatabase() {
//        if (!this.isDbConnect) {
//            open();
//        }
//        return this.dbDatabase;
//    }

//    public void setDbDatabase(DbDatabase dbDatabase) {
//        this.dbDatabase = dbDatabase;
//    }

    public boolean getIsDbConnect() {
        return this.isDbConnect;
    }

    public void setIsDbConnect(boolean isDbConnect) {
        this.isDbConnect = isDbConnect;
    }

    public String getEnterprise_id() {
        if (this.enterprise_id == null || this.enterprise_id.equals("")) {
            set();
        }
        return this.enterprise_id;
    }

    public void setEnterprise_id(String enterprise_id) {
        this.enterprise_id = enterprise_id;
    }

    public String getEnterprise_nm() {
        if (this.enterprise_nm == null || this.enterprise_nm.equals("")) {
            set();
        }
        return this.enterprise_nm;
    }

    public void setEnterprise_nm(String enterprise_nm) {
        this.enterprise_nm = enterprise_nm;
    }

    public String getEnterprise_tel() {
        if (this.enterprise_tel == null || this.enterprise_tel.equals("")) {
            set();
        }
        return this.enterprise_tel;
    }

    public void setEnterprise_tel(String enterprise_tel) {
        this.enterprise_tel = enterprise_tel;
    }

    public String getRegion() {
        if (this.region == null || this.region.equals("")) {
            set();
        }
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion_nm() {
        if (this.region_nm == null || this.region_nm.equals("")) {
            set();
        }
        return this.region_nm;
    }

    public void setRegion_nm(String region_nm) {
        this.region_nm = region_nm;
    }

    public String getMyTelNumber() {
        if (this.myTelNumber == null || this.myTelNumber.equals("")) {
            set();
        }
        return this.myTelNumber;
    }

    public void setMyTelNumber(String myTelNumber) {
        this.myTelNumber = myTelNumber;
    }

    public String getProgram_version() {
        return this.program_version;
    }

    public void setProgram_version(String programVersion) {
        this.program_version = programVersion;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEvent_title() {
        return this.event_title;
    }

    public String getBestProvider() {
        return this.bestProvider;
    }

    public void setBestProvider(String bestProvider) {
        this.bestProvider = bestProvider;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getNewMember() {
        return this.newMember;
    }

    public void setNewMember(String newMember) {
        this.newMember = newMember;
    }

    public String getSettingYN() {
        return this.settingYN;
    }

    public void setSettingYN(String settingYN) {
        this.settingYN = settingYN;
    }

    public String getMailAddress() {
        return this.mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getLocalType() {
        return this.localType;
    }

    public void setLocalType(String localType) {
        this.localType = localType;
    }

    public String getAppType() {
        return this.appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShowUpdate() {
        return this.showUpdate;
    }

    public void setShowUpdate(String showUpdate) {
        this.showUpdate = showUpdate;
    }

    public String getCard_type() {
        return this.card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCard_num1() {
        return this.card_num1;
    }

    public void setCard_num1(String card_num1) {
        this.card_num1 = card_num1;
    }

    public String getCard_num2() {
        return this.card_num2;
    }

    public void setCard_num2(String card_num2) {
        this.card_num2 = card_num2;
    }

    public String getCard_num3() {
        return this.card_num3;
    }

    public void setCard_num3(String card_num3) {
        this.card_num3 = card_num3;
    }

    public String getCard_num4() {
        return this.card_num4;
    }

    public void setCard_num4(String card_num4) {
        this.card_num4 = card_num4;
    }

    public String getCard_year() {
        return this.card_year;
    }

    public void setCard_year(String card_year) {
        this.card_year = card_year;
    }

    public String getCard_month() {
        return this.card_month;
    }

    public void setCard_month(String card_month) {
        this.card_month = card_month;
    }

    public Handler getMainHandler() {
        return this.mainHandler;
    }

    public void setMainHandler(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public Handler getQnaListHandler() {
        return this.qnaListHandler;
    }

    public void setQnaListHandler(Handler qnaListHandler) {
        this.qnaListHandler = qnaListHandler;
    }

    public TabHost getTabHost() {
        return this.tabHost;
    }

    public void setTabHost(TabHost tabHost) {
        this.tabHost = tabHost;
    }

    public int getTitleHeight() {
        return this.titleHeight;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }

    public int getTitleTextSize() {
        return this.titleTextSize;
    }

    public void setTitleTextSize(int titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    public int getListTextSize() {
        return this.listTextSize;
    }

    public void setListTextSize(int listTextSize) {
        this.listTextSize = listTextSize;
    }

    public int getSmallTextSize() {
        return this.smallTextSize;
    }

    public void setSmallTextSize(int smallTextSize) {
        this.smallTextSize = smallTextSize;
    }

    public int getMediumTextSize() {
        return this.mediumTextSize;
    }

    public void setMediumTextSize(int mediumTextSize) {
        this.mediumTextSize = mediumTextSize;
    }

    public int getBigTextSize() {
        return this.bigTextSize;
    }

    public void setBigTextSize(int bigTextSize) {
        this.bigTextSize = bigTextSize;
    }

    public float getScaleWidth() {
        return this.scaleWidth;
    }

    public void setScaleWidth(float scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    public float getScaleHeight() {
        return this.scaleHeight;
    }

    public void setScaleHeight(float scaleHeight) {
        this.scaleHeight = scaleHeight;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getFont() {
        return this.font;
    }

    public void setFont(float font) {
        this.font = font;
    }

    public String getAppUpdateType() {
        return this.appUpdateType;
    }

    public void setAppUpdateType(String appUpdateType) {
        this.appUpdateType = appUpdateType;
    }

    public String getEmail_yn() {
        return this.email_yn;
    }

    public void setEmail_yn(String email_yn) {
        this.email_yn = email_yn;
    }

    public String getEmail_address() {
        return this.email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public int getPageView_cnt() {
        return this.pageView_cnt;
    }

    public void setPageView_cnt(int pageView_cnt) {
        this.pageView_cnt = pageView_cnt;
    }

    public Map<Integer, List<String>> getPageMap() {
        return this.pageMap;
    }

    public void setPageMap(Map<Integer, List<String>> pageMap) {
        this.pageMap = pageMap;
    }

    public String getCard_owner_gubun() {
        return this.card_owner_gubun;
    }

    public void setCard_owner_gubun(String card_owner_gubun) {
        this.card_owner_gubun = card_owner_gubun;
    }

    public String getCard_owner_nm() {
        return this.card_owner_nm;
    }

    public void setCard_owner_nm(String card_owner_nm) {
        this.card_owner_nm = card_owner_nm;
    }

    public String getOrder_cancel() {
        return this.order_cancel;
    }

    public void setOrder_cancel(String order_cancel) {
        this.order_cancel = order_cancel;
    }

    public Handler getCouponListHandler() {
        return this.couponListHandler;
    }

    public void setCouponListHandler(Handler couponListHandler) {
        this.couponListHandler = couponListHandler;
    }

    public Handler getSettingviewHandler() {
        return this.settingviewHandler;
    }

    public void setSettingviewHandler(Handler settingviewHandler) {
        this.settingviewHandler = settingviewHandler;
    }

    public String getStory_yn() {
        return this.story_yn;
    }

    public void setStory_yn(String story_yn) {
        this.story_yn = story_yn;
    }

    public Handler getStoryLinkHandler() {
        return this.storyLinkHandler;
    }

    public void setStoryLinkHandler(Handler storyLinkHandler) {
        this.storyLinkHandler = storyLinkHandler;
    }

    public boolean isSideMenu() {
        return this.isSideMenu;
    }

    public void setIsSideMenu(boolean isSideMenu) {
        this.isSideMenu = isSideMenu;
    }

    public String getBeforTel() {
        return this.beforTel;
    }

    public void setBeforTel(String beforTel) {
        this.beforTel = beforTel;
    }

    public String getBeforRegion() {
        return this.beforRegion;
    }

    public void setBeforRegion(String beforRegion) {
        this.beforRegion = beforRegion;
    }

    public String getSidePageMove() {
        return this.sidePageMove;
    }

    public void setSidePageMove(String sidePageMove) {
        this.sidePageMove = sidePageMove;
    }

    public JoyLocationUtil getLocationUtil() {
        return this.locationUtil;
    }

    public JoyLocationUtil getLocationUtil(Context context) {
        if (this.locationUtil == null) {
            this.locationUtil = JoyLocationUtil.getInstance(context);
        }
        return this.locationUtil;
    }

    public void setLocationUtil(JoyLocationUtil locationUtil) {
        this.locationUtil = locationUtil;
    }

    public int getDialogWidth() {
        return this.dialogWidth;
    }

    public void setDialogWidth(int dialogWidth) {
        this.dialogWidth = dialogWidth;
    }

    public int getDialogHeight() {
        return this.dialogHeight;
    }

    public void setDialogHeight(int dialogHeight) {
        this.dialogHeight = dialogHeight;
    }

    public HashMap<String, Bitmap> getImageMap() {
        return this.imageMap;
    }

    public void setImageMap(HashMap<String, Bitmap> imageMap) {
        this.imageMap = imageMap;
    }

    public Handler getChangeListHandler() {
        return this.changeListHandler;
    }

    public void setChangeListHandler(Handler changeListHandler) {
        this.changeListHandler = changeListHandler;
    }

    public String getKakaoMsg() {
        return this.kakaoMsg;
    }

    public void setKakaoMsg(String kakaoMsg) {
        this.kakaoMsg = kakaoMsg;
    }

    public String getStoryMsg() {
        return this.storyMsg;
    }

    public void setStoryMsg(String storyMsg) {
        this.storyMsg = storyMsg;
    }

    public String getFacebookMsg() {
        return this.facebookMsg;
    }

    public void setFacebookMsg(String facebookMsg) {
        this.facebookMsg = facebookMsg;
    }

    public String getBandMsg() {
        return this.bandMsg;
    }

    public void setBandMsg(String bandMsg) {
        this.bandMsg = bandMsg;
    }

    public String getTwitterMsg() {
        return this.twitterMsg;
    }

    public void setTwitterMsg(String twitterMsg) {
        this.twitterMsg = twitterMsg;
    }

    public String getDb_version() {
        return this.db_version;
    }

    public void setDb_version(String db_version) {
        this.db_version = db_version;
    }

    public String getInitRecommend() {
        return this.initRecommend;
    }

    public void setInitRecommend(String initRecommend) {
        this.initRecommend = initRecommend;
    }

    public String getPopupRecommend() {
        return this.popupRecommend;
    }

    public void setPopupRecommend(String popupRecommend) {
        this.popupRecommend = popupRecommend;
    }

    public int getImgDialogWidth() {
        return this.imgDialogWidth;
    }

    public void setImgDialogWidth(int imgDialogWidth) {
        this.imgDialogWidth = imgDialogWidth;
    }

    public int getImgDialogHeight() {
        return this.imgDialogHeight;
    }

    public void setImgDialogHeight(int imgDialogHeight) {
        this.imgDialogHeight = imgDialogHeight;
    }

    public Handler getPointHandler() {
        return this.pointHandler;
    }

    public void setPointHandler(Handler pointHandler) {
        this.pointHandler = pointHandler;
    }

//    public KakaoLink getKakaoLink() {
//        return this.kakaoLink;
//    }
//
//    public void setKakaoLink(KakaoLink kakaoLink) {
//        this.kakaoLink = kakaoLink;
//    }
//
//    public KakaoTalkLinkMessageBuilder getKakaoTalkLinkMessageBuilder() {
//        return this.kakaoTalkLinkMessageBuilder;
//    }
//
//    public void setKakaoTalkLinkMessageBuilder(KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder) {
//        this.kakaoTalkLinkMessageBuilder = kakaoTalkLinkMessageBuilder;
//    }
}
