import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ProcessWeatherList implements Callable {
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

    /**
     * Callable method begins by setting process to true to loop
     * This will loop through all values in weatherData Array without accessing the shared object
     * A local array newResultSet is created to house returned top processed values
     * The next record to be processed is accessed through the multi-thread friendly getNext method
     * if the getNext returns null process is set to false thus exiting the loop
     * any other value is returned processed as normal
     * */
    public WeatherData[] call() throws Exception {
        boolean process = true;
        WeatherData[] newResultSet = new WeatherData[5];
        while(process){
            WeatherData nextRecord = getNext();
            if(nextRecord == null){
                process = false;
            }else  {
                    newResultSet = getResults(nextRecord,newResultSet);
            }
        }
        return newResultSet;
    }

    /**
     * A local WeatherData object is created and set to null before accessing the shared array
     * The array is locked and the atomic integer is validated to be less than the size of the array
     * If the index is not out of bounds the next value in the array is set to the created object
     * the index is incremented on return
     * The array is unlocked and the value is returned
     * */
    private WeatherData getNext() {
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

    /**
     * The newRecord and array of existing records are passed to be processed
     * The newRecord is used to search the existing records to see if it a top 5
     * The search method is (min/max) is determined by the userInput
     * @param newResultSet
     * @param weatherRecord
     * */
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

    /**
     * The search max checks the incoming value against the existing records
     * If the first record is null the new record is added to the array and sorted
     * if the first record is not null and the new record is greater it is replaced and sorted
     * @param weatherRecord
     * @param newResultSet
     * */
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
        return newResultSet;
    }

    /**
     * The search min checks the incoming value against the existing records
     * If the first record is null the new record is added to the array and sorted
     * if the first record is not null and the new record is lesser it is replaced and sorted
     * @param weatherRecord
     * @param newResultSet
     * */
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
        return newResultSet;
    }

    /**
     * The sortList method receives a 1/0 value from the calling record determining mix/max order sort
     * In either case the array is order by checking each index value against all following values
     * If a comparedTo index is null the value returned is a 0 and it is replaced
     * @param newResultSet
     * @param sortMinMax
     * */
    public static WeatherData[] sortList( int sortMinMax, WeatherData[] newResultSet) {

        switch (sortMinMax){
            case 1 :
                for(int i = 0; i < newResultSet.length-1;i++){
                    int j = i;
                    while (j+1<newResultSet.length && newResultSet[j] !=null && newResultSet[j].compare(newResultSet[j+1])>=0){
                        WeatherData promote = newResultSet[j];
                        WeatherData demote = newResultSet[j+1];
                        newResultSet[j] = demote;
                        newResultSet[j+1]= promote;
                        j++;
                    }
                }
                break;
            case 0 :
                for(int i = 0; i < newResultSet.length-1;i++){
                    int j = i;
                    while (j+1<newResultSet.length && newResultSet[j] !=null && newResultSet[j].compare(newResultSet[j+1])<=0){
                        WeatherData promote = newResultSet[j];
                        WeatherData demote = newResultSet[j+1];
                        newResultSet[j] = demote;
                        newResultSet[j+1]= promote;
                        j++;
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
