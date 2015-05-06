
/**
 * Write a description of class Category here.
 * 
 * @author Tracy Oguni 
 * @version 2
 */
import java.util.ArrayList;
import java.util.HashMap;

class Category{
    public String name;
    public int[] stars = new int[6];
    public ArrayList<String> businesses = new ArrayList<String>();
    public int count =0;

    public int getTotalRating(){
        return stars[1] + (stars[2] *2) + (stars[3]*3) + (stars[4]*4) + (stars[5]*5);
    }

    public double getAverageRating(){
        return getTotalRating()/count;
    }

    public double getAverage(){
        int total = stars[0] + stars[1] + stars[2] + stars[3] + stars[4] + stars[5];
        return (double)total/(double)count;
    }

    public double getPriorProb(int totalBusinesses){
        return (double)count/(double)totalBusinesses;
    }

    public double getAveRatingCondProb(double value, HashMap<String, Business> businessMap, HashMap<String, Category> categoryMap){
        int valueCount =0;
        int totalCount =0;
        for (String bus: businesses){
            Business business = businessMap.get(bus);
            if (business == null){
                continue;
            }
            totalCount++;
            double businessAve = business.getAverageRating();
            int ave = NaiveBayes.smoothRating(businessAve, (new Double(businessAve)).intValue());
            if (ave == value){
                valueCount++;
            }

        }
        if (totalCount ==0){
            return 0;
        }
        return (double)(valueCount+1)/(double)(totalCount+categoryMap.size());
    }
    
    public double getModeRatingCondProb(int value, HashMap<String, Business> businessMap, HashMap<String, Category> categoryMap){
        int valueCount =0;
        int totalCount =0;
        for (String bus: businesses){
            Business business = businessMap.get(bus);
            if (business == null){
                continue;
            }
            totalCount++;
            int mode = business.getModeRating();
            if (mode == value){
                valueCount++;
            }

        }
        if (totalCount ==0){
            return 0;
        }
        return (double)(valueCount+1)/(double)(totalCount+categoryMap.size());
    }
    
    public double getMedianRatingCondProb(int value, HashMap<String, Business> businessMap, HashMap<String, Category> categoryMap){
        int valueCount =0;
        int totalCount =0;
        for (String bus: businesses){
            Business business = businessMap.get(bus);
            if (business == null){
                continue;
            }
            totalCount++;
            int median = business.getMedianRating();
            if (median == value){
                valueCount++;
            }

        }
        if (totalCount ==0){
            return 0;
        }
        return (double)(valueCount+1)/(double)(totalCount+categoryMap.size());
    }
}