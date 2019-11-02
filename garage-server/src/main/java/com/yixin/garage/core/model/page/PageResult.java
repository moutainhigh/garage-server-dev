package com.yixin.garage.core.model.page;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 封装分页结果集
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -4071521319254024213L;

    private long page = 1L;// 第几页
    private long pageSize = 20L;// 每页显示多少条
    private Integer totalPage = 0;// 总页数
    private long totalRows = 0L;// 总记录数
    private List<T> rows;// 结果集

    public PageResult() {
    }

    public PageResult(Page<T> page) {
        this.setRows(page.getRecords());
        this.setTotalRows(page.getTotal());
        this.setPage(page.getCurrent());
        this.setPageSize(page.getSize());
        this.setTotalPage((int) ((totalRows + pageSize - 1)/pageSize));
    }

}
