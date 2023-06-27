package com.king.crm.dao;

import com.king.crm.base.BaseMapper;
import com.king.crm.vo.SaleChance;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;

//@Primary
public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
     /*
     由于考虑到多个模块均涉及多条件查询
     这⾥对于多条件分⻚查询⽅法由⽗接⼝BaseMapper定义
     */

}