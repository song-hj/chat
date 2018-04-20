package com.song.view.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.song.core.security.support.SecurityUserHolder;
import com.song.core.security.support.SpringUtils;
import com.song.core.tools.CommUtil;
import com.song.core.tools.FileConfig;
import com.song.core.tools.InterfaceVo;
import com.song.core.tools.InterfaceVo.Code;
import com.song.foundation.entity.Accessory;
import com.song.foundation.entity.User;
import com.song.foundation.facade.service.AccessoryFacadeService;
import com.song.foundation.facade.service.FriendsFacadeService;
import com.song.foundation.facade.service.MessageInfoFacadeService;
import com.song.foundation.facade.service.UserFacadeService;

/**
 * 用户控制层
 * @author songhj
 *
 */
@Controller
public class UserViewController {

	@Autowired
	private UserFacadeService userFacadeService;
	
	@Autowired
	private AccessoryFacadeService accessoryFacadeService;
	
	@Autowired
	private FriendsFacadeService friendsFacadeService;
	
	@Autowired
	private MessageInfoFacadeService messageInfoFacadeService;
	
	/**
	 * 验证用户是否登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user/checkLogin")  
    @ResponseBody  
    public InterfaceVo checkLogin(HttpServletRequest request,HttpServletResponse response) {
		InterfaceVo vo = new InterfaceVo();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user == null){
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}else{
			vo.setCodeEnum(Code.SUCCESS);
			vo.getOutData().put("uid", user.getId());
		}
		return vo;
	}
	
	/**
	 * 用户注册
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 */
	@RequestMapping("/user/register")
    @ResponseBody  
    public InterfaceVo register(HttpServletRequest request,HttpServletResponse response,
    		User user) {
		InterfaceVo vo = userFacadeService.saveUser(user);
        return vo;
	}
	
	
	/**
	 * 头像上传
	 * @param request
	 * @param response
	 * @param fileType
	 * @return
	 */
	@RequestMapping(value = {"/file/saveImage"}, method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public InterfaceVo uploadFile(HttpServletRequest request,HttpServletResponse response,String uploadType){
		InterfaceVo vo = new InterfaceVo();
		Long userId = -1L;
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			userId = user.getId();
		}
		if(CommUtil.null2Int(uploadType) == 1){
			vo = CommUtil.saveImage2(request, userId);
		}else{
			vo = CommUtil.saveImage(request, userId);
		}
		if(vo.getCode() == Code.SUCCESS.getCode()){
			Accessory acc = (Accessory) vo.getOutData().get("obj");
			acc.setCreateUserId(user.getId());
			acc = accessoryFacadeService.savePhoto(acc);
			vo.getOutData().put("imgUrl",acc.getPath() + acc.getName() + acc.getExt());
		}
		return vo;
	}
	
	
	/**
	 * 其他服务上传图片
	 * @param request
	 * @param response
	 * @param path
	 * @param picture
	 * @param ext
	 * @return
	 */
	@RequestMapping(value="/uploadImageFile")
	@ResponseBody
	public Map<String,Object> uploadImageFile(HttpServletRequest request,HttpServletResponse response,
			String path,String picture,String ext){
		Map<String,Object> map = Maps.newHashMap();
		if(!CommUtil.isNotNull(path) || !CommUtil.isNotNull(picture) || !CommUtil.isNotNull(ext)){
			map.put("code", -1);
			map.put("message", "参数错误");
		}
		map = CommUtil.saveImage3(path, picture, ext);
		return map;
		
	}
	
	
	/**
	 * 个人信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"/user/myinfo"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo myinfo(HttpServletRequest request,HttpServletResponse response) {
		InterfaceVo vo = new InterfaceVo();
		Map<String,Object> map = Maps.newHashMap();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user == null){
			vo.setCodeEnum(Code.USER_NO_LOGIN);
			return vo;
		}
		user = userFacadeService.findOne(user.getId());
		if(user.getHeadId() != null){
			Accessory accessory = accessoryFacadeService.findOne(user.getHeadId()); 
			map.put("imgUrl", accessory.getPath() + accessory.getName() + accessory.getExt());
		}
		map.put("nickName", user.getNickName());
		map.put("userName", user.getUserName());
		map.put("sex", user.getSex());
		map.put("city", user.getAddress());
		map.put("description", user.getDescription());
		vo.setOutData(map);
        vo.setCodeEnum(Code.SUCCESS);
        return vo;
	}
	
	
	/**
	 * 修改个人信息
	 * @param request
	 * @param response
	 * @param name
	 * @param value
	 * @return
	 */
	@RequestMapping(value = {"/user/update"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo update(HttpServletRequest request,HttpServletResponse response,
    		String name, String value) {
		InterfaceVo vo = new InterfaceVo();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			vo = userFacadeService.updateUser(name, value, user.getId());
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	
	/**
	 * 查找好友
	 * @param request
	 * @param response
	 * @param value
	 * @return
	 */
	@RequestMapping(value = {"/user/find"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo find(HttpServletRequest request,HttpServletResponse response,String value, Long fid, int status) {
		InterfaceVo vo = new InterfaceVo();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			vo = friendsFacadeService.findFriend(value, fid, user.getId(), status);
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	/**
	 * 添加好友
	 * @param request
	 * @param response
	 * @param fid
	 * @return
	 */
	@RequestMapping(value = {"/user/add"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo add(HttpServletRequest request,HttpServletResponse response,
    		Long fid) {
		InterfaceVo vo = new InterfaceVo();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			vo = friendsFacadeService.addFriend(fid,user.getId());
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	
	/**
	 * 好友列表
	 * @param request
	 * @param response
	 * @param fName
	 * @return
	 */
	@RequestMapping(value = {"/user/friendList"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo friendList(HttpServletRequest request,HttpServletResponse response,
    		String fName, int pageNo, int pageSize) {
		InterfaceVo vo = new InterfaceVo();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			vo = friendsFacadeService.friendList(CommUtil.null2String(fName),user.getId(), pageNo, pageSize);
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	
	
	/**
	 * 新的好友列表
	 * @param request
	 * @param response
	 * @param fName
	 * @return
	 */
	@RequestMapping(value = {"/user/newFriendList"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo newFriendList(HttpServletRequest request,HttpServletResponse response) {
		InterfaceVo vo = friendsFacadeService.friendList(SecurityUserHolder.getCurrentUser(request).getId());
        return vo;
	}
	
	
	/**
	 * 修改备注
	 * @param request
	 * @param response
	 * @param fName
	 * @return
	 */
	@RequestMapping(value = {"/user/updateFrienddName"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo updateFrienddName(HttpServletRequest request,HttpServletResponse response,
    		Long fid, String fName) {
		InterfaceVo vo = new InterfaceVo();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			vo = friendsFacadeService.updateFrienddName(CommUtil.null2String(fName), fid, user.getId());
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	
	/**
	 * 同意或拒绝
	 * @param request
	 * @param response
	 * @param id
	 * @param agree
	 * @return
	 */
	@RequestMapping(value = {"/user/agreeFriend"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo agreeFriend(HttpServletRequest request,HttpServletResponse response,
    		Long id, int agree) {
		InterfaceVo vo = new InterfaceVo();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			vo = friendsFacadeService.agreeFriend(id, agree, user.getId());
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	
	/**
	 * 保存聊天信息
	 * @param request
	 * @param response
	 * @param fid
	 * @param message
	 * @return
	 */
	@RequestMapping(value = {"/user/addMessage"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo addMessage(HttpServletRequest request,HttpServletResponse response,
    		Long fid, String message) {
		InterfaceVo vo = new InterfaceVo();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			vo = messageInfoFacadeService.saveMessage(fid, CommUtil.null2String(message), user.getId());
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	
	/**
	 * 已读信息列表
	 * @param request
	 * @param response
	 * @param fid
	 * @return
	 */
	@RequestMapping(value = {"/user/readMessageList"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo readMessageList(HttpServletRequest request,HttpServletResponse response,
    		Long fid,int pageNo, int pageSize) {
		InterfaceVo vo = new InterfaceVo();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			vo = messageInfoFacadeService.getReadMessage(true, fid, user.getId(), pageNo, pageSize);
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	/**
	 * 获取未读信息
	 * @param request
	 * @param response
	 * @param fid
	 * @return
	 */
	@RequestMapping(value = {"/user/noReadMessageList"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo noReadMessageList(HttpServletRequest request,HttpServletResponse response,
    		Long fid) {
		InterfaceVo vo = new InterfaceVo();
		int pageNo = 0;
		int pageSize = 20;
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			vo = messageInfoFacadeService.getReadMessage(false, fid, user.getId(), pageNo, pageSize);
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	
	/**
	 * 对话列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"/user/messageList"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo messageList(HttpServletRequest request,HttpServletResponse response) {
		InterfaceVo vo = new InterfaceVo();
		User user = SecurityUserHolder.getCurrentUser(request);
		if(user != null){
			vo = messageInfoFacadeService.messageList(user.getId());
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	
	/**
	 * 获取未读信息数量
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"/user/noReadMessageNum"}, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody  
    public InterfaceVo noReadMessageNum(HttpServletRequest request,HttpServletResponse response) {
		User user = SecurityUserHolder.getCurrentUser(request);
		InterfaceVo vo = new InterfaceVo();
		if(user != null){
			vo = messageInfoFacadeService.getNoReadMessageNum(user.getId());
		}else{
			vo.setCodeEnum(Code.USER_NO_LOGIN);
		}
        return vo;
	}
	
	/**
	 * 首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/user/index")
    @ResponseBody  
    public Map<String,Object> index(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map = Maps.newHashMap();
        List<User> users = null;//userFacadeService.findAll();
        map.put("users", users);
        return map;
	}
	

}
