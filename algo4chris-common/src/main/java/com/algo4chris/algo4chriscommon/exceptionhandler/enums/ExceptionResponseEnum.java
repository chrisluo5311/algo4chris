package com.algo4chris.algo4chriscommon.exceptionhandler.enums;

import com.algo4chris.algo4chriscommon.common.response.MgrResponseDto;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Arrays;

/**
 * 處理常見 Exception 包裝成 MgrResponseDto
 * 原因: 避免不確定的錯誤信息直接暴露給用戶
 *
 * @author chris
 * @Date  2022/01/29
 * */
@Slf4j
@Getter
@AllArgsConstructor
public enum ExceptionResponseEnum implements ExceptionMessageConstructor{
    MethodArgumentTypeMismatchException(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class){
        @Override
        public MgrResponseDto getMgrResponse(Exception e) {
            MgrResponseDto dto = new MgrResponseDto();
            //類型轉換出問題 ex. String轉換date
            String errorMsg = e.getMessage().substring(0,e.getMessage().indexOf(";"));
            if(errorMsg.contains("Date") && errorMsg.contains("String")){
                errorMsg = errorMsg.concat("請求參數的字符串無法轉為Date類型");
            }
            dto.setCode(MgrResponseCode.UNKNOWN_ERROR);
            dto.setMessage(errorMsg);
            return dto;
        }
    },
    NullPointerException(NullPointerException.class) {
        @Override
        public MgrResponseDto getMgrResponse(Exception e) {
            MgrResponseDto dto = new MgrResponseDto();
            dto.setCode(MgrResponseCode.UNKNOWN_ERROR);
            dto.setMessage("查無資料，請聯繫技術人員");
            return dto;
        }
    },
    SQLException(SQLException.class){
        @Override
        public MgrResponseDto getMgrResponse(Exception e) {
            MgrResponseDto dto = new MgrResponseDto();
            dto.setCode(MgrResponseCode.UNKNOWN_ERROR);
            dto.setMessage("數據庫操作失敗，請聯繫技術人員");
            return dto;
        }
    },
    UnsupportedTemporalTypeException(java.time.temporal.UnsupportedTemporalTypeException.class){
        @Override
        public MgrResponseDto getMgrResponse(Exception e) {
            MgrResponseDto dto = new MgrResponseDto();
            dto.setCode(MgrResponseCode.UNKNOWN_ERROR);
            dto.setMessage("時間轉換失敗，請聯繫技術人員");
            return dto;
        }
    },
    MaxUploadSizeExceededException(org.springframework.web.multipart.MaxUploadSizeExceededException.class){
        @Override
        public MgrResponseDto getMgrResponse(Exception e) {
            MgrResponseDto dto = new MgrResponseDto();
            dto.setCode(MgrResponseCode.UNKNOWN_ERROR);
            dto.setMessage("檔案過大，請重新上傳");
            return dto;
        }
    },
    Default(Exception.class){
        @Override
        public MgrResponseDto getMgrResponse(Exception e) {
            MgrResponseDto dto = new MgrResponseDto();
            dto.setCode(MgrResponseCode.UNKNOWN_ERROR);
            dto.setMessage("系統維護中，請聯繫技術人員");
            return dto;
        }
    };

    private Class classType;

    /**
     * 根據不同Exception響應不同訊息
     *
     * @param e Exception
     * */
    public static MgrResponseDto getMgrResponseFromException(Exception e){
        return Arrays.stream(ExceptionResponseEnum.values())
                     .filter(ex-> ex.getClassType().isInstance(e))
                     .findFirst()
                     .get()
                     .getMgrResponse(e);
    }
}
