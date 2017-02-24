package com.eshore.nrms.sysmgr.dao;

import com.eshore.khala.common.model.PageConfig;
import com.eshore.khala.core.data.api.dao.IBaseDao;
import com.eshore.nrms.sysmgr.pojo.User;

import java.util.List;

/**
 * Created by forgeeks at 2017-02-24 10:31
 */
public interface IUserDao extends IBaseDao<User> {

    public User queryUserByLoginName(String loginName);

    public List<User> queryUserList(User user, PageConfig page);


}
