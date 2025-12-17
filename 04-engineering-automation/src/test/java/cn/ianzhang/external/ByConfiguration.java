package cn.ianzhang.external;

/**
 * @Author: Ian
 * @Description:
 */
public class ByConfiguration {
    private String directory;
    private String[] extensions;
    private Boolean recursive;

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public void setExtensions(String[] extensions) {
        this.extensions = extensions;
    }

    public Boolean getRecursive() {
        return recursive;
    }

    public void setRecursive(Boolean recursive) {
        this.recursive = recursive;
    }
}
