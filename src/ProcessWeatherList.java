import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessWeatherList implements Callable {
//    private final WeatherData[] compareAray;
    private static List<WeatherData> weatherData;
    private static ReentrantLock weatherDataLock = new ReentrantLock();
    private static UserInterface userInput;
    private static AtomicInteger index = new AtomicInteger(0);
    private static int countdown = 0;

    public ProcessWeatherList(UserInterface userInput, List<WeatherData> weatherDataList) {
        this.userInput = userInput;
        this.weatherData = weatherDataList;
        this.countdown = weatherDataList.size();
    }

    public WeatherData[] call() throws Exception {
        boolean process = true;
        WeatherData[] newResultSet = new WeatherData[5];
        while(process){
            WeatherData nextRecord = getNextAndRemove();
            if(nextRecord == null){
                process = false;
            }else  {
                    newResultSet = getResults(nextRecord,newResultSet);
            }
        }
        return newResultSet;
    }

    private WeatherData getNextAndRemove() {
        WeatherData returnArray = null;
        weatherDataLock.lock();
        try{
            if(index.get()<weatherData.size()){
                returnArray = weatherData.get(index.getAndIncrement());
            }
        }finally {
            weatherDataLock.unlock();
        }
        return  returnArray;
    }

    private WeatherData[] getResults(WeatherData weatherRecord, WeatherData[] newResultSet) {
            if(userInput.isMaximum()){
                searchMax(weatherRecord,newResultSet);
                return newResultSet;
            }else if(userInput.isMinimum()){
                searchMin( weatherRecord,newResultSet);
                return newResultSet;
            }

        return newResultSet;
    }

    public static WeatherData[] searchMax(WeatherData weatherRecord, WeatherData[] newResultSet) {
        int sortMinMax = 1;
        /**can I sort nulls to the front?*/
            if(newResultSet[0]==null){
                newResultSet[0]=weatherRecord;
                newResultSet = sortList(sortMinMax, newResultSet);
            }else {
                if(weatherRecord.compareTo(newResultSet[0])>0){
                    newResultSet[0] = weatherRecord;
                    newResultSet = sortList(sortMinMax, newResultSet);
                }
            }
//        resultSet = sortList(resultSet,sortMinMax);
        return newResultSet;
    }

    public static WeatherData[] searchMin(WeatherData weatherRecord, WeatherData[] newResultSet) {
        int sortMinMax = 0;
        /**can I sort nulls to the front?*/
            if(newResultSet[0]==null){
                newResultSet[0]=weatherRecord;
                newResultSet = sortList(sortMinMax, newResultSet);
            }else {
                if(weatherRecord.compareTo(newResultSet[0])<0){
                    newResultSet[0] = weatherRecord;
                    newResultSet = sortList(sortMinMax, newResultSet);
                }
            }

//        resultSet = sortList(resultSet,sortMinMax);
        return newResultSet;
    }

    public static WeatherData[] sortList( int sortMinMax, WeatherData[] newResultSet) {

        switch (sortMinMax){
            case 1 :
                for(int i = 0; i < newResultSet.length-1;i++){
                    if (newResultSet[i].compare(newResultSet[i+1])>=0){
                        WeatherData promote = newResultSet[i];
                        WeatherData demote = newResultSet[i+1];
                        newResultSet[i] = demote;
                        newResultSet[i+1]= promote;
                    }
                }
                break;
            case 0 :
                for(int i = 0; i < newResultSet.length-1;i++){
                    if (newResultSet[i].compare(newResultSet[i+1])<=0){
                        WeatherData promote = newResultSet[i];
                        WeatherData demote = newResultSet[i+1];
                        newResultSet[i] = demote;
                        newResultSet[i+1]= promote;
                    }
                }
                break;
        }
        return newResultSet;
    }

    public UserInterface getUserInput() {
        return userInput;
    }

    public void setUserInput(UserInterface userInput) {
        this.userInput = userInput;
    }


}
