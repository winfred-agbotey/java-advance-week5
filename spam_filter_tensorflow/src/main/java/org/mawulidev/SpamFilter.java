package org.mawulidev;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class SpamFilter {
    public static void main(String[] args) throws Exception {
        // First, ensure the dataset is properly saved in a CSV file
        String datasetPath = new File("").getAbsolutePath() + "/src/main/java/org/mawulidev/spam.csv";

        // Create the dataset if it doesn't exist
        if (!new File(datasetPath).exists()) {
            createDatasetFile(datasetPath);
        }

        System.out.println("Loading file from: " + datasetPath);

        // Load dataset
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(datasetPath));

        // Make sure the string attribute is properly identified
        loader.setStringAttributes("2");  // v2 column contains the message text
        Instances data = loader.getDataSet();

        // Set the class index (v1 column: ham/spam)
        data.setClassIndex(0);

        // Print out some basic information about the dataset
        System.out.println("Dataset loaded: " + data.numInstances() + " instances");
        System.out.println("Class attribute: " + data.classAttribute().name());
        System.out.println("Class values: " + data.classAttribute().toString());

        // Configure the StringToWordVector filter
        StringToWordVector filter = new StringToWordVector();
        filter.setAttributeIndices("2");  // Process the second attribute (v2)
        filter.setWordsToKeep(1000);      // Keep the 1000 most frequent words
        filter.setDoNotOperateOnPerClassBasis(false);
        filter.setLowerCaseTokens(true);
        filter.setMinTermFreq(2);         // Words must appear at least twice
        filter.setOutputWordCounts(true); // Output word counts rather than boolean
        filter.setStemmer(null);          // No stemming
        filter.setStopwordsHandler(null); // No stopwords
        filter.setTFTransform(true);      // Apply TF transform
        filter.setIDFTransform(true);     // Apply IDF transform
//        filter.setNormalizeDocLength(new StringToWordVector.NormalizeDocLength());
        filter.setInputFormat(data);

        // Apply the filter to the data
        Instances filteredData = Filter.useFilter(data, filter);

        System.out.println("Filtered data: " + filteredData.numAttributes() + " attributes");

        // Split into training and testing sets (80/20 split)
        filteredData.randomize(new Random(42));  // Seed for reproducibility
        int trainSize = (int) Math.round(filteredData.numInstances() * 0.8);
        int testSize = filteredData.numInstances() - trainSize;

        Instances trainingData = new Instances(filteredData, 0, trainSize);
        Instances testingData = new Instances(filteredData, trainSize, testSize);

        System.out.println("Training set: " + trainingData.numInstances() + " instances");
        System.out.println("Testing set: " + testingData.numInstances() + " instances");

        // Train NaiveBayes classifier
        NaiveBayes classifier = new NaiveBayes();
        classifier.buildClassifier(trainingData);

        // Evaluate on test set
        int correct = 0;
        for (int i = 0; i < testingData.numInstances(); i++) {
            Instance instance = testingData.instance(i);
            double prediction = classifier.classifyInstance(instance);
            if (prediction == instance.classValue()) {
                correct++;
            }
        }

        System.out.println("Test set accuracy: " + (correct * 100.0 / testingData.numInstances()) + "%");

        // Create a new instance for the example spam message
        Instance rawInstance = new DenseInstance(data.numAttributes());
        rawInstance.setDataset(data);
        rawInstance.setValue(data.attribute(1),
                "Congratulations! You've won a $1000 Walmart gift card. Click here to claim now.");

//        rawInstance.setValue(data.attribute(1),
//                "I'm am going to school.");

        // Create a new dataset with just this instance
        Instances newDataset = new Instances(data, 0);
        newDataset.add(rawInstance);

        // Apply the same filter
        Instances filteredNewInstance = Filter.useFilter(newDataset, filter);

        // Classify
        double predictionValue = classifier.classifyInstance(filteredNewInstance.firstInstance());
        String prediction = data.classAttribute().value((int) predictionValue);

        System.out.println("Prediction for test message: " + prediction);

        // Print probabilities
        double[] distributionForInstance = classifier.distributionForInstance(filteredNewInstance.firstInstance());
        int spamIndex = data.classAttribute().indexOfValue("spam");
        int hamIndex = data.classAttribute().indexOfValue("ham");

        System.out.println("Probability of spam: " + distributionForInstance[spamIndex]);
        System.out.println("Probability of ham: " + distributionForInstance[hamIndex]);
    }




    private static void createDatasetFile(String filePath) throws Exception {
        System.out.println("Creating dataset file at: " + filePath);
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        // Create a FileWriter to write the dataset
        FileWriter writer = new FileWriter(file);

        // Write the header line
        writer.write("v1,v2\n");

        // These are the first few examples from the dataset you provided
        writer.write("ham,\"Go until jurong point, crazy.. Available only in bugis n great world la e buffet... Cine there got amore wat...\"\n");
        writer.write("ham,\"Ok lar... Joking wif u oni...\"\n");
        writer.write("spam,\"Free entry in 2 a wkly comp to win FA Cup final tkts 21st May 2005. Text FA to 87121 to receive entry question(std txt rate)T&C's apply 08452810075over18's\"\n");
        writer.write("ham,\"U dun say so early hor... U c already then say...\"\n");
        writer.write("ham,\"Nah I don't think he goes to usf, he lives around here though\"\n");
        writer.write("spam,\"FreeMsg Hey there darling it's been 3 week's now and no word back! I'd like some fun you up for it still? Tb ok! XxX std chgs to send, £1.50 to rcv\"\n");
        writer.write("ham,\"Even my brother is not like to speak with me. They treat me like aids patent.\"\n");
        writer.write("ham,\"As per your request 'Melle Melle (Oru Minnaminunginte Nurungu Vettam)' has been set as your callertune for all Callers. Press *9 to copy your friends Callertune\"\n");
        writer.write("spam,\"WINNER!! As a valued network customer you have been selected to receivea £900 prize reward! To claim call 09061701461. Claim code KL341. Valid 12 hours only.\"\n");
        writer.write("spam,\"Had your mobile 11 months or more? U R entitled to Update to the latest colour mobiles with camera for Free! Call The Mobile Update Co FREE on 08002986030\"\n");

        // Add more examples as needed or read from the document you provided

        writer.close();
        System.out.println("Dataset file created successfully.");
    }
}