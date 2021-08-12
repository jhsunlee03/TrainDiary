package com.example.traintwo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JournalSlidePageFragment extends Fragment {
    public static final int PICK_IMAGE = 1;
    private String imagePath;
    private ImageView imageView;
    private EditText editTextView;
    private int journalDBIndex =0;

    public JournalSlidePageFragment(){
    };

    public JournalSlidePageFragment(int position){
        this.journalDBIndex = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        DataBaseHandler db = new DataBaseHandler(getContext());
        ArrayList<JournalEntry> journalArrayList = db.getAllJournals();
        JournalEntry journalData;

        //get Views that need to be updated from data
        imageView = (ImageView) getView().findViewById(R.id.image);
        editTextView = (EditText) getView().findViewById(R.id.text);

        if (journalDBIndex < journalArrayList.size()) {
            journalData = journalArrayList.get(journalDBIndex);
            //set text from DB
            editTextView.setText(journalData.text);

            //read image and set it to imageView
            try {
                File imgFile = new File(journalData.imagePath);
                if (imgFile.exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    options.inJustDecodeBounds = false;
                    String path = imgFile.getAbsolutePath();
                    Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                    imageView.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                Log.e("I0", "I0" + e);
            }
            imageView.postInvalidate();
            imageView.invalidate(); //update image in imageview
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();// intent : ACTIVITY간
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                // pass constant to compare it
                // with the returned requestcode
                startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE);

            }
        });

        Button reset = (Button) getView().findViewById((R.id.reset));
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageResource(R.drawable.ic_baseline_image_24);
                editTextView.setText("");
            }
        });

        Button delete_all = (Button) getView().findViewById(R.id.delete_all);
        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBaseHandler db = new DataBaseHandler(getActivity());
                db.deleteAll();
            }
        });

        Button save = (Button) getView().findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePath.length() > 0 && editTextView.getText().toString().length() > 0) {
                    DataBaseHandler db = new DataBaseHandler(getActivity());
                    int newId = db.getJournalsCount(); //new ID is always the index pf the journal entry(if count 0, id 0)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' MM:mm:ss z");
                    String currentDateAndTime = sdf.format(new Date());
                    String text = editTextView.getText().toString();

                    //save image
                    String newSavedPath = saveImageToFile(newId, imageView);
                    db.addJournal(new JournalEntry(newId, newSavedPath, text, currentDateAndTime));

                } else {
                    CharSequence text = "Please add image and text";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                }
            }
        });
    }

        private String saveImageToFile(int newId, ImageView iv){
            iv.buildDrawingCache();
            Bitmap bm = iv.getDrawingCache();
            File imagefileDir;

            OutputStream fOut = null;

            try{
                imagefileDir = new File(getActivity().getExternalFilesDir(null)+File.separator+"traintwo"+File.separator);
                boolean result = imagefileDir.mkdirs();
                Log.println(Log.INFO, "result", Boolean.toString(result));
                File sdImageMainDirectory = new File(imagefileDir, Integer.toString(newId)+".jpg");
                sdImageMainDirectory.createNewFile();
                fOut = new FileOutputStream(sdImageMainDirectory);
                bm.compress(Bitmap.CompressFormat.JPEG, 20,fOut);
                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bm, Integer.toString(newId)+".jpg", "" );

                fOut.flush();
                fOut.close();
                return  sdImageMainDirectory.getAbsolutePath();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error occured. Please try again later", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                //if (requestCode == REQUEST_GET_SINGLE_FILE){
                Uri selectedImageUri = data.getData();

                //set image in ImageView
                ((ImageView) getView().findViewById(R.id.image)).setImageURI(selectedImageUri);
                imagePath = selectedImageUri.getPath();
            }
            //}
        }catch(Exception e){
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }


    }


