package cn.ianzhang.calculator;

/**
 * 单条计算用例的数据模型，用于 JSON/YAML 等数据驱动。
 */
public class CalculationCase {

    private String description;
    private String leftOperand;
    private String rightOperand;
    private String operator;
    private String expectedResult;
    private String expectedError;
    private CaseType caseType = CaseType.NORMAL;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(String leftOperand) {
        this.leftOperand = leftOperand;
    }

    public String getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(String rightOperand) {
        this.rightOperand = rightOperand;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getExpectedError() {
        return expectedError;
    }

    public void setExpectedError(String expectedError) {
        this.expectedError = expectedError;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    public enum CaseType {
        NORMAL,
        INPUT_ERROR,
        DIVISION_BY_ZERO,
        OTHER_ERROR
    }
}

