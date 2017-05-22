package com.hdos.platform.base.user.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.hdos.platform.base.user.mapper.UserInfoMapper;
import com.hdos.platform.base.user.model.UserInfoVO;
import com.hdos.platform.core.base.BaseService;

@Service
@Transactional
public class UserInfoService extends BaseService<UserInfoVO> {

	@Autowired
	private UserInfoMapper userInfoMapper;

	/**
	 * 保存操作
	 * 
	 * @param vo
	 *            对象
	 * @return 主键
	 */
	public String save(UserInfoVO userVO) {
		if (userVO == null || userVO.getUserId() == null) {
			throw new IllegalArgumentException("保存用户信息时，用户ID不能为空");
		}
		if (StringUtils.isNotBlank(userVO.getUserInfoId())) {
			userInfoMapper.update(userVO);
		} else {
			// 保证每个用户只有一条可用的用户信息数据
			String userId = userVO.getUserId();
			UserInfoVO existed = this.findByUserId(userId);
			if (existed != null) {
				// 还是更新
				userVO.setUserInfoId(existed.getUserInfoId());
				userInfoMapper.update(userVO);
			} else {
				// 真正的新增
				userVO.setUserInfoId(generateKey(userVO));
				userInfoMapper.insert(userVO);
			}
		}
		return userVO.getUserInfoId();
	}

	/**
	 * 根据用户ID删除个人信息
	 * 
	 * @param userId
	 *            用户ID
	 */
	public void deleteByUserId(String userId) {
		Map<String, Object> condition = Maps.newHashMap();
		condition.put("userId", userId);
		List<UserInfoVO> infos = userInfoMapper.list(condition);
		if (infos != null && infos.size() > 0) {
			for (UserInfoVO info : infos) {
				userInfoMapper.delete(info.getUserInfoId());
			}
		}
	}

	/**
	 * 根据主键获取实体
	 * 
	 * @param userInfoId
	 *            主键
	 * @return 实体
	 */
	public UserInfoVO findById(String userInfoId) {
		return userInfoMapper.getById(userInfoId);
	}

	/**
	 * 根据用户ID获取实体
	 * 
	 * @param userId
	 *            用户ID
	 * @return 实体
	 */
	public UserInfoVO findByUserId(String userId) {
		Map<String, Object> condition = Maps.newHashMap();
		condition.put("userId", userId);
		List<UserInfoVO> infos = userInfoMapper.list(condition);
		if (infos != null && infos.size() > 0) {
			// 应当只有一条未删除数据
			return infos.get(0);
		}
		return null;
	}
}
