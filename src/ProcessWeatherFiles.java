import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ProcessWeatherFiles implements Callable {
    private WeatherData[] resultSet = new WeatherData[5];
    private UserInterface userInput;
    private String processString;


    public ProcessWeatherFiles(UserInterface userInput, String processString) {
        this.userInput = userInput;
        this.processString = processString;
    }


    public WeatherData[] call() throws Exception {

        ArrayList<WeatherData> processList = processWeatherFile(processString, userInput);
        return getResults(processList, resultSet);
    }

    private WeatherData[] getResults(ArrayList<WeatherData> processList, WeatherData[] resultSet) {
        if(!(processList == null)){
            if(userInput.isMaximum()){
                return resultSet =searchMax(resultSet, processList);
            }else if(userInput.isMinimum()){
                return resultSet = searchMin(resultSet, processList);
            }
        }
        return resultSet;
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
        if(userInput.isMinimum()&& !element.equals("TMIN")){
                return null;
        }else if(userInput.isMaximum() && !element.equals("TMAX")){
            return null;
        }

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

    public static WeatherData[] searchMax(WeatherData[] resultSet, ArrayList<WeatherData> weatherData) {
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

    public static WeatherData[] searchMin(WeatherData[] resultSet, ArrayList<WeatherData> weatherData) {
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

    public static WeatherData[] sortList(WeatherData[] resultSet, int sortMinMax) {

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

    public WeatherData[] getResultSet() {
        return resultSet;
    }

    public void setResultSet(WeatherData[] resultSet) {
        this.resultSet = resultSet;
    }

    public UserInterface getUserInput() {
        return userInput;
    }

    public void setUserInput(UserInterface userInput) {
        this.userInput = userInput;
    }


}
