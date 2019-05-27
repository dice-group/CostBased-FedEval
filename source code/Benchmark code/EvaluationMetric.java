package org.aksw.simba.quetsal.core.error;


import java.util.*;


/**
 * This class provide the methods to evaluate a federated query engine. It contains functions to calculate
 * q-error, and cosine similarity error of a whole plan or joins in a plan or triple patterns in a plan.
 *
 */
public class EvaluationMetric {


    /**
     * This function returns the q-error corresponding to two vectors in arguments
     * @param vecActualcard This vector contains values of actual cardinalities in a query plan (Joins or TPs or Both)
     * @param vecEstimatedCard This vector contains values of estimated cardinalities in a query plan (Joins or TPs or Both)
     * @return q-error value
     * @throws IllegalArgumentException
     */
    public double getQerror (Vector vecActualcard, Vector vecEstimatedCard) throws IllegalArgumentException  {

        //check if both vectors are not equal
        if(vecActualcard.size()!=vecEstimatedCard.size()){
            throw new IllegalArgumentException ("Invalid Parameters: Please provide both vectors " +
                    "estimated and real cardinality (should be equal in size)");
        }

        //maximum value
        double max = 0;

        //calculate  q-error
        // Max(est/real) if est> real
        // Max (real/est) if real >est
        for (int i=0; i < vecEstimatedCard.size(); i++){
            double err = 0;
            if((Integer) vecEstimatedCard.get(i) < (Integer) vecActualcard.get(i)){
                 err = (Integer) vecActualcard.get(i) / (Integer) vecEstimatedCard.get(i);
            }else {
                 err = (Integer) vecEstimatedCard.get(i) / (Integer) vecActualcard.get(i);
            }
            if (err > max){
                max = err;
            }
        }

        return max;
    }


    /**
     * This method is for calculating the cosine similarity of query plan.
     * @param vecActualcard This vector contains values of actual cardinalities in a query plan (Joins or TPs or Both)
     * @param vecEstimatedCard This vector contains values of estimated cardinalities in a query plan (Joins or TPs or Both)
     * @return cosine similarity error value
     * @throws IllegalArgumentException
     */
    public double getCosSimError(Vector vecActualcard, Vector vecEstimatedCard) throws IllegalArgumentException  {
        //check if both vectors are not equal
        if(vecActualcard.size()!=vecEstimatedCard.size()){
            throw new IllegalArgumentException ("Invalid Parameters: Please provide both vectors " +
                    "estimated and real cardinality (should be equal in size)");
        }
        double result = 0;
        long ab=0;
        double a_norm =0;
        double b_norm =0;
        for(int i=0;i < vecEstimatedCard.size(); i++){
            ab += ((Integer)vecActualcard.get(i))* ((Integer)vecEstimatedCard.get(i));
            a_norm += Math.pow((Integer)vecActualcard.get(i),2);
            b_norm += Math.pow((Integer)vecEstimatedCard.get(i),2);
        }
        a_norm = Math.sqrt(a_norm);
        b_norm = Math.sqrt(b_norm);
        result = ab / (a_norm*b_norm);
        return result;
    }


    /**
     * This method is for calculating relative error between estimated and real cardinality
     * @param actualCard: Actual number of triples as a result of JOINs or triple patern query to endpoint.
     * @param estCard: Estimated number of triples as a result
     * @return relative error between actual and estimated results
     */
    protected double getRelativeError(int actualCard, int estCard){
        return (actualCard-estCard)/estCard;
    }
}
