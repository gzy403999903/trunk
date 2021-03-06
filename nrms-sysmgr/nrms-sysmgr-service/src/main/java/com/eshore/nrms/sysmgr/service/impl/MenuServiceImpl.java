package com.eshore.nrms.sysmgr.service.impl;

import com.eshore.khala.common.model.PageConfig;
import com.eshore.khala.core.data.api.dao.IBaseDao;
import com.eshore.khala.core.service.impl.BaseServiceImpl;
import com.eshore.nrms.sysmgr.dao.IMenuDao;
import com.eshore.nrms.sysmgr.pojo.Menu;
import com.eshore.nrms.sysmgr.service.IMenuService;
import com.eshore.nrms.util.PageUtil;
import com.eshore.nrms.vo.MenuVo;
import com.eshore.nrms.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by forgeeks at 2017-02-27 17:36
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements IMenuService {

    @Autowired
    IMenuDao menuDao;

    @Override
    public IBaseDao<Menu> getDao() {
        return this.menuDao;
    }

    @Override
    public Integer queryCountByName(String menuName) {
        return menuDao.queryCountByName(menuName);
    }

    @Override
    public List<Menu> queryAllMenu() {
        return menuDao.queryAllMenu();
    }

    @Override
    public List<Menu> queryMenuListByRoleId(String roleId) {
        return menuDao.queryMenuListByRoleId(roleId);
    }

    @Override
    public PageVo<Menu> querymenuListByPage(Menu menu, PageConfig pageConfig) {
        if(pageConfig==null)  pageConfig=new PageConfig();
        List<Menu> list=menuDao.querymenuListByPage(menu, pageConfig);
        return PageUtil.getPageList(pageConfig,list);
    }

    @Override
    public List<Menu> queryMenuListByPId(String pId, String roleId) {
        return menuDao.queryMenuListByPId(pId, roleId);
    }

    @Override
    public List<MenuVo> queryMenuVoList(String roleId) {

        String menuId = "0sabdvldkjchgbeiubjkn"; // 根目录 id
        List<MenuVo> voList = new ArrayList<MenuVo>();
        List<Menu> menulist = queryMenuListByRoleId(roleId);
        List<Menu> rootlist = queryMenuListByPId(menuId, roleId);
        MenuVo vo1 = new MenuVo();
        vo1.setChildMenus(new ArrayList<Menu>());
        MenuVo vo2 = new MenuVo();
        vo2.setChildMenus(new ArrayList<Menu>());
        if (rootlist.size() == 2) {
            vo1.setThisMenu(rootlist.get(0));
            vo2.setThisMenu(rootlist.get(1));
        }else if(rootlist.size() == 1){
            vo1.setThisMenu(rootlist.get(0));
        }else return  voList;
        voList.add(vo1);
        if( vo2.getThisMenu()!=null )  voList.add(vo2);
        for (Menu menu : menulist) {
            if (menu.getPid().equals(vo1.getThisMenu().getId())) {
                vo1.getChildMenus().add(menu);
            } else if ( vo2.getThisMenu()!=null && menu.getPid().equals(vo2.getThisMenu().getId())) {
                vo2.getChildMenus().add(menu);
            }
        }

        return voList;
    }


}
