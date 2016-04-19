package edu.csula.datascience.acquisition;

/**
 * Mock raw data
 */
public class MockData {
    private final String id;
    private final String content;

    public MockData(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
