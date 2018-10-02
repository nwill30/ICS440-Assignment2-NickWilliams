import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Main {

    public static void main(String[] args){
        WeatherData[] resultSet = new WeatherData[5];
        UserInterface userInput = null;
        try {
            userInput = new UserInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File stationInputFile = new File("stations.txt");
        ArrayList<String> stationInputData = readFileInput(stationInputFile);
        HashMap<String, StationData> stationMap = new HashMap<>();
        for(String i : stationInputData){
            StationData newStation = processStationLine(i);
            stationMap.put(newStation.getId(),newStation);
        }
        File dir = new File("ghcnd_hcn");
        File[] weatherInputFile = dir.listFiles();
        if(weatherInputFile != null ){
            for(File file : weatherInputFile){
                for(String i : readFileInput(file)){
                    ArrayList<WeatherData> processList = processWeatherFile(i, userInput);
                    if(!(processList == null)){
                        if(userInput.isMaximum()){
                            resultSet =searchMax(resultSet, processList);
                        }else if(userInput.isMinimum()){
                            resultSet = searchMin(resultSet, processList);
                        }
                    }
                }
            }
        }


        for(WeatherData i : resultSet){
            System.out.println(i.toString());
//            System.out.println(stationMap.get(i.getId()).toString());
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

    public static ArrayList<WeatherData> processWeatherFile(String thisLine, UserInterface userInput){
        ArrayList<WeatherData> weatherDataList = new ArrayList<>();
        String id = thisLine.substring(0,11);
        int year = Integer.valueOf(thisLine.substring(11,15).trim());
        int month = Integer.valueOf(thisLine.substring(15,17).trim());
        String element = thisLine.substring(17,21);
        int days = (thisLine.length() - 21) / 8; // Calculate the number of days in the line
        if(year < userInput.getYear_start() || (year == userInput.getYear_start() && month < userInput.getMonth_start())){
            return null;
        }else if(year > userInput.getYear_end() || (year == userInput.getYear_end() && month > userInput.getMonth_end())){
            return null;
        }else{
            for (int i = 0; i < days; i++) {         // Process each day in the line.
                int value = Integer.valueOf(thisLine.substring(21+8*i,26+8*i).trim());
                String qflag = thisLine.substring(27+8*i,28+8*i);
                WeatherData wd = new WeatherData(id,year,month,i+1,element,value,qflag);
                weatherDataList.add(wd);
            }
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
        int sortMinMax = 1;
        /**can I sort nulls to the front?*/
        for(WeatherData i : weatherData){
            if(resultSet[0]==null){
                resultSet[0]=i;
                resultSet = sortList(resultSet, sortMinMax);
            }else {
                if(i.compareTo(resultSet[0])>0){
                    resultSet[0] = i;
                    resultSet = sortList(resultSet, sortMinMax);
                }
            }
        }
        resultSet = sortList(resultSet,sortMinMax);
        return resultSet;
    }

    private static WeatherData[] searchMin(WeatherData[] resultSet, ArrayList<WeatherData> weatherData) {
        int sortMinMax = 0;
        /**can I sort nulls to the front?*/
        for(WeatherData i : weatherData){
            if(resultSet[0]==null){
                resultSet[0]=i;
                resultSet = sortList(resultSet, sortMinMax);
            }else {
                if(i.compareTo(resultSet[0])<0){
                    resultSet[0] = i;
                    resultSet = sortList(resultSet, sortMinMax);
                }
            }
        }
        resultSet = sortList(resultSet,sortMinMax);
        return resultSet;
    }

    private static WeatherData[] sortList(WeatherData[] resultSet, int sortMinMax) {

        switch (sortMinMax){
            case 1 :
                for(int i = 0; i < resultSet.length-1;i++){
                    if (resultSet[i].compare(resultSet[i+1])>=0){
                        WeatherData promote = resultSet[i];
                        WeatherData demote = resultSet[i+1];
                        resultSet[i] = demote;
                        resultSet[i+1]= promote;
                    }
                }
                break;
            case 0 :
                for(int i = 0; i < resultSet.length-1;i++){
                    if (resultSet[i].compare(resultSet[i+1])<=0){
                        WeatherData promote = resultSet[i];
                        WeatherData demote = resultSet[i+1];
                        resultSet[i] = demote;
                        resultSet[i+1]= promote;
                    }
                }
                break;
        }



        return resultSet;
    }
}


