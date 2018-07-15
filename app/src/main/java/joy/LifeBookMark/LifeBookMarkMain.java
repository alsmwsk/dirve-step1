package joy.LifeBookMark;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.example.seowoo.driveapp.BuildConfig;
import com.google.android.gms.drive.DriveFile;
import com.joy.db.DbCommand;
import com.joy.db.DbInterface;
import com.joy.db.DbRecordset;
import com.kakao.util.helper.Utility;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import joy.common.CommonUtil;
import joy.common.JoyDbAdapter;
import joy.common.JoyLocationUtil;
import joy.common.JoyNInterface;
import joy.common.JoyNUtil;

public class LifeBookMarkMain extends Activity {
    private static final String INIT_DATE = "2001-01-01 00:00:00";
    private static final String TAG = "LifeBookMarkMain";
    public static LifeBookMarkMain lifeBookMarkMain = null;
    private CommonUtil commonUtil = null;
    private JoyDbAdapter dbAdapter = null;
    private InitAsyncTask initAsyncTask = null;
    private boolean initSettingType = false;
    private JoyLocationUtil locationUtil = null;
    private Handler pageMoveHandle = null;
    private SharedPreferences pref = null;
    private ProgressDialog progress = null;
    private String today = "";

    private class InitAsyncTask extends AsyncTask<Void, Integer, Void> {
        private String lastLogin;

