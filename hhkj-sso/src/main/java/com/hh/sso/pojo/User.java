/**
 * 
 */
package com.hh.sso.pojo;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author XUFH
 *
 */
@Table(name="tb_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Length(min = 6, max = 20, message = "用户名只能在6~20之间!")
	private String username;

	//@JsonIgnore//json序列化时忽略此字段 解决 将用户的信息保存到redis中时 密码泄露的问题
	@Length(min = 6, max = 20, message = "密码只能在6~20之间!")
	private String password;

	@Length(min = 11, max = 11, message = "手机号只能是11位!")
	private String phone;

	@Email
	private String email;
	private Date created;
	/**更新时间*/
	private Date updated;
	/**
	 * @return #{bare_field_comment}
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id->#{bare_field_comment}
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return #{bare_field_comment}
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username->#{bare_field_comment}
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return #{bare_field_comment}
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password->#{bare_field_comment}
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return #{bare_field_comment}
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone->#{bare_field_comment}
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return #{bare_field_comment}
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email->#{bare_field_comment}
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return #{bare_field_comment}
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created->#{bare_field_comment}
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
	/**
	 * @return #{bare_field_comment}
	 */
	public Date getUpdated() {
		return updated;
	}
	/**
	 * @param updated->#{bare_field_comment}
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	

}
