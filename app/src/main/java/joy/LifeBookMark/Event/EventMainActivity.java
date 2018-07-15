package joy.LifeBookMark.Event;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.joy.db.DbCommand;
import com.joy.db.DbInterface;
import com.joy.db.DbRecordset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import joy.LifeBookMark.R;
import joy.common.CommonUtil;
import joy.common.JoyNInterface;
import joy.common.JoyNUtil;
import joy.common.NavigationActivity;
import joy.common.Security;

public class EventMainActivity extends NavigationActivity {
    private final String TAG = "EventMainActivity_new";
    private String beforRegion = "";
    private String beforTel = "";
    private CommonUtil commonUtil = null;
    private eventListAdapter eventAdapter = null;
    private ListView eventList = null;
    private List<HashMap<String, String>> eventParamList = null;
    private HashMap<String, Bitmap> imageMap = null;
    private int imageWidth = 0;

    private class DoImageLoad extends AsyncTask<String, Integer, Bitmap> {
        String fileName;
        ImageView imageView;

        public DoImageLoad(ImageView imageView) {
            this.imageView = imageView;
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... strData) {
            this.fileName = strData[0];
            if (this.fileName.equals("")) {
                return null;
            }
            Bitmap image = (Bitmap) EventMainActivity.this.imageMap.get(this.fileName);
            if (image != null) {
                return image;
            }
            image = JoyNUtil.getImageFile(EventMainActivity.this.commonUtil, this.fileName);
            EventMainActivity.this.imageMap.put(this.fileName, image);
            EventMainActivity.this.commonUtil.setImageMap(EventMainActivity.this.imageMap);
            return image;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Bitmap bm) {
            int iWidth = bm.getWidth();
            int iHeight = bm.getHeight();
            Log.d("EventMainActivity_new", "iWidth:" + iWidth + ",imageWidth:" + EventMainActivity.this.imageWidth);
            if (iWidth < EventMainActivity.this.imageWidth) {
                bm = Bitmap.createScaledBitmap(bm, EventMainActivity.this.imageWidth, (EventMainActivity.this.imageWidth * iHeight) / iWidth, true);
            }
            this.imageView.setImageBitmap(bm);
        }

        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class EventCountTask extends AsyncTask<String, Void, Void> {
        private EventCountTask() {
        }

        protected Void doInBackground(String... params) {
            String seq = JoyNUtil.nullCheck(params[0], "");
            DbCommand command = new DbCommand(EventMainActivity.this.commonUtil.getDbDatabase(), "sp_mobile02_getEventCount");
            command.addParameter((int) DbInterface.STRING_TYPE, 1, seq);
            new DbRecordset(EventMainActivity.this.commonUtil.getDbDatabase()).execute(command);
            return null;
        }
    }

    private class eventListAdapter extends ArrayAdapter {
        public eventListAdapter(Context context, int textViewResourceId, List<HashMap<String, String>> paramList) {
            super(context, textViewResourceId, paramList);
        }

