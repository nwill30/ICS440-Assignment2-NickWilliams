import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static void main(String[] args){
        WeatherData[] resultSet = new WeatherData[5];
        UserInterface userInput = null;
        try {
            userInput = new UserInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        File userDirectory = new File(System.getProperty("user.dir"));
        File stationInputFile = new File("stations.txt");
        ArrayList<String> stationInputData = readFileInput(stationInputFile);
        ArrayList<StationData> stationList = new ArrayList<>();
        for(String i : stationInputData){
            stationList.add(processStationLine(i));
        }

        File weatherInputFile = new File("ghcnd_hcn/USC00011084.dly");
        ArrayList<String > weatherInputData = readFileInput(weatherInputFile);
        ArrayList<WeatherData> weatherList = new ArrayList<>();
        for(String i : weatherInputData){
            if(userInput.isMaximum()){
                resultSet =searchMax(resultSet, processWeatherFile(i));
            }else if(userInput.isMinimum()){
//                resultSet = searchMin(resultSet, processWeatherFile(i));
            }
        }

        for(WeatherData i : resultSet){
            System.out.println(i.toString());
        }
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

    public static ArrayList<WeatherData> processWeatherFile(String thisLine){
        ArrayList<WeatherData> weatherDataList = new ArrayList<>();
        String id = thisLine.substring(0,11);
        int year = Integer.valueOf(thisLine.substring(11,15).trim());
        int month = Integer.valueOf(thisLine.substring(15,17).trim());
        String element = thisLine.substring(17,21);
        int days = (thisLine.length() - 21) / 8; // Calculate the number of days in the line
        for (int i = 0; i < days; i++) {         // Process each day in the line.
            int value = Integer.valueOf(thisLine.substring(21+8*i,26+8*i).trim());
            String qflag = thisLine.substring(27+8*i,28+8*i);
            WeatherData wd = new WeatherData(id,year,month,i+1,element,value,qflag);
            weatherDataList.add(wd);
        }
        return weatherDataList;
    }

    public static StationData processStationLine(String thisLine){

        StationData sd = new StationData();
        sd.setId(thisLine.substring(0,11));
        sd.setLatitude(Float.valueOf(thisLine.substring(12,20).trim()));
        sd.setLongitude(Float.valueOf(thisLine.substring(21,30).trim()));
        sd.setElevation(Float.valueOf(thisLine.substring(31,37).trim()));
        sd.setState(thisLine.substring(38,40));
        sd.setName(thisLine.substring(41,71));
        return sd;
    }

    private static WeatherData[] searchMax(WeatherData[] resultSet, ArrayList<WeatherData> weatherData) {
        /**can I sort nulls to the front?*/
        for(WeatherData i : weatherData){
            if(resultSet[0]==null){
                resultSet[0]=i;
                resultSet = sortList(resultSet);
            }else {
                if(i.compareTo(resultSet[0])>0){
                    resultSet[0] = i;
                    resultSet = sortList(resultSet);
                }
            }
        }
        resultSet = sortList(resultSet);
        return resultSet;
    }

    private static WeatherData[] sortList(WeatherData[] resultSet) {
//        ArrayList<WeatherData> tempList = new ArrayList<>();
//        for(int i = 0;i<resultSet.length;i++){
//            tempList.add(resultSet[i]);
//        }
//        Collections.sort(tempList);
//        WeatherData[] sortedSet = new WeatherData[5];
//        sortedSet = tempList.toArray(sortedSet);
        for(int i = 0; i < resultSet.length-1;i++){
            if (resultSet[i].compare(resultSet[i+1])>=0){
                WeatherData promote = resultSet[i];
                WeatherData demote = resultSet[i+1];
                resultSet[i] = demote;
                resultSet[i+1]= promote;
            }
        }

        return resultSet;
    }
}


