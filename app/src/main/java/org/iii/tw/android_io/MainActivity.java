package org.iii.tw.android_io;

import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private TextView info;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;          //內部類別
    private File sdroot, app1root, app2root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView)findViewById(R.id.tsxtInfo);

        sp = getSharedPreferences("game data", MODE_PRIVATE);   //存取多個檔
        //sp = getPreferences(MODE_PRIVATE);     //取得預設檔名
        editor = sp.edit();                      //自己取得操控

        String state = Environment.getExternalStorageState();   //透過環境(env)取得相關資訊
        Log.d("brad",state);
        if(isExternalStorageReadable()){                     //確認能否讀寫
            Log.i("brad","r");
        }
        if (isExternalStorageWritable()){
            Log.d("brad","w");
        }

        //外部存取(sd卡)
        sdroot = Environment.getExternalStorageDirectory();                 //找出目前所在位置
        app1root = new File(sdroot,"brad");                           //直接建立brad資料夾 隨便存   (公用檔案)
        app2root = new File(sdroot,"Android/data"+getPackageName());  //指定存取, 在這建立會隨app刪除 資料跟著刪除  (私有檔案)
        if(app1root.exists()){
            if(app1root.mkdirs()){
                Log.d("brad","Create dir1 ok");
            }else{
                Log.d("brad","Create dir1 Fail");
            }
        }else{
            Log.d("brad","app1root.exist");
        }
        if(app2root.exists()){
            if(app2root.mkdirs()){
                Log.d("brad","Create dir2 ok");
            }else{
                Log.d("brad","Create dir2 Fail");
            }
        }else{
            Log.d("brad","app2root.exist");
        }
    }

    public void test1(View v){
        editor.putString("user", "Bread");      //test1寫進
        editor.putInt("Stage", 5);
        editor.putBoolean("sound", true);
        editor.commit();                            //從這行寫進
        Toast.makeText(this, "save ok", Toast.LENGTH_SHORT).show();
    }
    public void test2(View v){
        int stage = sp.getInt("stage", 0);             //test2讀取
        String user = sp.getString("user", "nobody");
        info.setText(user+":"+stage);

    }

    //test3,test4內部存檔, 很少用到這方法,因為會占用到使用者的手機內部空間
    public void test3(View v){
        try {
            FileOutputStream fout =
                    openFileOutput("file1.tst",MODE_PRIVATE);
            fout.write("Hello, WOrld\nHello, Brad\n".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this,"Save ok:",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"Exception:"+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void test4(View v){
        info.setText("");
        try {
            FileInputStream fin = openFileInput("file.txt");
            int c;
            while((c = fin.read()) != -1){
                info.append(""+(char)c);
            }
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //5~8 為外部存檔sd卡
    public void test5(View v){
        File file1 = new File(app1root,"file1.txt");
        try {
            FileOutputStream fout = new FileOutputStream(file1);
            fout.write("test5".getBytes());
            fout.flush();
            fout.close();
        } catch (Exception e) {
            Log.d("brad",e.toString());
        }
    }
    public void test6(View v){
        File file1 = new File(app2root,"file1.txt");
        try {
            FileOutputStream fout = new FileOutputStream(file1);
            fout.write("test6".getBytes());
            fout.flush();
            fout.close();
        } catch (Exception e) {
            Log.d("brad",e.toString());
        }
    }
    public void test7(View v){
        info.setText("");
        File file1 = new File(app1root,"file1.txt");
        try {
            FileInputStream fin = new FileInputStream(file1);
            int c;
            while ((c = fin.read())!=-1){
                info.append(""+(char)c);
            }
        } catch (Exception e) {
        }
    }
    public void test8(View v){
        info.setText("");
        File file1 = new File(app2root,"file1.txt");
        try {
            FileInputStream fin = new FileInputStream(file1);
            int c;
            while ((c = fin.read())!=-1){
                info.append(""+(char)c);
            }
        } catch (Exception e) {
        }
    }

    //將檔案儲存在外部儲存空間
    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
