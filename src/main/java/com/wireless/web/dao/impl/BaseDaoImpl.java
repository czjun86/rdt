package com.wireless.web.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.wireless.support.PageBean;
import com.wireless.web.dao.commManager.BaseDao;
import com.wireless.web.dao.commManager.PageMapper;
import com.wireless.web.utils.WebConstants;

public class BaseDaoImpl implements BaseDao {
	@Autowired
	private SqlSessionFactoryBean sqlSessionFactoryTemplate;
	private Integer batch = 200;
	/**
	 * 分页查询
	 */
	public PageBean queryForList(Class<?> daoType, Map<String, Object> params)
			throws Exception {
		PageBean page = null;
		SqlSession sqlSession = null;
		try {
			if (params == null)
				return null;
	
			Integer pageIndex = (Integer) params.get("pageIndex");
			Integer pageSize = (Integer) params.get("pageSize");
	
			if (pageIndex == null) {
				pageIndex = WebConstants.PAGE_DEFAULT_PAGEINDEX;
			}
			if (pageSize == null) {
				pageSize = WebConstants.PAGE_DEFAULT_PAGESIZE;
			}
			int lbound = (pageIndex - 1) * pageSize;
			int mbound = pageIndex * pageSize;
			params.put("pageIndex", pageIndex);
			params.put("pageSize", pageSize);
			params.put("lbound", lbound);
			params.put("mbound", mbound);
			page = new PageBean();
	
			SqlSessionFactory sqlSessionFactory = sqlSessionFactoryTemplate
					.getObject();
			sqlSession = sqlSessionFactory.openSession();
			PageMapper pageDao = (PageMapper) sqlSession.getMapper(daoType);
	
			// 总记录
			int resultCount = pageDao.queryForCount(params);
			
			// 计算总页数
			int pageCount = resultCount / pageSize;
			if (resultCount % pageSize > 0) {
				pageCount++;
			}
			//当前页大于总也数时，重新设置查询条件
			if(pageCount<pageIndex){
				params.put("pageIndex", pageCount);
				pageIndex=pageCount;
				if(pageCount>0){
					lbound = (pageCount - 1) * pageSize;
				}else{
					lbound = 0;
				};
				mbound = pageCount * pageSize;
				params.put("lbound", lbound);
				params.put("mbound", mbound);
			}
			
			// 当前页数据
			List<?> list = pageDao.queryForList(params);
	
			page.setPageIndex(pageIndex==0?1:pageIndex);
			page.setPageSize(pageSize);
			page.setTotalPage(pageCount==0?1:pageCount);
			page.setTotalCount(resultCount==0?1:pageIndex);
			page.setList(list);
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			if(sqlSession!=null){
				sqlSession.close();
			}
		}
		return page;
	}
	
	/**
     * 批量插入,默认为每次200条
     * type为mapper接口类，且必须继承BatchDao接口
     */
    public void batchInsert(Class<?> type,List<?> list,Integer batchNum) throws Exception{
    	SqlSession sqlSession = null;
		try {
			if(type == null)
				return;
			if(CollectionUtils.isEmpty(list))
				return;
			if(batchNum != null && batchNum > 1)
				this.batch = batchNum;
			SqlSessionFactory sqlSessionFactory = sqlSessionFactoryTemplate.getObject();
			sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int i=0;i<list.size();i++){
				PageMapper mapper = (PageMapper)sqlSession.getMapper(type);
				mapper.insertByBatch((Map<String ,Object>)list.get(i));
				if((i != 0 && i % batch == 0) || i + 1 == list.size())
				{
					sqlSession.commit();
					sqlSession.clearCache();
				}
			}
		} catch (Exception e) {
			throw new Exception();
		}finally{
			if(sqlSession!=null){
				sqlSession.close();
			}
		}
    	
    }
}
