package com.kakaopay.housingfund.fund.model.api.response.institute;

public class InstituteResponse {

    private final String instituteCode;
    private final String instituteName;
    private final String attribute;

    public InstituteResponse(String instituteCode, String instituteName, String attribute) {
        this.instituteCode = instituteCode;
        this.instituteName = instituteName;
        this.attribute = attribute;
    }

    public String getInstituteCode() {
        return instituteCode;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public String getAttribute() {
        return attribute;
    }
}