        public int getCount() {
            return EventMainActivity.this.eventParamList.size();
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                v = ((LayoutInflater) EventMainActivity.this.getSystemService("layout_inflater")).inflate(R.layout.eventlistrow, null);
            }
            HashMap<String, String> param = (HashMap) EventMainActivity.this.eventParamList.get(position);
            String event_title = (String) param.get("event_title");
            String event_content = (String) param.get("event_content");
            String event_image = (String) param.get("event_image");
            String event_url = (String) param.get("event_url");
            String event_gubun = (String) param.get("event_gubun");
            String event_tag = (String) param.get("event_tag");
            RelativeLayout textLayout = (RelativeLayout) v.findViewById(R.id.eventrow_text);
            RelativeLayout imageLayout = (RelativeLayout) v.findViewById(R.id.eventrow_image);
            RelativeLayout webLayout = (RelativeLayout) v.findViewById(R.id.eventrow_web);
            if (event_gubun.equals("T")) {
                textLayout.setVisibility(0);
                imageLayout.setVisibility(8);
                webLayout.setVisibility(8);
                ((TextView) v.findViewById(R.id.eventrow_text_title)).setText(event_title);
                ((TextView) v.findViewById(R.id.eventrow_text_content)).setText(event_content);
                v.setTag(event_tag);
            } else if (event_gubun.equals("I") && !event_image.equals("")) {
                textLayout.setVisibility(8);
                imageLayout.setVisibility(0);
                webLayout.setVisibility(8);
                ((TextView) v.findViewById(R.id.eventrow_img_title)).setText(event_title);
                new DoImageLoad((ImageView) v.findViewById(R.id.eventrow_img_imageview)).execute(new String[]{event_image});
                v.setTag(event_tag);
            } else if (event_gubun.equals("U")) {
                textLayout.setVisibility(8);
                imageLayout.setVisibility(8);
                webLayout.setVisibility(0);
                ((TextView) v.findViewById(R.id.eventrow_web_title)).setText(event_title);
                WebView webView = (WebView) v.findViewById(R.id.eventrow_web_webview);
                final ImageView imageView = (ImageView) v.findViewById(R.id.webview_error);
                webView.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.setHorizontalScrollbarOverlay(false);
                        view.setHorizontalScrollBarEnabled(false);
                        view.setVerticalScrollbarOverlay(false);
                        view.setVerticalScrollBarEnabled(false);
                        view.loadUrl(url);
                        return true;
                    }

                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        imageView.setVisibility(0);
                        view.setVisibility(8);
                    }
                });
                webView.setWebChromeClient(new WebChromeClient() {
                    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                        super.onJsAlert(view, url, message, result);
                        Toast.makeText(EventMainActivity.this.getParent(), message, 0).show();
                        result.confirm();
                        return true;
                    }
                });
                webView.loadUrl(event_url);
                webView.setHorizontalScrollbarOverlay(false);
                webView.setHorizontalScrollBarEnabled(false);
                webView.setVerticalScrollbarOverlay(false);
                webView.setVerticalScrollBarEnabled(false);
                webView.addJavascriptInterface(new Object() {
                    @JavascriptInterface
                    public String toString() {
                        return "injectedObject";
                    }
                }, "injectedObject");
                webView.getSettings().setRenderPriority(RenderPriority.HIGH);
                webView.getSettings().setJavaScriptEnabled(false);
                webView.getSettings().setSupportZoom(false);
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setCacheMode(1);
                v.setTag(event_tag);
            }
            return v;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.eventmain);
        this.commonUtil = (CommonUtil) getApplicationContext();
        this.beforTel = this.commonUtil.getMyTelNumber();
        this.beforRegion = this.commonUtil.getRegion();
        this.imageWidth = (int) (((double) ((WindowManager) getSystemService("window")).getDefaultDisplay().getWidth()) * 0.8d);
        this.eventList = (ListView) findViewById(R.id.event_list);
        this.eventParamList = new ArrayList();
        this.imageMap = this.commonUtil.getImageMap();
        eventRowList();
        this.eventAdapter = new eventListAdapter(this, R.layout.eventlistrow, this.eventParamList);
        this.eventList.setAdapter(this.eventAdapter);
        this.eventList.setItemsCanFocus(true);
        this.eventList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                HashMap<String, String> param = (HashMap) EventMainActivity.this.eventParamList.get(pos);
                new EventCountTask().execute(new String[]{(String) param.get("event_seq")});
                if (((String) param.get("event_action_yn")).equals("Y")) {
                    EventMainActivity.this.alertLink((String) param.get("event_title"), (String) param.get("event_action_url"), String.valueOf(view.getTag()));
                } else if (!String.valueOf(view.getTag()).equals("01")) {
                }
            }
        });
    }

    protected void onResume() {
        super.onResume();
        if (!this.beforTel.equals(this.commonUtil.getMyTelNumber()) || !this.beforRegion.equals(this.commonUtil.getRegion())) {
            this.eventParamList.clear();
            this.imageMap.clear();
            eventRowList();
            this.eventAdapter.notifyDataSetChanged();
            this.eventList.setAdapter(this.eventAdapter);
            this.beforTel = this.commonUtil.getMyTelNumber();
            this.beforRegion = this.commonUtil.getRegion();
        }
    }

    private void eventRowList() {
        Cursor cursor = this.commonUtil.getDbAdapter().queryGetCursor((((((("SELECT a.event_seq, a.event_title, a.event_start, a.event_end, a.event_content," + "        a.event_image, a.event_url, a.event_action_yn, a.event_action_url, a.event_gubun,") + "        a.event_tag, a.winner_yn, a.winner_dt") + "   FROM tb_event a") + "  WHERE datetime('now') between a.event_start and a.event_end") + "    AND a.event_use = 'Y'") + "    AND (a.enterprise_id in ('" + this.commonUtil.getEnterprise_id() + "', '')  OR all_show != 'N')") + "  ORDER BY a.event_seq desc");
        if (cursor != null && cursor.getCount() > 0) {
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
            } while (cursor.moveToNext());
            if (cursor != null) {
                cursor.close();
            }
        } else if (cursor != null) {
            cursor.close();
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
            Intent intent = new Intent(this, EventDetailActivity.class);
            url = url + "?enterprise=" + this.commonUtil.getEnterprise_id() + "&security=Y&phone=" + Security.Encode(this.commonUtil.getMyTelNumber().replaceAll("-", ""));
            Log.d("EventMainActivity_new", "action_url:" + url);
            intent.putExtra("title", title);
            intent.putExtra("url", url);
            intent.setFlags(603979776);
            goNextHistory("EventDetailActivity", intent);
        } catch (Exception e) {
            Log.d("EventMainActivity_new", "e:" + e);
        }
    }
}
