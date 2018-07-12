package top.huiger.gensignature;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {

    private EditText etInput;
    private TextView tvShow;
    private Button btnCopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInput = findViewById(R.id.et);
        tvShow = findViewById(R.id.tv);
        btnCopy = findViewById(R.id.btn_copy);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_gen:
                String input = etInput.getText().toString().trim();
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(MainActivity.this, "请填写正确包名!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String sign = gen(input);
                if (TextUtils.isEmpty(sign)) {
                    btnCopy.setVisibility(View.INVISIBLE);
                    return;
                }
                tvShow.setText(sign);
                btnCopy.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_copy:
                copyStr(tvShow.getText().toString());
                break;
            default:

        }


    }

    /**
     * 获取签名
     *
     * @param packageName
     * @return
     */
    private String gen(String packageName) {
        String sign = "";
        try {
            Signature[] signatures = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
            if (signatures.length > 0) {
                MessageDigest digest = MessageDigest.getInstance("MD5");
                digest.update(signatures[0].toByteArray());
                BigInteger bigInteger = new BigInteger(1, digest.digest());
                sign = bigInteger.toString(16);
                Log.d("msg", "获取到包名 "+packageName+" 下的签名为: " + sign);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "获取失败!", Toast.LENGTH_SHORT).show();
        }
        return sign;
    }

    /**
     * copy
     *
     * @param str
     */
    private void copyStr(String str) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(str);
        Toast.makeText(MainActivity.this, "复制成功!", Toast.LENGTH_SHORT).show();
    }
}
