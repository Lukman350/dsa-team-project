/**
 * Class Mahasiswa
 */
public class Student {
    private String nim;
    private String name;
    private double ipk;

    // Constructor
    public Student(String nim, String name, double ipk) {
        this.nim = nim;
        this.name = name;
        this.ipk = ipk;
    }

    // Getters
    public String getNim() { return nim; }
    public String getName() { return name; }
    public double getIpk() { return ipk; }

    // Setters (if needed later for editing data)
    public void setName(String name) { this.name = name; }
    public void setIpk(double ipk) { this.ipk = ipk; }

    @Override
    public String toString() {
        return "NIM: " + nim + " | Name: " + name + " | IPK: " + ipk;
    }
}