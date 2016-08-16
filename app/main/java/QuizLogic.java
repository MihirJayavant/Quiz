package com.myapp2.mihir.quiz;

/**
 * Created by Mihir on 23-06-2016.
 */



        import java.io.BufferedReader;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.util.Random;

public class QuizLogic
{

    String ans;BufferedReader br;
    int countans=0;
    int quesno=0,prize=0;
    int l5050=0;
    QuizLogic(BufferedReader a) throws FileNotFoundException
    {
        br=a;
    }
    String getQuestion() throws Exception
    {
        String q=br.readLine();
        /*int len=q.length();
        int len2=49;
        while(len>50)
        {
            q=q.substring(0,len2)+q.substring(len2).replaceFirst(" ","\n    ");
            len-=50;
            len2*=2;
        }*/
        quesno++;
        return q;
    }
    String getAns() throws IOException
    {
        String q=br.readLine();
        q=q.trim();
        countans++;
        return q;
    }
    boolean isNextCorrectAns()
    {
        if(countans==4)
            return true;
        else
            return false;
    }
    String getCorrectAns()throws IOException
    {
        if(isNextCorrectAns())
        {
            countans=0;
            ans= br.readLine();
            return ans;
        }
        else
            throw new NullPointerException();
    }
    String[] lifeline_5050(String[] ans,int correctans)
    {
        if(ans.length!=4)
            return null;
        Random r=new Random();
        int r1=r.nextInt(4);
        while(r1==correctans)
        {
            r1=r.nextInt(4);
        }
        String[] t=new String[4];
        t[correctans]=ans[correctans];
        t[r1]=ans[r1];
        return t;
    }
    String toCurrency(int p)
    {

        StringBuffer s=new StringBuffer(p+"");
        s.ensureCapacity(10);
        if(s.length()<4)
            return s+"";
        s.reverse();
        String str=s.substring(0,3)+","+s.substring(3);
        for(int i=5;i<str.length()-1;i+=2)
        {
            str=str.substring(0, i+1)+","+str.substring(i+1);
            i++;
        }
        return new StringBuffer(str).reverse()+"";
    }
}
