
package com.example.policetracking.viewmodels.UserListing;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("content")
    @Expose
    private List<Content> content = null;
    @SerializedName("pageable")
    @Expose
    private Pageable pageable;
    @SerializedName("totalPages")
    @Expose
    private Integer totalPages;
    @SerializedName("totalElements")
    @Expose
    private Integer totalElements;
    @SerializedName("last")
    @Expose
    private Boolean last;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("sort")
    @Expose
    private Sort__1 sort;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("first")
    @Expose
    private Boolean first;
    @SerializedName("numberOfElements")
    @Expose
    private Integer numberOfElements;
    @SerializedName("empty")
    @Expose
    private Boolean empty;

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Sort__1 getSort() {
        return sort;
    }

    public void setSort(Sort__1 sort) {
        this.sort = sort;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getFirst() {
        return first;
    }

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public Integer getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(Integer numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

}
