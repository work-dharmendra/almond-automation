package com.dhar.automation.dto;

import com.dhar.automation.common.SortOrder;

/**
 * @author Dharmendra.Singh
 */
public class PaginationDTO {
    public PaginationDTO() {
    }

    public PaginationDTO(int start, int pageSize, long count) {
        this.start = start;
        this.pageSize = pageSize;
        this.count = count;
    }

    public int start;
    public int pageSize;
    public String searchTerm;
    public SortOrder order = SortOrder.ASC;
    public long count;


    public PaginationDTO(int start, int pageSize, SortOrder sortOrder) {
        this.start = start;
        this.pageSize = pageSize;
        this.order = sortOrder;
    }
}
