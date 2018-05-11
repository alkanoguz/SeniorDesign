package alkanoguz.androidakademiproject;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CamTestActivity extends Activity {
    private static final String TAG = "CamTestActivity";
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST=1777;
    private ImageView imageView;
    Button UploadImageToServer;
    Bitmap bmp;
    Uri uri;
    boolean check = true;
    ProgressDialog progressDialog ;
    String ImageNameFieldOnServer = "image_name" ;
    HashMap hashMap;
    String ImagePathFieldOnServer = "image_path" ;
    TextView textView;
    String adres = "";
    double lat;
    double lng;
    String lats;
    String lngs;
    EditText editText;
    String mesaj;
    String uid;
    HashMap<String,String> HashMapParams = new HashMap<String,String>();
    SharedPreferences pref;

    String ImageUploadPathOnSever = "http://10.0.2.2/PHP/upload.php";
    String filePath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        Button photoButton = (Button) this.findViewById(R.id.button);
        UploadImageToServer = (Button) findViewById(R.id.button2);
        Button SelectImageGallery = this.findViewById(R.id.button3);
        textView = this.findViewById(R.id.textView);
        editText = this.findViewById(R.id.editText);
        Intent intent = getIntent();

        if (intent.getStringExtra("adres")!=null) {
            adres = intent.getStringExtra("adres");
            lat = intent.getDoubleExtra("lat", 0);
            lng = intent.getDoubleExtra("lng", 0);
            lngs = Double.toString(lng);
            lats = Double.toString(lat);



            textView.setText(adres);
        }
        EnableRuntimePermissionToAccessCamera();




        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        UploadImageToServer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mesaj = editText.getText().toString();
                if (bmp == null){
                    Toast.makeText(getApplicationContext(), "Resim Seçiniz", Toast.LENGTH_LONG).show();
                }
                else if ((mesaj.length() == 0)){

                    Toast.makeText(getApplicationContext(), "Mesaj yazınız", Toast.LENGTH_LONG).show();
                }
                else if (adres == null){

                    Toast.makeText(getApplicationContext(), "Konum Seçiniz", Toast.LENGTH_LONG).show();
                }
                else {

                    ImageUploadToServerFunction();
                    imageView.setImageBitmap(null);
                    bmp = null;
                    textView.setText(null);
                    editText.setText(null);
                    Toast.makeText(getApplicationContext(), "Şikayetiniz iletildi teşekkürler!", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(CamTestActivity.this,ServerTest.class);
                            startActivity(i);
                            finish();
                        }
                    },1800);
            }}
        });
        SelectImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, GALLERY_REQUEST);

            }
        });



    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            bmp = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bmp);
            SaveImageSD(bmp);
        }

        if (requestCode == GALLERY_REQUEST) {
            uri = data.getData();
            try {

                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imageView.setImageBitmap(bmp);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
     }

    public void EnableRuntimePermissionToAccessCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(CamTestActivity.this,
                Manifest.permission.CAMERA)) {



        } else {

            ActivityCompat.requestPermissions(CamTestActivity.this, new String[]{Manifest.permission.CAMERA}, 1);

        }
    }









    public void SaveImageSD(Bitmap bmp) {
        FileOutputStream outStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        final String ConvertImage = Base64.encodeToString(b, Base64.DEFAULT);

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


            refreshGallery(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }
    public void ImageUploadToServerFunction(){

        ByteArrayOutputStream byteArrayOutputStreamObject ;

        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.

        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);



        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd_hhmmss");

                String date = dateFormat.format(new Date());

                ImageProcessClass imageProcessClass = new ImageProcessClass();
                uid= pref.getString("uid","0");
                HashMapParams.put("uid",uid);
                HashMapParams.put("adres",adres);
                HashMapParams.put("Latitude",lats);
                HashMapParams.put("Longtitude",lngs);
                HashMapParams.put("Mesaj",mesaj);
                HashMapParams.put(ImageNameFieldOnServer, "IMG_" + date + ".jpg");

                HashMapParams.put(ImagePathFieldOnServer, ConvertImage);

                String FinalData = imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;

                url = new URL(requestURL);

                httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                httpURLConnectionObject.setReadTimeout(19000);

                httpURLConnectionObject.setConnectTimeout(19000);

                httpURLConnectionObject.setRequestMethod("POST");

                httpURLConnectionObject.setDoInput(true);

                httpURLConnectionObject.setDoOutput(true);

                OutPutStream = httpURLConnectionObject.getOutputStream();

                bufferedWriterObject = new BufferedWriter(

                        new OutputStreamWriter(OutPutStream));

                bufferedWriterObject.write(bufferedWriterDataFN(PData));

                bufferedWriterObject.flush();

                bufferedWriterObject.close();

                OutPutStream.close();

                RC = httpURLConnectionObject.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReaderObject.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            StringBuilder stringBuilderObject;

            stringBuilderObject = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                if (check)

                    check = false;
                else
                    stringBuilderObject.append("&");

                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilderObject.append("=");

                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilderObject.toString();
        }

    }
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case 1:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {


                } else {



                }
                break;
        }
    }

}

