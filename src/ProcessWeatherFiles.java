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


    /**
     * the Callable method uses the obejct attributes to process the WeatherFile to a List of weather data
     * the list is then condensed to the top 5 and returned via getResults
     * */
    public WeatherData[] call() throws Exception {

        ArrayList<WeatherData> processList = processWeatherFile(processString, userInput);
        return getResults(processList, resultSet);
    }

    /**
     * The newRecord and array of existing records are passed to be processed
     * The newRecord is used to search the existing records to see if it a top 5
     * The search method is (min/max) is determined by the userInput
     * @param processList
     * @param resultSet
     * */
    private WeatherData[] getResults(ArrayList<WeatherData> processList, WeatherData[] resultSet) {
        if(!(processList == null)){
            if(userInput.isMaximum()){
                return searchMax(resultSet, processList);
            }else if(userInput.isMinimum()){
                return searchMin(resultSet, processList);
            }
        }
        return resultSet;
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

    /**
     * A string containing the record to process and the desired outcome (userInput) are received
     * The record is parsed by string index values and the element from the parsed record is checked against desired outcome
     * TMIN/TMAX if the element does not match the record is not processed
     * If the record date falls outside of the userInput date range the record is not processed
     * If the record satisfies the pre-conditions a new WeatherData object is created and added to the WeatherdataList]
     * @param userInput
     * @param thisLine
     * */
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

    /**
     * The search max checks the incoming value against the existing records
     * If the first record is null the new record is added to the array and sorted
     * if the first record is not null and the new record is greater it is replaced and sorted
     * @param resultSet
     * @param weatherData
     * */
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

    /**
     * The search min checks the incoming value against the existing records
     * If the first record is null the new record is added to the array and sorted
     * if the first record is not null and the new record is lesser it is replaced and sorted
     * @param weatherData
     * @param resultSet
     * */
    public static WeatherData[] searchMin(WeatherData[] resultSet, ArrayList<WeatherData> weatherData) {
        int sortMinMax = 0;
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

    /**
     * The sortList method receives a 1/0 value from the calling record determining mix/max order sort
     * In either case the array is order by checking each index value against all following values
     * If a comparedTo index is null the value returned is a 0 and it is replaced
     * @param resultSet
     * @param sortMinMax
     * */
    public static WeatherData[] sortList(WeatherData[] resultSet, int sortMinMax) {

        switch (sortMinMax){
            case 1 :
                for(int i = 0; i < resultSet.length-1;i++){
                    int j = i;
                    while (j+1<resultSet.length && resultSet[j] !=null && resultSet[j].compare(resultSet[j+1])>=0){
                        WeatherData promote = resultSet[j];
                        WeatherData demote = resultSet[j+1];
                        resultSet[j] = demote;
                        resultSet[j+1]= promote;
                        j++;
                    }
                }
                break;
            case 0 :
                for(int i = 0; i < resultSet.length-1;i++){
                    int j = i;
                    while (j+1<resultSet.length && resultSet[j] !=null && resultSet[j].compare(resultSet[j+1])<=0){
                        WeatherData promote = resultSet[j];
                        WeatherData demote = resultSet[j+1];
                        resultSet[j] = demote;
                        resultSet[j+1]= promote;
                        j++;
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
