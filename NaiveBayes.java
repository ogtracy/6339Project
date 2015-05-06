
/**
 * Write a description of class NaiveBayes here.
 * 
 * @author Tracy 
 * @version 1
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Iterator;

public class NaiveBayes
{
    private HashMap<String, Business> testList;

    double[] priorProbs;
    double[][] probTrue;
    double[][] probFalse;
    int[] classCounts = new int[7];
    int totalBusinesses;
    private static final int AVERATING = 0;
    private HashMap<String, Business> businessMap;
    private HashMap<String, Category> categoryMap;

    public NaiveBayes(){
        businessMap = new HashMap<String, Business>();
        categoryMap = new HashMap<String, Category>();
        testList = new HashMap<String, Business>();
    }

    public static void main(String[] args) {

        NaiveBayes obj = new NaiveBayes();
        obj.setup();
        //obj.calculateProbs();
        obj.classify();

    }

    private void setup(){
        setupBusiness();
        setupReviews();
        setupCategories();
    }

    private void setupReviews(){
        BufferedReader br = null;
        String line = "";
        int count =0;

        try{
            br = new BufferedReader(new FileReader("review.json"));
            while ((line = br.readLine()) != null) {
                JSONObject obj = new JSONObject(line);
                Object b_id = obj.get("business_id");
                Object u_id = obj.get("user_id");
                double stars = obj.getDouble("stars");
                Double star = Double.valueOf(stars);
                int intStar = star.intValue();
                intStar = smoothRating(stars, intStar);
                Business business = businessMap.get(b_id.toString());
                if (business == null){
                    business = testList.get(b_id.toString());
                }
                business.stars[intStar]++;
                count++;
            }

            System.out.println("There are " + count + " reviews.");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void setupBusiness(){
        BufferedReader br = null;
        String line = "";
        int count =0;

        try{
            br = new BufferedReader(new FileReader("business.json"));

            while ((line = br.readLine()) != null) {
                JSONObject obj = new JSONObject(line);
                Object id = obj.get("business_id");
                Object address = obj.get("full_address");
                JSONArray categories = obj.getJSONArray("categories");
                Object city = obj.get("city");
                Object reviewCount = obj.get("review_count");
                Object state = obj.get("state");
                Object stars = obj.get("stars");

                for (int x=0; x< categories.length(); x++){
                    if (!categoryMap.containsKey ( categories.getString(x) )){
                        Category category = new Category();
                        category.name = categories.getString(x);
                        ArrayList<String> businesses = new ArrayList<String>();
                        businesses.add(id.toString());
                        category.businesses = businesses;
                        categoryMap.put(categories.getString(x), category);
                    } else {
                        Category cat = categoryMap.get(categories.getString(x));
                        cat.businesses.add(id.toString());
                    }
                }
                Business business = new Business();
                business.id = id.toString();
                business.value = line;
                if (count < 15296){
                    testList.put(id.toString(), business);
                }
                else {
                    businessMap.put(id.toString(), business);
                }
                count++;
            }

            System.out.println("There are " + businessMap.size() + " businesses.");
            totalBusinesses = businessMap.size();
            System.out.println("There are " + categoryMap.size() + " categories.");

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void setupCategories(){
        for (String key: categoryMap.keySet()){
            Category cat = categoryMap.get(key);
            ArrayList<String> businesses = cat.businesses;
            for (String bus: businesses){
                Business business = businessMap.get(bus);
                if (business == null){
                    continue;
                }
                cat.count++;
                for(int x=0; x<6; x++){
                    
                    cat.stars[x] = cat.stars[x] + business.stars[x];
                }
            }
        }
    }

    private double getCondProb(int x, String y, int z){
        return 0.0;
    }

    public void classify()
    {
        int correctCount =0;
        int count =0;

        //for each item we are testing
        for (String itemKey: testList.keySet()){ 
            //get the item
            Business item = testList.get(itemKey);
            double aveRating = item.getAverageRating();
            Double averageRating = new Double(aveRating);
            int average = averageRating.intValue();
            average = smoothRating(aveRating, average);
            int median = item.getMedianRating();
            int mode = item.getModeRating();

            double highestProb =0;
            String highestKey="";
            for (String key: categoryMap.keySet()){
                Category cat = categoryMap.get(key);
                double condProb = cat.getAveRatingCondProb(average, businessMap, categoryMap) * cat.getMedianRatingCondProb(median, businessMap, categoryMap) * cat.getModeRatingCondProb(mode, businessMap, categoryMap) ;
                double priorProb = cat.getPriorProb(totalBusinesses);
                double prob = condProb * priorProb;
                if (prob > highestProb){
                    highestProb = prob;
                    highestKey = key;
                }
            }

            JSONObject obj = new JSONObject(item.value);
            JSONArray categories = obj.getJSONArray("categories");
            for (int x=0; x< categories.length(); x++){
                if(highestProb ==0){
                    System.out.println("prob is zero");
                    break;
                }
                if(categories.getString(x).equals(highestKey)){
                    correctCount++;
                    System.out.println(highestKey);
                    break;
                }
            }

            count++;
        }
        float accuracy = (float)correctCount/ (float)count;
        System.out.println(accuracy);
    }

    public static int smoothRating(double rating, int intRating){
        switch (intRating) {
            case 0:
            if (rating > 0.0){
                intRating++;
            }
            case 1:
            if (rating > 1.0){
                intRating++;
            }
            break;
            case 2:
            if (rating > 2.0){
                intRating++;
            }
            break;
            case 3:
            if (rating > 3.0){
                intRating++;
            }
            break;
            case 4:
            if (rating > 4.0){
                intRating++;
            }
            break;
            case 5:
            break;
        }
        return intRating;
    }
}
