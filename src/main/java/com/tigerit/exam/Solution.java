package com.tigerit.exam;
import java.io.*;


import static com.tigerit.exam.IO.*;
import java.util.*;
/**
 * All of your application logic should be placed inside this class.
 * Remember we will load your application from our custom container.
 * You may add private method inside this class but, make sure your
 * application's execution points start from inside run method.
 */
class table
{
    String tablename;
    HashMap<String,Integer> fieldtoid=new HashMap<String,Integer>();
    int nr,nc;
    ArrayList<String> fields=new ArrayList<String>();
    int[][] list;
    int pos=0;
    table(int nr,int nc,String tablename,String s)
    {
        this.nr=nr;
        this.nc=nc;
        list=new int[nr][nc];
        this.tablename=tablename;
        s=s.replace("\n","");
        String[] att=s.split(" ");
        for(int i=0;i<nc;i++)
        {
            fields.add(att[i]);
        }

        for(int i=0;i<att.length;i++)
        {
            fieldtoid.put(att[i],i);
        }
    }
    void addToTable(String s)
    {
        String[] sp=s.split(" ");
        for(int i=0;i<sp.length;i++)
        {
            list[pos][i]=Integer.parseInt(sp[i]);
        }
        pos++;
    }
    void summary()
    {
        System.out.println(tablename);
        for(int i=0;i<nr;i++)
        {
            for(int j=0;j<nc;j++)
            {
                System.out.print(list[i][j]+" ");
            }
            System.out.println("");
        }
    }

}

