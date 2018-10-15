public class WeatherData implements Comparable<WeatherData>{
    private String id;
    private int year;
    private int month;
    private int day;
    private String element;
    private int value;
    private String qflag;

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

    /**
     * Returns string value to Assignment 2 specifications
     * (ex. id=USC00042319 year=2005 month=7 day=20 element=TMAX value=53.9C qflag=)
     * */
    public String toString(){
        return String.format("id=%s year=%s month=%s day=%s element=%s value=%s qflag="
                ,this.getId(), this.getYear(), this.getMonth(), this.getDay(),this.getElement(),this.getValue());
    }

    @Override
    public int compareTo(WeatherData o2) {
        if (o2 == null) {
            return  0;
        }
        if (this.value > o2.value) {
            return 1;
        }
        if ( this.value < o2.value) {
            return -1;
        }
        return 0;
    }

    public int compare(WeatherData o2) {
        if (o2 == null) {
            return  0;
        }
        if (this.value > o2.value) {
            return 1;
        }
        if ( this.value < o2.value) {
            return -1;
        }
        return 0;
    }
}

