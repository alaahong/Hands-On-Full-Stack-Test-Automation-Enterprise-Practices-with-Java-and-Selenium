package cn.ianzhang.calculator;

import java.util.List;

/**
 * 一组计算用例的集合，对应 JSON/YAML 顶层结构。
 */
public class CalculationSuite {

    private String suiteName;
    private List<CalculationCase> cases;

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public List<CalculationCase> getCases() {
        return cases;
    }

    public void setCases(List<CalculationCase> cases) {
        this.cases = cases;
    }
}

