import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Uses the Naive Bayes algorithm to classify voting data
 * @author Zachary Cohan
 */
public class NaiveBayesClassifier {

    int demCount = 0;//number of democrats total
    int repCount = 0;//number of republicant total
    double dlikelihood;
    double rlikelihood;
    int total = 0; //total number of representatives
    int[][] counts;//a 2d array containing all of the counts
    
    public NaiveBayesClassifier(Datum[] data)
    {
        //table of labels and features. Labels are R and D,
        counts = new int[2][30];
        
        //initialize all counts to 1 to prevent and 0 counts from messing up calculations
        for(int i = 0;i<counts.length;i++)
        {
            for(int j = 0;j < counts[i].length;j++)
            {
                counts[i][j] = 1;
            }
        }
        
        //go through each Datum in the training set and increase counts accordingly
        for(Datum d : data)
        {
            if(d.classification.equals("D"))
            {
                demCount++;
                for(int i = 0;i<10;i++)
                {
                    if(d.getVote(i) == 1)counts[0][i*3]++;
                    else if(d.getVote(i) == -1)counts[0][(i*3)+1]++;
                    else counts[0][(i*3)+2]++;
                }
                
            }
            else
            {
                repCount++;
                for(int i = 0;i<10;i++)
                {
                    if(d.getVote(i) == 1)counts[1][i*3]++;
                    else if(d.getVote(i) == -1)counts[1][(i*3)+1]++;
                    else counts[1][(i*3)+2]++;
                }
            }
            total++;
            
            
        }
        
    }
    
    public NaiveBayesClassifier(ArrayList<Datum> data)
    {
        //table of labels and features. Labels are R and D,
        counts = new int[2][30];
        
        //initialize all counts to 1 to prevent and 0 counts from messing up calculations
        for(int i = 0;i<counts.length;i++)
        {
            for(int j = 0;j < counts[i].length;j++)
            {
                counts[i][j] = 1;
            }
        }
        
        //go through each Datum in the training set and increase counts accordingly
        for(Datum d : data)
        {
            if(d.classification.equals("D"))
            {
                demCount++;
                for(int i = 0;i<10;i++)
                {
                    if(d.getVote(i) == 1)counts[0][i*3]++;
                    else if(d.getVote(i) == -1)counts[0][(i*3)+1]++;
                    else counts[0][(i*3)+2]++;
                }
                
            }
            else
            {
                repCount++;
                for(int i = 0;i<10;i++)
                {
                    if(d.getVote(i) == 1)counts[1][i*3]++;
                    else if(d.getVote(i) == -1)counts[1][(i*3)+1]++;
                    else counts[1][(i*3)+2]++;
                }
            }
            total++;
        }
        dlikelihood = round( (double)demCount/(double)total  ,12);
        rlikelihood = round( (double)repCount/(double)total  ,12);
        
    }
    
    /**
     * Classifys a Datum based on the voting record using the naive Bayes algorithm
     * 
     * @param d the Datum to be classified
     * @return the classification
     */
    public String classify(Datum d)
    {
        double dem;
        double rep;
        char v;
        double dCount = counts[0][0]+counts[0][1]+counts[0][2];
        double rCount = counts[1][0]+counts[1][1]+counts[1][2];
        
        //this block of code just initializes values before looping through the rest of the voting record
        //being classified
        v = d.votes.charAt(0);
        if(v == '+'){
            dem = round((double)counts[0][0]/dCount,12);
            rep = round((double)counts[1][0]/rCount,12);
        }
        else if (v == '-'){
            dem = round((double)counts[0][1]/dCount,12);
            rep = round((double)counts[1][1]/rCount,12);
        }
        else{
            dem = round((double)counts[0][2]/dCount,12);
            rep = round((double)counts[1][2]/rCount,12);
        }
        
        //this loop goes through the remaining votes, calculating their likelihood according to the naive bayes algorithm
        for(int i = 1;i<10;i++)
        {
            v = d.votes.charAt(i);
            if(v == '+'){
                dem = round(dem*((double)counts[0][i*3]/dCount) ,12);
                rep = round(rep*((double)counts[1][i*3]/rCount) ,12);
            }
            else if (v == '-'){
                dem = round(dem*((double)counts[0][i*3+1]/dCount) ,12);
                rep = round(rep*((double)counts[1][i*3+1]/rCount) ,12);
            }
            else{
                dem = round(dem*((double)counts[0][i*3+2]/dCount) ,12);
                rep = round(rep*((double)counts[1][i*3+2]/rCount) ,12);
            }    
        }
        
        //these two lines modify the probability by the probability that a datum is either a dem or rep
        dem = round(dem*dlikelihood,12);
        rep = round(rep*rlikelihood,12);

        if (rep > dem)return "R";
        else return "D";
    }
   
    //TAKEN FROM STACK OVERFLOW, ROUNDS NUMBER TO A DESIRED NUMBER OF PLACES
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    /**
     * Creates a DataMap which will be used for testing. Leaves out the item that will be used for leave
     * one out cross validation
     * @param testSet the original unaltered set
     * @param index the index of the item that will get left out
     * @return the altered set
     */
    public static DataMap createTrainingSet(DataMap testSet,int index)
    {
        DataMap toReturn = new DataMap();
   
        for(int i = 0;i<testSet.size();i++)
        {
            if(i == index);
            else toReturn.add(testSet.data.get(index));
        }
        
        return toReturn;
    }

}
