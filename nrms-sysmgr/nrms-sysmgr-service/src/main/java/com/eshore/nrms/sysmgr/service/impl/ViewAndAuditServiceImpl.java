package com.eshore.nrms.sysmgr.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eshore.khala.common.model.PageConfig;
import com.eshore.khala.common.utils.type.StringUtils;
import com.eshore.khala.core.data.api.dao.IBaseDao;
import com.eshore.khala.core.service.impl.BaseServiceImpl;
import com.eshore.nrms.sysmgr.dao.IViewAndAuditDAO;
import com.eshore.nrms.sysmgr.pojo.Application;
import com.eshore.nrms.sysmgr.pojo.User;
import com.eshore.nrms.sysmgr.service.IViewAndAuditService;

@Service
@Transactional(propagation=Propagation.REQUIRED)
public class ViewAndAuditServiceImpl extends BaseServiceImpl<Application> implements IViewAndAuditService{
	
	@Autowired
	private IViewAndAuditDAO applicationDAO;
	
	@Override
	public IBaseDao<Application> getDao() {
		return applicationDAO;
	}

	@Override
	public Application getFull(String id) {
		Application app = new Application();
		app.setId(id);
		List<Application> list = applicationDAO.queryFull(app);
		if(list != null && !list.isEmpty())
			return iniUnames(list.get(0));
		
		return null;
	}

	private Application iniUnames(Application app) {
		if(app == null)
			return null;
		List<String> uids = new ArrayList<String>();
		if(StringUtils.isNotBlank(app.getUidApplicant())){
			uids.add(app.getUidApplicant());
		}
		if(StringUtils.isNotBlank(app.getUidAuditor())){
			uids.add(app.getUidAuditor());
		}
		if(StringUtils.isNotBlank(app.getUidMinutes())){
			uids.add(app.getUidMinutes());
		}
		
		List<String> list = applicationDAO.getUsersByIds(uids);
		
		if(StringUtils.isNotBlank(app.getUidApplicant())){
			app.setUnameApplicant(list.get(0));
		}
		if(StringUtils.isNotBlank(app.getUidAuditor())){
			if(app.getUidAuditor().equals(app.getUidApplicant()))//和申请人是同一个人
				app.setUnameAuditor(list.get(0));
			else
				app.setUnameAuditor(list.get(1));
		}
		if(StringUtils.isNotBlank(app.getUidMinutes())){
			if(app.getUidMinutes().equals(app.getUidApplicant())){
				app.setUnameMinutes(list.get(0));
			}else if(app.getUidMinutes().equals(app.getUidAuditor())){
				app.setUnameMinutes(list.get(1));
			}else
				app.setUnameMinutes(list.get(2));
		}
		return app;
	}

	@Override
	public List<Application> getFull(Application app) {
		List<Application> list = applicationDAO.queryFull(app);
		return list;
	}

	@Override
	public List<Application> getFullPage(Map<String, Object> map, PageConfig pc) {
		List<Application> list = applicationDAO.queryFullPage(map, pc);
		return list;
	}

	@Override
	public List<Application> getFullPage(Application app, PageConfig pc) {
		List<Application> list = applicationDAO.queryFullPage(app, pc);
		return list;
	}
	
	public List<Application> getFullPageWithMe(User user,Application app,PageConfig pc){
		if(user == null)
			return null;
		List<Application> list = applicationDAO.queryFullPageByUid(user.getId(), app, pc);
		return list;
	}

	@Override
	public Integer getCount(Application app) {
		return applicationDAO.queryCount(app);
	}

	@Override
	public boolean deleteFile(Application app) {
		File targetFile ;
		boolean f1 = true;
		boolean f2 = true;
		if(app.getAppUuidName() != null){
			targetFile = new File(IViewAndAuditService.PATH,app.getAppUuidName());
			if(targetFile.exists()){
				f1 = targetFile.delete();
			}
		}
		if(app.getMinutesUuidName() != null){
			targetFile = new File(IViewAndAuditService.PATH,app.getMinutesUuidName());
			if(targetFile.exists()){
				f1 = targetFile.delete();
			}
		}
		return f1 && f2;
	}

	@Override
	public List<User> getUsersInApplication(String appId) {
		return applicationDAO.queryUserInApplication(appId);
	}

	@Override
	public boolean verifyTimeConflict(Application app) {
		return applicationDAO.verifyTimeConflict(app);
	}

	@Override
	public List<Application> getFullPageNotEnd(String nowTime, String uid, Application app, PageConfig pc) {
		return applicationDAO.queryFullPageNotEnd(nowTime, uid, app, pc);
	}

}
