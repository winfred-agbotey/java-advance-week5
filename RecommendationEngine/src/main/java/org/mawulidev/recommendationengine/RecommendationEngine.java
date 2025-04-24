package org.mawulidev.recommendationengine;

import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.explode;

@RestController
public class RecommendationEngine {
    @GetMapping("/recommendation")
    public String recommendProduct () {
        SparkSession spark = SparkSession.builder()
                .appName("recommendationengine")
                .master("spark://4b83e7e11bb8:7077")
                .getOrCreate();

        StructType schema = new StructType()
                .add("UserID", DataTypes.IntegerType, false)
                .add("ProductID", DataTypes.IntegerType, false)
                .add("Rating", DataTypes.DoubleType, false);

        Dataset<Row> data = spark.read()
                .option("header", "true")
                .schema(schema)
                .csv(Objects.requireNonNull(getClass().getClassLoader().getResource("data.csv")).getPath());

        data.printSchema();
        data.show();


        //Validate Data
        Dataset<Row> numericData = data
                .withColumn("UserID", data.col("UserID").cast("int"))
                .withColumn("ProductID", data.col("ProductID").cast("int"))
                .withColumn("Rating", data.col("Rating").cast("double"));

        // Handle missing or null values (optional)
        numericData = numericData.na().drop(); // Drop rows with null values


        //Setting up dataset for training the model
        ALS als = new ALS()
                .setUserCol("UserID")
                .setItemCol("ProductID")
                .setRatingCol("Rating")
                .setMaxIter(10) //No of iterations the algorithm runs during the training
                .setRank(10); //No. of Latent features to learn. Higher rank == higher nuanced predictions.

        ALSModel model = als.fit(numericData); // Action to train the model

        //Generate Recommendations
        Dataset<Row> recommendations = model.recommendForAllUsers(10); //Generates a list of 10 recommended products of each user in the dataset.
        recommendations.show(); //Displays the recommendation for each user.

        //Flatten the recommendations
        Dataset<Row> flattenedRecommendations = recommendations
                .withColumn("recommendation", explode(col("recommendations"))) // Explode the array
                .select(
                        col("UserID"),
                        col("recommendation.ProductID").alias("ProductID"),
                        col("recommendation.rating").alias("Rating")
                );

        //Saving the Recommendation in a file
        flattenedRecommendations.write()
                .option("header", "true")
                .csv("recommendations");

        //Stop Spark Session
        spark.stop();

        return flattenedRecommendations.toString();
    }

}