package me.niotgg.configuration;

public class Config {


    private String input = "";
    private String output = "";
    private Boolean inOnlyOcr = false;


    public Config(String input, String output, Boolean inOnlyOcr) {
        this.input = input;
        this.output = output;
        this.inOnlyOcr = inOnlyOcr;
    }


    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Boolean getInOnlyOcr() {
        return inOnlyOcr;
    }

    public void setInOnlyOcr(Boolean inOnlyOcr) {
        this.inOnlyOcr = inOnlyOcr;
    }
}
