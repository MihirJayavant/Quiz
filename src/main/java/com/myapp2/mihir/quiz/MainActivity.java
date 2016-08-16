package com.myapp2.mihir.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity
{
    TextView dispques;
    RadioGroup rgroup;
    RadioButton radio[]=new RadioButton[4];
    Button next,quit,tryAgain;
    QuizLogic q;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        try
        {
            displayQuestion();
            setRadioOptions();
            q.getCorrectAns();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected  void onStart()
    {
        super.onStart();

    }

    public void init()
    {
        try {
            InputStream is= this.getResources().openRawResource(R.raw.question);
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            q=new QuizLogic(br);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dispques=(TextView)findViewById(R.id.display_question);
        rgroup=(RadioGroup)findViewById(R.id.answer_group);
        next=(Button)findViewById(R.id.next_btn);
        quit=(Button)findViewById(R.id.quit_btn);
        tryAgain=(Button)findViewById(R.id.try_btn);
        radio[0]=(RadioButton)findViewById(R.id.option1);
        radio[1]=(RadioButton)findViewById(R.id.option2);
        radio[2]=(RadioButton)findViewById(R.id.option3);
        radio[3]=(RadioButton)findViewById(R.id.option4);

        ButtonAction b=new ButtonAction(this);
        next.setOnClickListener(b);
        quit.setOnClickListener(b);
        tryAgain.setOnClickListener(b);
    }


    private void displayQuestion()throws Exception
    {
        String s=q.getQuestion();
        s=q.quesno+"."+s;
        dispques.setText(s);
    }

    private void setRadioOptions()throws Exception
    {
        String s[]=new String[4];

        for(int i=0;i<4;i++)
            s[i] = q.getAns();

        for(int i=0;i<4;i++)
            radio[i].setText(s[i]);
    }

    private void makeInvisible()
    {
        next.setVisibility(View.GONE);
        quit.setVisibility(View.GONE);
        for(int i=0;i<4;i++)
            radio[i].setVisibility(View.GONE);
    }

    private class ButtonAction implements View.OnClickListener
    {
        MainActivity m;
        ButtonAction(MainActivity m)
        {
            this.m=m;
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.next_btn: nextAction();
                    break;
                case R.id.quit_btn:
                    String s="You have given correction ans of "+(--q.quesno)+"/10 questions";
                    dispques.setText(s);
                    makeInvisible();
                    tryAgain.setVisibility(View.VISIBLE);
                    try
                    {
                        q.br.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    break;

                case R.id.try_btn:
                    clearAllToStartAgain();
                    try
                    {
                        displayQuestion();
                        setRadioOptions();
                        q.getCorrectAns();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        public void nextAction()
        {
            boolean b[]=checkAns();
            rgroup.clearCheck();
            if(b[0])
            {
                if(b[1])
                {
                    q.prize+=1000000;
                    try
                    {
                        if(q.quesno==10)
                        {
                            makeInvisible();
                            dispques.setText("You won the game");
                            q.br.close();
                            return;
                        }

                        displayQuestion();
                        setRadioOptions();
                        q.getCorrectAns();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    makeInvisible();
                    if(q.prize!=0)
                        q.prize-=1000000;
                    dispques.setText("You have given correction ans of "+(--q.quesno)+"/10 questions");
                    try
                    {
                        q.br.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    tryAgain.setVisibility(View.VISIBLE);
                }
            }
        }

        private boolean[] checkAns()
        {
            boolean b[]=new boolean[2];
            b[0]=false;
            b[1]=false;
            int a=3;
            switch (rgroup.getCheckedRadioButtonId())
            {
                case R.id.option1:  a--;
                case R.id.option2:  a--;
                case R.id.option3:  a--;
                case R.id.option4:  b[0]=true;
                    break;
                default:return b;
            }
            if(Integer.parseInt(q.ans)==a)
            {
                b[1]=true;
            }
            return b;
        }

        void clearAllToStartAgain()
        {
            try
            {
                InputStream is= m.getResources().openRawResource(R.raw.question);
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                q=new QuizLogic(br);
            } catch (Exception e) {
                e.printStackTrace();
            }
            next.setVisibility(View.VISIBLE);
            quit.setVisibility(View.VISIBLE);
            for(int i=0;i<4;i++)
                radio[i].setVisibility(View.VISIBLE);
            rgroup.clearCheck();
            tryAgain.setVisibility(View.GONE);
        }
    }
}
