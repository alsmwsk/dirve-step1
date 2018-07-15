package joy.common;

import java.util.ArrayList;
import java.util.List;

public class JoyDbInterface implements JoyNInterface {
    public static List<String> dbCreate() {
        List<String> createList = new ArrayList();
        createList.add("CREATE TABLE tb_ent_enterprise (enterprise_id text PRIMARY KEY  NOT NULL , enterprise_nm text NOT NULL, enterprise_type text NOT NULL, enterprise_level text NOT NULL , enterprise_tel text NOT NULL , enterprise_info text NOT NULL, enterprise_rank text NOT NULL, enterprise_yn text NOT NULL);");
        createList.add("CREATE  TABLE tb_ent_mapping (enterprise_type char(1) PRIMARY KEY  NOT NULL, enterprise_tel varchar(13) NOT NULL);");
        createList.add("CREATE  TABLE tb_usr_user (db_lastLogin DATETIME, db_ver varchar(10), phone_number varchar(15), region vahchar(3), region_nm vahchar(20), enterprise_id vahchar(8), enterprise_tel vahchar(13), enterprise_nm vahchar(50), push_yn varchar(1) default 'N', local_yn varchar(1) default 'Y', device_yn varchar(1) default 'Y', show_update varchar(1) default 'N', card_type varchar(50) default '', card_num1 varchar(4) default '', card_num2 varchar(4) default '', card_num3 varchar(4) default '', card_num4 varchar(4) default '', card_year varchar(4) default '', card_month varchar(2) default '', recommend_tel varchar(50), email_yn varchar(1) default 'N', email_address varchar(50), update_dt varchar(50), serial_yn varchr(2) default 'N', app_dt DATETIME default '2000-01-01 00:00:00', card_owner_gubun varchar(2) default '01', card_owner_nm varchar(100));");
        createList.add("CREATE  TABLE tb_ent_callList (call_dt DATETIME PRIMARY KEY, call_tel varchar(13), enterprise_id varchar(8), enterprise_type varchar(2));");
        createList.add("CREATE  TABLE tb_ent_callCount (enterprise_id varchar(8) PRIMARY KEY, enterprise_type char(1), enterprise_tel varchar(12), call_count INTEGER);");
        createList.add("CREATE  TABLE tb_event (event_seq INTEGER PRIMARY KEY, event_title varchar(50), event_start DATETIME, event_end DATETIME, event_content varchar(500), event_mainshow varchar(1), event_use varchar(1), enterprise_id varchar(8), event_image varchar(50), event_url varchar(100), event_action_yn varchar(1), event_action_url varchar(100), event_gubun varchar(2), event_tag varchar(2), all_show varchar(1) default 'Y', winner_yn varchar(1), winner_dt varchar(10));");
        createList.add("CREATE  TABLE tb_eventwinner (winner_seq INTEGER PRIMARY KEY, event_seq INTEGER, customer_tel varchar(12), rgdt DATETIME);");
        createList.add("CREATE INDEX ix_tb_ent_enterprise_01 ON tb_ent_enterprise (enterprise_id ASC, enterprise_type ASC);");
        createList.add("CREATE TABLE tb_notice (notice_seq INTEGER PRIMARY KEY, enterprise_id varchar(10), notice_title varchar(50), notice_content varchar(1000), notice_useyn varchar(2), notice_mainyn varchar(2), notice_check varchar(2) default 'N', notice_regdt varchar(10), notice_gubun varchar(2), notice_image varchar(200), notice_action_yn varchar(1), notice_action_url varchar(200));");
        createList.add("CREATE TABLE TB_BANK (BANK_CODE VARCHAR(2), BANK_NM VARCHAR(50));");
        createList.add("CREATE TABLE TB_REGION (REGION_ID VARCHAR(3) PRIMARY KEY, REGION_NM VARCHAR(50), REGION_ORDER VARCHAR(2));");
        createList.add("CREATE TABLE TB_DBUPDATE_STATE (PACKAGE_NM VARCHAR(50), UPDATE_STATE VARCHAR(2), LASTDATE DATETIME);");
        createList.add("CREATE TABLE TB_MESSAGE (rec INTEGER PRIMARY_KEY, MESSAGE_TITLE VARCHAR(50), MESSAGE_TEXT VARCHAR(100), MESSAGE_TYPE VARCHAR(10), MESSAGE_DT DATETIME)");
        createList.add("CREATE TABLE TB_DBUPDATE_FAIL (rec INTEGER PRIMARY_KEY, DBUPDATE_SQL VARCHAR(1000), FAIL_DT DATETIME)");
        return createList;
    }
}
