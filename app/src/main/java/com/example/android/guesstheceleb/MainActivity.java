package com.example.android.guesstheceleb;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ArrayList <String> celebURLs = new ArrayList<String>();
    ArrayList <String> celebNames = new ArrayList<String>();

    int chosenCeleb = 0;

    int locationOfCorrectAnswer = 0;
    String[] answers = new String[4];

    Button mButton1 ;
    Button mButton2 ;
    Button mButton3 ;
    Button mButton4 ;
    ImageView mImageView ;



    public void pickCeleb(View view){

        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))){

            Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(getApplicationContext(),"Wrong it was " + celebNames.get(chosenCeleb),Toast.LENGTH_LONG).show();

        }


        createNewQuestion();

    }


    public void createNewQuestion(){
        Random random = new Random();

        chosenCeleb =  random.nextInt(celebURLs.size());

        ImageDownloader imageTask = new ImageDownloader();

        Bitmap celebImage;

        try {
            celebImage = imageTask.execute(celebURLs.get(chosenCeleb)).get();

            mImageView.setImageBitmap(celebImage);

            locationOfCorrectAnswer = random.nextInt(4);

            int incorrectAnswerlocation = 0;

            for(int i = 0; i < 4; i++){
                if (i == locationOfCorrectAnswer){
                    answers[i] = celebNames.get(chosenCeleb);
                }else {
                    incorrectAnswerlocation = random.nextInt(celebURLs.size());

                    while (incorrectAnswerlocation == chosenCeleb){
                        incorrectAnswerlocation = random.nextInt(celebURLs.size());
                    }

                    answers[i] = celebNames.get(incorrectAnswerlocation);
                }
            }

            mButton1.setText(answers[0]);
            mButton2.setText(answers[1]);
            mButton3.setText(answers[2]);
            mButton4.setText(answers[3]);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton1 = (Button)findViewById(R.id.button1);
        mButton2 = (Button)findViewById(R.id.button2);
        mButton3 = (Button)findViewById(R.id.button3);
        mButton4 = (Button)findViewById(R.id.button4);
        mImageView = (ImageView)findViewById(R.id.imageView);

        DownloadTask task = new DownloadTask();
        String result = null;

        try {
            result = task.execute("http://www.posh24.com/celebrities").get();

            String[] splitResult = result.split(" <div class=\"sidebarContainer\">");

            Pattern p = Pattern.compile("<img src=\"(.*?)\"");

            Matcher m = p.matcher(splitResult[0]);

            while (m.find()){

                celebURLs.add(m.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");

            m = p.matcher(splitResult[0]);

            while (m.find()){

                celebNames.add(m.group(1));

            }

            Random random = new Random();

            chosenCeleb =  random.nextInt(celebURLs.size());

            ImageDownloader imageTask = new ImageDownloader();

            Bitmap celebImage;

            celebImage = imageTask.execute(celebURLs.get(chosenCeleb)).get();

            mImageView.setImageBitmap(celebImage);

            locationOfCorrectAnswer = random.nextInt(4);

            int incorrectAnswerlocation = 0;

            for(int i = 0; i < 4; i++){
                if (i == locationOfCorrectAnswer){
                    answers[i] = celebNames.get(chosenCeleb);
                }else {
                    incorrectAnswerlocation = random.nextInt(celebURLs.size());

                    while (incorrectAnswerlocation == chosenCeleb){
                        incorrectAnswerlocation = random.nextInt(celebURLs.size());
                    }

                    answers[i] = celebNames.get(incorrectAnswerlocation);
                }
            }

            mButton1.setText(answers[0]);
            mButton2.setText(answers[1]);
            mButton3.setText(answers[2]);
            mButton4.setText(answers[3]);


        } catch (InterruptedException e) {

            e.printStackTrace();

        } catch (ExecutionException e) {

            e.printStackTrace();

        }

        createNewQuestion();


    }
}
