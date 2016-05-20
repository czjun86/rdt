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
 * @类功能说明：    areaKpiES查询实体类
 * @类修改者：  
 * @修改日期：  
 * @修改说明：  
 * @公司名称：  
 * @作者：tc  
 * @创建时间：2015年12月4日 下午3:11:16  
 * @版本：V1.0  
 */
public class AreaKpiBean implements Comparable<AreaKpiBean>{
	private String cname1;
	private String cname2;
	private String cname3;
	private String tel1;
	private Double val1;
	private String tel2;
	private Double val2;
	private String tel3;
	private Double val3;
	public String getCname1() {
		return cname1;
	}
	public void setCname1(String cname1) {
		this.cname1 = cname1;
	}
	public String getCname2() {
		return cname2;
	}
	public void setCname2(String cname2) {
		this.cname2 = cname2;
	}
	public String getCname3() {
		return cname3;
	}
	public void setCname3(String cname3) {
		this.cname3 = cname3;
	}
	public String getTel1() {
		return tel1;
	}
	public void setTel1(String tel1) {
		this.tel1 = tel1;
	}
	public Double getVal1() {
		return val1;
	}
	public void setVal1(Double val1) {
		this.val1 = val1;
	}
	public String getTel2() {
		return tel2;
	}
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}
	public Double getVal2() {
		return val2;
	}
	public void setVal2(Double val2) {
		this.val2 = val2;
	}
	public String getTel3() {
		return tel3;
	}
	public void setTel3(String tel3) {
		this.tel3 = tel3;
	}
	public Double getVal3() {
		return val3;
	}
	public void setVal3(Double val3) {
		this.val3 = val3;
	}
	@Override
	public int compareTo(AreaKpiBean o) {
		int re = 0;
		if(this.val1!=null&&o!=null&&o.getVal1()!=null){
			if(this.val1>o.getVal1()){
				re = 1;
			}else if(this.val1<o.getVal1()){
				re =-1;
			}
		}else if(this.val1!=null){
			re = 1;
		}else if(o!=null&&o.getVal1()!=null){
			re =-1;
		}
		return re;
	}
	
}
