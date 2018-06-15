package sales.itemsforsale;

public enum CarManufacturer {

    VW, BMW, AUDI, OPEL;

    public String getFormatString() {
        int longestNameLength = 0;
        for (CarManufacturer m : values())
            longestNameLength = Math.max(longestNameLength, m.toString().length());
        return "%-" + longestNameLength + "s";
    }
}
