package com.lel;

import java.util.Objects;

/**
 * 通用返回结果
 * @author lel
 * @param <T>
 */
public class Result<T> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1844559448210088748L;
	
	public Result(){
		
	}
	
	/**
	 * 不建议提供/使用此构造方法
	 */
	public Result(String status,String msg,T data){
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	/**
	 * 1:成功<br>
	 * 0:失败<br>
	 */
	private String status = RStatus.FAIL.status;
	
	/**
	 * 返回消息
	 */
	private String msg = RStatus.FAIL.msg;
	
	/**
	 * 返回结果
	 */
	private T data;

	public String getStatus() {
		return status;
	}
	
	/**
	 * 禁止外部重置status
	 * @param msg
	 */
	private void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	/**
	 * 禁止外部重置msg
	 * @param msg
	 */
	private void setMsg(String msg) {
		this.msg = msg;
	}

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}
	
	public void fail(){
		this.fail(RStatus.FAIL.getMsg());
	}
	
	public void fail(String msg){
		this.fail(RStatus.FAIL.getStatus(), msg);
	}
	
	/**
	 * 完整失败状态设置<br>
	 * 暂不对外提供<br>
	 * @param status
	 * @param msg
	 */
	private void fail(String status, String msg){
		this.setStatus(RStatus.FAIL.getStatus());
		this.setMsg(msg);
	}
	
	public void success(){
		this.success(RStatus.SUCCESS.getMsg());
	}
	
	public void success(String msg){
		this.successT(null, msg);
	}
	
	public void successT(T data){
		this.successT(data, RStatus.SUCCESS.getMsg());
	}
	
	public void successT(T data,String msg){
		this.successT(RStatus.SUCCESS.getStatus(), data, msg);
	}
	
	/**
	 * 完整成功状态设置<br>
	 * 暂不对外提供<br>
	 * @param status
	 * @param t
	 * @param msg
	 */
	private void successT(String status, T data, String msg){
		this.setStatus(RStatus.SUCCESS.getStatus());
		this.setMsg(msg);
		this.setData(data);
	}
	
	/**
	 * 执行是否成功
	 * @return
	 */
	public boolean exeSuccess(){
		return this.exeSuccess(this.status, RStatus.SUCCESS.getStatus());
	}
	
	/**
	 * 预计值判断<br>
	 * 暂不对外提供<br>
	 * @return
	 */
	private boolean exeSuccess(String realStatus ,String exceptStatus){
		return Objects.equals(realStatus, exceptStatus);
	}
	
	/**
	 * 回执状态枚举
	 * @author lel
	 */
	static enum RStatus{
		FAIL("0", "失败"),
		SUCCESS("1","成功");
		
		private String status;
		private String msg;
		
		private RStatus(String status, String msg){
			this.status = status;
			this.msg = msg;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
}
