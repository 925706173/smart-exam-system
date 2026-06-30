package com.smartexam.module.dict.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.module.dict.entity.SysDict;
import com.smartexam.module.dict.mapper.SysDictMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典服务
 */
@Service
@RequiredArgsConstructor
public class DictService {

    private final SysDictMapper dictMapper;

    /**
     * 分页查询字典
     */
    public Page<SysDict> page(String type, int pageNum, int pageSize) {
        Page<SysDict> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();

        if (type != null && !type.isEmpty()) {
            wrapper.eq(SysDict::getType, type);
        }
        wrapper.orderByAsc(SysDict::getSortOrder);
        return dictMapper.selectPage(page, wrapper);
    }

    /**
     * 按类型查询字典
     */
    public List<SysDict> getByType(String type) {
        return dictMapper.selectList(
                new LambdaQueryWrapper<SysDict>()
                        .eq(SysDict::getType, type)
                        .eq(SysDict::getStatus, 1)
                        .orderByAsc(SysDict::getSortOrder)
        );
    }

    /**
     * 创建字典
     */
    public Long create(SysDict dict) {
        dictMapper.insert(dict);
        return dict.getId();
    }

    /**
     * 更新字典
     */
    public void update(SysDict dict) {
        dictMapper.updateById(dict);
    }

    /**
     * 删除字典
     */
    public void delete(Long id) {
        dictMapper.deleteById(id);
    }
}
