package com.hmdp.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.hmdp.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryTypeByAll() {
        // 1、从redis查询商铺缓存
        List<ShopType> cacheType = stringRedisTemplate.opsForList().range(CACHE_SHOP_TYPE_KEY, 0, -1)
                .stream().map(type->JSONUtil.toBean(type,ShopType.class))
                .collect(Collectors.toList());
        // 2、判断是否存在
        if (!CollUtil.isEmpty(cacheType)) {
            // 3、存在，直接返回
            return Result.ok(cacheType);
        }
        // 4、不存在，根据查询数据库
        List<ShopType> shopTypes = query().orderByAsc("sort").list();
        // 5、不存在，返回错误
        if (CollUtil.isEmpty(shopTypes)) {
            return Result.fail("店铺不存在");
        }
        // 6、存在，写入redis
        List<String> jsonTypes = shopTypes.stream().map(JSONUtil::toJsonStr).collect(Collectors.toList());
        stringRedisTemplate.opsForList().rightPushAll(CACHE_SHOP_TYPE_KEY, jsonTypes);
        // 7、返回
        return Result.ok(shopTypes);
    }
}
