package com.utopia.data.transfer.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.PortableInterceptor.INACTIVE;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRes<T> {

    private Long total;

    private Integer pageSize;

    private T data;

    public static <T> PageRes<T> getPage(Long total,Integer pageSize,T data){
        return new PageRes<>(total,pageSize,data);
    }
}
