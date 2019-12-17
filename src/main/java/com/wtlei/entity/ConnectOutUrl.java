package com.wtlei.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author wtlei
 * @name ConnectOutUrl
 * @date 2019-12-14 23:22
 */

@Data
@TableName("connect_out_url")
public class ConnectOutUrl {

    private String id;

    private String url;

}
