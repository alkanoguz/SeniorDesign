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
    Button UploadImageToServer;
    Bitmap bmp;
    boolean check = true;
    ProgressDialog progressDialog ;
    String ImageNameFieldOnServer = "image_name" ;

    String ImagePathFieldOnServer = "image_path" ;


    String ImageUploadPathOnSever ="http://192.168.1.5/php/upload.php" ;
    String filePath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        Button photoButton = (Button) this.findViewById(R.id.button);
        UploadImageToServer = (Button) findViewById(R.id.button2);



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
                ImageUploadToServerFunction();
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

    }
    public void SaveImageSD(Bitmap bmp) {
        FileOutputStream outStream = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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

            Log.d(TAG, "onPictureTaken - wrote bytes: " + b.length + " to " + outFile.getAbsolutePath());

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
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        Log.d(TAG, ConvertImage);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd_hhmmss");

                String date = dateFormat.format(new Date());

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

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

                        new OutputStreamWriter(OutPutStream, "UTF-8"));

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
}

