package com.wtlei.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtlei.entity.Image;
import com.wtlei.mapper.ImageMapper;
import org.springframework.stereotype.Component;

/**
 * @author wtlei
 * @name ImageService
 * @date 2019-12-08 17:39
 */

@Component
public class ImageService extends ServiceImpl<ImageMapper, Image> {
}
