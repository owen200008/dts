package com.utopia.data.transfer.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

/**
 * describe:
 *
 * @author niuyaze
 * @date 2021/10/15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseModel <T>{
    private Integer code;
    private String msg;
    private T data;
    private static final Integer SUCC_CODE = 200;
    private static final Integer ERR_CODE = 400;
    private static final String SUCCESS_STR = "success";
    public ResponseModel(Integer code) {
        this.code = code;
    }

    public ResponseModel(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseModel(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static<T> ResponseModel<T> success(){
        return new ResponseModel(SUCC_CODE,SUCCESS_STR);
    }
    public static<T> ResponseModel<T> success(T data){
        return new ResponseModel(SUCC_CODE,SUCCESS_STR,data);
    }
    public static <T>ResponseModel<T> error(String msg,T data){
        return new ResponseModel<>(ERR_CODE,msg,data);
    }
}
