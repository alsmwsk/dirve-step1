package joy.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.share.Sharer.Result;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareLinkContent.Builder;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.drive.DriveFile;
import com.google.firebase.appindexing.Indexable;
import com.joy.db.DbCommand;
import com.joy.db.DbInterface;
import com.joy.db.DbRecordset;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.ServerProtocol;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import joy.LifeBookMark.R;

public class JoyNUtil {
    private static final String TAG = "JoyNUtil";

    public static String getToday() {
        return getDate("yyyy-MM-dd", 0);
    }

    public static String getToday(String format) {
        return getDate(format, 0);
    }

    public static String getDate(String format, int interVal) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
            Calendar cal = Calendar.getInstance();
            cal.set(cal.get(1), cal.get(2), cal.get(5) + interVal);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDate(String format, String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
            int year = Integer.valueOf(date.substring(0, 4)).intValue();
            int month = Integer.valueOf(date.substring(4, 6)).intValue() - 1;
            int day = Integer.valueOf(date.substring(6, 8)).intValue();
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "";
        }
    }

    public static long isDateGap(String begin, String end) {
        long gap = 0;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            return (formatter.parse(end.replaceAll("-", "")).getTime() - formatter.parse(begin.replaceAll("-", "")).getTime()) / 86400000;
        } catch (Exception e) {
            e.printStackTrace();
            return gap;
        }
    }

    public static String nullCheck(String str, String def) {
        if (str == null || str.equals(null)) {
            return def;
        }
        return str;
    }

    public static String money(String money) {
        if (money == null || money.equals("null") || money.equals("")) {
            return AppEventsConstants.EVENT_PARAM_VALUE_NO;
        }
        return money(Long.valueOf(money).longValue());
    }

    public static String money(long money) {
        try {
            return new DecimalFormat("#,##0").format(money);
        } catch (Exception e) {
            return "" + money;
        }
    }

    public static String mile(CommonUtil commonUtil, String serial) {
        String customer_mile = AppEventsConstants.EVENT_PARAM_VALUE_NO;
        try {
            DbCommand command = new DbCommand(commonUtil.getDbDatabase(), "sp_mobile02_getCustomerMileNew");
            command.addParameter((int) DbInterface.STRING_TYPE, 1, commonUtil.getEnterprise_id());
            command.addParameter((int) DbInterface.STRING_TYPE, 1, commonUtil.getMyTelNumber());
            command.addParameter((int) DbInterface.STRING_TYPE, 1, serial);
            DbRecordset pRs = new DbRecordset(commonUtil.getDbDatabase());
            if (pRs.execute(command)) {
                customer_mile = pRs.getFieldStringValue("CUSTOMER_MILE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer_mile;
    }

    public static String getAddress(Context context, double lat, double lng) {
        List<Address> list = null;
        try {
            list = new Geocoder(context, Locale.KOREA).getFromLocation(lat, lng, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.size() == 0) {
            return fetchCityNameUsingGoogleMap(lat, lng);
        }
        String[] addr = ((Address) list.get(0)).getAddressLine(0).split(ServerProtocol.AUTHORIZATION_HEADER_DELIMITER);
        String address = "";
        for (int i = 1; i < addr.length; i++) {
            address = address + addr[i] + ServerProtocol.AUTHORIZATION_HEADER_DELIMITER;
        }
        return address;
    }

    public static String fetchCityNameUsingGoogleMap(double lat, double lng) {
        String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=false&language=ko";
        return "위치정보를 찾을수 없습니다.";
    }

    public static String getTelNumber(String tel_no) {
        if (tel_no.length() == 11) {
            return tel_no.substring(0, 3) + "-" + tel_no.substring(3, 7) + "-" + tel_no.substring(7, tel_no.length());
        }
        if (tel_no.length() == 10) {
            return tel_no.substring(0, 3) + "-" + tel_no.substring(3, 6) + "-" + tel_no.substring(6, tel_no.length());
        }
        if (tel_no.length() == 9) {
            return tel_no.substring(0, 2) + "-" + tel_no.substring(2, 5) + "-" + tel_no.substring(5, tel_no.length());
        }
        return tel_no;
    }

    public static boolean getTelNumCheck(String tel_no) {
        String[] firstNo = new String[]{"010", "011", "016", "017", "018", "019"};
        if (tel_no.length() != 10 && tel_no.length() != 11) {
            return false;
        }
        String tmp = tel_no.substring(0, 3);
        for (Object equals : firstNo) {
            if (tmp.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap getImageFile(Context context, String filename) {
        String url = "";
        if (filename.startsWith(JoyNInterface.IMAG_URL)) {
            filename = filename.replace(JoyNInterface.IMAG_URL, "");
        }
        url = JoyNInterface.IMAG_URL + filename;
        URL url_Icon = null;
        String filePath = context.getFilesDir().getPath() + File.separator + filename;
        if (new File(filePath).exists()) {
            return BitmapFactory.decodeFile(filePath);
        }
        try {
            url_Icon = new URL(url);
        } catch (MalformedURLException e3) {
            e3.printStackTrace();
        }
        URLConnection conn = null;
        try {
            conn = url_Icon.openConnection();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        conn.setDoInput(true);
        try {
            conn.connect();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        InputStream in = null;
        try {
            in = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap icon = BitmapFactory.decodeStream(in);
        SaveBitmapToFileCache(icon, filePath);
        try {
            in.close();
        } catch (Exception e4) {
        }
        return icon;
    }

    private static void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {
        Exception e;
        Throwable th;
        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;
        try {
            fileCacheItem.createNewFile();
            OutputStream out2 = new FileOutputStream(fileCacheItem);
            try {
                bitmap.compress(CompressFormat.PNG, 100, out2);
                try {
                    out2.close();
                    out = out2;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    out = out2;
                }
            } catch (Exception e3) {
                e = e3;
                out = out2;
                try {
                    e.printStackTrace();
                    try {
                        out.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        out.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                out = out2;
                out.close();
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            out.close();
        }
    }

    public static boolean isCheckEmailCorrect(String email) {
        if (email.length() != 0 && Pattern.compile("^\\D.+@.+\\.[a-z]+").matcher(email).matches()) {
            return true;
        }
        return false;
    }

    public static int marketVersionCheck(Activity activity) {
        int maketVersion = 0;
        List<PackageInfo> appinfo = activity.getPackageManager().getInstalledPackages(0);
        int i = 0;
        while (i < appinfo.size()) {
            PackageInfo pi = (PackageInfo) appinfo.get(i);
            Log.d(TAG, "pi.packageName:" + pi.packageName + ",pi.versionCode:" + pi.versionCode + ",pi.versionName:" + pi.versionName);
            if (pi.packageName.equals("com.skt.skaf.A000Z00040")) {
                if (pi.versionCode < 121) {
                    maketVersion = 1;
                } else {
                    maketVersion = 2;
                }
            } else if (pi.packageName.equals("com.kt.olleh.storefront") || pi.packageName.equals("com.kt.olleh.istore")) {
                if (pi.versionCode < 4101) {
                    maketVersion = 3;
                } else {
                    maketVersion = 4;
                }
            } else if (pi.packageName.equals("android.lgt.appstore")) {
                if (pi.versionCode < Indexable.MAX_STRING_LENGTH) {
                    maketVersion = 5;
                } else {
                    maketVersion = 7;
                }
            } else if (!pi.packageName.equals("com.lguplus.appstore")) {
                i++;
            } else if (pi.versionCode < Indexable.MAX_STRING_LENGTH) {
                maketVersion = 6;
            } else {
                maketVersion = 7;
            }
            Log.d(TAG, "maketVersion:" + maketVersion);
            return maketVersion;
        }
        Log.d(TAG, "maketVersion:" + maketVersion);
        return maketVersion;
    }

    public static Cursor dbSearchDate(Context context, String myPhoneNum) {
        String initSql = (((((((("" + "SELECT A.enterprise_id,") + "       B.enterprise_nm,") + "       B.enterprise_tel,") + "       A.region,") + "       A.region_nm,") + "       A.phone_number") + "  FROM tb_usr_user A, tb_ent_enterprise B") + " WHERE A.phone_number  = '" + myPhoneNum + "'") + "   AND B.enterprise_id = A.enterprise_id;";
        JoyDbAdapter dbAdapter = new JoyDbAdapter(context);
        dbAdapter.open();
        Cursor cursor = null;
        if (dbAdapter != null) {
            cursor = dbAdapter.queryGetCursor(initSql);
        }
        dbAdapter.close();
        return cursor;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.lang.String K2E(java.lang.String r7) {
        /*
        r4 = joy.common.JoyNUtil.class;
        monitor-enter(r4);
        r1 = 0;
        if (r7 != 0) goto L_0x0009;
    L_0x0006:
        r3 = 0;
    L_0x0007:
        monitor-exit(r4);
        return r3;
    L_0x0009:
        r2 = new java.lang.String;	 Catch:{ all -> 0x002b }
        r2.<init>(r7);	 Catch:{ all -> 0x002b }
        r1 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r3 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r5 = "KSC5601";
        r5 = r7.getBytes(r5);	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r6 = "8859_1";
        r3.<init>(r5, r6);	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r1.<init>(r3);	 Catch:{ UnsupportedEncodingException -> 0x0024 }
    L_0x0022:
        r3 = r1;
        goto L_0x0007;
    L_0x0024:
        r0 = move-exception;
        r1 = new java.lang.String;	 Catch:{ all -> 0x002e }
        r1.<init>(r7);	 Catch:{ all -> 0x002e }
        goto L_0x0022;
    L_0x002b:
        r3 = move-exception;
    L_0x002c:
        monitor-exit(r4);
        throw r3;
    L_0x002e:
        r3 = move-exception;
        r1 = r2;
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: joy.common.JoyNUtil.K2E(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized java.lang.String K2U(java.lang.String r7) {
        /*
        r4 = joy.common.JoyNUtil.class;
        monitor-enter(r4);
        r1 = 0;
        if (r7 != 0) goto L_0x0009;
    L_0x0006:
        r3 = 0;
    L_0x0007:
        monitor-exit(r4);
        return r3;
    L_0x0009:
        r2 = new java.lang.String;	 Catch:{ all -> 0x002b }
        r2.<init>(r7);	 Catch:{ all -> 0x002b }
        r1 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r3 = new java.lang.String;	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r5 = "KSC5601";
        r5 = r7.getBytes(r5);	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r6 = "UTF-8";
        r3.<init>(r5, r6);	 Catch:{ UnsupportedEncodingException -> 0x0024 }
        r1.<init>(r3);	 Catch:{ UnsupportedEncodingException -> 0x0024 }
    L_0x0022:
        r3 = r1;
        goto L_0x0007;
    L_0x0024:
        r0 = move-exception;
        r1 = new java.lang.String;	 Catch:{ all -> 0x002e }
        r1.<init>(r7);	 Catch:{ all -> 0x002e }
        goto L_0x0022;
    L_0x002b:
        r3 = move-exception;
    L_0x002c:
        monitor-exit(r4);
        throw r3;
    L_0x002e:
        r3 = move-exception;
        r1 = r2;
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: joy.common.JoyNUtil.K2U(java.lang.String):java.lang.String");
    }

    public static String getMD5Hex(String str) {
        String hex = "";
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(str.getBytes(), 0, str.length());
            hex = new BigInteger(1, m.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hex;
    }

    public static boolean isCheckNetworkState(Context context, boolean isKT) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        boolean bConnected = false;
        if (cm.getActiveNetworkInfo() != null) {
            switch (cm.getActiveNetworkInfo().getType()) {
                case 0:
                    bConnected = cm.getNetworkInfo(0).isConnected();
                    break;
                case 1:
                    bConnected = cm.getNetworkInfo(1).isConnected();
                    break;
                case 6:
                    bConnected = cm.getNetworkInfo(6).isConnected();
                    break;
                default:
                    bConnected = false;
                    break;
            }
        }
        if (!isKT || bConnected) {
            return true;
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View view = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.custom_alert, null);
        ((RelativeLayout) view.findViewById(R.id.dialog_titlelayout)).setVisibility(8);
        ((TextView) view.findViewById(R.id.dialog_contenttext)).setText("인터넷 연결이 원활하지 않아 정상적인 서비스가 이루어지지 않았습니다. 잠시후 다시 시도해 주십시오.");
        ((LinearLayout) view.findViewById(R.id.dialog_twobtnlayout)).setVisibility(8);
        ((LinearLayout) view.findViewById(R.id.dialog_onebtnlayout)).setVisibility(0);
        Button closeBtn = (Button) view.findViewById(R.id.dialog_closebtn);
        final Context context2 = context;
        closeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ((Activity) context2).moveTaskToBack(true);
                ((Activity) context2).finish();
                dialog.cancel();
            }
        });
        closeBtn.setText(context.getResources().getString(R.string.text_ok));
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().width = (int) (((double) ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth()) * 0.9d);
        dialog.show();
        return bConnected;
    }

    public static void showAlertMessage(Context context, String msg) {
        showAlertMessage(context, "", msg, "");
    }

    public static void showAlertMessage(Context context, String title, String msg) {
        showAlertMessage(context, title, msg, "");
    }

    public static void showAlertMessage(Context context, String title, String msg, String seq) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        View view = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.custom_alert, null);
        if (title.equals("")) {
            ((RelativeLayout) view.findViewById(R.id.dialog_titlelayout)).setVisibility(8);
        } else {
            ((TextView) view.findViewById(R.id.dialog_title)).setText(title);
        }
        ((TextView) view.findViewById(R.id.dialog_contenttext)).setText(msg);
        ((LinearLayout) view.findViewById(R.id.dialog_twobtnlayout)).setVisibility(8);
        ((LinearLayout) view.findViewById(R.id.dialog_onebtnlayout)).setVisibility(0);
        Button closeBtn = (Button) view.findViewById(R.id.dialog_closebtn);
        final String str = seq;
        closeBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!str.equals("")) {
                    if (!new CommonUtil().getDbAdapter().queryExecute(("UPDATE tb_notice" + "    SET notice_check = 'Y'") + "  WHERE notice_seq = '" + str + "'")) {
                        Log.d(JoyNUtil.TAG, "notice check false....");
                    }
                }
                dialog.cancel();
            }
        });
        closeBtn.setText(context.getResources().getString(R.string.text_ok));
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().width = (int) (((double) ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth()) * 0.9d);
        dialog.show();
    }

    public static void app_update(Context context, String type) {
        try {
            if (type.equals(JoyNInterface.ANDROID)) {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(JoyNInterface.GOOGLEMARKET_URL)));
                return;
            }
            switch (marketVersionCheck((Activity) context)) {
                case 1:
                    Intent intent = new Intent();
                    intent.addFlags(DriveFile.MODE_WRITE_ONLY);
                    intent.setClassName("com.skt.skaf.A000Z00040", "com.skt.skaf.A000Z00040.A000Z00040");
                    intent.setAction("COLLAB_ACTION");
                    intent.putExtra("com.skt.skaf.COL.URI", "PRODUCT_VIEW/0000108000/0".getBytes());
                    intent.putExtra("com.skt.skaf.COL.REQUESTER", "A000Z00040");
                    context.startActivity(intent);
                    return;
                case 2:
                    Intent intent1 = new Intent();
                    intent1.setData(Uri.parse("onestore://tstore/product/0000108000?view_type=3"));
                    context.startActivity(intent1);
                    return;
                case 3:
                    Intent intent2 = new Intent("android.intent.action.VIEW");
                    intent2.setType("vnd.kt.olleh.storefront/detail.kt.olleh.storefront");
                    intent2.putExtra("CONTENT_TYPE", "APPLICATION");
                    intent2.putExtra("P_TYPE", "c");
                    intent2.putExtra("P_ID", "51200002736409");
                    intent2.putExtra("N_ID", "A009004");
                    context.startActivity(intent2);
                    return;
                case 4:
                    Intent intent3 = new Intent();
                    intent3.setData(Uri.parse("onestore://ollehmarket/product/0000108000?view_type=3"));
                    context.startActivity(intent3);
                    return;
                case 5:
                    Intent intent4 = new Intent();
                    intent4.setClassName("android.lgt.appstore", "android.lgt.appstore.Store");
                    intent4.putExtra("payload", "PID=Q06010005677");
                    intent4.addFlags(DriveFile.MODE_READ_ONLY);
                    context.startActivity(intent4);
                    return;
                case 6:
                    Intent intent5 = new Intent();
                    intent5 = new Intent("ozstore.external.linked");
                    intent5.setData(Uri.parse("ozstore://STORE/PID=Q06010005677/3"));
                    intent5.addFlags(DriveFile.MODE_READ_ONLY);
                    context.startActivity(intent5);
                    return;
                case 7:
                    Intent intent6 = new Intent();
                    intent6.setData(Uri.parse("onestore://uplusstore/product/0000108000?view_type=3"));
                    intent6.addFlags(DriveFile.MODE_READ_ONLY);
                    context.startActivity(intent6);
                    return;
                default:
                    context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(JoyNInterface.GOOGLEMARKET_URL)));
                    return;
            }
            Log.d(TAG, e.getMessage());
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private static SharedPreferences getGcmPreferences(Context context) {
        return context.getSharedPreferences(context.getClass().getSimpleName(), 0);
    }

    public static String getFileData(Context context, String name) {
        return getFileData(context, name, "");
    }

    public static String getFileData(Context context, String name, String defVal) {
        String val = getGcmPreferences(context).getString(name, defVal);
        if (!val.equals(defVal)) {
            return val;
        }
        Log.d(TAG, name + " not found.");
        return defVal;
    }

    public static void setFileData(Context context, String name, String val) {
        Editor editor = getGcmPreferences(context).edit();
        editor.putString(name, val);
        editor.commit();
    }

    public static void kakaoLink(Context context) {
        try {
            KakaoLinkService.getInstance().sendDefault(context, FeedTemplate.newBuilder(ContentObject.newBuilder("", "http://www.joyndrive.co.kr/images/recommend_s.png", LinkObject.newBuilder().build()).setImageWidth(DbInterface.PST_EXECUTE_DB).setImageHeight(280).setDescrption(((CommonUtil) context.getApplicationContext()).getKakaoMsg()).build()).addButton(new ButtonObject("앱으로 이동", LinkObject.newBuilder().setMobileWebUrl(JoyNInterface.WEB_URL).setAndroidExecutionParams("key1=value1").setIosExecutionParams("key1=value1").build())).build(), new ResponseCallback<KakaoLinkResponse>() {
                public void onFailure(ErrorResult errorResult) {
                    Logger.e(errorResult.toString());
                }

                public void onSuccess(KakaoLinkResponse result) {
                    Log.d(JoyNUtil.TAG, "kakao Link Success result = " + result.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bandLink(Context context) {
        try {
            if (context.getPackageManager().getLaunchIntentForPackage("com.nhn.android.band") == null) {
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.nhn.android.band")));
                return;
            }
            try {
                String encodedText = URLEncoder.encode(((CommonUtil) context.getApplicationContext()).getBandMsg(), "UTF-8");
                Log.d(TAG, "encodedText:" + encodedText);
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("bandapp://create/post?text=" + encodedText + "&route=" + JoyNInterface.WEB_URL)));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.nhn.android.band")));
        }
    }

    public static void facebookLink(Context context, CallbackManager callbackManager) {
        CommonUtil commonUtil = (CommonUtil) context.getApplicationContext();
        Log.d(TAG, "facebookLink");
        ShareDialog shareDialog = new ShareDialog((Activity) context);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Result>() {
            public void onSuccess(Result result) {
                Log.d(JoyNUtil.TAG, "facebook toString:" + result.toString());
                Log.d(JoyNUtil.TAG, "facebook post id:" + result.getPostId());
            }

            public void onCancel() {
                Log.d(JoyNUtil.TAG, "facebook cancel");
            }

            public void onError(FacebookException error) {
                Log.d(JoyNUtil.TAG, "onError");
                error.printStackTrace();
            }
        });
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            shareDialog.show(((Builder) new Builder().setContentUrl(Uri.parse("http://goo.gl/ukqsFn"))).setQuote(commonUtil.getFacebookMsg()).build());
        }
    }
}
