package com.liu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.entity.CheckRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liu
 * @since 2020-05-27
 */
 @Mapper
 @Repository
 @Scope("prototype")
public interface CheckRoleDAO extends BaseMapper<CheckRoleEntity> {

}
