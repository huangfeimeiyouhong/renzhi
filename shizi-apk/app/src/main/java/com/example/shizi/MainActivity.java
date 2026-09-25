package com.example.shizi;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    // ===== 线上地址（renzhi.coolai.space）=====
    private static final String APP_URL = "https://renzhi.coolai.space";
    // APK 自身升级的配置（放到站点根目录即可生效；不存在则静默跳过，不影响使用）
    // 格式：{"versionCode":3,"versionName":"1.2","apkUrl":"https://.../app-release.apk","notes":"更新说明"}
    private static final String UPDATE_JSON_URL = "https://renzhi.coolai.space/app-update.json";
    // =========================================

    private static final int FILE_CHOOSER_REQ = 2001;
    private static final int PERM_REQ_STORAGE = 1001;
    private static final String PREFS = "shizi_prefs";
    private static final String KEY_WEB_FP = "web_fingerprint";

    private WebView webView;
    private View errorView;
    private ValueCallback<Uri[]> mFilePathCallback;

    private long lastWebCheck = 0;
    private boolean appUpdatePrompted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();

        webView = new WebView(this);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);                 // 识字大冒险依赖 JS
        ws.setDomStorageEnabled(true);                 // localStorage：进度持久化
        ws.setDatabaseEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setUseWideViewPort(true);
        ws.setAllowFileAccess(true);
        // 在线更新核心：不用缓存，每次都向服务器取最新内容（改完网页部署后，重开 App 即是新版）
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setMediaPlaybackRequiresUserGesture(false); // 允许自动播放音效 / TTS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        // 原生桥：导出备份 / 重新加载 / 检查更新，由网页调用
        webView.addJavascriptInterface(new ShiziShell(this), "ShiziShell");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false; // 站内全部在 WebView 内打开，不外跳浏览器
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                showError(false); // 加载成功 → 收起断网提示
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (request.isForMainFrame()) showError(true); // 断网/加载失败 → 显示重试页（不白屏）
            }
        });

        // 支持 <input type="file">（导入备份时选文件）与系统文件选择器
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, FILE_CHOOSER_REQ);
                } catch (Exception e) {
                    mFilePathCallback = null;
                    Toast.makeText(MainActivity.this, "当前设备无法打开文件选择器", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });

        // 根布局：WebView + 断网重试页（叠加在上层，默认隐藏）
        FrameLayout root = new FrameLayout(this);
        root.addView(webView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        errorView = buildErrorView();
        root.addView(errorView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(root);

        loadApp(false);
        warnOldWebView();

        // 启动 2 秒后静默检查 APK 自身是否有新版本（站点没放配置就自动跳过）
        new Handler(Looper.getMainLooper()).postDelayed(this::checkAppUpdate, 2000);
    }

    /* ===== 旧设备 WebView 内核兼容提示 =====
       Android 7.0 自带的 WebView 内核约等于 Chrome 50，可能渲染不出新版页面。
       这里检测内核版本，过旧时给家长一句明确提示（而不是白屏让人猜）。 ===== */
    private void warnOldWebView() {
        try {
            String ua = webView.getSettings().getUserAgentString();
            java.util.regex.Matcher m =
                    java.util.regex.Pattern.compile("Chrome/(\\d+)").matcher(ua);
            if (m.find()) {
                int ver = Integer.parseInt(m.group(1));
                if (ver < 70) {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> toast(
                            "本机浏览器组件较旧（Chrome " + ver + "），若页面显示异常，"
                                    + "请到「设置→应用→Android WebView」更新后重试"), 3500);
                }
            }
        } catch (Exception ignore) { /* 检测失败不影响使用 */ }
    }

    // ===== 在线更新：加载线上内容（带 cache-buster，绕过 WebView 与 CDN 缓存）=====
    private String bustUrl(String base) {
        return base + (base.contains("?") ? "&" : "?") + "_v=" + System.currentTimeMillis();
    }

    private void loadApp(boolean clearCache) {
        if (clearCache && webView != null) webView.clearCache(true);
        if (webView != null) webView.loadUrl(bustUrl(APP_URL));
    }

    // ===== 断网/加载失败时的重试页（依赖网络，故不做离线兜底，但给出明确提示与重试入口）=====
    private View buildErrorView() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setBackgroundColor(0xFFFFFFFF);

        TextView tv = new TextView(this);
        tv.setText("暂时连不上网络\n识字大冒险需要联网加载学习内容");
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(0xFF666666);
        tv.setTextSize(18);
        box.addView(tv);

        Button btn = new Button(this);
        btn.setText("重试");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 32;
        btn.setLayoutParams(lp);
        btn.setOnClickListener(v -> {
            showError(false);
            loadApp(true);
        });
        box.addView(btn);

        box.setVisibility(View.GONE);
        return box;
    }

    private void showError(final boolean show) {
        runOnUiThread(() -> {
            if (errorView != null) errorView.setVisibility(show ? View.VISIBLE : View.GONE);
        });
    }

    /* ===== 网页内容在线更新检查 =====
       对线上首页取 ETag / Last-Modified 作为指纹，与本地记录比较：
       指纹变化 → 清缓存并重新加载（用户部署新网页后，切后台再回来即自动更新）。 ===== */
    private void checkWebUpdate(final boolean force) {
        long now = System.currentTimeMillis();
        if (!force && now - lastWebCheck < 30_000) return; // 节流，避免频繁请求
        lastWebCheck = now;

        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) new URL(APP_URL).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Range", "bytes=0-0"); // 只取响应头，不下载正文
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                int code = conn.getResponseCode();
                if (code >= 400) {
                    if (force) toast("检查更新失败（" + code + "）");
                    return;
                }
                String etag = conn.getHeaderField("ETag");
                String lm = conn.getHeaderField("Last-Modified");
                String fp = (etag == null ? "" : etag) + "|" + (lm == null ? "" : lm);
                if (fp.equals("|")) { // 服务器没给指纹 → 无法判断，跳过
                    if (force) toast("已是最新版本");
                    return;
                }
                SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
                String old = sp.getString(KEY_WEB_FP, null);
                if (old == null) { // 首次运行：只记录，不提示
                    sp.edit().putString(KEY_WEB_FP, fp).apply();
                    if (force) toast("已是最新版本");
                    return;
                }
                if (!old.equals(fp)) {
                    sp.edit().putString(KEY_WEB_FP, fp).apply();
                    runOnUiThread(() -> {
                        toast("发现新版本，正在更新…");
                        loadApp(true);
                    });
                } else if (force) {
                    toast("已是最新版本");
                }
            } catch (Exception e) {
                if (force) toast("检查更新失败，请检查网络");
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }

    /* ===== APK 自身在线升级 =====
       站点根目录放 app-update.json 即生效；没有该文件（404）或字段缺失则静默跳过。 ===== */
    private void checkAppUpdate() {
        if (appUpdatePrompted) return;
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) new URL(UPDATE_JSON_URL).openConnection();
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setConnectTimeout(8000);
                conn.setReadTimeout(8000);
                int code = conn.getResponseCode();
                if (code != 200) return; // 未配置更新源 → 静默
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                br.close();

                JSONObject o = new JSONObject(sb.toString());
                int vc = o.optInt("versionCode", -1);
                String vn = o.optString("versionName", "");
                String apkUrl = o.optString("apkUrl", "");
                String notes = o.optString("notes", "");
                if (vc <= BuildConfig.VERSION_CODE || apkUrl.isEmpty()) return;

                appUpdatePrompted = true;
                runOnUiThread(() -> showUpdateDialog(vn, apkUrl, notes));
            } catch (Exception e) {
                // 静默：未配置 / 网络异常，都不打扰孩子使用
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }

    private void showUpdateDialog(String versionName, String apkUrl, String notes) {
        String msg = (notes == null || notes.isEmpty()) ? "是否下载并安装新版本？" : notes;
        new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                .setTitle("发现新版本 " + versionName)
                .setMessage(msg)
                .setPositiveButton("更新", (d, w) -> downloadAndInstall(apkUrl))
                .setNegativeButton("稍后", null)
                .show();
    }

    private void downloadAndInstall(String apkUrl) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("正在下载更新");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMax(100);
        pd.setCancelable(false);
        pd.show();

        new Thread(() -> {
            HttpURLConnection conn = null;
            File out = null;
            try {
                File dir = new File(getCacheDir(), "update");
                if (!dir.exists()) dir.mkdirs();
                out = new File(dir, "shizi-update.apk");
                if (out.exists()) out.delete();

                conn = (HttpURLConnection) new URL(apkUrl).openConnection();
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(30000);
                int total = conn.getContentLength();
                InputStream in = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(out);
                byte[] buf = new byte[65536];
                int len, done = 0;
                while ((len = in.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                    done += len;
                    if (total > 0) {
                        final int pct = (int) (done * 100L / total);
                        runOnUiThread(() -> pd.setProgress(pct));
                    }
                }
                fos.close();
                in.close();
                final File apk = out;
                runOnUiThread(() -> {
                    pd.dismiss();
                    installApk(apk);
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    pd.dismiss();
                    toast("下载失败：" + e.getMessage());
                });
            } finally {
                if (conn != null) conn.disconnect();
            }
        }).start();
    }

    private void installApk(File apk) {
        // Android 8+ 需要用户授权「允许安装未知应用」
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                toast("请先允许安装来自此来源的应用");
                startActivity(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                        Uri.parse("package:" + getPackageName())));
                return;
            }
        }
        try {
            PackageInstaller installer = getPackageManager().getPackageInstaller();
            PackageInstaller.SessionParams params =
                    new PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            int sessionId = installer.createSession(params);
            PackageInstaller.Session session = installer.openSession(sessionId);
            OutputStream os = session.openWrite("shizi", 0, -1);
            FileInputStream fis = new FileInputStream(apk);
            byte[] buf = new byte[65536];
            int c;
            while ((c = fis.read(buf)) > 0) os.write(buf, 0, c);
            fis.close();
            session.fsync(os);
            os.close();

            Intent cb = new Intent(this, MainActivity.class);
            cb.setAction(Intent.ACTION_MAIN);
            int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) flags |= PendingIntent.FLAG_MUTABLE;
            PendingIntent pi = PendingIntent.getActivity(this, sessionId, cb, flags);
            session.commit(pi.getIntentSender());
            session.close();
        } catch (Exception e) {
            toast("安装失败：" + e.getMessage());
        }
    }

    // 沉浸式全屏：隐藏状态栏 + 导航栏，像原生游戏一样
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQ) {
            if (mFilePathCallback == null) return;
            Uri[] results = (data != null && resultCode == Activity.RESULT_OK)
                    ? new Uri[]{data.getData()} : null;
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_REQ_STORAGE) {
            Toast.makeText(this, "已授权，请再次点「导出备份」", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBackInvoked();
        checkWebUpdate(false); // 回到前台时检查网页是否有更新（30 秒内不重复）
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) hideSystemUI(); // 用户临时滑出状态栏后，重新隐藏
    }

    // ===== 核心：禁用系统返回手势（用户诉求） =====
    // Android 13+ 预测式返回：注册一个空回调即可“吃掉”返回手势
    private void registerBackInvoked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    () -> { /* 什么都不做：右滑返回 / 系统返回完全无效 */ });
        }
    }

    // Android 12L 及以下：直接吞掉返回键 / 返回手势
    @Override
    public void onBackPressed() {
        // 不调用 super.onBackPressed() → 返回完全失效
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (webView != null) webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (webView != null) webView.restoreState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    private void toast(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show());
    }

    /* ===== 原生桥（网页通过 window.ShiziShell 调用）=====
       saveBackup(name, content) 导出备份写文件
       reload()                  重新加载（拿最新线上内容）
       checkUpdate()             主动检查网页更新 + APK 升级 ===== */
    private static class ShiziShell {
        private final Activity ctx;
        ShiziShell(Activity ctx) { this.ctx = ctx; }

        @JavascriptInterface
        public boolean saveBackup(String fileName, String content) {
            try {
                OutputStream os = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Android 10+：写入公共 Download 目录，无需权限
                    ContentValues cv = new ContentValues();
                    cv.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                    cv.put(MediaStore.Downloads.MIME_TYPE, "application/json");
                    cv.put(MediaStore.Downloads.RELATIVE_PATH,
                            Environment.DIRECTORY_DOWNLOADS + "/识字大冒险");
                    Uri uri = ctx.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, cv);
                    if (uri != null) os = ctx.getContentResolver().openOutputStream(uri);
                } else {
                    // Android 9 及以下：需要存储权限
                    if (ctx.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ctx.requestPermissions(
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_REQ_STORAGE);
                        toast("请授权存储权限后重试");
                        return false;
                    }
                    File dir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS), "识字大冒险");
                    if (!dir.exists()) dir.mkdirs();
                    os = new FileOutputStream(new File(dir, fileName));
                }
                if (os == null) { toast("备份保存失败"); return false; }
                os.write(content.getBytes("UTF-8"));
                os.close();
                toast("备份已保存到下载目录：" + fileName);
                return true;
            } catch (Exception e) {
                toast("备份保存失败：" + e.getMessage());
                return false;
            }
        }

        @JavascriptInterface
        public void reload() {
            ctx.runOnUiThread(() -> ((MainActivity) ctx).loadApp(true));
        }

        @JavascriptInterface
        public void checkUpdate() {
            ctx.runOnUiThread(() -> {
                MainActivity a = (MainActivity) ctx;
                a.appUpdatePrompted = false; // 允许手动检查时再次弹出升级提示
                a.checkWebUpdate(true);
                a.checkAppUpdate();
                Toast.makeText(a, "正在检查更新…", Toast.LENGTH_SHORT).show();
            });
        }

        private void toast(final String msg) {
            ctx.runOnUiThread(() -> Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show());
        }
    }
}
