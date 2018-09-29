public class WeatherData {
    private String id;
    private int year;
    private int month;
    private int day;
    private String element;
    private int value;
    private String qflag;

//    public WeatherData() {
//    }

    public WeatherData(String id, int year, int month, int day, String element, int value, String qflag) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.element = element;
        this.value = value;
        this.qflag = qflag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getQflag() {
        return qflag;
    }

    public void setQflag(String qflag) {
        this.qflag = qflag;
    }
}

