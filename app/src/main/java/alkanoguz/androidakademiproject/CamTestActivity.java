package alkanoguz.androidakademiproject;

        import android.app.Activity;
        import android.content.Context;
        import android.content.ContextWrapper;
        import android.database.Cursor;
        import android.graphics.BitmapFactory;
        import android.hardware.Camera;
        import android.os.Environment;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.Manifest;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.util.Log;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Toast;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.net.HttpURLConnection;
        import android.graphics.Bitmap;
        import android.os.Bundle;
        import android.view.View;
        import android.app.ProgressDialog;
        import android.os.AsyncTask;
        import android.widget.EditText;
        import android.net.Uri;
        import java.io.InputStreamReader;
        import java.io.OutputStream;
        import javax.net.ssl.HttpsURLConnection;
        import java.io.BufferedWriter;
        import java.nio.ByteBuffer;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Map;
        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.util.HashMap;
        import java.io.OutputStreamWriter;
        import java.net.URL;
        import android.provider.MediaStore;
        import java.io.BufferedReader;
        import java.net.URLEncoder;
        import java.io.UnsupportedEncodingException;
        import android.util.Base64;

        import static alkanoguz.androidakademiproject.AppController.TAG;

public class CamTestActivity extends Activity {
    private static final String TAG = "CamTestActivity";
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    Camera camera;
    Context ctx;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        Button photoButton = (Button) this.findViewById(R.id.button);

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bmp);



            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] b = baos.toByteArray();

            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd_hhmmss");
                String date = dateFormat.format(new Date());
                String fileName = "IMG_" + date + ".jpg";
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(b);
                outStream.flush();
                outStream.close();

                Log.d(TAG, "onPictureTaken - wrote bytes: " + b.length + " to " + outFile.getAbsolutePath());

                refreshGallery(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
             catch (IOException e) {
                e.printStackTrace();
            }



        }

    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }


}

