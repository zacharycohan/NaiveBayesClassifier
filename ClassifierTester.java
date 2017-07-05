import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/**
 *  This class test the Naive Bayes Classifier
 * @author Zachary Cohan
 */
public class ClassifierTester {
    
    private static DataMap fullTestingSet;
    private static DataMap test;
    private static NaiveBayesClassifier cl;

    public static void main(String[] args) {
        
//        if(args.length < 1){
//            System.out.println("One command line argument needed. The name of the file that will be used in this classification");
//            System.out.println("(voting-data.tsv)");
//            System.exit(-1);
//        }
        File vd = new File("voting-data.tsv");
        //File vd = new File(args[0]);
        Scanner in = null;
        String[] datumString;
        Datum datum;
        try {
            in = new Scanner(vd);
        } catch (FileNotFoundException ex) {
            System.out.println("The file was not found.");
            System.out.println("Please enter the file you would like to use as the command line('voting-data.tsv')" );
        }
        fullTestingSet = new DataMap();
        
        
        //creates the training set
        System.out.println("Creating training set...");
        while (in.hasNext()) {
            datumString = in.nextLine().split("\t");
            datum = new Datum(datumString[0], datumString[1], datumString[2]);
            
            fullTestingSet.add(datum);

        }
        System.out.println("training set completed...");
        
        int correct = 0;
        int incorrect = 0;
        Datum d;
        String s;
        
        
        System.out.println("Now classifying using Leave-One-Out cross validation...");
        for(int i = 0;i<fullTestingSet.size();i++)
        {
            d = fullTestingSet.data.get(i);
            test = NaiveBayesClassifier.createTrainingSet(fullTestingSet, i);
            cl = new NaiveBayesClassifier(test.data);
            s = cl.classify(d);
            if(s.equals(d.classification))correct++;
            else incorrect++;   
        }
        double percentCorrect = ((double)correct/(double)fullTestingSet.size())*100;
        
        System.out.println("Leave-One-Out cross validation shows this classifier to classify ");
        System.out.println("correctly "+NaiveBayesClassifier.round(percentCorrect, 4)+"% of the time");

        
        
    
    }
}
