package com.fusion.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 统一分页返回结构
 *
 * @param <T> 列表元素类型
 * @author Fusion
 */
@Data
@Schema(description = "分页返回结果")
public class PageResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    @Schema(description = "总记录数")
    private Long total;

    /** 总页数 */
    @Schema(description = "总页数")
    private Long pages;

    /** 当前页码（从 1 开始） */
    @Schema(description = "当前页码")
    private Long current;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Long size;

    /** 当前页数据 */
    @Schema(description = "当前页数据列表")
    private List<T> records;

    public PageResult() {
    }

    public PageResult(Long total, Long pages, Long current, Long size, List<T> records) {
        this.total = total;
        this.pages = pages;
        this.current = current;
        this.size = size;
        this.records = records;
    }

    /**
     * 由 MyBatis-Plus 的 IPage 转换而来
     */
    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<>(page.getTotal(), page.getPages(), page.getCurrent(), page.getSize(), page.getRecords());
    }

    /**
     * 空分页
     */
    public static <T> PageResult<T> empty(long current, long size) {
        return new PageResult<>(0L, 0L, current, size, new ArrayList<>());
    }
}
