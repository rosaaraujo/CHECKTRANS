package es.araujo.checktrans.dto;

public class ChecklistHeaderValueDTO {

    private Long id;
    private Long headerId;
    private String headerCode;
    private String headerLabel;
    private String headerType;
    private String value;
    private Integer headerOrder;

    public ChecklistHeaderValueDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getHeaderCode() {
        return headerCode;
    }

    public void setHeaderCode(String headerCode) {
        this.headerCode = headerCode;
    }

    public String getHeaderLabel() {
        return headerLabel;
    }

    public void setHeaderLabel(String headerLabel) {
        this.headerLabel = headerLabel;
    }

    public String getHeaderType() {
        return headerType;
    }

    public void setHeaderType(String headerType) {
        this.headerType = headerType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getHeaderOrder() {
        return headerOrder;
    }

    public void setHeaderOrder(Integer headerOrder) {
        this.headerOrder = headerOrder;
    }
}
