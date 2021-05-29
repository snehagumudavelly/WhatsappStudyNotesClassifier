package com.example.notesclassifier;

/*import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.io.File;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //File file = new File(Environment.getExternalStorageDirectory()+"/Sample Directory");
        File sdir = new File (getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/NotesFolder/");
        boolean success = true;
        if(!sdir.exists()) {
            Toast.makeText(getApplicationContext(),"Directory does not exist, create it",
                    Toast.LENGTH_LONG).show();
        }
        if(success) {
            Toast.makeText(getApplication(),"Directory created",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Failed to create Directory",
                    Toast.LENGTH_LONG).show();
        }
    }
}*/

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesclassifier.ml.NotesclassifierWorkingThurs2;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.*;

public class MainActivity extends AppCompatActivity {

    private ImageView imgView;
    private Button select;
    private Button predict;
    private Bitmap img;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgView = (ImageView) findViewById(R.id.imageView);
        select = (Button) findViewById(R.id.button);
        predict = (Button) findViewById(R.id.button2);
        tv = (TextView) findViewById(R.id.textView);


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE)
                startActivityForResult(intent, 100);



                /*String path = "/external_files/DCIM/Camera/";
                File file = new File(path);
                Uri uri_path = Uri.fromFile(file);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension
                        (MimeTypeMap.getFileExtensionFromUrl(path));


                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

                intent.setType(mimeType);
                intent.setDataAndType(uri_path, mimeType);
                startActivityForResult(intent, 100);*/

            }

        });

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img = Bitmap.createScaledBitmap(img, 124, 124, true);
                String abs;
                try {
                    NotesclassifierWorkingThurs2 model = NotesclassifierWorkingThurs2.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 124, 124, 3}, DataType.FLOAT32);
                    TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                    tensorImage.load(img);

                    ByteBuffer byteBuffer = tensorImage.getBuffer();


                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    NotesclassifierWorkingThurs2.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    // Releases model resources if no longer used.
                    model.close();
                    String str = String.valueOf(outputFeature0.getIntArray()[0]);
                    if (str.equals("0")) {

                        tv.setText("Notes Image");

                        /*Uri tempUri = getImageUri(getApplicationContext(), img);
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        String pa = finalFile.getAbsolutePath();*/



                    }else{
                        tv.setText("Others");


                    }
                } catch (IOException e) {

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100)
        {
            imgView.setImageURI(data.getData());
            Uri uri = data.getData();
            try {
                img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {


                case 22:
                    Log.d("onActivityResult", "uriImagePath Gallary :" + data.getData().toString());
                    Intent intentGallary = new Intent(mContext, ShareInfoActivity.class);
                    intentGallary.putExtra(IMAGE_DATA, uriImagePath);
                    intentGallary.putExtra(TYPE, "photo");
                    File f = new File(imagepath);
                    if (!f.exists()) {
                        try {
                            f.createNewFile();
                            copyFile(new File(getRealPathFromURI(data.getData())), f);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    startActivity(intentGallary);
                    finish();
                    break;


            }
        }
    }*/
}