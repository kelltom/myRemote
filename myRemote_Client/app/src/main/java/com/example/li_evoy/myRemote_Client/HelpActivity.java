package com.example.li_evoy.myRemote_Client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class handles actions on the Help page.
 * To optimize this activity, it would be best to make questions and answers load dynamically
 * from an ArrayList of questions. Due to time constraints, this is the current solution.
 */
public class HelpActivity extends Activity {

    private TextView txtQuestion1;
    private TextView txtAnswer1;
    private ImageView imgArrow1;

    private TextView txtQuestion2;
    private TextView txtAnswer2;
    private ImageView imgArrow2;

    private TextView txtQuestion3;
    private TextView txtAnswer3;
    private ImageView imgArrow3;

    private TextView txtQuestion4;
    private TextView txtAnswer4;
    private ImageView imgArrow4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getViewReferences();

        getButtonListeners();

    }

    private void getViewReferences() {
        txtQuestion1 = findViewById(R.id.txt_question_1);
        txtAnswer1 = findViewById(R.id.txt_answer_1);
        imgArrow1 = findViewById(R.id.img_arrow_1);

        txtQuestion2 = findViewById(R.id.txt_question_2);
        txtAnswer2 = findViewById(R.id.txt_answer_2);
        imgArrow2 = findViewById(R.id.img_arrow_2);

        txtQuestion3 = findViewById(R.id.txt_question_3);
        txtAnswer3 = findViewById(R.id.txt_answer_3);
        imgArrow3 = findViewById(R.id.img_arrow_3);

        txtQuestion4 = findViewById(R.id.txt_question_4);
        txtAnswer4 = findViewById(R.id.txt_answer_4);
        imgArrow4 = findViewById(R.id.img_arrow_4);
    }

    private void getButtonListeners() {
        txtQuestion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtAnswer1.getVisibility() == View.VISIBLE) {
                    txtAnswer1.setVisibility(View.GONE);
                    imgArrow1.setImageResource(R.drawable.ic_arrow_right);
                } else {
                    txtAnswer1.setVisibility(View.VISIBLE);
                    imgArrow1.setImageResource(R.drawable.ic_arrow_down);
                }
            }
        });

        txtQuestion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtAnswer2.getVisibility() == View.VISIBLE) {
                    txtAnswer2.setVisibility(View.GONE);
                    imgArrow2.setImageResource(R.drawable.ic_arrow_right);
                } else {
                    txtAnswer2.setVisibility(View.VISIBLE);
                    imgArrow2.setImageResource(R.drawable.ic_arrow_down);
                }
            }
        });

        txtQuestion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtAnswer3.getVisibility() == View.VISIBLE) {
                    txtAnswer3.setVisibility(View.GONE);
                    imgArrow3.setImageResource(R.drawable.ic_arrow_right);
                } else {
                    txtAnswer3.setVisibility(View.VISIBLE);
                    imgArrow3.setImageResource(R.drawable.ic_arrow_down);
                }
            }
        });

        txtQuestion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtAnswer4.getVisibility() == View.VISIBLE) {
                    txtAnswer4.setVisibility(View.GONE);
                    imgArrow4.setImageResource(R.drawable.ic_arrow_right);
                } else {
                    txtAnswer4.setVisibility(View.VISIBLE);
                    imgArrow4.setImageResource(R.drawable.ic_arrow_down);
                }
            }
        });
    }

}
