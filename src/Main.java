import jdk.nashorn.internal.codegen.CompilerConstants;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * ICS440 Assignment 2 Multi-thread Historical Weather cache
 * Supplied files from ftp://ftp.ncdc.noaa.gov/pub/data/ghcn/daily/ contain Weather station details and historical reports for those stations.
 * Application requests a date rang and desired outcome, 1 =  top 5 highest temps , 0 = top 5 lowest temps
 * It reads and stores station details before processing weather files.
 * Then searches for TMIN/TMAX records depending on the user input using x number of threads.
 * The min/max 5 are returned from each file and added to a list which is then parsed once again by 4 threads to a subset of 20 minx/max values
 * finally the subset is parsed once more to the final set of 5. *
 * @author  Nick Williams
 * @since   2018-10-14
 */
public class Main {

    public static void main(String[] args) {
        UserInterface userInput = null;
        WeatherData[] resultSet = new WeatherData[5];//Final result set destination (min/max 5)
        try {
            userInput = new UserInterface();//Instantiate new UI for input ranges and min/max outcome.
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashMap<String, StationData> stationMap = getStationMap();
        resultSet = getResultsSet(userInput);
        for (WeatherData i : resultSet) {
            System.out.println(i.toString());
            System.out.println(stationMap.get(i.getId()).toString());
        }
    }

    /**
     * Processes station text in application directory
     * Set station data to hashmap for easy retrival
     *
     * @return HashMap: Station Name String, StationData Object
     */
    private static HashMap<String, StationData> getStationMap() {
        File stationInputFile = new File("stations.txt");
        ArrayList<String> stationInputData = readFileInput(stationInputFile);
        HashMap<String, StationData> stationMap = new HashMap<>();
        for (String i : stationInputData) {
            StationData newStation = processStationLine(i);
            stationMap.put(newStation.getId(), newStation);
        }
        return stationMap;
    }

    /**
     * Station parsing code provided in assignment
     * Set's station object values from predetermined string index ranges
     * @param thisLine
     * */
    public static StationData processStationLine(String thisLine) {

        StationData sd = new StationData();
        sd.setId(thisLine.substring(0, 11));
        sd.setLatitude(Float.valueOf(thisLine.substring(12, 20).trim()));
        sd.setLongitude(Float.valueOf(thisLine.substring(21, 30).trim()));
        sd.setElevation(Float.valueOf(thisLine.substring(31, 37).trim()));
        sd.setState(thisLine.substring(38, 40));
        sd.setName(thisLine.substring(41, 71));
        return sd;
    }

    /**
     * The initial process (1/3)
     * Receives userInput object to parameterize the parsing of the files and inform the outcome
     * resultList is created to house the weatherData array results attained by each thread from a future Object
     * The number of threads is set to the executorService and the local directory containing the files is parsed
     * Each file in the directory is parsed for the top 5 results (based on the userInput parms) and sent to a Future
     * Once resultList has received all future promises each WeatherData obejct is parsed from it's array into an arrayList
     * The new arrayList is sent to be processed further by processResults
     * @param userInput
     * */
    private static WeatherData[] getResultsSet(UserInterface userInput) {
        List<Future<WeatherData[]>> resultList = new ArrayList<Future<WeatherData[]>>();
        ExecutorService executorService = Executors.newFixedThreadPool(25);

        File dir = new File("ghcnd_hcn");
        File[] weatherInputFile = dir.listFiles();
        if (weatherInputFile != null) {//Test file exists before processing
            for (File file : weatherInputFile) {
                for (String i : readFileInput(file)) {
                    Callable processWeatherFiles = new ProcessWeatherFiles(userInput, i);
                    Future<WeatherData[]> results = executorService.submit(processWeatherFiles);
                    resultList.add(results);
                }
            }
        }
        List<WeatherData> weatherDataList = new ArrayList<>();
        for (Future<WeatherData[]> i : resultList) {
            try {
                for (WeatherData j : i.get()) {
                    if (j != null) {
                        weatherDataList.add(j);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
        WeatherData[] finalResults = processResults(userInput, weatherDataList);
        return finalResults;
    }

    /**
     * In the middle processing step (2/3)
     * 4 Threads and Future WeatherData Arrays are created to process the ArrayList of results that's been stored into processWeatherList
     * The futures are again saved to an array list, individual results are not removed from their arrays at this point however.
     * @param userInput
     * @param weatherDataList
     * */
    private static WeatherData[] processResults(UserInterface userInput, List<WeatherData> weatherDataList) {
        Callable processWeatherList = new ProcessWeatherList(userInput, weatherDataList);
        List<Future<WeatherData[]>> resultList = new ArrayList<Future<WeatherData[]>>();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Future<WeatherData[]> futureResult1 = executorService.submit(processWeatherList);
        resultList.add(futureResult1);
        Future<WeatherData[]> futureResult2 = executorService.submit(processWeatherList);
        resultList.add(futureResult2);
        Future<WeatherData[]> futureResult3 = executorService.submit(processWeatherList);
        resultList.add(futureResult3);
        Future<WeatherData[]> futureResult4 = executorService.submit(processWeatherList);
        resultList.add(futureResult4);
        executorService.shutdown();

        WeatherData[] finalResults = getFinalResults(userInput,resultList);
        return finalResults;
    }

    /**
     * In the final step (3/3)
     * The collection of WeatherData arrays are processed iteratively
     * Each record is added to another array list tempList which is the sorted.
     * Finally once all of the values have been sorted the top 5 results are stored into a separate WeatherData Array and returned
     * */
    private static WeatherData[] getFinalResults(UserInterface userInput, List<Future<WeatherData[]>> resultList) {
        WeatherData[] returnList = new WeatherData[5];
        ArrayList<WeatherData> tempList = new ArrayList<>();
        for (Future<WeatherData[]> i : resultList){
            try {
                for (WeatherData j : i.get()){
                    tempList.add(j);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(tempList);//Sorts min -> max
        if(userInput.isMaximum()){//check to sort mex -> min
            Collections.reverse(tempList);
        }
        for(int i =0;i<returnList.length;i++){
            returnList[i] = tempList.get(i);
        }
        return returnList;
    }

    /**
     * Receives an existing input file and reads through and assigns each line to an array
     * @param inputFile
     * @return fileList
     */
    public static ArrayList<String> readFileInput(File inputFile) {
        ArrayList<String> fileList = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile.getPath()));
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                fileList.add(readLine);
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound: No file at specified file path" + inputFile.getPath());
        } catch (IOException e) {
            System.out.println("IOException: No data in selected file");
        }
        return fileList;
    }

}


