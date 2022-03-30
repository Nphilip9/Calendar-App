package com.nphilip.calendar.listView;

public class Task {

    String title, description, date;
    int importance;
    boolean isProtected, isDone;

    /**
     * Main constructor
     * @param title String
     * @param description String
     * @param date String
     * @param importance Integer
     * @param isProtected Boolean
     * @param isDone Boolean
     */
    public Task(String title, String description, String date, int importance, boolean isProtected, boolean isDone) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.importance = importance;
        this.isProtected = isProtected;
        this.isDone = isDone;
    }

    /**
     * Returns the title
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     * @param title String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description
     * @return String
     */
    public String getContent() {
        return description;
    }

    /**
      * Sets the description
     * @param description String
     */
    public void setContent(String description) {
        this.description = description;
    }

    /**
     * Returns the date
     * @return String
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date
     * @param date String
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the importance
     * @return Integer
     */
    public int getImportance() {
        return importance;
    }

    /**
     * Sets the importance
     * @param importance Integer
     */
    public void setImportance(int importance) {
        this.importance = importance;
    }

    /**
     * Returns the protected state
     * @return Boolean
     */
    public boolean isProtected() {
        return isProtected;
    }

    /**
     * Sets the protected state
     * @param isProtected Boolean
     */
    public void setProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    /**
     * Returns the done state
     * @return Boolean
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Sets the done state
     * @param done Boolean
     */
    public void setDone(boolean done) {
        isDone = done;
    }
}
