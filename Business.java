
/**
 * Write a description of class Business here.
 * 
 * @author Tracy 
 * @version 2
 */
public class Business {
    public String id;
    public String value;
    public int[] stars = new int[6];
    public int getNOfRatings(){
        return stars[0] + stars[1] + stars[2] + stars[3] + stars[4] + stars[5];
    }

    public int getTotalRating(){
        return stars[1] + (stars[2] *2) + (stars[3]*3) + (stars[4]*4) + (stars[5]*5);
    }

    public double getAverageRating(){
        if (getNOfRatings() == 0){
            return 0;
        }
        return (double)getTotalRating()/(double)getNOfRatings();
    }

    public int getMedianRating(){
        int n = getNOfRatings();
        int median =0;
        int sum =0;
        if ( n == 0){
            return 0;
        }
        if (n % 2 == 0){
            int middleLeft = (n/2);
            int middleRight = (n/2) +1;
            for (int x =0; x < 6; x++){
                sum += stars[x];
                if (sum >= middleLeft){
                    if (sum >= middleRight){
                        median =x;
                    } else {
                        median = x=1;
                    }
                    break;
                }
            }
        } else {
            int middle = (n/2) +1;

            for (int x =0; x < 6; x++){
                sum += stars[x];
                if (sum >= middle){
                    median =x;
                    break;
                }
            }
        }
        return median;
    }

    public int getModeRating(){
        int n = getNOfRatings();
        if ( n == 0){
            return 0;
        }
        int mode =0;

        for (int x =0; x < 6; x++){
            if (stars[x] > stars[mode]){
                mode =x;
            }
        }
        return mode;
    }
}
