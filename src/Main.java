import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){

        UserInterface userInput = new UserInterface();
//        File userDirectory = new File(System.getProperty("user.dir"));
        File stationInputFile = new File("stations.txt");
        ArrayList<String> stationIntputData = readFileInput(stationInputFile);
        ArrayList<StationData> stationList = new ArrayList<>();
        for(String i : stationIntputData){
            stationList.add(processStationLine(i));
        }

        File weatherInputFile = new File("ghcnd_hcn/USC00011084.dly");
        ArrayList<String > weatherInputData = readFileInput(weatherInputFile);
        ArrayList<WeatherData> weatherList = new ArrayList<>();
        for(String i : weatherInputData){
            processWeatherLine(i);
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

    public static void processWeatherLine(String thisLine){
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
        System.out.println(weatherDataList);
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

}

