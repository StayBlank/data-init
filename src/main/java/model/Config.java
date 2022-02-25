package model;


import java.util.List;

public class Config {
    private String output;
    private int number;
    private List<FieldConfig> fieldConfig;

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<FieldConfig> getFieldConfig() {
        return fieldConfig;
    }

    public void setFieldConfig(List<FieldConfig> fieldConfig) {
        this.fieldConfig = fieldConfig;
    }

    public static class FieldConfig {
        private String name;
        private String type;
        private String clazz;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getClazz() {
            return clazz;
        }

        public void setClazz(String clazz) {
            this.clazz = clazz;
        }
    }

}
