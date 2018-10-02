public  class StationData {
    private String id;
    private float latitude;
    private float longitude;
    private float elevation;
    private String state;
    private String name;

    public StationData() {
    }

    public StationData(String id, float latitude, float longitude, float elevation, String state, String name) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
        this.state = state;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * id=USC00042319 latitude=36.4622 longitude=-116.8669 elevation=-59.1 state=CA name=DEATH VALLEY

     * */
    public String toString(){
        return String.format("id=%s latitude=%s longitude=%s elevation=%s state=%s name=%s "
                ,this.getId(), this.getLatitude(), this.getLongitude(), this.getElevation(),this.getState(),this.getName());
    }
}
