package com.example.shizi;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

    // ===== 已配置为你的线上地址（renzhi.coolai.space） =====
    private static final String APP_URL = "https://renzhi.coolai.space";
    // =====================================================

    private static final int FILE_CHOOSER_REQ = 2001;
    private static final int PERM_REQ_STORAGE = 1001;

    private WebView webView;
    private ValueCallback<Uri[]> mFilePathCallback;

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
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setMediaPlaybackRequiresUserGesture(false); // 允许自动播放音效 / TTS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        // 原生桥：导出备份时由网页调用，把 JSON 写到本机下载目录
        webView.addJavascriptInterface(new ShiziShell(this), "ShiziShell");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false; // 站内全部在 WebView 内打开，不外跳浏览器
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

        setContentView(webView);
        webView.loadUrl(APP_URL);
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

    @Override
    protected void onResume() {
        super.onResume();
        registerBackInvoked();
    }

    // Android 12L 及以下：直接吞掉返回键 / 返回手势
    @Override
    public void onBackPressed() {
        // 不调用 super.onBackPressed() → 返回完全失效
        // 若以后想“再按一次退出”，可在此加双击判断
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

    /* ===== 原生桥：网页通过 window.ShiziShell.saveBackup(name, content) 调用，
       把备份 JSON 写入本机下载目录（Android 10+ 用 MediaStore，无需存储权限；
       更低版本需要 WRITE_EXTERNAL_STORAGE）。返回 true 表示保存成功。 ===== */
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

        private void toast(final String msg) {
            ctx.runOnUiThread(() -> Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show());
        }
    }
}
