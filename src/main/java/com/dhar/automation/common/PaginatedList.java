package com.dhar.automation.common;

import java.util.List;

/**
 * @author Dharmendra.Singh
 */
public class PaginatedList<T> {

    public PaginatedList() {
    }

    public PaginatedList(int start, int pageSize, List<T> list, long count) {
        this.start = start;
        this.pageSize = pageSize;
        this.list = list;
        this.count = count;
    }

    public PaginatedList(int start, int pageSize, String searchTerm, SortOrder order, List<T> list) {
        this.start = start;
        this.pageSize = pageSize;
        this.searchTerm = searchTerm;
        this.order = order;
        this.list = list;
    }

    public int start;
    public int pageSize;
    public String searchTerm;
    public SortOrder order;
    public List<T> list;

    public long count;
}
