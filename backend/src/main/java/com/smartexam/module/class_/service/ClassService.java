package com.smartexam.module.class_.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartexam.common.exception.BizException;
import com.smartexam.common.result.ResultCode;
import com.smartexam.module.class_.entity.SysClass;
import com.smartexam.module.class_.entity.SysClassUser;
import com.smartexam.module.class_.mapper.SysClassMapper;
import com.smartexam.module.class_.mapper.SysClassUserMapper;
import com.smartexam.module.user.entity.SysUser;
import com.smartexam.module.user.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassService {

    private final SysClassMapper classMapper;
    private final SysClassUserMapper classUserMapper;
    private final SysUserMapper userMapper;

    public Page<SysClass> page(String keyword, int pageNum, int pageSize) {
        Page<SysClass> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysClass> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(SysClass::getName, keyword);
        }
        wrapper.orderByDesc(SysClass::getCreateTime);
        Page<SysClass> result = classMapper.selectPage(page, wrapper);

        // 填充教师和学生数量
        for (SysClass cls : result.getRecords()) {
            fillCounts(cls);
        }
        return result;
    }

    public List<SysClass> listAll() {
        List<SysClass> list = classMapper.selectList(
                new LambdaQueryWrapper<SysClass>().orderByAsc(SysClass::getCreateTime)
        );
        for (SysClass cls : list) {
            fillCounts(cls);
        }
        return list;
    }

    private void fillCounts(SysClass cls) {
        List<SysClassUser> members = classUserMapper.selectList(
                new LambdaQueryWrapper<SysClassUser>().eq(SysClassUser::getClassId, cls.getId())
        );
        if (members.isEmpty()) {
            cls.setTeacherCount(0);
            cls.setStudentCount(0);
            return;
        }
        List<Long> userIds = members.stream().map(SysClassUser::getUserId).collect(Collectors.toList());
        List<SysUser> users = userMapper.selectList(
                new LambdaQueryWrapper<SysUser>().in(SysUser::getId, userIds)
        );
        cls.setTeacherCount((int) users.stream().filter(u -> "TEACHER".equals(u.getRole())).count());
        cls.setStudentCount((int) users.stream().filter(u -> "STUDENT".equals(u.getRole())).count());
    }

    public Long create(SysClass cls) {
        classMapper.insert(cls);
        return cls.getId();
    }

    public void update(SysClass cls) {
        classMapper.updateById(cls);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        classMapper.deleteById(id);
        classUserMapper.delete(
                new LambdaQueryWrapper<SysClassUser>().eq(SysClassUser::getClassId, id)
        );
    }

    public void addClassUser(Long classId, Long userId) {
        SysClass cls = classMapper.selectById(classId);
        if (cls == null) throw new BizException(ResultCode.FAILURE.getCode(), "班级不存在");
        SysUser user = userMapper.selectById(userId);
        if (user == null) throw new BizException(ResultCode.FAILURE.getCode(), "用户不存在");

        Long count = classUserMapper.selectCount(
                new LambdaQueryWrapper<SysClassUser>()
                        .eq(SysClassUser::getClassId, classId)
                        .eq(SysClassUser::getUserId, userId)
        );
        if (count > 0) return; // 已存在则跳过

        SysClassUser cu = new SysClassUser();
        cu.setClassId(classId);
        cu.setUserId(userId);
        classUserMapper.insert(cu);
    }

    public void removeClassUser(Long classId, Long userId) {
        classUserMapper.delete(
                new LambdaQueryWrapper<SysClassUser>()
                        .eq(SysClassUser::getClassId, classId)
                        .eq(SysClassUser::getUserId, userId)
        );
    }

    public List<Map<String, Object>> getClassUsers(Long classId, String role) {
        List<SysClassUser> members = classUserMapper.selectList(
                new LambdaQueryWrapper<SysClassUser>().eq(SysClassUser::getClassId, classId)
        );
        if (members.isEmpty()) return List.of();

        List<Long> userIds = members.stream().map(SysClassUser::getUserId).collect(Collectors.toList());
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>().in(SysUser::getId, userIds);
        if (role != null && !role.isEmpty()) {
            wrapper.eq(SysUser::getRole, role);
        }

        return userMapper.selectList(wrapper).stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("userId", u.getId());
            m.put("username", u.getUsername());
            m.put("realName", u.getRealName());
            m.put("role", u.getRole());
            return m;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAvailableUsers(String role) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDeleted, 0)
                .eq(SysUser::getStatus, 1);
        if (role != null && !role.isEmpty()) {
            wrapper.eq(SysUser::getRole, role);
        }
        return userMapper.selectList(wrapper).stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("userId", u.getId());
            m.put("username", u.getUsername());
            m.put("realName", u.getRealName());
            m.put("role", u.getRole());
            return m;
        }).collect(Collectors.toList());
    }

    public List<Long> getUserClassIds(Long userId) {
        return classUserMapper.selectList(
                new LambdaQueryWrapper<SysClassUser>().eq(SysClassUser::getUserId, userId)
        ).stream().map(SysClassUser::getClassId).collect(Collectors.toList());
    }
}
