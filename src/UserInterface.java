import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;

public class UserInterface {

    private static int year_start;
    private static int year_end;
    private static int month_start;
    private static int month_end;
    private static boolean minimum = false;
    private static boolean maximum = false;

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
        UserInterface.year_start = year_start;
    }

    public int getYear_end() {
        return year_end;
    }

    public void setYear_end(int year_end) {
        UserInterface.year_end = year_end;
    }

    public int getMonth_start() {
        return month_start;
    }

    public void setMonth_start(int month_start) {
        UserInterface.month_start = month_start;
    }

    public int getMonth_end() {
        return month_end;
    }

    public void setMonth_end(int month_end) {
        UserInterface.month_end = month_end;
    }

    public boolean isMinimum() {
        return minimum;
    }

    public void setMinimum(boolean minimum) {
        UserInterface.minimum = minimum;
    }

    public boolean isMaximum() {
        return maximum;
    }

    public void setMaximum(boolean maximum) {
        UserInterface.maximum = maximum;
    }


}
