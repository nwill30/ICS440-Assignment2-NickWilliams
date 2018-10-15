import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;

public class UserInterface {

    private int year_start;
    private int year_end;
    private int month_start;
    private int month_end;
    private boolean minimum = false;
    private boolean maximum = false;

    /**
     * UserInterface begins with a prompt to the user briefly explaining the application design
     * each class attribute is set by it's accompanying console interface
     * The reader and inputstream reader are closed before returning control back to the application.
     * */
    public UserInterface() throws IOException {
        System.out.println("This application will display the top/bottom five temperatures around the world from a given period.");
        System.out.println("Please provide a start and end year/month and weather you'd like the min or max temps for that period.");
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader reader =
                new BufferedReader(inputStreamReader);
        try {
            setYearStartConsole(reader);
            setYearEndConsole(reader);
            setMonthStartConsole(reader);
            setMonthEndConsole(reader);
            setQueryFilter(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            reader.close();
            inputStreamReader.close();
        }
    }

    /**
     * Min/MaxYear values are hard coded to match the years available in the Weather files
     * For the start year the user need only enter a valid year between minx/max
     * If an invalid value is entered the user is notified and asked to re-enter a value.
     * @param reader
     * */
    private void setYearStartConsole(BufferedReader reader) throws IOException {
        int minYear = 1890;
        int maxYear = 2018;
        System.out.println(String.format("Enter a year between %s and %s you'd like to begin your search: ",minYear,maxYear));

        String inputValue = reader.readLine();
        try{
            if(Integer.parseInt(inputValue) <minYear|| Integer.parseInt(inputValue)>maxYear){
                throw new InputMismatchException();
            }else{
                this.year_start = Integer.parseInt(inputValue);
            }
        }catch (InputMismatchException IME){
            System.out.println(String.format("Input must be valid integer between %s-%s",minYear,maxYear));
            setYearStartConsole(reader);
        }
    }

    /**
     * The minYear is determined by the startYear entered previously by the user
     * The max year need only be greater than the startYear
     * If an invalid value is entered the user is notified and asked to re-enter a value.
     * @param reader
     * */
    private void setYearEndConsole(BufferedReader reader) throws IOException {
        int minYear = this.year_start;
        int maxYear = 2018;
        System.out.println(String.format("Enter a year between %s and %s you'd like to end your search: ",minYear,maxYear));

        String inputValue = reader.readLine();
        try{
            if(Integer.parseInt(inputValue) <minYear|| Integer.parseInt(inputValue)>maxYear){
                throw new InputMismatchException();
            }else{
                this.year_end = Integer.parseInt(inputValue);
            }
        }catch (InputMismatchException IME){
            System.out.println(String.format("Input must be valid integer between %s-%s",minYear,maxYear));
            setYearEndConsole(reader);
        }
    }

    /**
     * Any value 1-12 is valid for the start month
     * If an invalid value is entered the user is notified and asked to re-enter a value.
     * @param reader
     * */
    private void setMonthStartConsole(BufferedReader reader) throws IOException {
        int minMonth = 1;
        int maxMonth = 12;
        System.out.println(String.format("Enter a month between %s and %s you'd like to begin your search: ",minMonth,maxMonth));

        String inputValue = reader.readLine();
        try{
            if(Integer.parseInt(inputValue) <minMonth|| Integer.parseInt(inputValue)>maxMonth){
                throw new InputMismatchException();
            }else{
                this.month_start = Integer.parseInt(inputValue);
            }
        }catch (InputMismatchException IME){
            System.out.println(String.format("Input must be valid integer between %s-%s",minMonth,maxMonth));
            setMonthStartConsole(reader);
        }
    }

    /**
     * The user may enter any value 1-12 provided that if  startYear = endYear the startMonth >= endMonth
     * If an invalid value is entered the user is notified and asked to re-enter a value.
     * @param reader
     * */
    private void setMonthEndConsole(BufferedReader reader) throws IOException {
        int minMonth = 1;
        int maxMonth = 12;
        System.out.println(String.format("Enter a year between %s and %s you'd like to end your search: ",minMonth,maxMonth));

        String inputValue = reader.readLine();
        try{
            if(Integer.parseInt(inputValue) <minMonth|| Integer.parseInt(inputValue)>maxMonth){
                throw new InputMismatchException();
            }else if(this.year_start == this.year_end && Integer.parseInt(inputValue)<this.month_start){
                throw new InputMismatchException();
            }else{
                this.month_end = Integer.parseInt(inputValue);
            }
        }catch (InputMismatchException IME){
            System.out.println(String.format("Input must be valid integer between %s-%s",minMonth,maxMonth));
            System.out.println("Your end month must falls after your start month");
            setMonthEndConsole(reader);
        }
    }

    /**
     * The user may enter a value of 0/1
     * 0 Retrieves the top 5 TMIN values for the provided date range
     * 1 Retrieves the top 5 TMAX values for the provided date range
     * If an invalid value is entered the user is notified and asked to re-enter a value.
     * @param reader
     * */
    private void setQueryFilter(BufferedReader reader) throws IOException {
        System.out.println("Choose desired result for request range.");
        System.out.println("Enter 0 to retrieve the bottom 5 temperatures for the request range.");
        System.out.println("Enter 1 to retrieve the top 5 temperatures for the request range.");

        String inputValue = reader.readLine();
        try{
            if(Integer.parseInt(inputValue)<0|| Integer.parseInt(inputValue)>1){
                throw new InputMismatchException();
            }else if(Integer.parseInt(inputValue)==0){
                this.minimum = true;
            }else if(Integer.parseInt(inputValue)==1){
                this.maximum = true;
            }else {
                throw new InputMismatchException();
            }
        }catch (InputMismatchException IME){
            System.out.println("Input must be a 0 or 1");
            setQueryFilter(reader);
        }
    }

    public int getYear_start() {
        return year_start;
    }

    public void setYear_start(int year_start) {
        this.year_start = year_start;
    }

    public int getYear_end() {
        return year_end;
    }

    public void setYear_end(int year_end) {
        this.year_end = year_end;
    }

    public int getMonth_start() {
        return month_start;
    }

    public void setMonth_start(int month_start) {
        this.month_start = month_start;
    }

    public int getMonth_end() {
        return month_end;
    }

    public void setMonth_end(int month_end) {
        this.month_end = month_end;
    }

    public boolean isMinimum() {
        return minimum;
    }

    public void setMinimum(boolean minimum) {
        this.minimum = minimum;
    }

    public boolean isMaximum() {
        return maximum;
    }

    public void setMaximum(boolean maximum) {
        this.maximum = maximum;
    }


}
