/**     
* @文件名称: AreaKpiBean.java  
* @类路径: com.wireless.web.model  
* @描述: TODO  
* @作者：tc  
* @时间：2015年12月4日 下午3:11:16  
* @版本：V1.0     
*/
package com.wireless.web.model;
/**  
 * @类功能说明：  areaKpiES查询排序辅助类
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：  
 * @作者：tc  
 * @创建时间：2015年12月4日 下午3:11:16  
 * @版本：V1.0  
 */
public class AreaKpi4ESBean implements Comparable<AreaKpi4ESBean>{
	private String tel;//运营商名称
	private Double val;//指定的kpi值
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Double getVal() {
		return val;
	}
	public void setVal(Double val) {
		this.val = val;
	}
	
	@Override
	public int compareTo(AreaKpi4ESBean o) {
		int re = 0;
		if(this.val!=null&&o!=null&&o.getVal()!=null){
			if(this.val>o.getVal()){
				re = 1;
			}else if(this.val<o.getVal()){
				re =-1;
			}
		}else if(this.val!=null){
			re = 1;
		}else if(o!=null&&o.getVal()!=null){
			re =-1;
		}
		
		return re;
	}
}