class Query
{
    static Comparator<ArrayList<Integer>> comp = new Comparator<ArrayList<Integer>>() {
        public int compare(ArrayList<Integer> a1, ArrayList<Integer> a2) {
            int sz = a1.size();
            for (int i = 0; i < sz; i++) {
                if (a1.get(i) > a2.get(i)) {
                    return 1;
                } else if (a1.get(i) < a2.get(i)) {
                    return -1;
                }
            }
            return 0;
        }
    };
    ArrayList<ArrayList<Integer> > aList =
            new ArrayList<ArrayList<Integer> >();
    String table1,table2;
    String field1,field2;
    String alias1,alias2;
    String line1,line2,line3,line4;
    String eq1,eq2,eq3,eq4;
    ArrayList<String> toPrintfirst=new ArrayList<String>();
    ArrayList<String> toPrintsecond=new ArrayList<String>();
    HashMap<String,String> alias=new HashMap<String,String>();
    Query(String line1,String line2,String line3,String line4,HashMap<String,table> hm)
    {
        this.line1=line1;
        this.line2=line2;
        this.line3=line3;
        this.line4=line4;
        parseSecondThird();
        parse4();
        parse1(hm);
    }
    void parseSecondThird()
    {
        line2=line2.replace("FROM ","");
        String[] temp=line2.split(" ");
        if(temp.length==1)
        {
            alias1=temp[0];
            field1=temp[0];
        }
        else
        {
            alias1=temp[1];
            field1=temp[0];
        }
        line3=line3.replace("JOIN ","");
        String[] temp1=line3.split(" ");
        if(temp1.length==1)
        {
            alias2=temp1[0];
            field2=temp1[0];
        }
        else
        {
            alias2=temp1[1];
            field2=temp1[0];
        }
    }
    void parse4()
    {
        line4=line4.replace("ON ","");
        String[] temp1=line4.split(" = ");
        eq1=field1;
        eq3 = field2;
        //System.out.println(temp1[0]);
        String[] t=temp1[0].split("\\.");
        //System.out.println(t.length);
        eq2=t[1];
        t=temp1[1].split("\\.");
        eq4=t[1];
    }
    void parse1(HashMap<String,table> hm)
    {
        line1=line1.replace("SELECT ","");
        String[] temp=line1.split(", ");
        if(!line1.equals("*"))
        {
            for(int i=0;i<temp.length;i++)
            {
                //System.out.println(temp[i]);
                String[] temp1=temp[i].split("\\.");
                if(temp1[0].equals(alias1)){
                    toPrintfirst.add(field1);
                }
                else if(temp1[0].equals(alias2)){
                    toPrintfirst.add(field2);
                }
                toPrintsecond.add(temp1[1]);
            }
        }
        else
        {
            ArrayList<String> t1=hm.get(field1).fields;
            for(int i=0;i<t1.size();i++)
            {
                toPrintfirst.add(field1);
                toPrintsecond.add(t1.get(i));
            }
            t1=hm.get(field2).fields;
            for(int i=0;i<t1.size();i++)
            {
                toPrintfirst.add(field2);
                toPrintsecond.add(t1.get(i));
            }
        }

    }
    void solve(HashMap<String,table> hm)
    {
        table t1=hm.get(field1);
        table t2=hm.get(field2);
        int idj1=t1.fieldtoid.get(eq2);
        int idj2=t2.fieldtoid.get(eq4);
        for(int k=0;k<toPrintsecond.size();k++)
        {
            if(k!=0)
            {
                System.out.print(" ");
            }
            System.out.print(toPrintsecond.get(k));

        }
        System.out.println("");
        for(int i=0;i<t1.nr;i++)
        {
            for(int j=0;j<t2.nr;j++)
            {
                if(t1.list[i][idj1]==t2.list[j][idj2])
                {
                    ArrayList<Integer> temp=new ArrayList<Integer>();
                    for(int k=0;k<toPrintfirst.size();k++)
                    {
                        String tn=toPrintfirst.get(k);
                        String tf=toPrintsecond.get(k);
                        if(tn.equals(t1.tablename))
                        {
                            temp.add(t1.list[i][t1.fieldtoid.get(tf)]);
                        }
                        else
                        {
                            temp.add(t2.list[j][t2.fieldtoid.get(tf)]);
                        }
                    }
                    aList.add(temp);
                }
            }
        }
        Collections.sort(aList,comp);
        for(int i=0;i<aList.size();i++)
        {

            for(int j=0;j<aList.get(i).size();j++)
            {
                if(j!=0)
                {
                    System.out.print(" ");
                }
                System.out.print(aList.get(i).get(j));

            }
            System.out.println("");
        }
        System.out.println("");
    }
    void summary()
    {
        for(int i=0;i<toPrintfirst.size();i++)
        {
            System.out.print(toPrintfirst.get(i)+"."+toPrintsecond.get(i)+" ");
        }
        System.out.println();
    }
}
public class Solution implements Runnable {
    @Override
    public void run() {
        // your application entry point

        // sample input process
        //String string = readLine();

        Integer integer = readLineAsInteger();
        for(int i=1;i<=integer;i++)
        {
            HashMap<String,table> hm=new HashMap<String,table>();
            Integer num_of_table=readLineAsInteger();
            for(int j=0;j<num_of_table;j++)
            {
                String tablename = readLine();
                tablename=tablename.replace("\n","");
                Scanner scanner=new Scanner(readLine());
                int nr=(int) scanner.nextInt();
                int nc=(int) scanner.nextInt();
                String att = readLine();
                table newtable=new table(nr,nc,tablename,att);

                for(int k=0;k<nr;k++)
                {
                    String line = readLine();
                    newtable.addToTable(line);
                }
                hm.put(tablename,newtable);
                //hm.get(tablename).summary();
            }
            Integer num_of_query=readLineAsInteger();
            for(int j=0;j<num_of_query;j++)
            {
                String line1 = readLine();
                line1=line1.replace("\n","");
                String line2 = readLine();
                line2=line2.replace("\n","");
                String line3 = readLine();
                line3=line3.replace("\n","");
                String line4 = readLine();
                line4=line4.replace("\n","");
                String line5 = readLine();
                Query newquery=new Query(line1,line2,line3,line4,hm);
                newquery.solve(hm);
            }

        }

        //Checking if push working
        // sample output process
        //printLine(string);
    }
}
