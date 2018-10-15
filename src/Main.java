import jdk.nashorn.internal.codegen.CompilerConstants;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

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
            System.out.println("Final results" + i.toString());
//            System.out.println(stationMap.get(i.getId()).toString());
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

    private static WeatherData[] processResults(UserInterface userInput, List<WeatherData> weatherDataList) {
        int countdown = weatherDataList.size();
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

    private static WeatherData[] getFinalResults(UserInterface userInput, List<Future<WeatherData[]>> resultList) {
        WeatherData[] returnList = new WeatherData[5];
        ArrayList<WeatherData> tempList = new ArrayList<>();
        for (Future<WeatherData[]> i : resultList){
            try {
                for (WeatherData j : i.get()){
                    tempList.add(j);
                    Collections.sort(tempList);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        for(int i =0;i<returnList.length;i++){
            returnList[i] = tempList.get(i);
        }
        return returnList;
    }

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


