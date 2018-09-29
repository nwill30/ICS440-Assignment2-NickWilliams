import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterface {

    private static int year_start;
    private static int year_end;
    private static int month_start;
    private static int month_end;
    private static boolean minimum = false;
    private static boolean maximum = false;

    public UserInterface(){
        System.out.println("This application will display the top/bottom five temperatures around the world from a given period.");
        System.out.println("Please provide a start and end year/month and weather you'd like the min or max temps for that period.");
        setYearStartConsol();
        setYearEndConsol();
        setMonthStartConsol();
        setMonthEndConsol();
        setQueryFilter();

    }

    private void setYearStartConsol(){
        int minYear = 1890;
        int maxYear = 2018;
        System.out.println(String.format("Enter a year between %s and %s you'd like to begin your search: ",minYear,maxYear));
        Scanner reader = new Scanner(System.in);
        try{
            if(reader.nextInt()<minYear||reader.nextInt()>maxYear){
                throw new InputMismatchException();
            }else{
                this.year_start = reader.nextInt();
            }
        }catch (InputMismatchException IME){
            System.out.println(String.format("Input must be valid integer between %s-%s",minYear,maxYear));
            setYearStartConsol();
        }finally {
            reader.close();
        }
    }

    private void setYearEndConsol(){
        int minYear = this.year_start;
        int maxYear = 2018;
        System.out.println(String.format("Enter a year between %s and %s you'd like to end your search: ",minYear,maxYear));
        Scanner reader = new Scanner(System.in);
        try{
            if(reader.nextInt()<minYear||reader.nextInt()>maxYear){
                throw new InputMismatchException();
            }else{
                this.year_end = reader.nextInt();
            }
        }catch (InputMismatchException IME){
            System.out.println(String.format("Input must be valid integer between %s-%s",minYear,maxYear));
            setYearEndConsol();
        }finally {
            reader.close();
        }
    }

    private void setMonthStartConsol(){
        int minMonth = 1;
        int maxMonth = 12;
        System.out.println(String.format("Enter a month between %s and %s you'd like to begin your search: ",minMonth,maxMonth));
        Scanner reader = new Scanner(System.in);
        try{
            if(reader.nextInt()<minMonth||reader.nextInt()>maxMonth){
                throw new InputMismatchException();
            }else{
                this.month_start = reader.nextInt();
            }
        }catch (InputMismatchException IME){
            System.out.println(String.format("Input must be valid integer between %s-%s",minMonth,maxMonth));
            setMonthStartConsol();
        }finally {
            reader.close();
        }
    }

    private void setMonthEndConsol(){
        int minMonth = 1;
        int maxMonth = 12;
        System.out.println(String.format("Enter a year between %s and %s you'd like to end your search: ",minMonth,maxMonth));
        Scanner reader = new Scanner(System.in);
        try{
            if(reader.nextInt()<minMonth||reader.nextInt()>maxMonth){
                throw new InputMismatchException();
            }else if(this.year_start == this.year_end && reader.nextInt()<this.month_start){
                throw new InputMismatchException();
            }else{
                this.month_end = reader.nextInt();
            }
        }catch (InputMismatchException IME){
            System.out.println(String.format("Input must be valid integer between %s-%s",minMonth,maxMonth));
            System.out.println("Your end month must falls after your start month");
            setMonthEndConsol();
        }finally {
            reader.close();
        }
    }

    private void setQueryFilter() {
        System.out.println("Choose desired result for request range.");
        System.out.println("Enter 0 to retrieve the bottom 5 temperatures for the request range.");
        System.out.println("Enter 1 to retrieve the top 5 temperatures for the request range.");
        Scanner reader = new Scanner(System.in);
        try{
            if(reader.nextInt()<0||reader.nextInt()>1){
                throw new InputMismatchException();
            }else if(reader.nextInt()==0){
                this.minimum = true;
            }else if(reader.nextInt()==1){
                this.maximum = true;
            }else {
                throw new InputMismatchException();
            }
        }catch (InputMismatchException IME){
            System.out.println("Input must be a 0 or 1");
            setQueryFilter();
        }finally {
            reader.close();
        }
    }

    public static int getYear_start() {
        return year_start;
    }

    public static void setYear_start(int year_start) {
        UserInterface.year_start = year_start;
    }

    public static int getYear_end() {
        return year_end;
    }

    public static void setYear_end(int year_end) {
        UserInterface.year_end = year_end;
    }

    public static int getMonth_start() {
        return month_start;
    }

    public static void setMonth_start(int month_start) {
        UserInterface.month_start = month_start;
    }

    public static int getMonth_end() {
        return month_end;
    }

    public static void setMonth_end(int month_end) {
        UserInterface.month_end = month_end;
    }

    public static boolean isMinimum() {
        return minimum;
    }

    public static void setMinimum(boolean minimum) {
        UserInterface.minimum = minimum;
    }

    public static boolean isMaximum() {
        return maximum;
    }

    public static void setMaximum(boolean maximum) {
        UserInterface.maximum = maximum;
    }
}
