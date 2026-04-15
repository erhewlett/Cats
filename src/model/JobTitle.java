package model;

/**
 * JobTitle.java
 * Team: Cats | CSc3350 SP2026
 *
 * Represents one row from the job_titles table.
 * Maps a numeric ID to a job title string.
 *
 * Example: job_title_id=102, job_title="software engineer"
 *
 * Why a separate table? So "software engineer" is stored once
 * and referenced by ID — same reason cities/states are separate.
 * If the title changes company-wide you update one row, not 50.
 */
public class JobTitle {

    private int    jobTitleId;
    private String jobTitle;

    // ----------------------------------------------------------------
    // CONSTRUCTORS
    // ----------------------------------------------------------------

    public JobTitle() {}

    public JobTitle(int jobTitleId, String jobTitle) {
        this.jobTitleId = jobTitleId;
        this.jobTitle   = jobTitle;
    }

    // ----------------------------------------------------------------
    // GETTERS & SETTERS
    // ----------------------------------------------------------------
    public int    getJobTitleId()             { return jobTitleId; }
    public String getJobTitle()               { return jobTitle; }
    public void   setJobTitleId(int id)       { this.jobTitleId = id; }
    public void   setJobTitle(String title)   { this.jobTitle = title; }

    @Override
    public String toString() {
        return String.format("%-4d | %s", jobTitleId, jobTitle);
    }
}
