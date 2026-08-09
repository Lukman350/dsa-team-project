package domain;

/**
 * Class Mata Kuliah
 */
public class Course {
    private String code;
    private String name; 
    private int sks;     

    // Constructor
    public Course(String code, String name, int sks) {
        this.code = code;
        this.name = name;
        this.sks = sks;
    }

    // Getters
    public String getCode() { return code; }
    public String getName() { return name; }
    public int getSks() { return sks; }

    @Override
    public String toString() {
        return code + " - " + name + " (" + sks + " SKS)";
    }
}