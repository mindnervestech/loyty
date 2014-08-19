package com.mnt.core.utils;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GridViewModel {

    public int page;
    public int total;
    public long records;
    public List<RowViewModel> rows;

    public GridViewModel(int page, int total, long records, List<RowViewModel> rows) {
        this.page = page;
        this.total = total;
        this.records = records;
        this.rows = rows;
    }

    public GridViewModel(Paging pageData, long totalEntries, List<RowViewModel> rows) {
        this(pageData.pageIndex(), totalPagesFrom(totalEntries,pageData.rowsPerPage()), totalEntries, rows);
    }

    public static int totalPagesFrom(long totalEntries, int rowsPerPage) {
        double pages = (double)totalEntries/(double)rowsPerPage;
        if(pages == 0d) {
            return 1;
        }
        return (int) Math.ceil(pages);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class RowViewModel {
        public String id;
        public List<String> cell;

        public RowViewModel(String id, List<String> cell) {
            this.id = id;
            this.cell = cell;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public static class PageData implements Paging {
        // The names of the fields are in line with the URL parameters sent by jQGrid.
        private int rows;
        private int page;

        public PageData() { // required for the Spring MVC data binding
        }

        public PageData(int rows, int page) {
            this.rows = rows;
            this.page = page;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public void setPage(int page) {
            this.page = page;
        }

        @Override
        public int rowsPerPage() {
            return rows;
        }

        @Override
        public int pageIndex() {
            return page;
        }
    }

    public static class SortData implements Sorting {
        // The names of the fields are in line with the URL parameters sent by jQGrid.
        private String sidx;
        private String sord;

        public void setSidx(String sidx) {
            this.sidx = sidx;
        }

        public void setSord(String sord) {
            this.sord = sord;
        }

        @Override
        public String sortBy() {
            return sidx;
        }

        @Override
        public SortOrder sortOrder() {
            return "desc".equals(sord) ? SortOrder.DESCENDING: SortOrder.ASCENDING;
        }
    }
}
