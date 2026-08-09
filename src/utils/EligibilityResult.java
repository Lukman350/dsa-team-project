package utils;

import java.util.List;

/**
 * Class hasil pengecekan kelayakan (eligibility) seorang mahasiswa
 * untuk mengambil suatu Mata Kuliah, berdasarkan prasyarat yang berlaku.
 *
 * Dipakai sebagai nilai balik (return value) dari Graph.checkEligibility()
 * dan Graph.checkFullEligibility().
 */
public class EligibilityResult {
    private String courseCode;          // Kode mata kuliah yang dicek
    private boolean eligible;           // true jika semua prasyarat sudah dipenuhi
    private List<String> missingPrerequisites; // Daftar kode prasyarat yang BELUM dipenuhi

    public EligibilityResult(String courseCode, boolean eligible, List<String> missingPrerequisites) {
        this.courseCode = courseCode;
        this.eligible = eligible;
        this.missingPrerequisites = missingPrerequisites;
    }

    // Getters
    public String getCourseCode() { return courseCode; }
    public boolean isEligible() { return eligible; }
    public List<String> getMissingPrerequisites() { return missingPrerequisites; }

    @Override
    public String toString() {
        if (eligible) {
            return "Mata kuliah " + courseCode + " -> BOLEH diambil (semua prasyarat terpenuhi).";
        } else {
            return "Mata kuliah " + courseCode + " -> BELUM BOLEH diambil. "
                    + "Prasyarat yang belum dipenuhi: " + missingPrerequisites;
        }
    }
}