        private InitAsyncTask() {
            this.lastLogin = LifeBookMarkMain.INIT_DATE;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            LifeBookMarkMain.this.commonUtil.dbOpen();
            try {
                LifeBookMarkMain.this.dbAdapter = LifeBookMarkMain.this.commonUtil.getDbAdapter();
                String program_version = LifeBookMarkMain.this.commonUtil.getProgram_version();
                Cursor cursor = LifeBookMarkMain.this.dbAdapter.queryGetCursor("SELECT db_lastLogin, db_ver FROM tb_usr_user;");
                if (cursor == null || cursor.getCount() <= 0) {
                    if (LifeBookMarkMain.this.dbAdapter.queryExecute("INSERT INTO tb_usr_user (db_lastLogin, db_ver, phone_number, region, region_nm, enterprise_id, enterprise_nm, enterprise_tel)" + " VALUES ('" + LifeBookMarkMain.this.today + "', '" + program_version + "', '', '', '', '', '', '');")) {
                        this.lastLogin = LifeBookMarkMain.INIT_DATE;
                        LifeBookMarkMain.this.initSettingType = true;
                    } else {
                        this.lastLogin = LifeBookMarkMain.INIT_DATE;
                        LifeBookMarkMain.this.initSettingType = true;
                    }
                } else {
                    this.lastLogin = cursor.getString(0);
                    LifeBookMarkMain.this.commonUtil.setDb_version(cursor.getString(1));
                    Cursor cursor2 = LifeBookMarkMain.this.dbAdapter.queryGetCursor("select rowid, dbupdate_sql from tb_dbupdate_fail");
                    Cursor cursor1;
                    if (cursor2 == null || cursor2.getCount() <= 0) {
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        cursor1 = LifeBookMarkMain.this.dbAdapter.queryGetCursor(((((((((((((((((((((("" + "SELECT A.db_lastLogin,") + "       A.db_ver,") + "       A.region,") + "       A.region_nm,") + "       A.enterprise_id,") + "       B.enterprise_nm,") + "       B.enterprise_tel,") + "       A.local_yn,") + "       A.card_type,") + "       A.card_num1,") + "       A.card_num2,") + "       A.card_num3,") + "       A.card_num4,") + "       A.card_year,") + "       A.card_month,") + "       A.email_yn,") + "       A.email_address,") + "       A.card_owner_gubun,") + "       A.card_owner_nm,") + "       A.phone_number") + "  FROM tb_usr_user A, tb_ent_enterprise B") + " WHERE B.enterprise_id = A.enterprise_id;");
                        if (cursor1 != null || cursor1.getCount() <= 0) {
                            LifeBookMarkMain.this.initSettingType = true;
                        } else {
                            if (cursor1.getString(2).equals("")) {
                                LifeBookMarkMain.this.initSettingType = true;
                            } else {
                                LifeBookMarkMain.this.commonUtil.setRegion(cursor1.getString(2));
                                LifeBookMarkMain.this.commonUtil.setRegion_nm(cursor1.getString(3));
                                LifeBookMarkMain.this.commonUtil.setEnterprise_id(cursor1.getString(4));
                                LifeBookMarkMain.this.commonUtil.setEnterprise_nm(cursor1.getString(5));
                                LifeBookMarkMain.this.commonUtil.setEnterprise_tel(cursor1.getString(6));
                                LifeBookMarkMain.this.commonUtil.setLocalType(cursor1.getString(7));
                                LifeBookMarkMain.this.commonUtil.setCard_type(cursor1.getString(8));
                                LifeBookMarkMain.this.commonUtil.setCard_num1(cursor1.getString(9));
                                LifeBookMarkMain.this.commonUtil.setCard_num2(cursor1.getString(10));
                                LifeBookMarkMain.this.commonUtil.setCard_num3(cursor1.getString(11));
                                LifeBookMarkMain.this.commonUtil.setCard_num4(cursor1.getString(12));
                                LifeBookMarkMain.this.commonUtil.setCard_year(cursor1.getString(13));
                                LifeBookMarkMain.this.commonUtil.setCard_month(cursor1.getString(14));
                                LifeBookMarkMain.this.commonUtil.setEmail_yn(cursor1.getString(15));
                                LifeBookMarkMain.this.commonUtil.setEmail_address(cursor1.getString(16));
                                LifeBookMarkMain.this.commonUtil.setCard_owner_gubun(cursor1.getString(17));
                                LifeBookMarkMain.this.commonUtil.setCard_owner_nm(cursor1.getString(18));
                                LifeBookMarkMain.this.commonUtil.setMyTelNumber(cursor1.getString(19).replaceAll("-", ""));
                                String sql = ("UPDATE tb_usr_user SET" + " db_lastLogin = '" + LifeBookMarkMain.this.today + "',") + " db_ver       = '" + program_version + "';";
                                this.lastLogin = cursor1.getString(0);
                                if (LifeBookMarkMain.this.dbAdapter.queryExecute(sql)) {
                                    LifeBookMarkMain.this.commonUtil.setStory_yn(JoyNUtil.getFileData(LifeBookMarkMain.this, JoyNInterface.FILE_STORY_YN, "N"));
                                } else {
                                    LifeBookMarkMain.this.commonUtil.setStory_yn(JoyNUtil.getFileData(LifeBookMarkMain.this, JoyNInterface.FILE_STORY_YN, "N"));
                                }
                            }
                            if (cursor1 != null) {
                                cursor1.close();
                            }
                        }
                    } else {
                        do {
                            if (LifeBookMarkMain.this.dbAdapter.queryExecute(cursor2.getString(1))) {
                                LifeBookMarkMain.this.dbAdapter.queryExecute("delete from tb_dbupdate_fail where rowid = " + cursor2.getString(0));
                            }
                        } while (cursor2.moveToNext());
                        if (cursor2 != null) {
                            cursor2.close();
                        }
                        cursor1 = LifeBookMarkMain.this.dbAdapter.queryGetCursor(((((((((((((((((((((("" + "SELECT A.db_lastLogin,") + "       A.db_ver,") + "       A.region,") + "       A.region_nm,") + "       A.enterprise_id,") + "       B.enterprise_nm,") + "       B.enterprise_tel,") + "       A.local_yn,") + "       A.card_type,") + "       A.card_num1,") + "       A.card_num2,") + "       A.card_num3,") + "       A.card_num4,") + "       A.card_year,") + "       A.card_month,") + "       A.email_yn,") + "       A.email_address,") + "       A.card_owner_gubun,") + "       A.card_owner_nm,") + "       A.phone_number") + "  FROM tb_usr_user A, tb_ent_enterprise B") + " WHERE B.enterprise_id = A.enterprise_id;");
                        if (cursor1 != null) {
                        }
                        LifeBookMarkMain.this.initSettingType = true;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                LifeBookMarkMain.this.initSettingType = true;
            }
            String progress_msg = "";
            if (LifeBookMarkMain.this.initSettingType) {
                progress_msg = "데이타 업데이트 중입니다.";
            } else {
                progress_msg = "Loading......";
            }
            if (LifeBookMarkMain.this.progress != null) {
                LifeBookMarkMain.this.progress.dismiss();
            }
            LifeBookMarkMain.this.progress = ProgressDialog.show(LifeBookMarkMain.this, "", progress_msg, true);
        }

        protected Void doInBackground(Void... params) {
            LifeBookMarkMain.this.commonUtil.open();
            if (!LifeBookMarkMain.this.commonUtil.getIsDbConnect()) {
                LifeBookMarkMain.this.commonUtil.open();
            }
            if (!(LifeBookMarkMain.this.initSettingType || LifeBookMarkMain.this.commonUtil.getProgram_version().equals(LifeBookMarkMain.this.commonUtil.getDb_version()))) {
                LifeBookMarkMain.this.dbAdapter.dbUpgrade(LifeBookMarkMain.this.commonUtil.getDb_version(), LifeBookMarkMain.this.commonUtil.getProgram_version());
            }
            LifeBookMarkMain.this.verCheck();
            LifeBookMarkMain.this.enterpriseInfo(JoyNInterface.PST_ENTLIST_NEW, this.lastLogin);
            LifeBookMarkMain.this.reginInfo(JoyNInterface.PST_REGION_LIST, this.lastLogin);
            LifeBookMarkMain.this.eventInfo(JoyNInterface.PST_EVENTLIST_NEW2, this.lastLogin);
            LifeBookMarkMain.this.noticInfo(JoyNInterface.PST_NOTICELIST, this.lastLogin);
            LifeBookMarkMain.this.imageList();
            LifeBookMarkMain.this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            if (!LifeBookMarkMain.this.initSettingType) {
                LifeBookMarkMain.this.device_insert();
                LifeBookMarkMain.this.CustomerCardList();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (LifeBookMarkMain.this.progress != null) {
                LifeBookMarkMain.this.progress.dismiss();
            }
            LifeBookMarkMain.this.pageMoveHandle.sendEmptyMessageDelayed(0, 500);
        }
    }

    private void verCheck() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0181 in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:43)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:56)
	at jadx.core.ProcessClass.process(ProcessClass.java:39)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:282)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
*/
        /*
        r11 = this;
        r10 = 2131099964; // 0x7f06013c float:1.7812296E38 double:1.05290328E-314;
        r9 = 2131099942; // 0x7f060126 float:1.7812251E38 double:1.052903269E-314;
        r2 = "언제 어디서나 원터치 프리미엄 대리운전 어플 조이앤드라이브\n1. 어플설치시 기본 5,000P 적립\n2. 카드결제도 10% 적립. 적립금 현금환급/포인트몰 사용\n3. 포인트 11,000P 적립을 위한 조이앤드라이브 특별추천 이벤트 진행\n[추천방법]\n어플 실행 후 [더보기]-[설정]-[추천인]에 추천인 전화번호를 입력해주세요.";
        r4 = "sp_mobile02_getMobileVerCheck";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r0 = new com.joy.db.DbCommand;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r5.getDbDatabase();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r0.<init>(r5, r4);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = 202; // 0xca float:2.83E-43 double:1.0E-321;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = 1;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = r7.getAppType();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r0.addParameter(r5, r6, r7);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = 202; // 0xca float:2.83E-43 double:1.0E-321;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = 1;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = r7.getProgram_version();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r0.addParameter(r5, r6, r7);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r3 = new com.joy.db.DbRecordset;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r5.getDbDatabase();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r3.<init>(r5);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r3.execute(r0);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        if (r5 == 0) goto L_0x01e0;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
    L_0x0040:
        r5 = r3.getRowCount();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        if (r5 <= 0) goto L_0x0109;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
    L_0x0046:
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = "MOBILE_VERSION";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r3.getFieldStringValue(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setAppUpdateType(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = "INIT_RECOMMEND";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r3.getFieldStringValue(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = "\\n";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r8 = "\n";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.replace(r7, r8);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setInitRecommend(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = "POPUP_RECOMMEND";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r3.getFieldStringValue(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = "\\n";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r8 = "\n";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.replace(r7, r8);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setPopupRecommend(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = "RECOMMEND_MSG";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r3.getFieldStringValue(r5);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = "\\n";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = "\n";	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r2 = r5.replace(r6, r7);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
    L_0x008f:
        r5 = r11.commonUtil;
        r5 = r5.getInitRecommend();
        r6 = "";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x00ab;
    L_0x009e:
        r5 = r11.commonUtil;
        r6 = r11.getResources();
        r6 = r6.getString(r9);
        r5.setInitRecommend(r6);
    L_0x00ab:
        r5 = r11.commonUtil;
        r5 = r5.getPopupRecommend();
        r6 = "";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x00c7;
    L_0x00ba:
        r5 = r11.commonUtil;
        r6 = r11.getResources();
        r6 = r6.getString(r10);
        r5.setPopupRecommend(r6);
    L_0x00c7:
        r5 = r11.commonUtil;
        r5.setKakaoMsg(r2);
        r5 = r11.commonUtil;
        r5.setStoryMsg(r2);
        r5 = r11.commonUtil;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r6 = r6.append(r2);
        r7 = "\nhttp://goo.gl/ukqsFn";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r5.setBandMsg(r6);
        r5 = r11.commonUtil;
        r5.setFacebookMsg(r2);
        r5 = r11.commonUtil;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r6 = r6.append(r2);
        r7 = "\nhttp://goo.gl/ukqsFn";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r5.setTwitterMsg(r6);
    L_0x0108:
        return;
    L_0x0109:
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.getProgram_version();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setAppUpdateType(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r11.getResources();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = 2131099942; // 0x7f060126 float:1.7812251E38 double:1.052903269E-314;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setInitRecommend(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r11.getResources();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = 2131099964; // 0x7f06013c float:1.7812296E38 double:1.05290328E-314;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setPopupRecommend(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        goto L_0x008f;
    L_0x0136:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.getProgram_version();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setAppUpdateType(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r11.getResources();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = 2131099942; // 0x7f060126 float:1.7812251E38 double:1.052903269E-314;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setInitRecommend(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r11.getResources();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = 2131099964; // 0x7f06013c float:1.7812296E38 double:1.05290328E-314;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setPopupRecommend(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;
        r5 = r5.getInitRecommend();
        r6 = "";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x0181;
    L_0x0174:
        r5 = r11.commonUtil;
        r6 = r11.getResources();
        r6 = r6.getString(r9);
        r5.setInitRecommend(r6);
    L_0x0181:
        r5 = r11.commonUtil;
        r5 = r5.getPopupRecommend();
        r6 = "";
        r5 = r5.equals(r6);
        if (r5 == 0) goto L_0x019d;
    L_0x0190:
        r5 = r11.commonUtil;
        r6 = r11.getResources();
        r6 = r6.getString(r10);
        r5.setPopupRecommend(r6);
    L_0x019d:
        r5 = r11.commonUtil;
        r5.setKakaoMsg(r2);
        r5 = r11.commonUtil;
        r5.setStoryMsg(r2);
        r5 = r11.commonUtil;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r6 = r6.append(r2);
        r7 = "\nhttp://goo.gl/ukqsFn";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r5.setBandMsg(r6);
        r5 = r11.commonUtil;
        r5.setFacebookMsg(r2);
        r5 = r11.commonUtil;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r6 = r6.append(r2);
        r7 = "\nhttp://goo.gl/ukqsFn";
        r6 = r6.append(r7);
        r6 = r6.toString();
        r5.setTwitterMsg(r6);
        goto L_0x0108;
    L_0x01e0:
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.getProgram_version();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setAppUpdateType(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r11.getResources();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = 2131099942; // 0x7f060126 float:1.7812251E38 double:1.052903269E-314;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setInitRecommend(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5 = r11.commonUtil;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r11.getResources();	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r7 = 2131099964; // 0x7f06013c float:1.7812296E38 double:1.05290328E-314;	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r6 = r6.getString(r7);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        r5.setPopupRecommend(r6);	 Catch:{ Exception -> 0x0136, all -> 0x020d }
        goto L_0x008f;
    L_0x020d:
        r5 = move-exception;
        r6 = r11.commonUtil;
        r6 = r6.getInitRecommend();
        r7 = "";
        r6 = r6.equals(r7);
        if (r6 == 0) goto L_0x022a;
    L_0x021d:
        r6 = r11.commonUtil;
        r7 = r11.getResources();
        r7 = r7.getString(r9);
        r6.setInitRecommend(r7);
    L_0x022a:
        r6 = r11.commonUtil;
        r6 = r6.getPopupRecommend();
        r7 = "";
        r6 = r6.equals(r7);
        if (r6 == 0) goto L_0x0246;
    L_0x0239:
        r6 = r11.commonUtil;
        r7 = r11.getResources();
        r7 = r7.getString(r10);
        r6.setPopupRecommend(r7);
    L_0x0246:
        r6 = r11.commonUtil;
        r6.setKakaoMsg(r2);
        r6 = r11.commonUtil;
        r6.setStoryMsg(r2);
        r6 = r11.commonUtil;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r7 = r7.append(r2);
        r8 = "\nhttp://goo.gl/ukqsFn";
        r7 = r7.append(r8);
        r7 = r7.toString();
        r6.setBandMsg(r7);
        r6 = r11.commonUtil;
        r6.setFacebookMsg(r2);
        r6 = r11.commonUtil;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r7 = r7.append(r2);
        r8 = "\nhttp://goo.gl/ukqsFn";
        r7 = r7.append(r8);
        r7 = r7.toString();
        r6.setTwitterMsg(r7);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: joy.LifeBookMark.LifeBookMarkMain.verCheck():void");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.logo);
        Log.d(TAG, "LifeBookMarkMain onCreate....");
        lifeBookMarkMain = this;
        this.commonUtil = (CommonUtil) getApplicationContext();
        this.commonUtil.setDbAdapter(new JoyDbAdapter(this));
        this.today = JoyNUtil.getToday("yyyy-MM-dd HH:mm:ss");
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int height = (int) (((double) display.getHeight()) * 0.9d);
        this.commonUtil.setDialogWidth((int) (((double) display.getWidth()) * 0.9d));
        this.commonUtil.setDialogHeight(height);
        int imgHeight = (int) (((double) display.getHeight()) * 0.55d);
        this.commonUtil.setImgDialogWidth((int) (((double) display.getWidth()) * 0.875d));
        this.commonUtil.setImgDialogHeight(imgHeight);
        this.commonUtil.setProgram_version(BuildConfig.VERSION_NAME);
        this.commonUtil.setAppType(JoyNInterface.ANDROID);
        this.locationUtil = JoyLocationUtil.getInstance(this);
        this.locationUtil.start();
        this.commonUtil.setLocationUtil(this.locationUtil);
        this.commonUtil.setImageMap(new HashMap());
        addShortcut();
        if (JoyNUtil.isCheckNetworkState(this, this.commonUtil.getAppType().equals(JoyNInterface.KT))) {
            new InitAsyncTask().execute(new Void[0]);
        }
        this.pageMoveHandle = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (LifeBookMarkMain.this.initSettingType) {
                    Intent intent = new Intent(LifeBookMarkMain.this, SettingStepViewActivity.class);
                    intent.setFlags(67108864);
                    LifeBookMarkMain.this.startActivity(intent);
                    return;
                }
                Intent intent = new Intent(LifeBookMarkMain.this, LifeBookMarkTabMain.class);
                intent.setFlags(DriveFile.MODE_WRITE_ONLY);
                LifeBookMarkMain.this.startActivity(intent);
            }
        };
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void enterpriseInfo(int r14, java.lang.String r15) {
        /*
        r13 = this;
        r2 = r15;
        r1 = "N";
        r2 = r13.updateCheck(r14, r15);	 Catch:{ Exception -> 0x0278 }
        r8 = "sp_mobile02_getEnterpriseList_new";
        r0 = new com.joy.db.DbCommand;	 Catch:{ Exception -> 0x0278 }
        r10 = r13.commonUtil;	 Catch:{ Exception -> 0x0278 }
        r10 = r10.getDbDatabase();	 Catch:{ Exception -> 0x0278 }
        r0.<init>(r10, r8);	 Catch:{ Exception -> 0x0278 }
        r10 = 202; // 0xca float:2.83E-43 double:1.0E-321;
        r11 = 1;
        r0.addParameter(r10, r11, r2);	 Catch:{ Exception -> 0x0278 }
        r7 = new com.joy.db.DbRecordset;	 Catch:{ Exception -> 0x0278 }
        r10 = r13.commonUtil;	 Catch:{ Exception -> 0x0278 }
        r10 = r10.getDbDatabase();	 Catch:{ Exception -> 0x0278 }
        r7.<init>(r10);	 Catch:{ Exception -> 0x0278 }
        r10 = r7.execute(r0);	 Catch:{ Exception -> 0x0278 }
        if (r10 == 0) goto L_0x026e;
    L_0x002d:
        r10 = r7.getRowCount();	 Catch:{ Exception -> 0x0278 }
        if (r10 <= 0) goto L_0x026e;
    L_0x0033:
        r5 = 0;
    L_0x0034:
        r10 = r7.getRowCount();	 Catch:{ Exception -> 0x0278 }
        if (r5 >= r10) goto L_0x026b;
    L_0x003a:
        r3 = "DELETE FROM tb_ent_enterprise WHERE ENTERPRISE_ID = '";
        r6 = "INSERT INTO tb_ent_enterprise (ENTERPRISE_ID, ENTERPRISE_NM, ENTERPRISE_TYPE, ENTERPRISE_LEVEL, ENTERPRISE_TEL, ENTERPRISE_INFO, ENTERPRISE_RANK, ENTERPRISE_YN) VALUES";
        r9 = "UPDATE tb_ent_enterprise SET";
        r10 = "ENTERPRISE_STATE";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x0278 }
        r11 = "I";
        r10 = r10.equals(r11);	 Catch:{ Exception -> 0x0278 }
        if (r10 == 0) goto L_0x017d;
    L_0x0053:
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r3);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_ID";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "';";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r3 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = r13.dbAdapter;	 Catch:{ Exception -> 0x0278 }
        r10 = r10.queryExecute(r3);	 Catch:{ Exception -> 0x0278 }
        if (r10 != 0) goto L_0x007a;
    L_0x007a:
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r6);	 Catch:{ Exception -> 0x0278 }
        r11 = "('";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_ID";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "', '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r6 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r6);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_NM";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "', '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r6 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r6);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_TYPE";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "', '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r6 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r6);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_GRADE";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "', '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r6 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r6);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_TEL";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "', '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r6 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r6);	 Catch:{ Exception -> 0x0278 }
        r11 = "', '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r6 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r6);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_RANK";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "', '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r6 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r6);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_YN";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "');";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r6 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = r13.dbAdapter;	 Catch:{ Exception -> 0x0278 }
        r10 = r10.queryExecute(r6);	 Catch:{ Exception -> 0x0278 }
        if (r10 != 0) goto L_0x0176;
    L_0x0176:
        r7.moveNext();	 Catch:{ Exception -> 0x0278 }
        r5 = r5 + 1;
        goto L_0x0034;
    L_0x017d:
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r9);	 Catch:{ Exception -> 0x0278 }
        r11 = "       ENTERPRISE_NM    = '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_NM";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "',";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r9 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r9);	 Catch:{ Exception -> 0x0278 }
        r11 = "       ENTERPRISE_LEVEL = '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_GRADE";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "',";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r9 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r9);	 Catch:{ Exception -> 0x0278 }
        r11 = "       ENTERPRISE_TEL   = '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_TEL";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "',";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r9 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r9);	 Catch:{ Exception -> 0x0278 }
        r11 = "       ENTERPRISE_YN    = '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_YN";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "'";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r9 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r9);	 Catch:{ Exception -> 0x0278 }
        r11 = " WHERE ENTERPRISE_ID    = '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_ID";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "'";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r9 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0278 }
        r10.<init>();	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r9);	 Catch:{ Exception -> 0x0278 }
        r11 = "   AND ENTERPRISE_TYPE  = '";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "ENTERPRISE_TYPE";
        r11 = r7.getFieldStringValue(r11);	 Catch:{ Exception -> 0x0278 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r11 = "';";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0278 }
        r9 = r10.toString();	 Catch:{ Exception -> 0x0278 }
        r10 = r13.dbAdapter;	 Catch:{ Exception -> 0x0278 }
        r10 = r10.queryExecute(r9);	 Catch:{ Exception -> 0x0278 }
        if (r10 != 0) goto L_0x0176;
    L_0x0269:
        goto L_0x0176;
    L_0x026b:
        r1 = "Y";
    L_0x026e:
        r10 = java.lang.String.valueOf(r14);
        r11 = r13.today;
        r13.dbUpdate(r10, r11, r15, r1);
    L_0x0277:
        return;
    L_0x0278:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ all -> 0x0289 }
        r1 = "N";
        r10 = java.lang.String.valueOf(r14);
        r11 = r13.today;
        r13.dbUpdate(r10, r11, r15, r1);
        goto L_0x0277;
    L_0x0289:
        r10 = move-exception;
        r11 = java.lang.String.valueOf(r14);
        r12 = r13.today;
        r13.dbUpdate(r11, r12, r15, r1);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: joy.LifeBookMark.LifeBookMarkMain.enterpriseInfo(int, java.lang.String):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void reginInfo(int r13, java.lang.String r14) {
        /*
        r12 = this;
        r2 = r14;
        r1 = "N";
        r2 = r12.updateCheck(r13, r14);	 Catch:{ Exception -> 0x00e7 }
        r8 = "sp_mobile02_getRegionList";
        r0 = new com.joy.db.DbCommand;	 Catch:{ Exception -> 0x00e7 }
        r9 = r12.commonUtil;	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.getDbDatabase();	 Catch:{ Exception -> 0x00e7 }
        r0.<init>(r9, r8);	 Catch:{ Exception -> 0x00e7 }
        r9 = 202; // 0xca float:2.83E-43 double:1.0E-321;
        r10 = 1;
        r0.addParameter(r9, r10, r2);	 Catch:{ Exception -> 0x00e7 }
        r7 = new com.joy.db.DbRecordset;	 Catch:{ Exception -> 0x00e7 }
        r9 = r12.commonUtil;	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.getDbDatabase();	 Catch:{ Exception -> 0x00e7 }
        r7.<init>(r9);	 Catch:{ Exception -> 0x00e7 }
        r9 = r7.execute(r0);	 Catch:{ Exception -> 0x00e7 }
        if (r9 == 0) goto L_0x00dd;
    L_0x002d:
        r9 = r7.getRowCount();	 Catch:{ Exception -> 0x00e7 }
        if (r9 <= 0) goto L_0x00dd;
    L_0x0033:
        r5 = 0;
    L_0x0034:
        r9 = r7.getRowCount();	 Catch:{ Exception -> 0x00e7 }
        if (r5 >= r9) goto L_0x00da;
    L_0x003a:
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e7 }
        r9.<init>();	 Catch:{ Exception -> 0x00e7 }
        r10 = "DELETE FROM TB_REGION WHERE REGION_ID = '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x00e7 }
        r10 = "REGION_ID";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x00e7 }
        r10 = "';";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x00e7 }
        r3 = r9.toString();	 Catch:{ Exception -> 0x00e7 }
        r6 = "INSERT INTO TB_REGION (REGION_ID, REGION_NM, REGION_ORDER) VALUES ";
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e7 }
        r9.<init>();	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x00e7 }
        r10 = "('";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x00e7 }
        r10 = "REGION_ID";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x00e7 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x00e7 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x00e7 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e7 }
        r9.<init>();	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x00e7 }
        r10 = "REGION_NM";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x00e7 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x00e7 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x00e7 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e7 }
        r9.<init>();	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x00e7 }
        r10 = "REGION_ORDER";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x00e7 }
        r10 = "');";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x00e7 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x00e7 }
        r9 = r12.dbAdapter;	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.queryExecute(r3);	 Catch:{ Exception -> 0x00e7 }
        if (r9 != 0) goto L_0x00cb;
    L_0x00cb:
        r9 = r12.dbAdapter;	 Catch:{ Exception -> 0x00e7 }
        r9 = r9.queryExecute(r6);	 Catch:{ Exception -> 0x00e7 }
        if (r9 != 0) goto L_0x00d3;
    L_0x00d3:
        r7.moveNext();	 Catch:{ Exception -> 0x00e7 }
        r5 = r5 + 1;
        goto L_0x0034;
    L_0x00da:
        r1 = "Y";
    L_0x00dd:
        r9 = java.lang.String.valueOf(r13);
        r10 = r12.today;
        r12.dbUpdate(r9, r10, r14, r1);
    L_0x00e6:
        return;
    L_0x00e7:
        r4 = move-exception;
        r1 = "N";
        r9 = java.lang.String.valueOf(r13);
        r10 = r12.today;
        r12.dbUpdate(r9, r10, r14, r1);
        goto L_0x00e6;
    L_0x00f5:
        r9 = move-exception;
        r10 = java.lang.String.valueOf(r13);
        r11 = r12.today;
        r12.dbUpdate(r10, r11, r14, r1);
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: joy.LifeBookMark.LifeBookMarkMain.reginInfo(int, java.lang.String):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void eventInfo(int r13, java.lang.String r14) {
        /*
        r12 = this;
        r2 = r14;
        r1 = "N";
        r2 = r12.updateCheck(r13, r14);	 Catch:{ Exception -> 0x02a9 }
        r8 = "sp_mobile02_getEventList_new";
        r0 = new com.joy.db.DbCommand;	 Catch:{ Exception -> 0x02a9 }
        r9 = r12.commonUtil;	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.getDbDatabase();	 Catch:{ Exception -> 0x02a9 }
        r0.<init>(r9, r8);	 Catch:{ Exception -> 0x02a9 }
        r9 = 202; // 0xca float:2.83E-43 double:1.0E-321;
        r10 = 1;
        r0.addParameter(r9, r10, r2);	 Catch:{ Exception -> 0x02a9 }
        r7 = new com.joy.db.DbRecordset;	 Catch:{ Exception -> 0x02a9 }
        r9 = r12.commonUtil;	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.getDbDatabase();	 Catch:{ Exception -> 0x02a9 }
        r7.<init>(r9);	 Catch:{ Exception -> 0x02a9 }
        r9 = r7.execute(r0);	 Catch:{ Exception -> 0x02a9 }
        if (r9 == 0) goto L_0x029f;
    L_0x002d:
        r9 = r7.getRowCount();	 Catch:{ Exception -> 0x02a9 }
        if (r9 <= 0) goto L_0x029f;
    L_0x0033:
        r5 = 0;
    L_0x0034:
        r9 = r7.getRowCount();	 Catch:{ Exception -> 0x02a9 }
        if (r5 >= r9) goto L_0x029c;
    L_0x003a:
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r10 = "DELETE FROM tb_event WHERE event_seq = '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_SEQ";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "';";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r3 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = r12.dbAdapter;	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.queryExecute(r3);	 Catch:{ Exception -> 0x02a9 }
        if (r9 != 0) goto L_0x0064;
    L_0x0064:
        r9 = "EVENT_USE";
        r9 = r7.getFieldStringValue(r9);	 Catch:{ Exception -> 0x02a9 }
        r10 = "Y";
        r9 = r9.equals(r10);	 Catch:{ Exception -> 0x02a9 }
        if (r9 == 0) goto L_0x0295;
    L_0x0074:
        r6 = "INSERT INTO tb_event (event_seq, event_title, event_start, event_end, event_content, event_use, event_mainshow, enterprise_id, event_image, event_url, event_action_yn, event_action_url, event_gubun, event_tag, all_show, winner_yn, winner_dt) VALUES ";
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "('";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_SEQ";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_TITLE";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_START";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_END";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_CONTENT";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_USE";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "MAIN_SHOW";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "ENTERPRISE_ID";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_IMAGE";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_URL";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_ACTION_YN";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_ACTION_URL";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_GUBUN";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "EVENT_TAG";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "ALL_SHOW";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "WINNER_YN";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "', '";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02a9 }
        r9.<init>();	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r6);	 Catch:{ Exception -> 0x02a9 }
        r10 = "WINNER_DT";
        r10 = r7.getFieldStringValue(r10);	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r10 = "')";
        r9 = r9.append(r10);	 Catch:{ Exception -> 0x02a9 }
        r6 = r9.toString();	 Catch:{ Exception -> 0x02a9 }
        r9 = r12.dbAdapter;	 Catch:{ Exception -> 0x02a9 }
        r9 = r9.queryExecute(r6);	 Catch:{ Exception -> 0x02a9 }
        if (r9 != 0) goto L_0x0295;
    L_0x0295:
        r7.moveNext();	 Catch:{ Exception -> 0x02a9 }
        r5 = r5 + 1;
        goto L_0x0034;
    L_0x029c:
        r1 = "Y";
    L_0x029f:
        r9 = java.lang.String.valueOf(r13);
        r10 = r12.today;
        r12.dbUpdate(r9, r10, r14, r1);
    L_0x02a8:
        return;
    L_0x02a9:
        r4 = move-exception;
        r1 = "N";
        r9 = java.lang.String.valueOf(r13);
        r10 = r12.today;
        r12.dbUpdate(r9, r10, r14, r1);
        goto L_0x02a8;
    L_0x02b7:
        r9 = move-exception;
        r10 = java.lang.String.valueOf(r13);
        r11 = r12.today;
        r12.dbUpdate(r10, r11, r14, r1);
        throw r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: joy.LifeBookMark.LifeBookMarkMain.eventInfo(int, java.lang.String):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void noticInfo(int r21, java.lang.String r22) {
        /*
        r20 = this;
        r6 = r22;
        r5 = "N";
        r15 = "SELECT notice_seq, notice_check FROM tb_notice";
        r0 = r20;
        r0 = r0.dbAdapter;
        r17 = r0;
        r0 = r17;
        r12 = r0.queryGetCursor(r15);
        r11 = new java.util.HashMap;
        r11.<init>();
        if (r12 == 0) goto L_0x003e;
    L_0x001b:
        r17 = r12.getCount();
        if (r17 <= 0) goto L_0x003e;
    L_0x0021:
        r17 = 0;
        r0 = r17;
        r17 = r12.getString(r0);
        r18 = 1;
        r0 = r18;
        r18 = r12.getString(r0);
        r0 = r17;
        r1 = r18;
        r11.put(r0, r1);
        r17 = r12.moveToNext();
        if (r17 != 0) goto L_0x0021;
    L_0x003e:
        r6 = r20.updateCheck(r21, r22);	 Catch:{ Exception -> 0x02c7 }
        r16 = "sp_mobile02_getNoticeList_new";
        r4 = new com.joy.db.DbCommand;	 Catch:{ Exception -> 0x02c7 }
        r0 = r20;
        r0 = r0.commonUtil;	 Catch:{ Exception -> 0x02c7 }
        r17 = r0;
        r17 = r17.getDbDatabase();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r1 = r16;
        r4.<init>(r0, r1);	 Catch:{ Exception -> 0x02c7 }
        r17 = 202; // 0xca float:2.83E-43 double:1.0E-321;
        r18 = 1;
        r0 = r17;
        r1 = r18;
        r4.addParameter(r0, r1, r6);	 Catch:{ Exception -> 0x02c7 }
        r14 = new com.joy.db.DbRecordset;	 Catch:{ Exception -> 0x02c7 }
        r0 = r20;
        r0 = r0.commonUtil;	 Catch:{ Exception -> 0x02c7 }
        r17 = r0;
        r17 = r17.getDbDatabase();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r14.<init>(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r14.execute(r4);	 Catch:{ Exception -> 0x02c7 }
        if (r17 == 0) goto L_0x02b1;
    L_0x007a:
        r17 = r14.getRowCount();	 Catch:{ Exception -> 0x02c7 }
        if (r17 <= 0) goto L_0x02b1;
    L_0x0080:
        r9 = 0;
    L_0x0081:
        r17 = r14.getRowCount();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        if (r9 >= r0) goto L_0x02ae;
    L_0x0089:
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r18 = "DELETE FROM tb_notice WHERE notice_seq = '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_SEQ";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "';";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r7 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r0 = r20;
        r0 = r0.dbAdapter;	 Catch:{ Exception -> 0x02c7 }
        r17 = r0;
        r0 = r17;
        r17 = r0.queryExecute(r7);	 Catch:{ Exception -> 0x02c7 }
        if (r17 != 0) goto L_0x00bb;
    L_0x00bb:
        r17 = "NOTICE_USEYN";
        r0 = r17;
        r17 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r18 = "Y";
        r17 = r17.equals(r18);	 Catch:{ Exception -> 0x02c7 }
        if (r17 == 0) goto L_0x02a7;
    L_0x00cd:
        r13 = "N";
        r17 = "NOTICE_SEQ";
        r0 = r17;
        r17 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r11.containsKey(r0);	 Catch:{ Exception -> 0x02c7 }
        if (r17 == 0) goto L_0x00f2;
    L_0x00e1:
        r17 = "NOTICE_SEQ";
        r0 = r17;
        r17 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r13 = r11.get(r0);	 Catch:{ Exception -> 0x02c7 }
        r13 = (java.lang.String) r13;	 Catch:{ Exception -> 0x02c7 }
    L_0x00f2:
        r10 = "INSERT INTO tb_notice (notice_seq, enterprise_id, notice_title, notice_content, notice_useyn, notice_mainyn, notice_check, notice_gubun, notice_image, notice_action_yn, notice_action_url, notice_regdt) VALUES ";
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "('";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_SEQ";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "ENTERPRISE_ID";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_TITLE";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_CONTENT";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_USEYN";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_MAINYN";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r13);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_GUBUN";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_IMAGE";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_ACTION_YN";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_ACTION_URL";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "', '";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r17 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x02c7 }
        r17.<init>();	 Catch:{ Exception -> 0x02c7 }
        r0 = r17;
        r17 = r0.append(r10);	 Catch:{ Exception -> 0x02c7 }
        r18 = "NOTICE_REG";
        r0 = r18;
        r18 = r14.getFieldStringValue(r0);	 Catch:{ Exception -> 0x02c7 }
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r18 = "');";
        r17 = r17.append(r18);	 Catch:{ Exception -> 0x02c7 }
        r10 = r17.toString();	 Catch:{ Exception -> 0x02c7 }
        r0 = r20;
        r0 = r0.dbAdapter;	 Catch:{ Exception -> 0x02c7 }
        r17 = r0;
        r0 = r17;
        r17 = r0.queryExecute(r10);	 Catch:{ Exception -> 0x02c7 }
        if (r17 != 0) goto L_0x02a7;
    L_0x02a7:
        r14.moveNext();	 Catch:{ Exception -> 0x02c7 }
        r9 = r9 + 1;
        goto L_0x0081;
    L_0x02ae:
        r5 = "Y";
    L_0x02b1:
        r17 = java.lang.String.valueOf(r21);
        r0 = r20;
        r0 = r0.today;
        r18 = r0;
        r0 = r20;
        r1 = r17;
        r2 = r18;
        r3 = r22;
        r0.dbUpdate(r1, r2, r3, r5);
    L_0x02c6:
        return;
    L_0x02c7:
        r8 = move-exception;
        r5 = "N";
        r17 = java.lang.String.valueOf(r21);
        r0 = r20;
        r0 = r0.today;
        r18 = r0;
        r0 = r20;
        r1 = r17;
        r2 = r18;
        r3 = r22;
        r0.dbUpdate(r1, r2, r3, r5);
        goto L_0x02c6;
    L_0x02e1:
        r17 = move-exception;
        r18 = java.lang.String.valueOf(r21);
        r0 = r20;
        r0 = r0.today;
        r19 = r0;
        r0 = r20;
        r1 = r18;
        r2 = r19;
        r3 = r22;
        r0.dbUpdate(r1, r2, r3, r5);
        throw r17;
        */
        throw new UnsupportedOperationException("Method not decompiled: joy.LifeBookMark.LifeBookMarkMain.noticInfo(int, java.lang.String):void");
    }

    private String updateCheck(int package_nm, String lastLogin) {
        String db_date = "";
        Cursor state = this.dbAdapter.queryGetCursor("SELECT UPDATE_STATE, LASTDATE FROM TB_DBUPDATE_STATE WHERE PACKAGE_NM = '" + package_nm + "';");
        if (state == null || state.getCount() <= 0) {
            db_date = INIT_DATE;
        } else if (state.getString(0).equals("N")) {
            db_date = state.getString(1);
        } else {
            db_date = lastLogin;
        }
        if (state != null) {
            state.close();
        }
        return db_date;
    }

    private void dbUpdate(String package_nm, String today, String lastLogin, String dbUpdteState) {
        String date;
        if (this.dbAdapter.queryExecute("DELETE FROM TB_DBUPDATE_STATE WHERE PACKAGE_NM = '" + package_nm + "';")) {
            date = today;
        } else {
            date = today;
        }
        if (dbUpdteState.equals("Y")) {
            date = today;
        } else {
            date = lastLogin;
        }
        if (!this.dbAdapter.queryExecute("INSERT INTO TB_DBUPDATE_STATE(PACKAGE_NM, UPDATE_STATE, LASTDATE) VALUES ('" + package_nm + "', '" + dbUpdteState + "', '" + date + "');")) {
        }
    }

    private void device_insert() {
        String myPhoneNum = this.commonUtil.getMyTelNumber();
        Cursor cursor1 = this.dbAdapter.queryGetCursor("SELECT device_yn FROM tb_usr_user WHERE phone_number='" + myPhoneNum + "';");
        boolean bSend = false;
        if (cursor1 == null || cursor1.getCount() == 0) {
            bSend = true;
        } else if (!cursor1.getString(0).equals("Y")) {
            bSend = true;
        }
        if (cursor1 != null) {
            cursor1.close();
        }
        if (bSend) {
            try {
                String serial = ((TelephonyManager) getSystemService("phone")).getDeviceId();
                DbCommand command = new DbCommand(this.commonUtil.getDbDatabase(), "sp_mobile02_insertDeviceLogin");
                command.addParameter((int) DbInterface.STRING_TYPE, 1, this.commonUtil.getEnterprise_id());
                command.addParameter((int) DbInterface.STRING_TYPE, 1, myPhoneNum);
                command.addParameter((int) DbInterface.STRING_TYPE, 1, this.commonUtil.getProgram_version());
                command.addParameter((int) DbInterface.STRING_TYPE, 1, "A");
                command.addParameter((int) DbInterface.STRING_TYPE, 1, serial);
                if (new DbRecordset(this.commonUtil.getDbDatabase()).execute(command)) {
                    this.dbAdapter.queryExecute("UPDATE tb_usr_user SET device_yn = 'Y' WHERE phone_number = '" + myPhoneNum + "';");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void CustomerCardList() {
        String update_dt;
        String myPhoneNum = this.commonUtil.getMyTelNumber();
        Cursor cursor1 = this.dbAdapter.queryGetCursor("SELECT update_dt FROM tb_usr_user WHERE phone_number='" + myPhoneNum + "';");
        if (cursor1 == null || cursor1.getCount() == 0) {
            update_dt = JoyNUtil.getToday("yyyy-MM-dd HH:mm:ss");
        } else {
            update_dt = cursor1.getString(0);
        }
        if (cursor1 != null) {
            cursor1.close();
        }
        try {
            String serial = ((TelephonyManager) getSystemService("phone")).getDeviceId();
            DbCommand command = new DbCommand(this.commonUtil.getDbDatabase(), "sp_mobile02_getCustomerCard");
            command.addParameter((int) DbInterface.STRING_TYPE, 1, this.commonUtil.getEnterprise_id());
            command.addParameter((int) DbInterface.STRING_TYPE, 1, myPhoneNum);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, serial);
            command.addParameter((int) DbInterface.STRING_TYPE, 1, update_dt);
            DbRecordset pRs = new DbRecordset(this.commonUtil.getDbDatabase());
            if (pRs.execute(command) && pRs.getRowCount() > 0) {
                for (int i = 0; i < pRs.getRowCount(); i++) {
                    String updateSql = (((((((((("UPDATE TB_USR_USER" + "    SET card_type        = '" + pRs.getFieldStringValue("CARD_TYPE") + "',") + "        card_num1        = '" + pRs.getFieldStringValue("CARD_NUM1") + "',") + "        card_num2        = '" + pRs.getFieldStringValue("CARD_NUM2") + "',") + "        card_num3        = '" + pRs.getFieldStringValue("CARD_NUM3") + "',") + "        card_num4        = '" + pRs.getFieldStringValue("CARD_NUM4") + "',") + "        card_year        = '" + pRs.getFieldStringValue("CARD_VALID_YEAR") + "',") + "        card_month       = '" + pRs.getFieldStringValue("CARD_VALID_MONTH") + "',") + "        card_owner_gubun = '" + pRs.getFieldStringValue("CARD_OWNER_GUBUN") + "',") + "        card_owner_nm    = '" + pRs.getFieldStringValue("CARD_OWNER_NM") + "',") + "        update_dt        = '" + JoyNUtil.getToday("yyyy-MM-dd HH:mm:ss") + "'") + "  WHERE phone_number = '" + myPhoneNum + "'";
                    this.commonUtil.setCard_type(pRs.getFieldStringValue("CARD_TYPE"));
                    this.commonUtil.setCard_num1(pRs.getFieldStringValue("CARD_NUM1"));
                    this.commonUtil.setCard_num2(pRs.getFieldStringValue("CARD_NUM2"));
                    this.commonUtil.setCard_num3(pRs.getFieldStringValue("CARD_NUM3"));
                    this.commonUtil.setCard_num4(pRs.getFieldStringValue("CARD_NUM4"));
                    this.commonUtil.setCard_year(pRs.getFieldStringValue("CARD_VALID_YEAR"));
                    this.commonUtil.setCard_month(pRs.getFieldStringValue("CARD_VALID_MONTH"));
                    this.commonUtil.setCard_owner_gubun(pRs.getFieldStringValue("CARD_OWNER_GUBUN"));
                    this.commonUtil.setCard_owner_nm(pRs.getFieldStringValue("CARD_OWNER_NM"));
                    if (this.dbAdapter.queryExecute(updateSql)) {
                        pRs.moveNext();
                    } else {
                        pRs.moveNext();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addShortcut() {
        this.pref = getSharedPreferences("pref", 0);
        if (!this.pref.getBoolean("shortcutInstall", false)) {
            if (!this.commonUtil.getAppType().equals(JoyNInterface.ANDROID)) {
                Intent shortcutIntent = new Intent();
                shortcutIntent.addCategory("android.intent.category.LAUNCHER");
                shortcutIntent.setClassName(getPackageName(), getPackageName() + ".LifeBookMarkMain");
                shortcutIntent.addFlags(270532608);
                Intent intent = new Intent();
                intent.putExtra("android.intent.extra.shortcut.INTENT", shortcutIntent);
                intent.putExtra("android.intent.extra.shortcut.NAME", "조이앤대리운전");
                intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", ShortcutIconResource.fromContext(this, R.drawable.icon));
                intent.putExtra("duplicate", false);
                intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                sendBroadcast(intent);
            }
            Editor editor = this.pref.edit();
            editor.putBoolean("shortcutInstall", true);
            editor.commit();
        }
    }

    private void imageList() {
        Cursor cursor = this.commonUtil.getDbAdapter().queryGetCursor((("" + "SELECT event_image FROM tb_event WHERE event_gubun = 'I' ") + "union all ") + "SELECT notice_image FROM tb_notice WHERE notice_gubun = 'I'");
        if (cursor != null) {
            HashMap<String, Bitmap> imageMap = this.commonUtil.getImageMap();
            if (cursor.getCount() > 0) {
                do {
                    try {
                        String fileName = cursor.getString(0);
                        Bitmap image = JoyNUtil.getImageFile(this, fileName);
                        if (image == null) {
                            imageMap.put(fileName, null);
                        } else {
                            imageMap.put(fileName, image);
                        }
                    } catch (Exception e) {
                    }
                } while (cursor.moveToNext());
            }
            this.commonUtil.setImageMap(imageMap);
        }
    }

    private String getAppKeyHash() {
        try {
            PackageInfo packageInfo = Utility.getPackageInfo(this, 64);
            if (packageInfo == null) {
                return null;
            }
            Signature[] signatureArr = packageInfo.signatures;
            int length = signatureArr.length;
            int i = 0;
            while (i < length) {
                Signature signature = signatureArr[i];
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d(TAG, "hash value=" + Base64.encodeToString(md.digest(), 2));
                    return Base64.encodeToString(md.digest(), 2);
                } catch (NoSuchAlgorithmException e) {
                    Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
                    i++;
                }
            }
            return "";
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e("name not found", e2.toString());
        }
    }
}
