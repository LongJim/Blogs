package xlong.blogs.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 *     主题信息
 * </pre>
 *
 */
@Data
public class Theme implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 是否支持设置
     */
    private boolean hasOptions;
}
