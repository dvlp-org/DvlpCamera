package news.dvlp.testcamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import news.dvlp.testcamera.utils.StringUtils;
import news.dvlp.testcamera.view.TouchImageView;


/**
 * 查看照片
 *
 * @Author Yangkui
 * @Date 2017/12/1.
 */
public class ImageBrowserActivity extends Activity {
    TouchImageView browserImageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_browser_image);
        browserImageView=findViewById(R.id.blv);
        initData();
    }

    /**
     * 退出
     *
     * @param view
     */
    private void blvOnclick(View view) {
        finish();
    }




    /**
     * 获取IntentString值
     *
     * @param intent
     * @param key
     * @return
     */
    public static String getIntentString(Intent intent, String key) {
        String value = "";
        if (intent != null) {
            value = intent.getStringExtra(key);
            value = stringEmpty(value);
        }
        return value;
    }

    /**
     * 转换空字符串
     *
     * @param text
     * @return
     */
    public static String stringEmpty(String text) {
        return StringUtils.isEmpty(text) ? "" : text;
    }

    protected void initData() {
        browserImageView.setImageBitmap(BitmapFactory.decodeFile(this.getIntentString(getIntent(), "path")));
        // 设置WindowManager In showing
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

}
