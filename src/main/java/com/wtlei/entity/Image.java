package com.wtlei.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * @author wtlei
 * @name Image
 * @date 2019-12-08 17:29
 */

@Data
@TableName("image")
@ToString
public class Image {

    private String id;

    private String title;

    private String imageName;

    private String path;

    @TableField("local_path")
    private String localPath;

    private String issuer;

    private String periodicalNum;

    private String imageCount;

    private String resolution;

    private String modelName;

    private String issueDate;

    private String imageTags;
}
