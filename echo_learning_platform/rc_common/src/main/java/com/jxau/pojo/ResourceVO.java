package com.jxau.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 资源视图类
 * @author cheng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceVO {
    private String id;
    private String name;
    private String type;
    private String size;
    private String url;
}
