package com.assignment.user.response;

import lombok.Data;
import java.util.List;

@Data
public class PaginatedResponse<T> {
    private List<T> data; // List of data items
    private int pageNumber;  // Current page number
    private int pageSize;    // Number of items per page
    private long totalElements; // Total number of records
    private int totalPages;  // Total number of pages
    private boolean last;    // Is this the last page?
}

